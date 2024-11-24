package org.lukas.server.db;

import java.sql.*;

public class DbManager {
    private final String jdbcUrl = "jdbc:h2:./data/proddb";
    private final String testJdbcUrl = "jdbc:h2:./data/testdb";
    private final String username = "user";
    private final String password = "1234";

    private Connection connection;

    public DbManager() throws SQLException {
        this.connection = DriverManager.getConnection(jdbcUrl, username, password);

        initialize();
    }

    public DbManager(boolean test) throws SQLException {
        if (test) {
            this.connection = DriverManager.getConnection(testJdbcUrl, username, password);
        } else {
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
        }

        initialize();
    }

    public Connection getConnection() {
        return connection;
    }

    public void truncateTables() {
        try {
            String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()";
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String truncateQuery = "TRUNCATE TABLE " + tableName;
                this.getConnection().prepareStatement(truncateQuery).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize() {
        try {
            String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()";
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return;
            }

            String initQuery = "CREATE TABLE Decisions(" +
                    "id int, " +
                    "component varchar(255), " +
                    "added_on date, " +
                    "description varchar(511), " +
                    "user_name varchar(255), " +
                    "importance varchar(255))";

            PreparedStatement initialization = this.connection.prepareStatement(initQuery);
            initialization.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
