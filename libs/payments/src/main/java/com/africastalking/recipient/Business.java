package com.africastalking.recipient;

import com.africastalking.Currency;

import java.util.HashMap;

public class Business {

    public enum Provider {
        MPESA("Mpesa"),
        ATHENA("Athena");


        private final String text;

        private Provider(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }

    public enum TransferType {

        BUYGOODS("BusinessBuyGoods"),
        PAYBILL("BusinessPayBill"),
        DISBURSE("DisburseFundsToBusiness"),
        TRANSFER("BusinessToBusinessTransfer");

        private final String text;

        private TransferType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

    }

    public Currency currencyCode;
    public float amount;
    public String provider = Provider.ATHENA.toString();
    public String transferType;
    public String destinationChannel;
    public String destinationAccount = null;
    public HashMap<String, String> metadata = new HashMap<>();

    public Business(String destinationChannel, String destinationAccount, TransferType transferType, Currency currency, float amount) {
        this.transferType = transferType.toString();
        this.destinationChannel = destinationChannel;
        this.destinationAccount = destinationAccount;
        this.currencyCode = currency;
        this.amount = amount;
    }

}
