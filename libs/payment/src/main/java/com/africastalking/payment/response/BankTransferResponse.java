package com.africastalking.payment.response;

import java.util.List;

public final class BankTransferResponse {
    public String errorMessage;
    public List<BankEntries> entries;

    public static final class BankEntries {
        public String accountNumber;
        public String status;
        public String transactionId;
        public String transactionFee;
        public String errorMessage;
    }
}
