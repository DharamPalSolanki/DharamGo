package com.example.dharampalsolanki.dharamgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dharampalsolanki.dharamgo.DBAdapter;
import com.example.dharampalsolanki.dharamgo.R;
import com.example.dharampalsolanki.dharamgo.model.ItemData;


import java.util.ArrayList;

/**
 * Created by it on 2/10/2017.
 */

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.MyViewHolder> {
    private ArrayList<ItemData> dataSet;
Context context;
    TextView cartItemNumber;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
TextView endDate;
        ImageView itemIcon;
TextView count, addMoreProducts, minusProduct;
        public MyViewHolder(final View itemView, final TextView totalItem) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.name);

            itemIcon = (ImageView) itemView.findViewById(R.id.icon);
            endDate = (TextView) itemView.findViewById(R.id.endDate);
            addMoreProducts = (TextView) itemView.findViewById(R.id.addMoreProduct);
            minusProduct = (TextView) itemView.findViewById(R.id.minusProduct);
            count = (TextView) itemView.findViewById(R.id.countProduct);
addMoreProducts.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String itemNamevalue = itemName.getText().toString();
String endDate1 = endDate.getText().toString();
        DBAdapter db = null;
        try {

            Context context = itemView.getContext();
            db = new DBAdapter(context);
            db.open();

          int  count1 = db.getProductCount(itemNamevalue);


            if (count1 == 0) {
                count1++;



                db.insertdata(itemNamevalue, count1, endDate1, "icon");
                count.setText(String.valueOf(count1));
                Cursor cursor1 = db.fetchData();


                if (cursor1.getCount()==0){
                    totalItem.setVisibility(View.GONE);
                }
                else {
                    totalItem.setVisibility(View.VISIBLE);
                    totalItem.setText(String.valueOf(cursor1.getCount()));}

               /* TextView cartIcon = (TextView) findViewById(R.id.cartIconText);
                DBAdapter db1 = null;
                db1 = new DBAdapter(getBaseContext());
                db1.open();
                Cursor cursor1 = db1.fetchData();
                cartNumber = (TextView) findViewById(R.id.tab_counter_text);
                if (cursor1.getCount()==0){
                    cartNumber.setVisibility(View.GONE);
                }
                else {
                    cartNumber.setText(String.valueOf(cursor1.getCount()));}
*/
            } else {

                count1++;

                db.updateData(itemNamevalue, count1);
count.setText(String.valueOf(count1));
                Cursor cursor1 = db.fetchData();


                if (cursor1.getCount()==0){
                    totalItem.setVisibility(View.GONE);
                }
                else {
                    totalItem.setVisibility(View.VISIBLE);
                    totalItem.setText(String.valueOf(cursor1.getCount()));}

             /*   DBAdapter db1 = null;
                db1 = new DBAdapter(getBaseContext());
                db1.open();
                Cursor cursor1 = db1.fetchData();

                cartNumber = (TextView) findViewById(R.id.tab_counter_text);
                if (cursor1.getCount()==0){
                    cartNumber.setVisibility(View.GONE);
                }
                else {
                    cartNumber.setText(String.valueOf(cursor1.getCount()));}
                db1.close();*/
                //text_itemCount.setText(count1 + "");
            }
            // setbottombar();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }

        }
    }
});

            minusProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemNamevalue = itemName.getText().toString();
                    String endDate1 = endDate.getText().toString();
                    DBAdapter db = null;
                    try {

                        Context context = itemView.getContext();
                        db = new DBAdapter(context);
                        db.open();

                        int  count1 = db.getProductCount(itemNamevalue);


                        if (count1 > 1) {
                            count1--;



                            db.updateData(itemNamevalue, count1);
                            count.setText(String.valueOf(count1));
                            Cursor cursor1 = db.fetchData();


                            if (cursor1.getCount()==0){
                                totalItem.setVisibility(View.GONE);
                            }
                            else {
                                totalItem.setVisibility(View.VISIBLE);
                                totalItem.setText(String.valueOf(cursor1.getCount()));}

               /* TextView cartIcon = (TextView) findViewById(R.id.cartIconText);
                DBAdapter db1 = null;
                db1 = new DBAdapter(getBaseContext());
                db1.open();
                Cursor cursor1 = db1.fetchData();
                cartNumber = (TextView) findViewById(R.id.tab_counter_text);
                if (cursor1.getCount()==0){
                    cartNumber.setVisibility(View.GONE);
                }
                else {
                    cartNumber.setText(String.valueOf(cursor1.getCount()));}
*/
                        } else {
                          db.deleteData(itemNamevalue);
                            count.setText("0");

                Cursor cursor1 = db.fetchData();


                if (cursor1.getCount()==0){
                    totalItem.setVisibility(View.GONE);
                }
                else {
                    totalItem.setVisibility(View.VISIBLE);
                    totalItem.setText(String.valueOf(cursor1.getCount()));}

                            //text_itemCount.setText(count1 + "");
                        }
                        // setbottombar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (db != null) {
                            db.close();
                        }

                    }
                }
            });
        }
    }

    public ItemRecyclerAdapter(ArrayList<ItemData> data, Context context, TextView cartItem) {
        this.dataSet = data;
        this.context = context;
        this.cartItemNumber = cartItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);



       MyViewHolder myViewHolder = new MyViewHolder(view, cartItemNumber);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.itemName;

        textViewName.setVisibility(View.VISIBLE);
TextView endDate = holder.endDate;
        endDate.setText(dataSet.get(listPosition).getEndDate());
        textViewName.setText(dataSet.get(listPosition).getName());
ImageView imageView = holder.itemIcon;
     Glide.with(context).load(dataSet.get(listPosition).getIcon()).into(imageView);

        DBAdapter db = null;

        try {

            db = new DBAdapter(context);
            db.open();
            int i = db.getProductCount(dataSet.get(listPosition).getName());
            holder.count.setText(String.valueOf(i));
Log.d("productCount",""+i);

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            db.close();

        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }}

