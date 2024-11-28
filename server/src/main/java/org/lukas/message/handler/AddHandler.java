package org.lukas.message.handler;

import org.json.JSONException;
import org.lukas.decision.model.Decision;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.handler.Handler;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;

import java.text.ParseException;
import java.util.Optional;

public class AddHandler implements Handler {
    private final DecisionService decisionService = new DecisionService();

    @Override
    public Optional<Message> handle(Message message) {
        try {
            Decision decision = Decision.fromJson(message.getContent());
            decisionService.add(decision);
            return Optional.of(new Message(MessageType.OK));
        } catch (ParseException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong data format: " + e.getMessage()));
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        } catch (RuntimeException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong data"));
        }
    }
}
