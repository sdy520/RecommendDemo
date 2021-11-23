package com.example.recommenddemo;

import androidx.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RegisterApi {
    @GET("RegisterServlet")
    Call<ResponseBody> registerUser(@Query("username") String username,@Query("password") String password);
    @GET("RegisterServlet")
    LiveData<ResponseBody> registerUsers(@Query("username") String username, @Query("password") String password);
}
