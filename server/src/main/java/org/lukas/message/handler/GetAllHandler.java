package org.lukas.message.handler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lukas.decision.model.Decision;
import org.lukas.decision.serializers.DecisionJsonEncoder;
import org.lukas.decision.service.DecisionService;
import org.lukas.message.model.MessageType;
import org.lukas.server.handler.Handler;
import org.lukas.message.model.Message;

import java.util.List;
import java.util.Optional;

public class GetAllHandler implements Handler {
    private final DecisionService decisionService = new DecisionService();

    @Override
    public Optional<Message> handle(Message message) {
        List<Decision> decisions = decisionService.getAll();
        JSONObject responseBody = new JSONObject();
        JSONArray decisionsJson = DecisionJsonEncoder.encodeDecisions(decisions);
        responseBody.put("type", "multiple");
        responseBody.put("decisions", decisionsJson);
        return Optional.of(new Message(MessageType.OK, responseBody.toString()));
    }
}
