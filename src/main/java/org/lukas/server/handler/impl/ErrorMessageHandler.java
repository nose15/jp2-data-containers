package org.lukas.server.handler.impl;

import org.lukas.server.Message;
import org.lukas.server.handler.Handler;

import java.util.Optional;

public class ErrorMessageHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        return Optional.empty();
    }
}
