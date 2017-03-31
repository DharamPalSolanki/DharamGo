package com.example.dharampalsolanki.dharamgo.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DharampalSolanki on 3/30/2017.
 */

public class ListItem {
    @SerializedName("data")
    JsonArray data;
public ListItem(){}
    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
