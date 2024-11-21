package org.lukas.server.handler;

import org.lukas.server.message.Message;

import java.util.Optional;

public interface Handler {
    Optional<Message> handle(Message message);
}
