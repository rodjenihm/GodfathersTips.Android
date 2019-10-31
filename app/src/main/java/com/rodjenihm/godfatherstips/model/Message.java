package com.rodjenihm.godfatherstips.model;

import java.util.Date;

public class Message {
    private String messageId;
    private String text;
    private Date time;
    private String senderEmail;

    public Message(String messageId, String text, Date time, String senderEmail) {
        this.messageId = messageId;
        this.text = text;
        this.time = time;
        this.senderEmail = senderEmail;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Message withId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public Message withText(String text) {
        this.text = text;
        return this;
    }

    public Message withTime(Date time) {
        this.time = time;
        return this;
    }

    public Message withSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }
}
