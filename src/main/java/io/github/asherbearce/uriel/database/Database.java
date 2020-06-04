package io.github.asherbearce.uriel.database;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Database {
    public static final String DB_URL = "jdbc:sqlite:ServerDB.sqlite";
    private Connection connection = null;
    private static Database instance;
    private PreparedStatement addWarningStatement;
    private PreparedStatement getWarningsStatement;

    private Database() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");

        if (!res.next()){
            System.out.println("Initializing.");
            initialize();
        }

        addWarningStatement = connection.prepareStatement(
                    "INSERT INTO warnings (" +
                        "date_issued, issuer_id, reason, warned_user_id) " +
                        "VALUES(?, ?, ?, ?);");
        getWarningsStatement = connection.prepareStatement("SELECT * FROM warnings WHERE warned_user_id == ?);");
    }

    private void initialize() throws SQLException{
        JDA jda = Main.getJda();
        Statement createUserTable = connection.createStatement();
        Statement createWarningsTable = connection.createStatement();

        createWarningsTable.executeUpdate(
                    "CREATE TABLE warnings (" +
                        "warning_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "date_issued DATETIME, " +
                        "issuer_id TEXT, " +
                        "reason TEXT, warned_user_id TEXT);");
    }

    public void addWarning(Date issued, String issuerID, String reason, String warnedID) throws SQLException{
        addWarningStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        addWarningStatement.setString(2, issuerID);
        addWarningStatement.setString(3, reason);
        addWarningStatement.setString(4, warnedID);
        addWarningStatement.executeUpdate();
    }

    public ResultSet getAllUserWarnings(String warnedUserID) throws SQLException{
        getWarningsStatement.setString(1, warnedUserID);
        return getWarningsStatement.executeQuery();
    }

    public static Database getDatabase() throws ClassNotFoundException, SQLException{
        if (instance == null){
            instance = new Database();
        }

        return instance;
    }
}
