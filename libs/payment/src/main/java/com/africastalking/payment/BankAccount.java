package com.africastalking.payment;

/**
 * A bank account
 */
public final class BankAccount {

    public String accountName;
    public String accountNumber;
    public String bankName;
    public int bankCode;
    public String countryCode;

    /**
     * A bank account
     * @param accountName Bank account name e.g. Odeyola LeGrand
     * @param accountNumber Bank account number e.g. 0982627488993
     * @param countryCode <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2ISO">3166-1 alpha-2</a> country code e.g. NG for Nigeria
     * @param bankCode Bank code e.g. 8982
     * @param bankName Bank name e.g. Zenith Bank
     */
    public BankAccount(String accountName, String accountNumber, String countryCode, int bankCode, String bankName) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.countryCode = countryCode;
    }
}