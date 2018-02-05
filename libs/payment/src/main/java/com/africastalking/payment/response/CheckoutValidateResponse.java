package com.africastalking.payment.response;

import com.google.gson.Gson;

public final class CheckoutValidateResponse {

    /**
     * Status
     */
    public String status;

    /**
     * Status description
     */
    public String description;

    /**
     * Optional checkout token
     */
    public String checkoutToken;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
