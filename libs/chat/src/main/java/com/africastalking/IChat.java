package com.africastalking;


import com.africastalking.chat.*;

import retrofit2.Call;
import retrofit2.http.*;

interface IChat {

    @Headers("Content-Type: application/json")
    @POST("whatsapp/message/send")
    Call<ChatResponse> sendMessage(@Body ChatMessage message);


    @Headers("Content-Type: application/json")
    @POST("whatsapp/template/send")
    Call<ChatResponse> sendTemplate(@Body ChatMessage message);
}
