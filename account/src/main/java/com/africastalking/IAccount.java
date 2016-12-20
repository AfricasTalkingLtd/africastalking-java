package com.africastalking;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IAccount {

    @GET("user")
    Call<String> fetchUser();

    /*@GET("user")
    Call<UserData> getUserData();*/

}
