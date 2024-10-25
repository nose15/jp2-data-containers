package org.lukas.router.impl;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.Handler;
import org.lukas.router.Router;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageTypeRouter implements Router {
    private final Map<MessageType, Handler> handlers = new HashMap<>();

    public Map<MessageType, Handler> getHandlers() {
        return this.handlers;
    }

    public void setHandler(MessageType messageType, Handler handler) {
        handlers.put(messageType, handler);
    }

    public void removeHandler(MessageType messageType) {
        handlers.remove(messageType);
    }

    @Override
    public void dispatch(Message message) {
        MessageType messageType = message.getMessageType();
        Handler handler = handlers.get(messageType);

        if (handler == null) {
            throw new NullPointerException("No handler specified for MessageType " + messageType.name());
        }

        Optional<Message> response = handler.handle(message);
        response.ifPresent(this::sendResponse);
    }

    // TODO: Rethink if this is the right place, maybe it should have its own sender or something
    private void sendResponse(Message message) {
        System.out.println("Responding with " + message.toString());
        // TODO: ResponseService.send(message);
    }
}
