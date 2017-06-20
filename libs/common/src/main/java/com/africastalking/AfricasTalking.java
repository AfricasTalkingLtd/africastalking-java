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
    private static Currency sCurrency;

    static Environment ENV = Environment.SANDBOX;
    static Boolean LOGGING = false;
    static Logger LOGGER = new BaseLogger();

    /**
     * Initialize the SDK
     * @param username
     * @param apiKey
     * @param format
     */
    public static void initialize(String username, String apiKey, Format format, Currency currency) {

        destroyAllServices();

        // Init
        sUsername = username;
        sApiKey = apiKey;
        sFormat = format;
        sCurrency = currency;
    }

    /**
     *
     * @param username
     * @param apiKey
     * @param format
     */
    public static void initialize(String username, String apiKey, Format format) {
        initialize(username, apiKey, format, Currency.KES);
    }

    /**
     *
     * @param username
     * @param apiKey
     */
    public static void initialize(String username, String apiKey) {
        initialize(username, apiKey, Format.XML, Currency.KES);
    }


    /**
     * Define environment
     * @param env
     */
    public static void setEnvironment(Environment env) {
        ENV = env;
    }

    /**
     * Enable/Disable logging
     * @param enable
     */
    public static void enableLogging(boolean enable) {
        LOGGING = enable;
    }

    /**
     * Set logger
     * @param logger
     */
    public static void setLogger(Logger logger) {
        if (logger != null) {
            enableLogging(true);
        }
        LOGGER = logger;
    }

    /**
     * Get a service by class. e.g. SMService.class
     * @param classInfo
     * @param <T>
     * @return
     */
    public static <T extends Service> T getService(Class<T> classInfo) {
        try {
            T raw = classInfo.newInstance();

            if (sApiKey == null || sUsername == null) {
                return raw;
            }
            return (T)raw.getInstance(sUsername, sApiKey, sFormat, sCurrency);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to init service");
    }

    /**
     * Get a service by name
     * @param serviceName see AfricasTalking.SERVICES_*
     * @param <T>
     * @return
     */
    public static <T extends Service> T getService(String serviceName) {

        try {
            Class<T> tClass = (Class<T>)Class.forName(serviceName);
            return getService(tClass);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Destroy all initialized services
     */
    private static void destroyAllServices() {
        Service services[] = new Service[] {
                getService(SERVICE_ACCOUNT),
                getService(SERVICE_VOICE),
                getService(SERVICE_SMS),
                getService(SERVICE_USSD),
                getService(SERVICE_AIRTIME),
                getService(SERVICE_PAYMENTS)
        };
        for (Service service:services) {
            if (service != null && service.isInitialized()) {
                service.destroyService();
            }
        }
    }

}