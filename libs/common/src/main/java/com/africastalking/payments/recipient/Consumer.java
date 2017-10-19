package com.africastalking.payments.recipient;

import java.util.HashMap;

public class Consumer {

    public enum Reason {
        SalaryPayment("SalaryPayment"),
        SalaryPaymentWithWithdrawalChargePaid("SalaryPaymentWithWithdrawalChargePaid"),
        BusinessPayment("BusinessPayment"),
        BusinessPaymentWithWithdrawalChargePaid("BusinessPaymentWithWithdrawalChargePaid"),
        PromotionPayment("PromotionPayment");

        private final String text;

        private Reason(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }

    public String name;
    public String phoneNumber;
    public String currencyCode;
    public float amount;
    public String providerChannel;

    public Reason reason = null;
    public HashMap<String, String> metadata = new HashMap<>();

    public Consumer(String name, String phoneNumber, String amount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        String[] amountParts = amount.trim().split(" ");
        this.currencyCode = amountParts[0];
        this.amount = Float.parseFloat(amountParts[1]);
    }

}
