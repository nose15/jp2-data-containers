package org.lukas.message.handler;

import org.json.JSONException;
import org.json.JSONObject;
import org.lukas.decision.model.Decision;
import org.lukas.decision.serializers.DecisionJsonEncoder;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.handler.Handler;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;

import java.util.Optional;

public class GetOneHandler implements Handler {
    private final DecisionService decisionService = new DecisionService();

    @Override
    public Optional<Message> handle(Message message) {
        try {
            int id = parseId(message.getContent());
            Optional<Decision> decision = decisionService.getById(id);
            if (decision.isPresent()) {
                JSONObject decisionJson = DecisionJsonEncoder.encodeDecision(decision.get());
                JSONObject responseBody = new JSONObject();
                responseBody.put("type", "single");
                responseBody.put("decision", decisionJson);
                return Optional.of(new Message(MessageType.OK, responseBody.toString()));
            } else {
                return Optional.of(new Message(MessageType.ERROR, "Not found"));
            }
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        }
    }

    private int parseId(String content) {
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getInt("id");
    }
}
