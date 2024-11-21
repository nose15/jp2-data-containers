package org.lukas.decision;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonEncoder {
    public static JSONArray encodeDecisions(List<Decision> decisions) {
        JSONArray decisionsJson = new JSONArray();

        for (Decision decision : decisions) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", decision.getId());
            jsonObject.put("personId", decision.getPerson());
            jsonObject.put("date", decision.getDate());
            jsonObject.put("importance", decision.getImportance().name());
            jsonObject.put("description", decision.getDescription());
        }

        return decisionsJson;
    }
}
