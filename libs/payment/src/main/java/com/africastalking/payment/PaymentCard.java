package com.africastalking.payment;

import com.google.gson.Gson;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.util.Calendar;

import static org.valid4j.Assertive.*;

/**
 * A payment card
 */
public final class PaymentCard {

    public String number;
    public int cvvNumber;
    public int expiryMonth;
    public int expiryYear;
    public String countryCode;
    public String authToken;

    /*
    "number"      : "123456789000",
      "countryCode" : "NG",
      "cvvNumber"   : "654",
      "expiryMonth" : 12,
      "expiryYear"  : 2019,
      "authToken"   : "2345",
     */

    /**
     * A payment card
     * @param number Card number (PAN)
     * @param cvvNumber 3-4 Card Verification Value
     * @param expiryMonth Expiration month on the card. e.g. 11 for November
     * @param expiryYear Card expiration year e.g. 2023
     * @param countryCode <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2ISO">3166-1 alpha-2</a> country code e.g. NG for Nigeria
     * @param authToken PIN from card owner
     */
    public PaymentCard(String number, int cvvNumber, int expiryMonth, int expiryYear, String countryCode, String authToken) {

        require(number != null && number.matches("^\\d{12,19}$"), "Invalid card number %s", String.valueOf(number));
        require(String.valueOf(cvvNumber).matches("^\\d{3,4}$"), "Invalid cvv number %s", String.valueOf(cvvNumber));
        require(expiryMonth >= 1 && expiryMonth <= 12, "Invalid expiry month %s. Should be between 1 and 12", String.valueOf(expiryMonth));
        require(expiryYear >= Calendar.getInstance().get(Calendar.YEAR), "Invalid expiry year %s. Should be greater or equal to current year", String.valueOf(expiryYear));
        require(countryCode != null && countryCode.matches("^[A-Z]{2}$"), "Invalid country code %s. Should be a two letter country code. See https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2ISO", countryCode);
        require(authToken != null, "authToken is required");

        this.number = number;
        this.cvvNumber = cvvNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.countryCode = countryCode;
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}