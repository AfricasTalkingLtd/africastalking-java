package com.africastalking.payment;

/**
 * A payment card
 */
public final class PaymentCard {

    public long number;
    public int cvvNumber;
    public String expiryMonth;
    public int expiryYear;
    public String countryCode;
    public String authToken;

    /**
     * A payment card
     * @param number Card number (PAN)
     * @param cvvNumber 3-4 Card Verification Value
     * @param expiryMonth The 3-letter expiration month on the card. e.g. Jan, Dec, Sep
     * @param expiryYear Card expiration year e.g. 23 for 2023
     * @param countryCode <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2ISO">3166-1 alpha-2</a> country code e.g. NG for Nigeria
     * @param authToken PIN from card owner
     */
    public PaymentCard(long number, int cvvNumber, String expiryMonth, int expiryYear, String countryCode, String authToken) {
        this.number = number;
        this.cvvNumber = cvvNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.countryCode = countryCode;
        this.authToken = authToken;
    }
}