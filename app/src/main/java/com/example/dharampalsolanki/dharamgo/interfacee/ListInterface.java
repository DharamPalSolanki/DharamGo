package com.example.dharampalsolanki.dharamgo.interfacee;

import com.example.dharampalsolanki.dharamgo.model.ListItem;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by DharampalSolanki on 3/30/2017.
 */

public interface ListInterface {
    @GET("service/v2/upcomingGuides/")
    Call<ListItem> getAllObjects();
}
