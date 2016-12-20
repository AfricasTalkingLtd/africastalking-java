package com.africastalking;


public final class SMS {

    private static SMS instance = new SMS();

    public static SMS getInstance() {
        return instance;
    }

    private SMS() {

    }
}
