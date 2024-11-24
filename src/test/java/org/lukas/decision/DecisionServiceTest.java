package org.lukas.decision;

import org.jooq.SQL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.lukas.decision.model.Decision;
import org.lukas.decision.model.Importance;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.db.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DecisionServiceTest {
    private final DbManager dbManager;

    public DecisionServiceTest() throws SQLException {
        this.dbManager = new DbManager(true);
    }

    @AfterEach
    public void afterEach() throws SQLException {
        dbManager.truncateTables();
        ResultSet results = dbManager.getConnection().prepareStatement("SELECT * FROM Decisions").executeQuery();
        if (results.next()) {
            System.out.println(results.getString("component"));
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

    @Test
    public void testGetAll() throws SQLException {
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (1, 'dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MINOR', 'siemanosiemano')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (2, 'cipy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MAJOR', 'dzwonipapagei')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (3, 'japy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'CRITICAL', 'SIEMA ENIU')").execute();
        DecisionService decisionService = new DecisionService(dbManager);
        List<Decision> result = decisionService.getAll();

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFilter() throws SQLException {
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (1, 'dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MINOR', 'siemanosiemano')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (2, 'cipy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MAJOR', 'dzwonipapagei')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (3, 'japy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'CRITICAL', 'SIEMA ENIU')").execute();

        DecisionService decisionService = new DecisionService(dbManager);
        List<Decision> result = decisionService.filter("COMPONENT", "japy");
        assertEquals(1, result.size());
    }

    @Test
    public void testAdd() throws SQLException {
        Decision decision = new Decision();
        decision.setId(2);
        decision.setImportance(Importance.CRITICAL);
        decision.setDescription("siema");
        decision.setPerson("123");
        decision.setDate(new Date());
        decision.setComponent("niszczycielstwo");

        DecisionService decisionService = new DecisionService(dbManager);
        decisionService.add(decision);

        String query = "SELECT * FROM Decisions WHERE id=2";
        ResultSet resultSet = dbManager.getConnection().prepareStatement(query).executeQuery();
        assertTrue(resultSet.next());
    }
}
