package com.africastalking.voice.action;

public class Dequeue extends Action {

    /**
     * Dequeue
     * @param name
     * @param phoneNumber
     */
    public Dequeue(String name, String phoneNumber) {
        this.tag = "Dequeue";
        this.attributes.put("phoneNumber", phoneNumber);
        if (name != null) {
            this.attributes.put("name", name);
        }
    }

    public Dequeue(String phoneNumber) {
        this(null, phoneNumber);
    }

}
