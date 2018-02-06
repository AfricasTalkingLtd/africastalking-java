package com.africastalking.voice.action;

import java.net.URL;

public class GetDigits extends Action {

    /**
     * Get Digits
     * @param say
     * @param numDigits
     * @param finishOnKey
     * @param callbackUrl
     */
    public GetDigits(Say say, int numDigits, String finishOnKey, URL callbackUrl) {
        init(say, numDigits, finishOnKey, callbackUrl);
    }

    public GetDigits(Say say) {
        init(say, 0, null, null);
    }


    /**
     * Get Digits
     * @param play
     * @param numDigits
     * @param finishOnKey
     * @param callbackUrl
     */
    public GetDigits(Play play, int numDigits, String finishOnKey, URL callbackUrl) {
        init(play, numDigits, finishOnKey, callbackUrl);
    }

    public GetDigits(Play play) {
        init(play, 0, null, null);
    }

    private void init(Action child, int numDigits, String finishOnKey, URL callbackUrl) {
        this.tag = "GetDigits";

        if (numDigits > 0) {
            this.attributes.put("numDigits", String.valueOf(numDigits));
        }

        if (finishOnKey != null) {
            this.attributes.put("finishOnKey", finishOnKey);
        }

        if (callbackUrl != null) {
            this.attributes.put("callbackUrl", callbackUrl.toString());
        }

        this.children.add(child);
    }
}
