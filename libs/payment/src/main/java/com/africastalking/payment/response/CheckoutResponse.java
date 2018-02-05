package com.africastalking.payment.response;


import com.google.gson.Gson;

/**
 * Response received checkout
 */
public final class CheckoutResponse {

    /**
     * Unique transaction ID
     */
    public String transactionId;
    /**
     * Transaction status e.g. CheckoutResponse.STATUS_PENDING
     */
    public String status;

    /**
     * Status description
     */
    public String description;


    /**
     * Optional checkout token
     */
    public String checkoutToken = null;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
