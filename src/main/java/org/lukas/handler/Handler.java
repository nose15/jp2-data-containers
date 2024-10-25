package org.lukas.handler;

import org.lukas.dtos.Message;

import java.util.Optional;

public interface Handler {
    Optional<Message> handle(Message message);
}
