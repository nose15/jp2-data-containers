package org.lukas.server.handler.impl;

import org.lukas.decision.Decision;
import org.lukas.server.handler.Handler;
import org.lukas.server.message.Message;
import org.lukas.server.message.MessageType;

import java.text.ParseException;
import java.util.Optional;

public class AddHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        try {
            Decision decision = Decision.fromJson(message.getContent());
            // TODO: Use the repository to add new and then return OK or ERROR based on the output

            return Optional.of(new Message(MessageType.OK));
        } catch (ParseException e) {
            return Optional.of(new Message(MessageType.ERROR, e.getMessage()));
        }
    }
}
