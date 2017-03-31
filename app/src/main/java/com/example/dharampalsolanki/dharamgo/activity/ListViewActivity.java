package com.example.dharampalsolanki.dharamgo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dharampalsolanki.dharamgo.DBAdapter;
import com.example.dharampalsolanki.dharamgo.ListApiClient;
import com.example.dharampalsolanki.dharamgo.R;
import com.example.dharampalsolanki.dharamgo.adapter.ItemRecyclerAdapter;
import com.example.dharampalsolanki.dharamgo.interfacee.ListInterface;
import com.example.dharampalsolanki.dharamgo.model.ItemData;
import com.example.dharampalsolanki.dharamgo.model.ListItem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ListViewActivity extends AppCompatActivity
        {
    private ListInterface apiService;
            com.example.dharampalsolanki.dharamgo.activity.MainActivity mainActivity;
    private static final String TAG = com.example.dharampalsolanki.dharamgo.activity.MainActivity.class.getSimpleName();
    private ArrayList<ItemData> objectList;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private Context context = this;
    private ProgressDialog progressDialog;
ImageView cartIcon;
TextView logout;
            public static String tokenId;
            public static String email;
            private TextView cartItem;
            private RecyclerView itemRecycler;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
        logout = (TextView) findViewById(R.id.logout);

                cartIcon = (ImageView) findViewById(R.id.cartIcon);
                cartIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cartIntent = new Intent(ListViewActivity.this, com.example.dharampalsolanki.dharamgo.activity.CartDetails.class);
                        startActivity(cartIntent);
                    }
                });
                logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //mainActivity.signOut();
            }
        });
        Intent intent =getIntent();
        String name = intent.getStringExtra("name");
        tokenId = intent.getStringExtra("tokenId");
                Log.d("tokenId",""+tokenId);
        email = intent.getStringExtra("email");
        TextView emailText = (TextView) findViewById(R.id.user_email);
        emailText.setText(com.example.dharampalsolanki.dharamgo.activity.MainActivity.email);
        progressDialog = new ProgressDialog(ListViewActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Logging in");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        objectList = new ArrayList<ItemData>();

                    DBAdapter db = null;
                    db = new DBAdapter(getBaseContext());
                    db.open();

                    Cursor cursor1 = db.fetchData();
                    cartItem = (TextView) findViewById(R.id.tab_counter_text);
                    if (cursor1.getCount()==0){
                        cartItem.setVisibility(View.GONE);
                    }
                    else {
                        cartItem.setVisibility(View.VISIBLE);
                        cartItem.setText(String.valueOf(cursor1.getCount()));}

        apiService =
                ListApiClient.getClient().create(ListInterface.class);

getData();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("solanki", "solanki");
                        com.example.dharampalsolanki.dharamgo.activity.MainActivity maniActivity = com.example.dharampalsolanki.dharamgo.activity.MainActivity.getInstance();
                      boolean status =   maniActivity.signOut();
                        if (status == true){
                            Intent logoutintent = new Intent(ListViewActivity.this, com.example.dharampalsolanki.dharamgo.activity.MainActivity.class);
                            startActivity(logoutintent);
                        }
                      //  MainActivity.signOut();
                    }
                });
    }

    private void getData() {
        Call<ListItem> call = apiService.getAllObjects();
        call.enqueue(new Callback<ListItem>() {


            @Override
            public void onResponse(Call<ListItem>call, Response<ListItem> response) {

                JsonArray jsonArray = response.body().getData();
                try {
                    JSONArray data = new JSONArray(jsonArray.toString());
                    int l = data.length();
                    if (l!=0) {
                        progressDialog.show();
                        for (int i = 0; i < l; i++) {
                            JSONObject dataObject = data.getJSONObject(i);
                            ItemData itemData = new ItemData();
                            if (dataObject.has("name")){
                                String name = dataObject.getString("name");
                                itemData.setName(name);
                            }
                            if (dataObject.has("endDate")){
                                String endDate = dataObject.getString("endDate");
                                itemData.setEndDate(endDate);
                            }
                            if (dataObject.has("icon")){
                                String icon = dataObject.getString("icon");
                                itemData.setIcon(icon);


                            }
objectList.add(itemData);
                            cartItem = (TextView) findViewById(R.id.tab_counter_text);
                            LinearLayoutManager itemRecyclerLayout
                                    = new LinearLayoutManager(ListViewActivity.this, LinearLayoutManager.VERTICAL, false);
                            itemRecycler = (RecyclerView) findViewById(R.id.itemRecyclerView);
                            itemRecyclerAdapter = new ItemRecyclerAdapter(objectList, context,cartItem);
                            itemRecycler.setLayoutManager(itemRecyclerLayout);
                            itemRecycler.setAdapter(itemRecyclerAdapter);
                            progressDialog.dismiss();
             //               Log.d("dataObject", "" + dataObject);
                        }
                    }

                    Log.d("ObjectListSize", ""+objectList.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
           //     Log.d("jsonArrayData", ""+jsonArray);
            }
            @Override
            public void onFailure(Call<ListItem>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_view, menu);
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
            public void onResume(){
                super.onResume();
                cartItem = (TextView) findViewById(R.id.tab_counter_text);
                LinearLayoutManager itemRecyclerLayout
                        = new LinearLayoutManager(ListViewActivity.this, LinearLayoutManager.VERTICAL, false);
                itemRecycler = (RecyclerView) findViewById(R.id.itemRecyclerView);
                itemRecyclerAdapter = new ItemRecyclerAdapter(objectList, context,cartItem);
                itemRecycler.setLayoutManager(itemRecyclerLayout);
                itemRecycler.setAdapter(itemRecyclerAdapter);
                DBAdapter db = null;
                db = new DBAdapter(getBaseContext());
                db.open();

                Cursor cursor1 = db.fetchData();
                cartItem = (TextView) findViewById(R.id.tab_counter_text);
                if (cursor1.getCount()==0){
                    cartItem.setVisibility(View.GONE);
                }
                else {
                    cartItem.setVisibility(View.VISIBLE);
                    cartItem.setText(String.valueOf(cursor1.getCount()));}
            }

}
