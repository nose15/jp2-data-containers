package org.lukas.decision.service;

import org.lukas.decision.model.Decision;
import org.lukas.decision.model.Importance;
import org.lukas.server.db.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DecisionService {
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
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM DECISIONS WHERE id LIKE (?)");
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return Optional.empty();
            }

            Decision decision = new Decision();
            decision.setId(result.getInt("id"));
            decision.setComponent(result.getString("component"));
            decision.setDate(result.getDate("added_on"));
            decision.setDescription(result.getString("description"));
            decision.setPerson(result.getString("user_id"));
            decision.setImportance(Importance.valueOf(result.getString("importance")));

            return Optional.of(decision);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Decision> getAll() {
        return new ArrayList<>();
    }

    public List<Decision> filter(String field, Object keyword) {
        return new ArrayList<>();
    }

    public void add(Decision decision) throws IllegalArgumentException {
    }
}
