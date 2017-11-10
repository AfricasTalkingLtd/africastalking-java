package com.africastalking;


public final class UssdService extends Service {

    public static final String SESSION_CONTINUE = "CON";
    public static final String SESSION_END = "END";

    public static final String FLAG_SESSION_ID = "sessionId";
    public static final String FLAG_SERVICE_CODE = "serviceCode";
    public static final String FLAG_PHONE_NUMBER = "phoneNumber";
    public static final String FLAG_TEXT = "text";


    private static UssdService sInstance;

    private UssdService(String username, String apiKey) {
        super(username, apiKey);
    }

    UssdService() {
        super();
    }

    @Override
    protected UssdService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new UssdService(username, apiKey);
        }

        return sInstance;
    }

    @Override
    protected void initService() { }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

}
