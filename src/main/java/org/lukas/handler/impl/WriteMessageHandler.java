package org.lukas.handler.impl;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.filemanager.FileManager;
import org.lukas.handler.Handler;

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
