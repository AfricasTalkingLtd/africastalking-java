package com.africastalking.chat;

final public class TextMessageBody extends MessageBody {
    public String text;
    public TextMessageBody(String text) {
        super("Text");
        this.text = text;
    }
}
