package org.lukas.server.handler.impl;

import org.json.JSONException;
import org.json.JSONObject;
import org.lukas.server.handler.Handler;
import org.lukas.server.message.Message;
import org.lukas.server.message.MessageType;

import java.util.Optional;

public class GetOneHandler implements Handler {
    @Override
    public Optional<Message> handle(Message message) {
        try {
            int id = parseId(message.getContent());

            // TODO: Retrieve and encode to json

            return Optional.of(new Message(MessageType.OK, "Decision in json here"));
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        }
    }

    private int parseId(String content) {
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getInt("id");
    }
}
