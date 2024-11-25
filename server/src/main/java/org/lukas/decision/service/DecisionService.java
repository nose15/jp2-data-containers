package org.lukas.decision.service;

import org.lukas.decision.model.Decision;
import org.lukas.server.db.DbManager;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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
            String query = "SELECT * FROM DECISIONS WHERE id LIKE (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return Optional.empty();
            }

            Decision decision = Decision.fromQueryRes(result);
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
                Decision decision = Decision.fromQueryRes(results);
                decisions.add(decision);
            }

            return decisions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Decision> filter(String column, String keyword) {
        Connection conn = dbManager.getConnection();
        try {
            if (!dbManager.columnExists(column)) {
                throw new IllegalArgumentException("No such column: " + column);
            }

            if (!dbManager.getColumnDatatype(column).equals("CHARACTER VARYING")) {
                throw new IllegalArgumentException("Only string columns can be searched against");
            }

            String query = "SELECT * FROM Decisions WHERE " + column + " = (?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, keyword);
            ResultSet results = statement.executeQuery();
            List<Decision> decisions = new ArrayList<>();

            while (results.next()) {
                Decision decision = Decision.fromQueryRes(results);
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
            String query = "INSERT INTO Decisions(component, added_on, user_name, importance, description) " +
                    "VALUES ((?), (?), (?), (?), (?))";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, decision.getComponent());
            statement.setDate(2, new Date(decision.getDate().getTime()));
            statement.setString(3, decision.getPerson());
            statement.setString(4, decision.getImportance().name());
            statement.setString(5, decision.getDescription());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
