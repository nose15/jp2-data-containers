package org.lukas.server.db;

import java.sql.*;

public class DbManager {
    String jdbcUrl = "jdbc:h2:./data/testdb";
    String username = "sa";
    String password = "";

    Connection connection;

    public DbManager() throws SQLException {
        this.connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    public boolean insert(String table, )
}
