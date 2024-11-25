package org.lukas.client;

import org.lukas.decision.Decision;

public class Command {
    private String type;
    private Decision decision;

    public Command(String type) {
        this.decision = null;
    }

    public Command(String type, Decision decision) {
        this.type = type;
        this.decision = decision;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getType() {
        return type;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public void setType(String type) {
        this.type = type;
    }
}
