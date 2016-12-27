package com.africastalking;

public enum Format {

    JSON("application/json"),
    XML("application/xml");

    private final String text;

    private Format(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
