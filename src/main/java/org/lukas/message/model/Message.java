package org.lukas.message.model;

// TODO: Override toString
public class Message {
    static final int MAX_SIZE = Integer.MAX_VALUE; // TODO: Ogarnąć max size, bo nie da sie zrobic unsigned

    private MessageType messageType;
    private int contentLength;
    private String content;

    public String getContent() {
        return content;
    }

    public int getContentLength() {
        return contentLength;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message(MessageType messageType) {
        this.messageType = messageType;
        this.content = "";
    }

    public Message(MessageType messageType, String content) {
        if (content.length() > MAX_SIZE) {
            throw new IllegalArgumentException("Message content is too big, should be smaller than " + MAX_SIZE);
        }

        this.content = content;
        this.contentLength = content.length();
        this.messageType = messageType;
    }
}
