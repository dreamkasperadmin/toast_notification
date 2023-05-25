package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public  static Retrofit retrofit;
    public  static Retrofit  getRetroFitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl("https://588f-45-115-53-172.ngrok-free.app").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
