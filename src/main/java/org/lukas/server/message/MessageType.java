package org.lukas.server.message;

public enum MessageType {
    OK((byte) 0x1),
    ERROR((byte) 0x2),
    ADD((byte) 0x3),
    GET_ALL((byte) 0x4),
    GET_ONE((byte) 0x5),
    FIND((byte) 0x6),
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
