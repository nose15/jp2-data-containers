package org.lukas.handler.impl;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.Handler;

import java.util.Optional;

public class OkMessageHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        if (message.getContentLength() != 0) {
            return Optional.of(new Message(MessageType.ERROR, "Content length must be 0"));
        }

        return Optional.empty();
    }
}

