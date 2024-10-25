package org.lukas.parser;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;

import java.nio.ByteBuffer;

public class Parser {
    public static ByteBuffer encode(Message message) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(message.getContentLength() + 8);

        byte messageTypeByte = parseMessageType(message.getMessageType());

        byteBuffer.put(0, messageTypeByte);
        byteBuffer.putInt(4, message.getContentLength());
        byteBuffer.put(8, message.getContent().getBytes());

        return byteBuffer;
    }

    // TODO: Think about moving it to MessageType enum so the Parser doesnt depend on messageType that much
    public static byte parseMessageType(MessageType messageType) {
        switch (messageType) {
            case OK -> {
                return (byte) 0x1;
            }
            case WRITE -> {
                return (byte) 0x2;
            }
            case CLEAR -> {
                return (byte) 0x3;
            }
            case ERROR -> {
                return (byte) 0x4;
            }
            case PING -> {
                return (byte) 0x5;
            }
            default -> {
                return (byte) -0x1;
            }
        }
    }
}
