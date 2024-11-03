package org.lukas.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lukas.server.Message;
import org.lukas.server.MessageType;
import org.lukas.server.filemanager.FileManager;
import org.lukas.server.handler.impl.WriteMessageHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WriteMessageHandlerTest {
    private final WriteMessageHandler handler;
    private final File file;
    private final FileManager fileManager;

    public WriteMessageHandlerTest() throws IOException {
        this.file = new File("/home/lukasz/test");
        this.fileManager = new FileManager(file);
        this.handler = new WriteMessageHandler(fileManager);
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        fileManager.clear();
    }

    @Test
    public void respondTest() {
        Message request = new Message(MessageType.WRITE, "abc");
        Optional<Message> response = handler.handle(request);

        assertTrue(response.isPresent());
    }

    @Test
    public void writeTest() throws FileNotFoundException {
        Message request = new Message(MessageType.WRITE, "abc");
        Optional<Message> response = handler.handle(request);

        assertTrue(response.isPresent());
        Message responseMes = response.get();

        assertEquals(MessageType.OK, responseMes.getMessageType());

        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.next());
        }

        assertEquals("abc", stringBuilder.toString());
    }
}
