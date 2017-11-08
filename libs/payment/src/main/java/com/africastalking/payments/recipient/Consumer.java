package com.africastalking.payments.recipient;

import java.util.HashMap;

/**
 * Consumer-type payment recipient, used in B2C transactions
 */
public class Consumer {

    /*
     * Payment Reasons
     */
    public static final String REASON_SALARY = "SalaryPayment";
    public static final String REASON_SALARY_WITH_CHARGE = "SalaryPaymentWithWithdrawalChargePaid";
    public static final String REASON_BUSINESS = "BusinessPayment";
    public static final String REASON_BUSINESS_WITH_CHARGE = "BusinessPaymentWithWithdrawalChargePaid";
    public static final String REASON_PROMOTION = "PromotionPayment";

    public String name;
    public String phoneNumber;
    public String currencyCode;
    public float amount;
    public String providerChannel;

    public String reason;
    public HashMap<String, String> metadata = new HashMap<>();

    /**
     * Consumer-type payment recipient, used in B2C transactions
     * @param name Consumer name
     * @param phoneNumber Consumer phone number
     * @param amount Amount to transact, along with the currency code. e.g. KES 345
     * @param reason Purpose for the payment. e.g. {@link com.africastalking.payments.recipient.Consumer Consumer.REASON_SALARY}
     */
    public Consumer(String name, String phoneNumber, String amount, String reason) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.reason = reason;

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            this.currencyCode = currenciedAmount[0];
            this.amount = Float.parseFloat(currenciedAmount[1]);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
