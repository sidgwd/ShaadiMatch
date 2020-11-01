package com.shaadi.challenge.retrofit.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
//https://randomuser.me/api/?results=10
    @GET("api/")
    Call<ResponseBody> fetchUser(@Query("results") String count);

}
