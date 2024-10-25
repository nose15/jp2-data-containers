package org.lukas.router;

import org.lukas.dtos.Message;

import java.util.Optional;

// This class handles the whole message lifecycle
public interface Router {
    void dispatch(Message message);
}
