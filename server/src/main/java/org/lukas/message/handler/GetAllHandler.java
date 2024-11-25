package org.lukas.message.handler;

import org.json.JSONArray;
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
        JSONArray decisionsJson = DecisionJsonEncoder.encodeDecisions(decisions);
        return Optional.of(new Message(MessageType.OK, decisionsJson.toString()));
    }
}
