package com.africastalking.voice.action;

import java.net.URL;

public class Play extends Action {
    /**
     * Play
     * @param url
     */
    public Play(URL url) {
        this.tag = "Play";
        this.attributes.put("url", url.toString());
    }
}
