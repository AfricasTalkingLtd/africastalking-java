package com.africastalking;

import com.africastalking.voice.CallResponse;
import com.africastalking.voice.CallTransferResponse;
import com.africastalking.voice.QueuedCallsResponse;

import retrofit2.Call;
import retrofit2.http.*;

interface IVoice {

    @FormUrlEncoded
    @POST("call")
    Call<CallResponse> call(@Field("username") String username, @Field("to") String to,
                            @Field("from") String from);

    @FormUrlEncoded
    @POST("callTransfer")
    Call<CallTransferResponse> callTransfer(@Field("username") String username, @Field("phoneNumber") String phoneNumber,
                                            @Field("sessionId") String sessionId);

    @FormUrlEncoded
    @POST("callTransfer")
    Call<CallTransferResponse> callTransfer(@Field("username") String username, @Field("phoneNumber") String phoneNumber,
                                    @Field("sessionId") String sessionId, @Field("callLeg") String callLeg);

    @FormUrlEncoded
    @POST("callTransfer")
    Call<CallTransferResponse> callTransfer(@Field("username") String username, @Field("phoneNumber") String phoneNumber,
                                    @Field("sessionId") String sessionId, @Field("callLeg") String callLeg,
                                    @Field("holdMusicUrl") String holdMusicUrl);

    @FormUrlEncoded
    @POST("queueStatus")
    Call<QueuedCallsResponse> queueStatus(@Field("username") String username, @Field("phoneNumbers") String phoneNumbers);

    @FormUrlEncoded
    @POST("mediaUpload")
    Call<String> mediaUpload(@Field("username") String username, @Field("url") String url,
                            @Field("phoneNumber") String phoneNumber);

}
