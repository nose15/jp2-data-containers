package org.lukas.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ParserTest {
    Message message = new Message(MessageType.WRITE, "test message 1");

    @BeforeEach
    public void beforeEach() {
        message = new Message(MessageType.WRITE, "test message 1");
    }

    @Test
    public void testEncodedMessageContentLength() {
        ByteBuffer encoded = Parser.encode(message);
        // TODO: Get array offset by 8 and measure length
    }

    @Test
    public void testEncodeMessageHasProperLength() {
        ByteBuffer encoded = Parser.encode(message);
        int length = encoded.getInt(4);
        assertEquals(length, message.getContentLength());
    }

    @Test
    public void testParseMessageType() {
        byte res = MessageType.ERROR.getHex();
        assertEquals(String.format("0x%08X", res), "0x00000004");
    }

    @Test
    public void testContentEncodingDecoding() {
        ByteBuffer encoded = Parser.encode(message);
        Message decoded = Parser.decode(encoded);

        assertEquals(decoded.getContent(), message.getContent());
    }

    @Test
    public void testMessageTypeEncodingDecoding() {
        ByteBuffer encoded = Parser.encode(message);
        Message decoded = Parser.decode(encoded);

        assertEquals(decoded.getMessageType(), message.getMessageType());
    }
}
