package org.lukas.server.handler.impl;

import org.json.JSONException;
import org.json.JSONObject;
import org.lukas.server.handler.Handler;
import org.lukas.server.message.Message;
import org.lukas.server.message.MessageType;

import java.util.Optional;

public class FindHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        try {
            JSONObject query = parseQuery(message.getContent());

            // TODO: Use repository or jooq with parameters to retrieve data and return it
            // TODO: Encode ResultSet to JSON and pack it into the message

            JSONObject result = new JSONObject();

            return Optional.of(new Message(MessageType.OK, result.toString()));
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        }
    }

    private JSONObject parseQuery(String content) {
        return new JSONObject(content);
    }
}
