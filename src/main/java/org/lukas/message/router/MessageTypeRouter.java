package org.lukas.message.router;

import org.lukas.server.handler.Handler;
import org.lukas.server.handler.impl.DefaultHandler;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;
import org.lukas.server.router.Router;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageTypeRouter implements Router<MessageType> {
    private final Handler defaultHandler;
    private final Map<MessageType, Handler> handlers = new HashMap<>();

    public MessageTypeRouter(Handler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public MessageTypeRouter() {
        this.defaultHandler = new DefaultHandler();
    }

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
    public Optional<Message> dispatch(Message message) {
        MessageType messageType = message.getMessageType();
        Handler handler = handlers.get(messageType);

        if (handler == null) {
            handler = defaultHandler;
        }

        return handler.handle(message);
    }
}
