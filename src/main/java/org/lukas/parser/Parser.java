package org.lukas.parser;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;

import java.nio.ByteBuffer;

public class Parser {
    public static byte[] encode(Message message) {
        int size = message.getContentLength() + 8;
        byte[] bytes = new byte[size];

        byte messageTypeByte = parseMessageType(message.getMessageType());
        byte[] contentLengthBytes = ByteBuffer.allocate(4).putInt(message.getContentLength()).array();
        byte[] contentBytes = message.getContent().getBytes();

        bytes[0] = messageTypeByte;
        System.arraycopy(contentLengthBytes, 0, bytes, 4, contentLengthBytes.length);
        System.arraycopy(contentBytes, 0, bytes, 8, contentBytes.length);

        return bytes;
    }

    // TODO: Think about moving it to MessageType enum so the Parser doesnt depend on messageType that much
    private static byte parseMessageType(MessageType messageType) {
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
