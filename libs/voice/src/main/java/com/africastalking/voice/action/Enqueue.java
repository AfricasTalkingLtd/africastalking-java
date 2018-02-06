package com.africastalking.voice.action;

import java.net.URL;

public class Enqueue extends Action {

    /**
     * Enqueue
     * @param name
     * @param holdMusic
     */
    public Enqueue(String name, URL holdMusic) {
        this.tag = "Enqueue";
        if (holdMusic != null) {
            this.attributes.put("holdMusic", holdMusic.toString());
        }
        if (name != null) {
            this.attributes.put("name", name);
        }
    }

    public Enqueue() {
        this(null, null);
    }
}
