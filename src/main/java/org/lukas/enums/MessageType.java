package org.lukas.enums;

public enum MessageType {
    OK((byte) 0x1),
    WRITE((byte) 0x2),
    CLEAR((byte) 0x3),
    ERROR((byte) 0x4),
    PING((byte) 0x5),
    UNKNOWN((byte) -0x1);

    private final byte hex;

    MessageType(byte hex) {
        this.hex = hex;
    }
    
    public byte getHex() {
        return hex;
    }

    public static MessageType fromHex(byte hex) {
        for (var messageType : MessageType.values()) {
            if (messageType.hex == hex) {
                return messageType;
            }
        }
        return UNKNOWN;
    }
}
