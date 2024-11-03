package org.lukas.handler;

import org.junit.jupiter.api.Test;
import org.lukas.server.Message;
import org.lukas.server.MessageType;
import org.lukas.server.handler.impl.OkMessageHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OkMessageHandlerTest {
    private final OkMessageHandler handler = new OkMessageHandler();

    @Test
    public void contentTooLongTest() {
        Message request = new Message(MessageType.OK, "aa");

        Optional<Message> response = handler.handle(request);

        assertTrue(response.isPresent());

        Message resMessage = response.get();

        assertEquals(resMessage.getMessageType(), MessageType.ERROR);
    }

    @Test
    public void noContentTest() {
        Message request = new Message(MessageType.OK, "");

        Optional<Message> response = handler.handle(request);

        assertFalse(response.isPresent());
    }

}
