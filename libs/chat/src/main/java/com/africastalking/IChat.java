package com.africastalking;


import com.africastalking.chat.*;

import retrofit2.Call;
import retrofit2.http.*;

interface IChat {
    @POST("chat/consent")
    Call<ConsentResponse> sendConsent(@Body Consent consent);

    @POST("message/send")
    Call<ChatResponse> sendMessage(@Body ChatMessage message);
}
