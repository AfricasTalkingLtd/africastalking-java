package com.africastalking;


public final class USSDService extends Service {

    public static final String SESSION_CONTINUE = "CON";
    public static final String SESSION_END = "END";

    public static final String FLAG_SESSION_ID = "sessionId";
    public static final String FLAG_SERVICE_CODE = "serviceCode";
    public static final String FLAG_PHONE_NUMBER = "phoneNumber";
    public static final String FLAG_TEXT = "text";


    private static USSDService sInstance;

    private USSDService(String username, String apiKey) {
        super(username, apiKey);
    }

    USSDService() {
        super();
    }

    @Override
    protected USSDService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new USSDService(username, apiKey);
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
