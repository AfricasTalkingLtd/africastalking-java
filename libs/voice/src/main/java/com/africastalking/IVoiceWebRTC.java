package com.africastalking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.africastalking.voice.CapabilityTokenRequest;
import com.africastalking.voice.CapabilityTokenResponse;

public interface IVoiceWebRTC {

    @Headers("Content-Type: application/json")
    @POST("capability-token/request")
    Call<CapabilityTokenResponse> requestCapabilityToken(@Body CapabilityTokenRequest request);

}
