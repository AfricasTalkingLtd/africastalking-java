package com.africastalking.chat;

import java.util.List;

final public class Template {
    public String name;
    public List<String> params;
    public Template(String name, List<String> params) {
        this.name = name;
        this.params = params;
    }
}
