package com.africastalking;


public final class AfricasTalking {



    private static AccountService mAccount;

    public static void initialize(String username, String apiKey, Format format, boolean debug) {


        mAccount = new AccountService(username, apiKey, format, debug);

    }

    public static void initialize(String username, String apiKey, Format format) {
        initialize(username, apiKey, format, false);
    }

    public static void initialize(String username, String apiKey) {
        initialize(username, apiKey, Format.JSON, false);
    }

    /*public SMSService getSMSService() {

    }

    public VoiceService getVoiceService() {

    }

    public AirtimeService getAirtimeService() {

    }

    public PaymentsService getPaymentsService() {

    }

    public USSDService getUSSDService() {

    }*/

    public static AccountService getAccountService() {
        return mAccount;
    }

}
