package com.africastalking.chat;

final public class MediaMessageBody extends MessageBody {
    public String media;
    public String url;

    public MediaMessageBody(String mediaType, String url) {
        super("Media");
        this.media = mediaType;
        this.url = url;
    }
}
