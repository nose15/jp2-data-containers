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
            PreparedStatement statement = this.connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()");
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
            PreparedStatement statement = this.connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return;
            }

            PreparedStatement initialization = this.connection.prepareStatement("CREATE TABLE Decisions(id int, component varchar(255), added_on date, description varchar(255), user_id int, importance varchar(255))");
            initialization.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
