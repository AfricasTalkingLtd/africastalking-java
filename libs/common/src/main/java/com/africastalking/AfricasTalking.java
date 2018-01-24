package com.africastalking;

public final class AfricasTalking {

    private static final String BASE_PACKAGE = AfricasTalking.class.getPackage().getName();

    public static final String SERVICE_ACCOUNT = BASE_PACKAGE + ".AccountService";
    public static final String SERVICE_SMS = BASE_PACKAGE + ".SmsService";
    public static final String SERVICE_VOICE = BASE_PACKAGE + ".VoiceService";
    public static final String SERVICE_USSD = BASE_PACKAGE + ".UssdService";
    public static final String SERVICE_AIRTIME = BASE_PACKAGE + ".AirtimeService";
    public static final String SERVICE_PAYMENT = BASE_PACKAGE + ".PaymentService";
    public static final String SERVICE_TOKEN = BASE_PACKAGE + ".TokenService";


    @Deprecated
    public static String hostOverride = null;

    static String sUsername, sApiKey;

    
    /**
     * Initialize the SDK
     * @param username app username
     * @param apiKey app API key
     */
    public static void initialize(String username, String apiKey){

        destroyAllServices();

        // Init
        sUsername = username;
        sApiKey = apiKey;
        Service.isSandbox = username.toLowerCase().contentEquals("sandbox");
    }

    /**
     * Set request logger
     * @param logger logger object
     */
    public static void setLogger(Logger logger) {
        Service.LOGGER = logger;
    }

    /**
     * Get a service by class. e.g. AirtimeService.class
     * @param classInfo service class
     * @param <T> service type
     * @return An instance of the requested service
     */
    public static <T extends Service> T getService(Class<T> classInfo) {
        try {
            T raw = classInfo.newInstance();

            if (sApiKey == null || sUsername == null) {
                return raw;
            }
            return (T)raw.getInstance(sUsername, sApiKey);
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
     * @param <T> service type
     * @return An instance of the requested service
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
                getService(SERVICE_PAYMENT),
                getService(SERVICE_TOKEN)
        };
        for (Service service:services) {
            if (service != null && service.isInitialized()) {
                service.destroyService();
            }
        }
    }

}