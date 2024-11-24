package org.lukas.server.db;

import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbManagerTest {

    @Test
    public void testCreation() {
        try {
            DbManager dbManager = new DbManager(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTruncation() {
        try {
            DbManager dbManager = new DbManager(true);
            PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement("INSERT INTO Decisions(id, component, added_on, user_id, importance, description) VALUES (1, 'dupy', parsedatetime('2022-12-12','yyyy-MM-dd'), 1, 'MINOR', 'SIEMA ENIU')");
            preparedStatement.execute();

            PreparedStatement query = dbManager.getConnection().prepareStatement("SELECT * FROM Decisions");
            ResultSet preTruncate = query.executeQuery();

            assertTrue(preTruncate.next());
            dbManager.truncateTables();
            ResultSet postTruncate = query.executeQuery();
            assertFalse(postTruncate.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}