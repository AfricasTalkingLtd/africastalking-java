package com.africastalking.chat;

import java.util.List;

public final class TextMessageTemplate extends MessageBody {
    public Template template;
    public TextMessageTemplate(String name, List<String> params) {
        super("Text");
        this.template = new Template(name, params);
    }

    public TextMessageTemplate(Template template) {
        this(template.name, template.params);
    }
}
