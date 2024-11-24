package org.lukas.decision.model;

import org.json.JSONObject;
import org.lukas.decision.serializers.DecisionJsonDecoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class Decision {

    private int id;
    private String component;
    private Date date;
    private String person;
    private Importance importance;
    private String description;

    public Decision() {
        this.date = new Date();
        this.person = null;
        this.component = "";
        this.importance = Importance.MINOR;
        this.description = null;
    }

    public Decision(Date date, String component, String person, Importance importance, String description) {
        this.date = date;
        this.component = component;
        this.person = person;
        this.importance = importance;
        this.description = description;
    }

    public static Decision fromJson(String jsonString) throws ParseException {
        Decision decision = new Decision();
        JSONObject jsonObject = new JSONObject(jsonString);

        decision.date = DecisionJsonDecoder.parseDate(jsonObject);
        decision.component = DecisionJsonDecoder.parseComponent(jsonObject);
        decision.importance = DecisionJsonDecoder.parseImportance(jsonObject);
        decision.person = DecisionJsonDecoder.parsePersonId(jsonObject);
        decision.description = DecisionJsonDecoder.parseDescription(jsonObject);

        return decision;
    }

    public static Decision fromQueryRes(ResultSet set) throws SQLException {
        Decision decision = new Decision();
        decision.setId(set.getInt("id"));
        decision.setComponent(set.getString("component"));
        decision.setDate(set.getDate("added_on"));
        decision.setDescription(set.getString("description"));
        decision.setPerson(set.getString("user_name"));
        decision.setImportance(Importance.valueOf(set.getString("importance")));

        return decision;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Importance getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    public String getPerson() {
        return person;
    }

    public String getComponent() {
        return component;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public void setPerson(String personId) {
        this.person = personId;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
