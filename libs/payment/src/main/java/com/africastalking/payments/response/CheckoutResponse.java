package com.africastalking.payments.response;


/**
 * Response received checkout
 */
public class CheckoutResponse {

    public static final String STATUS_PENDING = "PendingConfirmation";
    public static final String STATUS_INVALID_REQUEST = "InvalidRequest";
    public static final String STATUS_NOT_SUPPORTED = "NotSupported";
    public static final String STATUS_FAILED = "Failed";

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

}
