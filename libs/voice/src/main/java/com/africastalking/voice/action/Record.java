package com.africastalking.voice.action;

import java.net.URL;

public class Record extends Action {

    public Record() {
        init(null, null, 0, 0, false, false, null);
    }

    public Record(String finishOnKey, int maxLength, int timeout, boolean trimSilence, boolean playBeep, URL callbackUrl) {
        init(null, finishOnKey, maxLength, timeout, trimSilence, playBeep, callbackUrl);
    }

    public Record(Say say, String finishOnKey, int maxLength, int timeout, boolean trimSilence, boolean playBeep, URL callbackUrl) {
        init(say, finishOnKey, maxLength, timeout, trimSilence, playBeep, callbackUrl);
    }

    public Record(Play play, String finishOnKey, int maxLength, int timeout, boolean trimSilence, boolean playBeep, URL callbackUrl) {
        init(play, finishOnKey, maxLength, timeout, trimSilence, playBeep, callbackUrl);
    }

    private void init(Action action, String finishOnKey, int maxLength, int timeout, boolean trimSilence, boolean playBeep, URL callbackUrl) {
        this.tag = "Record";

        if (action != null) {
            this.children.add(action);
        }

        if (finishOnKey != null) {
            this.attributes.put("finishOnKey", finishOnKey);
        }
        if (maxLength > 0) {
            this.attributes.put("maxLength", String.valueOf(maxLength));
        }
        if (timeout > 0) {
            this.attributes.put("timeout", String.valueOf(timeout));
        }
        if (trimSilence) {
            this.attributes.put("trimSilence", "true");
        }
        if (playBeep) {
            this.attributes.put("playBeep", "true");
        }
        if (callbackUrl != null) {
            this.attributes.put("callbackUrl", callbackUrl.toString());
        }
    }
}
