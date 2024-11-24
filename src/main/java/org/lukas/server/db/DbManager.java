package org.lukas.server.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

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

    public void resetAutoIncrement() {
        try {
            String query = "ALTER TABLE Decisions ALTER COLUMN ID RESTART WITH 1";
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean columnExists(String colName) throws SQLException {
        String query = "SHOW columns FROM Decisions";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        Set<String> tableNames = new HashSet<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(1));
        }

        return tableNames.contains(colName.toUpperCase());
    }

    public String getColumnDatatype(String colName) throws SQLException {
        String query = "SELECT DATA_TYPE " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME='DECISIONS' AND COLUMN_NAME=(?);";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, colName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }

        throw new SQLException("No such column");
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
                    "id bigint auto_increment, " +
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
