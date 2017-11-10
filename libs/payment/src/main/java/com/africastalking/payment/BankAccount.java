package com.africastalking.payment;

/**
 * A bank account
 */
public final class BankAccount {

    public enum BankCode {

        GTBank_NG(234004),
        Ecobank_NG(234005),
        Diamond_NG(234006),
        Providus_NG(234007),
        Unity_NG(234008),
        Stanbic_NG(234009),
        Sterling_NG(234010),
        Parkway_NG(234011),
        Afribank_NG(234012),
        Enterprise_NG(234013),
        Fidelity_NG(234014),
        Heritage_NG(234015),
        Keystone_NG(234016),
        Skye_NG(234017),
        Stanchart_NG(234018),
        Union_NG(234019),
        Uba_NG(234020),
        Wema_NG(234021),
        First_NG(234022),
        CBA_KE(254001);

        private final int code;
        BankCode(int code) {
            this.code = code;
        }
    }

    public String accountName;
    public String accountNumber;
    public int bankCode;
    public String dateOfBirth;

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