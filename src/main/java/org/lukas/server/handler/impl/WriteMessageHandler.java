package org.lukas.server.handler.impl;

import org.lukas.server.Message;
import org.lukas.server.MessageType;
import org.lukas.server.filemanager.FileManager;
import org.lukas.server.handler.Handler;

import java.io.IOException;
import java.util.Optional;

public class WriteMessageHandler implements Handler {
    private final FileManager fileManager;

    public WriteMessageHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public Optional<Message> handle(Message message) {
        try {
            fileManager.write(message.getContent());
            return Optional.of(new Message(MessageType.OK, ""));
        } catch (IOException e) {
            return Optional.of(new Message(MessageType.ERROR, e.getMessage()));
        }
    }
}
