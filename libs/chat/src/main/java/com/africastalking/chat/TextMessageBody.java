package com.africastalking.chat;

final public class TextMessageBody extends MessageBody {
    public String message;
    public TextMessageBody(String text) {
        this.message = text;
    }
}
