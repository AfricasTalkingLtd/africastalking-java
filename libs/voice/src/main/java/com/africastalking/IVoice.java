package com.africastalking;

import retrofit2.Call;
import retrofit2.http.*;

interface IVoice {

    @FormUrlEncoded
    @POST("call")
    Call<String> call(@Field("username") String username, @Field("to") String to,
                      @Field("from") String from);

    @FormUrlEncoded
    @POST("/queueStatus")
    Call<String> queueStatus(@Field("username") String username, @Field("phoneNumbers") String phoneNumbers);

    @FormUrlEncoded
    @POST("/mediaUpload")
    Call<String> mediaUpload(@Field("username") String username, @Field("url") String url,
                            @Field("phoneNumber") String phoneNumber);

}
