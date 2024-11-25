package org.lukas.decision.serializers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lukas.decision.model.Decision;

import java.util.List;

public class DecisionJsonEncoder {
    public static JSONArray encodeDecisions(List<Decision> decisions) {
        JSONArray decisionsJson = new JSONArray();

        for (Decision decision : decisions) {
            decisionsJson.put(encodeDecision(decision));
        }

        return decisionsJson;
    }

    public static JSONObject encodeDecision(Decision decision) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", decision.getId());
        jsonObject.put("person", decision.getPerson());
        jsonObject.put("date", decision.getDate());
        jsonObject.put("component", decision.getComponent());
        jsonObject.put("importance", decision.getImportance().name());
        jsonObject.put("description", decision.getDescription());

        return jsonObject;
    }
}
