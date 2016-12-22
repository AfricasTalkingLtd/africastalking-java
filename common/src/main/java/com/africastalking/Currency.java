package com.africastalking;


public enum Currency {

    KES("KES"),
    UGX("UGX"),
    NGN("NGN"),
    TZS("TZS"),
    RWF("RWF"),
    MWK("MWK"),
    USD("USD"),
    CDF("CDF");

    private final String text;

    private Currency(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
