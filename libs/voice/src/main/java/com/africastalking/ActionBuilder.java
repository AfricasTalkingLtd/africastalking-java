package com.africastalking;

import com.africastalking.voice.action.*;
import com.africastalking.voice.action.Record;

/**
 * Voice Actions builder
 */
public class ActionBuilder {

    private Boolean finalized = false;
    private StringBuilder xml;

    public ActionBuilder() {
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response>");
    }

    public String build() {
        finalized = true;
        xml.append("</Response>");
        return xml.toString();
    }

    private ActionBuilder action(Action action) {
        if (finalized) throw new RuntimeException("This builder has been finalized by a call to build()");
        xml.append(action.build());
        return this;
    }

    public ActionBuilder say(Say action) {
        return this.action(action);
    }

    public ActionBuilder play(Play action) {
        return this.action(action);
    }

    public ActionBuilder dial(Dial action) {
        return this.action(action);
    }

    public ActionBuilder enqueue(Enqueue action) {
        return this.action(action);
    }

    public ActionBuilder dequeue(Dequeue action) {
        return this.action(action);
    }

    public ActionBuilder conference(Conference action) {
        return this.action(action);
    }

    public ActionBuilder redirect(Redirect action) {
        return this.action(action);
    }

    public ActionBuilder reject(Reject action) {
        return this.action(action);
    }

    public ActionBuilder record(Record action) {
        return this.action(action);
    }

    public ActionBuilder getDigits(GetDigits action) {
        return this.action(action);
    }
}

