package com.africastalking.payments;

public class BankAccount {
    public String accountName;
    public String accountNumber;
    public String bankName;
    public String countryCode;

    /**
     * A bank account
     * @param accountName Bank account name e.g. Odeyola LeGrand
     * @param accountNumber Bank account number e.g. 0982627488993
     * @param bankName Bank name e.g. Zenith Bank
     * @param countryCode <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2ISO">3166-1 alpha-2</a> country code e.g. NG for Nigeria
     */
    public BankAccount(String accountName, String accountNumber, String bankName, String countryCode) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.countryCode = countryCode;
    }
}