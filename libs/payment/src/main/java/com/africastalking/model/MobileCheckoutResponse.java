package com.africastalking.model;


public class MobileCheckoutResponse {

    public static final String STATUS_PENDING = "PendingConfirmation";
    public static final String STATUS_INVALID_REQUEST = "InvalidRequest";
    public static final String STATUS_NOT_SUPPORTED = "NotSupported";
    public static final String STATUS_FAILED = "Failed";

    public String transactionId;
    public String status;
    public String description;

    /**
     * Response received on mobile checkout
     * @param transactionId Transaction unique ID
     * @param status Transaction status e.g. {@link com.africastalking.model.MobileCheckoutResponse.STATUS_PENDING MobileCheckoutResponse.STATUS_PENDING}
     * @param description Description of the request
     */
    public MobileCheckoutResponse(String transactionId, String status, String description) {
        this.transactionId = transactionId;
        this.status = status;
        this.description = description;
    }
}
