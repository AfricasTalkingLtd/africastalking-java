package com.africastalking.voice.action;

import java.net.URL;
import java.util.List;

public class Dial extends Action {
    /**
     * Dial
     * @param phoneNumbers
     * @param record
     * @param sequential
     * @param callerId
     * @param ringBackTone
     * @param maxDuration
     */
    public Dial(List<String> phoneNumbers, boolean record, boolean sequential, String callerId, URL ringBackTone, int maxDuration) {
        this.tag = "Dial";

        this.attributes.put("phoneNumbers", String.join(",", phoneNumbers));

        if (record) {
            this.attributes.put("record", "true");
        }
        if (sequential) {
            this.attributes.put("sequential", "true");
        }
        if (callerId != null) {
            this.attributes.put("callerId", callerId);
        }
        if (ringBackTone != null) {
            this.attributes.put("ringBackTone", ringBackTone.toString());
        }
        if (maxDuration > 0) {
            this.attributes.put("maxDuration", String.valueOf(maxDuration));
        }
    }

    public Dial(List<String> phoneNumbers) {
        this(phoneNumbers, false, false, null, null, -1);
    }
}
