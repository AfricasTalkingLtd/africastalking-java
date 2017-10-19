package com.africastalking;


import java.net.URL;

public class VoiceMessage {


    private String xml;

    private VoiceMessage(String xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        return this.xml;
    }

    public static class Builder{

        private StringBuilder xml;

        public Builder() {
            xml = new StringBuilder();
            xml.append("<Response>");
        }

        public VoiceMessage build() {
            xml.append("</Response>");
            return new VoiceMessage(xml.toString());
        }

        public Builder say(String text, boolean playBeep, String voice) {
            xml.append("<Say playBeep=\""+ String.valueOf(playBeep) +"\" voice=\""+voice+"\"></Say>");
            return this;
        }

        public Builder say(String text) {
            return say(text, false, "woman");
        }

        public Builder play(URL url) {
            xml.append("<Play url=\""+ url.toString() +"\" />");
            return this;
        }

        public Builder getDigits(String text, URL url, int numDigits, long timeout, String finishOnKey, URL callbackUrl) {

            String str = "<GetDigits timeout=\"" + timeout + "\" finishOnKey=\""+finishOnKey+"\" " +
                    "numDigits=\"" + numDigits + "\" callbackUrl=\""+ callbackUrl +"\">";
            if (url == null) {
                str += "<Say>" + text + "</Say>";
            } else {
                str += "<Play url=\""+ url.toString() +"\" />";
            }

            str += "</GetDigits>";
            xml.append(str);

            return this;
        }

        public Builder getDigits(String text) {
            return getDigits(text, null, -1, -1, null, null);
        }

        public Builder getDigits(URL url) {
            return getDigits(null, url, -1, -1, null, null);
        }

        public Builder dial(String phoneNumbers, String ringbackTone, boolean record, boolean sequential, String callerId, long maxDuration) {
            xml.append("<Dial phoneNumbers=\"" + phoneNumbers + "\" ringbackTone=\"" + ringbackTone + "\" " +
                    "record=\""+String.valueOf(record)+"\" sequential=\"" + String.valueOf(sequential) + "\" " +
                    "callerId=\"" + callerId + "\" maxDuration=\"" + maxDuration + "\"/>");
            return this;
        }

        public Builder dial(String phoneNumbers) {
            return dial(phoneNumbers, null, false, false, null, -1 /* FIXME: What?? */);
        }

        public Builder enqueue(URL holdMusic, String name) {
            xml.append("<Dequeue holdMusic=\"" + holdMusic.toString() +"\" name=\"" + name + "\" />");
            return this;
        }

        public Builder enqueue(URL holdMusic) {
            return enqueue(holdMusic, null);
        }

        public Builder dequeue(String number, String name) {
            xml.append("<Dequeue phoneNumber=\""+number+"\" name=\"" + name + "\" />");
            return this;
        }

        public Builder dequeue(String number) {
            return dequeue(number, null);
        }

        public Builder conference() {
            xml.append("<Conference />");
            return this;
        }

        public Builder redirect(URL url) {
            xml.append("<Redirect>" + url.toString() + "</Redirect>");
            return this;
        }

        public Builder reject() {
            xml.append("<Reject />");
            return this;
        }

        public Builder record(String text, URL url, int maxLength, long timeout, String finishOnKey, boolean trimSilence, boolean playBeep, URL callbackUrl) {
            xml.append("<Record finishOnKey=\""+finishOnKey+"\" maxLength=\""+ maxLength +"\" timeout=\""+timeout+"\" trimSilence=\""+ String.valueOf(trimSilence) + "\"" +
            "playBeep=\"" + String.valueOf(playBeep) + "\" callbackUrl=\""+ callbackUrl +"\">");
            this.say(text);
            xml.append("</Record>");
            return this;
        }

        public Builder record() {
            xml.append("<Record />");
            return this;
        }

    }
}
