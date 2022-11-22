package com.africastalking.chat;

public class LocationMessageBody extends MessageBody {
    public float latitude;
    public float longitude;
    public LocationMessageBody(float latitude, float longitude) {
        super("Location");
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
