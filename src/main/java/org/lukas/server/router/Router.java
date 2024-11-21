package org.lukas.server.router;

import org.lukas.server.message.Message;
import org.lukas.server.handler.Handler;

import java.util.Optional;

// This class handles the whole message lifecycle
public interface Router<T> {
    Optional<Message> dispatch(Message message);
    void setHandler(T key, Handler handler);
    void removeHandler(T key);
}
