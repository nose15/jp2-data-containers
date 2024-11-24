package org.lukas.decision.service;

import org.lukas.decision.model.Decision;
import org.lukas.decision.model.Importance;
import org.lukas.server.db.DbManager;

import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DecisionService {
    // TODO: Remove userId in favor of just name - it'd be too much; no time for that
    // TODO: Write a client
    // TODO (Optional): Add enum in db for the Importance field

    DbManager dbManager;

    public DecisionService() {
        try {
            dbManager = new DbManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DecisionService(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public Optional<Decision> getById(int id) {
       Connection conn = this.dbManager.getConnection();
        try {
            String query = "SELECT * FROM DECISIONS WHERE id LIKE (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return Optional.empty();
            }

            Decision decision = decisionFromQueryRes(result);
            return Optional.of(decision);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Decision> getAll() {
        Connection conn = dbManager.getConnection();

        try {
            String query = "SELECT * FROM Decisions";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            List<Decision> decisions = new ArrayList<>();
            while (results.next()) {
                Decision decision = decisionFromQueryRes(results);
                decisions.add(decision);
            }

            return decisions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Decision> filter(String field, Object keyword) {
        Connection conn = dbManager.getConnection();
        try {
            String query = "SELECT * FROM Decisions WHERE " + field + " = (?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, (String) keyword);
            ResultSet results = statement.executeQuery();
            List<Decision> decisions = new ArrayList<>();

            while (results.next()) {
                Decision decision = decisionFromQueryRes(results);
                decisions.add(decision);
            }

            return decisions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Decision decision) {
        Connection conn = dbManager.getConnection();
        try {
            String query = "INSERT INTO Decisions(id, component, added_on, user_id, importance, description) " +
                    "VALUES ((?), (?), (?), (?), (?), (?))";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, decision.getId());
            statement.setString(2, decision.getComponent());
            statement.setDate(3, new Date(decision.getDate().getTime()));
            statement.setInt(4, Integer.parseInt(decision.getPerson()));
            statement.setString(5, decision.getImportance().name());
            statement.setString(6, decision.getDescription());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Decision decisionFromQueryRes(ResultSet set) throws SQLException {
        Decision decision = new Decision();
        decision.setId(set.getInt("id"));
        decision.setComponent(set.getString("component"));
        decision.setDate(set.getDate("added_on"));
        decision.setDescription(set.getString("description"));
        decision.setPerson(set.getString("user_id"));
        decision.setImportance(Importance.valueOf(set.getString("importance")));

        return decision;
    }
}
