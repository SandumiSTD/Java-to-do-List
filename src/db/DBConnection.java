package db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @auther : R.P.Sandumi Tharika Dilransamie
 * @since : 2/7/2023
 **/
public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/todolist" , "root" , "");


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static DBConnection getInstance(){
        return (dbConnection == null) ? dbConnection = new DBConnection() : dbConnection;
    }

    public Connection getConnection(){

        return connection;
    }
}
