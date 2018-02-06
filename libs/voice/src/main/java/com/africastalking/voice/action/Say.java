package com.africastalking.voice.action;

public class Say extends Action {

    public enum Voice { MAN, WOMAN }

    /**
     * Say
     * @param text
     * @param playBeep
     * @param voice
     */
    public Say(String text, boolean playBeep, Voice voice) {
        this.tag = "Say";
        this.text = text;

        if (playBeep) {
            this.attributes.put("playBeep", "true");
        }

        if (voice != null) {
            this.attributes.put("voice", voice.name().toLowerCase());
        }
    }

    public Say(String text) {
        this(text, false, null);
    }

    public Say(String text, Voice voice) {
        this(text, false, voice);
    }

    public Say(String text, boolean playBeep) {
        this(text, playBeep, null);
    }

}
