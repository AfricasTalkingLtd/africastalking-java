package com.africastalking.payment.recipient;

import com.google.gson.Gson;

import java.util.HashMap;


/**
 * Business-type payment recipient, used in B2B transactions.
 */
public final class Business {

    /*
     * Payment Providers
     */
    public static final String PROVIDER_MPESA = "Mpesa";
    public static final String PROVIDER_ATHENA = "Athena";

    /*
     * Transfer Types
     */
    public static final String TRANSFER_TYPE_BUYGOODS = "BusinessBuyGoods";
    public static final String TRANSFER_TYPE_PAYBILL = "BusinessPayBill";
    public static final String TRANSFER_TYPE_DISBURSE = "DisburseFundsToBusiness";
    public static final String TRANSFER_TYPE_B2B = "BusinessToBusinessTransfer";


    public float amount;
    public String provider;
    public String currencyCode;
    public String transferType;
    public String destinationChannel;
    public String destinationAccount = null;
    public String requester = null;
    public HashMap<String, String> metadata = new HashMap<>();

    /**
     *
     * Business-type payment recipient, used in B2B transactions.
     *
     * @param destinationChannel Name or number of the channel that will receive payment by the provider.
     *                           e.g. Mobile Provider's Paybill or Buy Goods number that belongs to the business.
     * @param destinationAccount Account name used by the business to receive money on the provided @{param destinationAccount}.
     * @param requester PhoneNumber through which KPLC will send tokens when using B2B to buy electricity tokens.
     * @param transferType Nature of transaction. e.g. {@link com.africastalking.payment.recipient.Business Business.TRANSFER_TYPE_BUYGOODS}
     * @param provider Payment Provider. e.g. {@link com.africastalking.payment.recipient.Business Business.PROVIDER_MPESA}
     * @param amount Amount to transact, along with the currency code. e.g. USD 3435
     */
    public Business(String destinationChannel, String destinationAccount, String requester, String transferType, String provider, String curencyCode, float amount) {
        this.amount = amount;
        this.provider = provider;
        this.currencyCode = curencyCode;
        this.transferType = transferType;
        this.destinationChannel = destinationChannel;
        this.requester = requester;
        this.destinationAccount = destinationAccount;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
