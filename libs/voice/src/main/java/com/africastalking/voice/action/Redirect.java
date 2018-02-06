package com.africastalking.voice.action;

import java.net.URL;

public class Redirect extends Action {

    /**
     * Redirect
     * @param url
     */
    public Redirect(URL url) {
        this.tag = "Redirect";
        this.text = url.toString();
    }
}
