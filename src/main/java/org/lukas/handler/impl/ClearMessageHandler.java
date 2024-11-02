package org.lukas.handler.impl;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.filemanager.FileManager;
import org.lukas.handler.Handler;

import java.io.IOException;
import java.util.Optional;

public class ClearMessageHandler implements Handler {
    private final FileManager fileManager;

    public ClearMessageHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public Optional<Message> handle(Message message) {
        if (message.getContentLength() != 0) {
            return Optional.of(new Message(MessageType.ERROR, "Content length must be 0"));
        }
        try {
            fileManager.clear();
            return Optional.of(new Message(MessageType.OK, ""));
        } catch (IOException e) {
            return Optional.of(new Message(MessageType.ERROR, e.getMessage()));
        }

    }
}
