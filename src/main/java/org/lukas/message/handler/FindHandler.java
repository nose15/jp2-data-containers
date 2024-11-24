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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } catch (ParseException e) {
            return Optional.of(new Message(MessageType.ERROR, "Wrong data format: " + e.getMessage()));
        }
    }

    private JSONObject parseQuery(String content) throws ParseException {
        Pattern fieldPattern = Pattern.compile("^[a-z]*$");
        Pattern keywordPattern = Pattern.compile("^[a-zA-Z0-9 ]*$");

        JSONObject json = new JSONObject(content);
        String field = json.getString("field");
        String keyword = json.getString("keyword");

        Matcher fieldMatcher = fieldPattern.matcher(field);
        Matcher keywordMatcher = keywordPattern.matcher(keyword);


        if (!fieldMatcher.matches()) {
            throw new ParseException("Field should only contain letters a-z", 0);
        }

        if (!keywordMatcher.matches()) {
            throw new ParseException("Keyword should only contain letters a-Z and numbers 0-9", 0);
        }

        return json;
    }
}
