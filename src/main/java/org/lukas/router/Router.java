package org.lukas.router;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.Handler;

import java.util.Optional;

// This class handles the whole message lifecycle
public interface Router {
    void dispatch(Message message);
    void setHandler(MessageType messageType, Handler handler);
    void removeHandler(MessageType messageType);
}
