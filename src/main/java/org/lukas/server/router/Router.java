package org.lukas.server.router;

import org.lukas.server.Message;
import org.lukas.server.MessageType;
import org.lukas.server.handler.Handler;

import java.util.Optional;

// This class handles the whole message lifecycle
public interface Router {
    Optional<Message> dispatch(Message message);
    void setHandler(MessageType messageType, Handler handler);
    void removeHandler(MessageType messageType);
}
