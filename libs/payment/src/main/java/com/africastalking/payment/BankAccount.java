package com.africastalking.payment;

/**
 * A bank account
 */
public final class BankAccount {

    public enum BankCode {
        FirstBank_NG(234001),
        ZenithBank_NG(234002),
        AccessBank_NG(234003),
        GTBank_NG(234004),
        Ecobank_NG(234005),
        Diamond_NG(234007),
        Providus_NG(234008),
        FirstCityMonument_NG(234006),
        CBA_KE(254001);

        private final int code;
        BankCode(int code) {
            this.code = code;
        }
    }

    public String accountName;
    public String accountNumber;
    public int bankCode;

    /**
     * A bank account
     * @param accountName Bank account name e.g. Odeyola LeGrand
     * @param accountNumber Bank account number e.g. 0982627488993
     * @param bankCode Bank code. See supported banks {@link com.africastalking.payment.BankAccount.BankCode BankCode}
     */
    public BankAccount(String accountName, String accountNumber, BankCode bankCode) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode.code;
    }
}