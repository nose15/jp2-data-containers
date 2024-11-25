package org.lukas.decision;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.lukas.decision.model.Decision;
import org.lukas.decision.model.Importance;
import org.lukas.decision.service.DecisionService;
import org.lukas.server.db.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DecisionServiceTests {
    private final DbManager dbManager;

    public DecisionServiceTests() throws SQLException {
        this.dbManager = new DbManager(true);
    }

    @AfterEach
    public void afterEach() throws SQLException {
        dbManager.truncateTables();
        dbManager.resetAutoIncrement();
    }

    @Test
    public void testGetById() throws SQLException {
        PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Grzybiarz', 'MINOR', 'siemanosiemano')");
        preparedStatement.execute();
        DecisionService decisionService = new DecisionService(dbManager);
        Optional<Decision> decision = decisionService.getById(1);

        assertTrue(decision.isPresent());
    }

    @Test
    public void testGetAll() throws SQLException {
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Grzybiarz', 'MINOR', 'siemanosiemano')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('cipy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Janush', 'MAJOR', 'dzwonipapagei')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('japy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Elo', 'CRITICAL', 'SIEMA ENIU')").execute();
        DecisionService decisionService = new DecisionService(dbManager);
        List<Decision> result = decisionService.getAll();

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFilter() throws SQLException, ParseException {
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Grzybiarz', 'MINOR', 'siemanosiemano')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('cipy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Janush', 'MAJOR', 'dzwonipapagei')").execute();
        dbManager.getConnection().prepareStatement("INSERT INTO Decisions(component, added_on, user_name, importance, description) VALUES ('japy', parsedatetime('2022-12-12','yyyy-MM-dd'), 'Elo', 'CRITICAL', 'SIEMA ENIU')").execute();
        DecisionService decisionService = new DecisionService(dbManager);
        Map<String, String> querries = new HashMap<>();
        querries.put("component", "dupy");

        List<Decision> result = decisionService.filter(querries);
        assertEquals(1, result.size());
    }

    @Test
    public void testAdd() throws SQLException {
        Decision decision = new Decision();
        decision.setImportance(Importance.CRITICAL);
        decision.setDescription("siema");
        decision.setPerson("123");
        decision.setDate(new Date());
        decision.setComponent("niszczycielstwo");

        DecisionService decisionService = new DecisionService(dbManager);
        decisionService.add(decision);

        String query = "SELECT * FROM Decisions WHERE id=1";
        ResultSet resultSet = dbManager.getConnection().prepareStatement(query).executeQuery();
        assertTrue(resultSet.next());
    }
}
