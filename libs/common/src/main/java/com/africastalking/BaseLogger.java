package com.africastalking;

class BaseLogger implements Logger {
    @Override
    public void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
