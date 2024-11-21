package org.lukas.server.handler.impl;

import org.lukas.server.handler.Handler;
import org.lukas.server.message.Message;
import org.lukas.server.message.MessageType;

import java.util.Optional;

public class DefaultHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        return Optional.of(new Message(MessageType.ERROR, "Bad request"));
    }
}
