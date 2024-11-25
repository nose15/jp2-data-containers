package org.lukas.server.handler.impl;

import org.lukas.server.handler.Handler;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;

import java.util.Optional;

public class DefaultHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        return Optional.of(new Message(MessageType.ERROR, "Bad request"));
    }
}
