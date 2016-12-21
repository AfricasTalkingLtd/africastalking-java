package com.africastalking;


public final class AfricasTalking {

    private static final String BASE_PACKAGE = AfricasTalking.class.getPackage().getName();

    public static final String SERVICE_ACCOUNT = BASE_PACKAGE + ".AccountService";
    public static final String SERVICE_SMS = BASE_PACKAGE + ".SMSService";
    public static final String SERVICE_VOICE = BASE_PACKAGE + ".VoiceService";
    public static final String SERVICE_USSD = BASE_PACKAGE + ".USSDService";
    public static final String SERVICE_AIRTIME = BASE_PACKAGE + ".AirtimeService";
    public static final String SERVICE_PAYMENTS = BASE_PACKAGE + ".PaymentsService";


    private static String sUsername, sApiKey;
    private static Format sFormat;
    private static Boolean DEBUG;

    public static void initialize(String username, String apiKey, Format format, boolean debug) {
        DEBUG = debug;
        sUsername = username;
        sApiKey = apiKey;
        sFormat = format;
    }

    public static void initialize(String username, String apiKey, Format format) {
        initialize(username, apiKey, format, false);
    }

    public static void initialize(String username, String apiKey) {
        initialize(username, apiKey, Format.XML, false);
    }


    public static <T extends Service> T getService(Class<T> classInfo) {
        try {
            T raw = classInfo.newInstance();
            return (T)raw.getInstance(sUsername, sApiKey, sFormat, DEBUG);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to init service");
    }

    public static <T extends Service> T getService(String serviceName) {

        try {
            Class<T> tClass = (Class<T>)Class.forName(serviceName);
            return getService(tClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to init service");
    }

}