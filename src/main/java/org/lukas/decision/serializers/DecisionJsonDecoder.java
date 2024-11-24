package org.lukas.decision.serializers;

import org.json.JSONObject;
import org.lukas.decision.model.Importance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionJsonDecoder {
    public static Date parseDate(JSONObject decisionJson) throws ParseException {
        String dateString = decisionJson.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.parse(dateString);
    }

    public static String parseComponent(JSONObject decisionJson) throws ParseException {
        String componentStr = decisionJson.getString("componentStr");
        componentStr = componentStr.trim();
        componentStr = componentStr.strip();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9 ]*$");
        Matcher matcher = pattern.matcher(componentStr);

        if (matcher.matches()) {
            return componentStr;
        }

        throw new ParseException("Component should only contain letters a-Z and numbers 0-9", 0);
    }

    public static String parsePersonId(JSONObject decisionJson) throws ParseException {
        String personIdStr = decisionJson.getString("personId");
        personIdStr = personIdStr.trim();
        personIdStr = personIdStr.strip();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(personIdStr);

        if (matcher.matches()) {
            return personIdStr;
        }

        throw new ParseException("Person should only contain letters a-Z and numbers 0-9", 0);
    }

    public static Importance parseImportance(JSONObject decisionJson) throws ParseException {
        String importanceStr = decisionJson.getString("importance");
        importanceStr = importanceStr.toLowerCase(Locale.ROOT);
        importanceStr = importanceStr.trim();
        importanceStr = importanceStr.strip();

        return Importance.fromString(importanceStr);
    }

    public static String parseDescription(JSONObject decisionJson) throws ParseException {
        String descStr = decisionJson.getString("description");
        descStr = descStr.trim();
        descStr = descStr.strip();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9 .)(/]*$");
        Matcher matcher = pattern.matcher(descStr);

        if (matcher.matches()) {
            return descStr;
        }

        throw new ParseException("Illegal characters in description", 0);
    }
}
