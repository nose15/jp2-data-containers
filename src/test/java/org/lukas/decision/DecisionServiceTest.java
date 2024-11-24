package org.lukas.decision;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lukas.decision.model.Decision;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.db.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecisionServiceTest {
    private final DbManager dbManager;

    public DecisionServiceTest() {
        try {
            this.dbManager = new DbManager(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void afterEach() {
        dbManager.truncateTables();
        try {
            ResultSet results = dbManager.getConnection().prepareStatement("SELECT * FROM Decisions").executeQuery();
            if (results.next()) {
                System.out.println(results.getString("component"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetById() throws SQLException {
        PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (1, 'dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MINOR', 'SIEMA ENIU')");
        preparedStatement.execute();
        DecisionService decisionService = new DecisionService(dbManager);
        Optional<Decision> decision = decisionService.getById(1);

        assertTrue(decision.isPresent());
    }
}
