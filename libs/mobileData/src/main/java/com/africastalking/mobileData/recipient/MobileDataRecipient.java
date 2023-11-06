package com.africastalking.mobileData.recipient;

import java.util.HashMap;

public class MobileDataRecipient {

    public static enum DataUnit { MB, GB }

    public static enum DataValidity { Day, Week, Month }

    public String phoneNumber;
    public int quantity;
    public DataUnit unit;
    public DataValidity validity;
    public HashMap<String, String> metadata = new HashMap<>();

    public MobileDataRecipient(String phoneNumber, int quantity, DataUnit unit, DataValidity validity) {
        this.phoneNumber = phoneNumber;
        this.quantity = quantity;
        this.unit = unit;
        this.validity = validity;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
