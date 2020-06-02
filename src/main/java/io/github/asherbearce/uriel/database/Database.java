package io.github.asherbearce.uriel.database;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import java.sql.*;

public class Database {
    public static final String DB_URL = "jdbc:sqlite:ServerDB.sqlite";
    private Connection connection = null;
    private static Database instance;

    private Database() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'user';");

        if (!res.next()){
            System.out.println("Initializing.");
            initialize();
        }
    }

    private void initialize() throws SQLException{
        JDA jda = Main.getJda();
        Statement createUserTable = connection.createStatement();
        Statement createWarningsTable = connection.createStatement();
        createUserTable.executeUpdate(
                    "CREATE TABLE user(" +
                        "user_id TEXT PRIMARY KEY, " +
                        "username TEXT);");

        createWarningsTable.executeUpdate(
                    "CREATE TABLE warnings (" +
                        "warning_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "date_issued DATETIME, " +
                        "issuer_id TEXT, " +
                        "reason TEXT);");

        PreparedStatement prep = connection.prepareStatement("INSERT INTO user values(?, ?);");

        for (Guild guild : jda.getGuilds()) {
            for (Member member : guild.getMembers()) {
                if (!member.getUser().isBot()){
                    prep.setString(1, member.getId());
                    prep.setString(2, member.getEffectiveName());
                    prep.execute();
                }
            }
        }
    }

    public static Database getDatabase() throws ClassNotFoundException, SQLException{
        if (instance == null){
            instance = new Database();
        }

        return instance;
    }
}
