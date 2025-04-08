package com.lancaster.musicapp;

import java.sql.*;

public class Database {
    private static final String url = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t39";
    //ADMIN USERNAME AND PASSWORD, FOR DATA USERS PLEASE REFER TO THE EMAIL SENT BY MARTIN 13/02/2025
    private static final String username = "in2033t39_a";
    private static final String password = "FaxFwIqv5TA";

    /** Open and retrieve a connection to the database
     * @return an open connection to the database
     * @throws SQLException a connection to the database could not be established
     */
    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}