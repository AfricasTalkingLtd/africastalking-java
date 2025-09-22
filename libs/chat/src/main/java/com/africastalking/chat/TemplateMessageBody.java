package com.africastalking.chat;

import java.util.ArrayList;
import java.util.List;

public final class TemplateMessageBody extends MessageBody {
    public String templateId;
    public String headerValue = null;
    public List<String> bodyValues = new ArrayList<String>();

    public TemplateMessageBody(String templateId, String header, List<String> body) {
        this.templateId = templateId;
        this.headerValue = header;
        this.bodyValues = body;
    }
}
