package com.africastalking.payments.recipient;

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

        BusinessBuyGoods("BusinessBuyGoods"),
        BusinessPayBill("BusinessPayBill"),
        DisburseFundsToBusiness("DisburseFundsToBusiness"),
        BusinessToBusinessTransfer("BusinessToBusinessTransfer");

        private final String text;

        private TransferType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        /**
         * 
         */
        public static TransferType fromString(String text) {
            for (TransferType type : TransferType.values()) {
              if (type.text.contentEquals(text)) {
                return type;
              }
            }
            return null;
          }

    }

    public String currencyCode;
    public float amount;
    public String provider = Provider.ATHENA.toString();
    public String transferType;
    public String destinationChannel;
    public String destinationAccount = null;
    public HashMap<String, String> metadata = new HashMap<>();

    public Business(String destinationChannel, String destinationAccount, TransferType transferType, String amount) {
        this.transferType = transferType.toString();
        this.destinationChannel = destinationChannel;
        this.destinationAccount = destinationAccount;

        String[] amountParts = amount.trim().split(" ");
        this.currencyCode = amountParts[0];
        this.amount = Float.parseFloat(amountParts[1]);
    }

}
