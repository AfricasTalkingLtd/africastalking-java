package com.africastalking.voice.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Action {

    protected String tag;
    protected String text;
    protected HashMap<String, String> attributes = new HashMap<>();
    protected List<Action> children = new ArrayList<>();

    public String build() {
        StringBuilder str = new StringBuilder();
        str.append("<" + tag);

        if (!attributes.isEmpty()) {
            Iterator<String> it = attributes.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                str.append(" " + key + "=\"" + attributes.get(key) + "\"");
            }
        }

        // Can't have text and children ??
        if (!children.isEmpty()) {
            str.append(">");
            for (Action child : children) {
                str.append(child.build());
            }
            str.append("</" + tag + ">");
        } else { // text
            if (text != null) {
                str.append(">" + text + "</" + tag + ">");
            } else {
                str.append("/>");
            }
        }

        return str.toString();
    }
}
