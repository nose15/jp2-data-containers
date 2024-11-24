package org.lukas.server.handler;

import org.lukas.message.model.Message;

import java.util.Optional;

public interface Handler {
    Optional<Message> handle(Message message);
}
