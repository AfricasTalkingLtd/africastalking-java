package com.africastalking;


public final class PaymentsService extends Service {

    @Override
    protected <T extends Service> T getInstance(String username, String apiKey, Format format, Currency currency) {
        return null;
    }

    @Override
    protected boolean isInitialized() {
        return false;
    }

    @Override
    protected void initService() {

    }

    @Override
    protected void destroyService() {

    }
}
