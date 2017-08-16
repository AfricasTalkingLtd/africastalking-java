package com.africastalking.payments.recipient;

import com.africastalking.Currency;

import java.util.HashMap;

public class Consumer {

    public enum Reason {
        SALARY("SalaryPayment"),
        SALARY_WITH_CHARGE("SalaryPaymentWithWithdrawalChargePaid"),
        BUSINESS("BusinessPayment"),
        BUSINESS_WITH_CHARGE("BusinessPaymentWithWithdrawalChargePaid"),
        PROMOTION("PromotionPayment");

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
    public Currency currencyCode;
    public float amount;
    public String providerChannel;

    public Reason reason = null;
    public HashMap<String, String> metadata = new HashMap<>();

    public Consumer(String name, String phoneNumber, Currency currency, float amount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.currencyCode = currency;
        this.amount = amount;
    }

}
