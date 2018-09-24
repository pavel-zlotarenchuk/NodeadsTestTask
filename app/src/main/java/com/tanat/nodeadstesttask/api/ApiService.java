package com.tanat.nodeadstesttask.api;

import com.tanat.nodeadstesttask.model.Declarations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of DataList
    */
    @GET("/v1/declaration/")
    Call<Declarations> getDeclarations(@Query("q") String q);
}