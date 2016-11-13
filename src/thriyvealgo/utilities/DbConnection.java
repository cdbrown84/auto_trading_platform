/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.utilities;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import thriyvealgo.datamanager.IbDataManager;
import thriyvealgo.models.Context;

/**
 *
 * @author Christopher
 */
public class DbConnection {

    private Connection mysql;
    Thread mysqlThread;
    Thread dbThread;
    public static int dbErrorCode = 0;

    private String sqlQuery = null;
    private Statement statement = null;
    private GuiManager guiM = new GuiManager();
    

    //Connect to the MySql Database
    public void mySqlConnect(int num) {

        if (num < 1) {
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    guiM.setMainTextView("Connecting to a selected database...");
                    
                    
                } catch (InstantiationException | IllegalAccessException ex) {
                    System.out.println("Error: "+ex);
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: "+ex);
            }
            try {
                mysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/thriyvealgo?"
                        + "user=thriyvealgo&password=thriyvealgo");
                
                guiM.setMainTextView("Connected database successfully...");
                
            } catch (SQLException ex) {
                System.out.println("Error, could not connect to database. "+ex);
                dbErrorCode = 1;
            }

        }

    }

    public void createHistoricalDataTable(String sym) throws SQLException {

        //String sym = symbol.toUpperCase();
        String symbol = sym.toLowerCase();

        sqlQuery = "CREATE TABLE IF NOT EXISTS " + symbol + "_historical_trade "
                + "(id INTEGER not NULL AUTO_INCREMENT, "
                + " date VARCHAR(255), "
                + " open DOUBLE, "
                + " high DOUBLE, "
                + " low DOUBLE, "
                + " close DOUBLE, "
                + " volume BIGINT, "
                + " count BIGINT, "
                + " WAP DOUBLE, "
                + " hasGaps BOOLEAN, "
                + " PRIMARY KEY ( id ));";
        //System.out.println(statement);
        try {

            mysql.createStatement().executeUpdate(sqlQuery);

        } catch (SQLException ex) {
            System.out.println("Unable to create table. "+ex);

        }

    }

    public void storeHistoricalData(String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, String sym) {

        sqlQuery = "INSERT INTO " + sym + "_historical_trade "
                + "VALUES (default, '"
                + date + "', "
                + open + ", "
                + high + ", "
                + low + ", "
                + close + ", "
                + volume + ", "
                + count + ", "
                + WAP + ", "
                + hasGaps + ");";
        //System.out.println(sqlQuery);
        try {
            mysql.createStatement().executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            System.out.println("Unable to amend table "+sym+". "+ex);
        }

    }

    public void createRealTimeData(String tableName) {
        sqlQuery = "CREATE TABLE IF NOT EXISTS "+tableName.toLowerCase()
                + "(id INTEGER not NULL AUTO_INCREMENT, "
                + " symbol VARCHAR(12) NOT NULL, "
                + " date DATE NOT NULL, "
                + " timestamp Timestamp NOT NULL, "
                + " detailed_timestamp VARCHAR(100) NOT NULL, "
                + " value DOUBLE, "
                + " type VARCHAR(20), "
                + " PRIMARY KEY ( id, date ))"
                + "ENGINE = InnoDB PARTITION BY KEY(date) PARTITIONS 1;";
        //System.out.println(statement);
        try {

            mysql.createStatement().executeUpdate(sqlQuery);

        } catch (SQLException ex) {
            System.out.println("Unable to create "+tableName+" table. "+ex);

        }
    }

    public void storeRealTimeData(String tableName, String symbol, Date date, java.sql.Timestamp time, String detailedTime, String type, double value) {

        sqlQuery = "INSERT INTO "+tableName
                + " VALUES (default, '"
                + symbol + "', '"
                + date + "', '"
                + time + "', '"
                + detailedTime + "', "
                + value + ", '"
                + type + "');";
        //System.out.println(sqlQuery);
        try {
            mysql.createStatement().executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            System.out.println("Unable to amend tickdata table "+ex);
        }

    }

    public void storeSqlQuery(String sqlQuery) {
        try {
            mysql.createStatement().executeUpdate(sqlQuery);
            //System.out.println("DB Storage Complete");
        } catch (SQLException ex) {
            System.out.println("Unable to store items in database. "+ex);
        }
    }

    public ResultSet getHistoricalData(String sym) {
        sqlQuery = "SELECT * FROM " + sym + "_HISTORICAL_TRADE ORDER BY date";
        ResultSet results = null;
        try {
            results = mysql.createStatement().executeQuery(sqlQuery);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return results;
    }

    public void closeDB() {
        try {
            mysql.close();
        } catch (SQLException ex) {
            System.out.println("Error attempting to close db. "+ex);
        }

    }

}
