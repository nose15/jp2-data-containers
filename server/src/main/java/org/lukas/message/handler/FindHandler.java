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

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindHandler implements Handler {
    private final DecisionService decisionService = new DecisionService();

    @Override
    public Optional<Message> handle(Message message) {
        try {
            Map<String, String> queries = parseQueries(message.getContent());
            List<Decision> decisions = decisionService.filter(queries);
            JSONArray decisionsJson = DecisionJsonEncoder.encodeDecisions(decisions);
            JSONObject responseBody = new JSONObject();
            responseBody.put("type", "multiple");
            responseBody.put("decisions", decisionsJson);
            return Optional.of(new Message(MessageType.OK, responseBody.toString()));
        } catch (JSONException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong JSON format: " + e.getMessage()));
        } catch (ParseException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong data format: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Optional.of(new Message(MessageType.ERROR, "Bad request: " + e.getMessage()));
        }
    }

    private Map<String, String> parseQueries(String content) throws ParseException {
        Pattern fieldPattern = Pattern.compile("^[a-z]*$");
        Pattern keywordPattern = Pattern.compile("^[a-zA-Z0-9 ]*$");

        JSONObject json = new JSONObject(content);
        Map<String, String> queries = new HashMap<>();

        for (Object n : json.names()) {
            String name = (String) n;
            Matcher fieldMatcher = fieldPattern.matcher(name);
            Matcher keywordMatcher = keywordPattern.matcher(json.getString(name));

            if (!fieldMatcher.matches()) {
                throw new ParseException("Field should only contain letters a-z", 0);
            }

            if (!keywordMatcher.matches()) {
                throw new ParseException("Keyword should only contain letters a-Z and numbers 0-9", 0);
            }

            queries.put(name, json.getString(name));
        }

        System.out.println(queries);
        return queries;
    }
}
