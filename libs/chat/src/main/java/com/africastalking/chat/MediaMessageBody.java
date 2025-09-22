package com.africastalking.chat;

final public class MediaMessageBody extends MessageBody {
    public String mediaType;
    public String url;
    public String caption;

    public enum MediaType {
        Image,
        Audio,
        Video,
        Voice,
        Document
    }


    public MediaMessageBody(MediaType mediaType, String url) {
        this.mediaType = mediaType.name();
        this.url = url;
        this.caption = null;
    }

    public MediaMessageBody(MediaType mediaType, String url, String caption) {
        this(mediaType, url);
        this.caption = caption;
    }
}
