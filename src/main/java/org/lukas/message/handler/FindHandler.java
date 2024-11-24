package org.lukas.message.handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lukas.decision.model.Decision;
import org.lukas.decision.serializers.DecisionJsonEncoder;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.handler.Handler;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;

import java.util.List;
import java.util.Optional;

public class FindHandler implements Handler {
    private final DecisionService decisionService = new DecisionService();

    @Override
    public Optional<Message> handle(Message message) {
        try {
            JSONObject query = parseQuery(message.getContent());
            List<Decision> decisions = decisionService.filter(query.getString("field"), query.get("value"));
            JSONArray decisionsJson = DecisionJsonEncoder.encodeDecisions(decisions);
            return Optional.of(new Message(MessageType.OK, decisionsJson.toString()));
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        }
    }

    private JSONObject parseQuery(String content) {
        // TODO: JSON Parsing
        return new JSONObject(content);
    }
}
