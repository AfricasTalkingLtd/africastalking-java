package com.africastalking.insights;

import com.africastalking.ATCash;
import com.google.gson.Gson;


public class SwapResponse {

    public String status;
    public String requestId;
    public ATCash cost;
    public PhoneNumber phoneNumber;

    static class PhoneNumber {
        public String carrierName;
        public int countryCode;
        public String networkCode;
        public String number;
        public String numberType;
        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
