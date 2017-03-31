package com.example.dharampalsolanki.dharamgo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dharampalsolanki.dharamgo.DBAdapter;
import com.example.dharampalsolanki.dharamgo.R;
import com.example.dharampalsolanki.dharamgo.adapter.CartAdapter;
import com.example.dharampalsolanki.dharamgo.model.ItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartDetails extends AppCompatActivity
         {
    TextView addMoreObject, minusMoreObjects;
    private Context context= this;
    private TextView userNameLogedIn;
    private ArrayList<ItemData> cartProductList;
    private RecyclerView cartProduct;
    private CartAdapter cartAdapter;
    Button shopNow;
   ImageView back;
             private TextView totalObjects;

             @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shopNow  = (Button) findViewById(R.id.shopNow);
        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopNow = new Intent(CartDetails.this, ListViewActivity.class);
                startActivity(shopNow);
            }
        });
                 back = (ImageView) findViewById(R.id.back);
                 back.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
Intent i = new Intent(CartDetails.this, ListViewActivity.class);
                     startActivity(i);}
                 });
        cartProductList = new ArrayList<ItemData>();
        JSONArray cart_dataJson = new JSONArray();

        JSONObject data = new JSONObject();
        DBAdapter db = null;
        Cursor cursor = null;

        addMoreObject = (TextView) findViewById(R.id.addMoreProduct);
        minusMoreObjects = (TextView) findViewById(R.id.minusProduct);

        try {

            db = new DBAdapter(getBaseContext());
            db.open();


            //  Log.d("totalccosting", ""+totalCost);
            //  Log.d("curosr1countinsgoldf", ""+cursor1.getCount());
            cursor = db.fetchData();
            Log.d("cursorCount", ""+cursor.getCount());
            if (cursor.getCount()!=0) {
                do {
                    data = new JSONObject();
                    data.put("itemNameValue", cursor.getString(cursor.getColumnIndex("itemName")));

                    data.put("count", cursor.getString(cursor.getColumnIndex("count")));
                    data.put("endDate", cursor.getString(cursor.getColumnIndex("endDate")));
                    data.put("icon", cursor.getString(cursor.getColumnIndex("icon")));

                    cart_dataJson.put(data);
                } while (cursor.moveToNext());
            }
            //  Log.e("entity_id", cursor.getString(cursor.getColumnIndex("count")));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            db.close();
            cursor.close();
        }

        Log.e("jsonLength", cart_dataJson.length() + "");
        String cart_data = cart_dataJson.toString();
//Log.d("cart_data", ""+cart_data);
        if (cart_dataJson.length()==0){
            LinearLayout noItem = (LinearLayout) findViewById(R.id.noItem);
            noItem.setVisibility(View.VISIBLE);
            totalObjects = (TextView) findViewById(R.id.tab_counter_text);
            totalObjects.setVisibility(View.GONE);


        }else {
            try {

                final int numberOfItemsInResp =cart_dataJson.length();
                totalObjects = (TextView) findViewById(R.id.tab_counter_text);
                totalObjects.setVisibility(View.VISIBLE);
totalObjects.setText(String.valueOf(numberOfItemsInResp));
                for (int i =0; i < numberOfItemsInResp; i++){
                    JSONObject cartData = cart_dataJson.getJSONObject(i);
                    // Log.d("CartData", ""+cartData);
                    ItemData itemData = new ItemData();
                    if (cartData.has("itemNameValue")){
                        String pname = cartData.getString("itemNameValue");
                        itemData.setName(pname);
                    }
                    if (cartData.has("endDate")){
                        String sprice = cartData.getString("endDate");

                       itemData.setEndDate(sprice);
                    }
                    if (cartData.has("count")){
                        String count = cartData.getString("count");
                        itemData.setCount(count);
                    }

                    if (cartData.has("icon")){
                        String pimage = cartData.getString("icon");
                        itemData.setIcon(pimage);

                    }

                    cartProductList.add(itemData);

                }

                cartProduct= (RecyclerView) findViewById(R.id.cartObject);

                LinearLayoutManager verticalLayoutmanager
                        = new LinearLayoutManager(CartDetails.this, LinearLayoutManager.VERTICAL, false);
                verticalLayoutmanager.setAutoMeasureEnabled(true);
               totalObjects = (TextView) findViewById(R.id.tab_counter_text);
                cartAdapter = new CartAdapter(cartProductList, context, totalObjects);
                cartProduct.setAdapter(cartAdapter);
                cartProduct.setLayoutManager(verticalLayoutmanager);



            } catch (JSONException e) {


            }}}




            @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Intent backIntent = new Intent(CartDetails.this, ListViewActivity.class);
            startActivity(backIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
