package com.example.recommenddemo.api;

import androidx.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginApi {
    @GET("LoginServlet")
    Call<ResponseBody> loginUser(@Query("username") String username, @Query("password") String password);
    @GET("LoginServlet")
    LiveData<ResponseBody> loginUsers(@Query("username") String username, @Query("password") String password);
}
