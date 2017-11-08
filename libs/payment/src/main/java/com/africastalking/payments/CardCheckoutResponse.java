package com.africastalking.payments;


public class CardCheckoutResponse extends CheckoutResponse {
    public String checkoutToken;

    public CardCheckoutResponse(String transactionId, String status, String description, String checkoutToken) {
        super(transactionId, status, description);
        this.checkoutToken = checkoutToken;
    }
}
