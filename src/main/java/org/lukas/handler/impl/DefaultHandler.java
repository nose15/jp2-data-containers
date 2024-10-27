package org.lukas.handler.impl;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.Handler;

import java.util.Optional;

public class DefaultHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        return Optional.of(new Message(MessageType.ERROR, "Bad request"));
    }
}
