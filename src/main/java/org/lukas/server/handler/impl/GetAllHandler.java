package org.lukas.server.handler.impl;

import org.lukas.server.handler.Handler;
import org.lukas.server.message.Message;

import java.util.Optional;

public class GetAllHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        // TODO: Call service just to fetch all decisions

        return Optional.empty();
    }
}
