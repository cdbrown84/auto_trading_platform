/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.datamanager;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import thriyvealgo.controllers.RootApplication;
import thriyvealgo.models.Context;
import thriyvealgo.utilities.DbConnection;
import thriyvealgo.utilities.GuiManager;
import thriyvealgo.utilities.IbConnection;
import thriyvealgo.utilities.IbEWrapperiml;

/**
 *
 * @author Christopher
 */
public class IbDataManager {

    Thread updateThread;
    Thread dbThread;
    private static DbConnection mysql = new DbConnection();
    private static String sqlQuery;
    public String mainDataReceived;
    double pricea = 0;
    double priceb = 0;
    double sum = 0;
    public static int connection = 0;
    private static int tickDataCapture = 0;

    //variables from Historical data
    /*
     These are variables from tickPrice(), tickSize(), tickOptionComputation, tickGeneric(), tickEFP(), tickString(),
    
    
     */
    private int tickerId;
    private int field;
    private double price;
    private int size;
    private int canAutoExecute;
    private double impliedVol;
    private double delta;
    private double optPrice;
    private double pvDividend;
    private double gamma;
    private double vega;
    private double theta;
    private double undPrice;
    private int tickType;
    private double value;
    private double basisPoints;
    private String formattedBasisPoints;
    private double impliedFuture;
    private int holdDays;
    private String futureExpiry;
    private double dividendImpact;
    private double dividendsToExpiry;
    private String tickStringValue;
    private static String table;

    private int num = 0;
    public static int check = 0;
    private int i = 0;
    private static int processCount = 0;

    private String testData;

    public static List<String> sym = new ArrayList<>(1000000);
    public static List<String> type = new ArrayList<>(1000000);
    public static List<Double> val = new ArrayList<>(1000000);
    public static List<java.sql.Date> date = new ArrayList<>(1000000);
    public static List<java.sql.Timestamp> time = new ArrayList<>(1000000);
    public static List<String> detailedTime = new ArrayList<>(1000000);
    java.util.Date longDate;
    java.sql.Date realDate;
    java.sql.Timestamp realTimestamp;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
    
    // Historical Data Variables
    public static List<String> dateArray = new ArrayList<>();
    public static List<Double> openArray = new ArrayList<>();
    public static List<Double> highArray = new ArrayList<>();
    public static List<Double> lowArray = new ArrayList<>();
    public static List<Double> closeArray = new ArrayList<>();
    public static List<Integer> volumeArray = new ArrayList<>();
    public static List<Integer> countArray = new ArrayList<>();
    public static List<Double> WAPArray = new ArrayList<>();
    public static List<Boolean> hasGapsArray = new ArrayList<>();
    
    private static GuiManager guiM = new GuiManager();

    //Constructor
    public IbDataManager() {

    }

    public static void storeHistoricalData() {
        
        String sym = IbConnection.symbol.toLowerCase();
        String cur = IbConnection.currency.toLowerCase();
        String contract = sym+cur;
        
        if (connection == 0) {
            mysql.mySqlConnect(0);

            IbDataManager.connection = 1;
        }

        try {
            mysql.createHistoricalDataTable( contract );
        } catch (SQLException ex) {
            Logger.getLogger(IbDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //New way to store items to the database
        if (!dateArray.isEmpty()) {
            sqlQuery = "INSERT INTO " + contract + "_historical_trade "
                    + "VALUES ";

            for (int x = 0; x < dateArray.size(); x++) {

                if (openArray.get(x) != -1) {

                    if (x == 0) {
                        sqlQuery = sqlQuery + "(default, '"
                                + dateArray.get(x) + "', "
                                + openArray.get(x) + ", "
                                + highArray.get(x) + ", "
                                + lowArray.get(x) + ", "
                                + closeArray.get(x) + ", "
                                + volumeArray.get(x) + ", "
                                + countArray.get(x) + ", "
                                + WAPArray.get(x) + ", "
                                + hasGapsArray.get(x) + ")";
                    } else {
                        sqlQuery = sqlQuery + ", (default, '"
                                + dateArray.get(x) + "', "
                                + openArray.get(x) + ", "
                                + highArray.get(x) + ", "
                                + lowArray.get(x) + ", "
                                + closeArray.get(x) + ", "
                                + volumeArray.get(x) + ", "
                                + countArray.get(x) + ", "
                                + WAPArray.get(x) + ", "
                                + hasGapsArray.get(x) + ")";

                        //System.out.println(IbEWrapperiml.volumeArray.get(x));
                    }

                    processCount++;
                }
                //mysqlDB.storeRealTimeData(, , IbDataManager.time.get(x), IbDataManager.detailedTime.get(x), IbDataManager.type.get(x), IbDataManager.val.get(x));

                

            }

            sqlQuery = sqlQuery + ";";

            mysql.storeSqlQuery(sqlQuery);
            
            guiM.setMainTextView("Processed " + processCount + " items into the database");
                       
            dateArray.clear();
            openArray.clear();
            highArray.clear();
            lowArray.clear();
            closeArray.clear();
            volumeArray.clear();
            countArray.clear();
            WAPArray.clear();
            hasGapsArray.clear();
            
        }

    }

    //Example of the print data received
    public void setTarea1(final String data) {

        
        

    }

    public void saveRecord(String symbol, String field, double value, java.sql.Date actualDate, java.sql.Timestamp timestamp, String longdate) {

        sym.add(symbol);
        type.add(field);
        val.add(value);
        date.add(actualDate);
        time.add(timestamp);
        detailedTime.add(longdate);
        
        System.out.println("Record saved");
        
        /*
        if (field == 4 && IbLiveDataManager.runLiveData == 1){
            
            IbLiveDataManager.symId.add(tickerId);
            IbLiveDataManager.time.add(realTimestamp);
            IbLiveDataManager.detailedTime.add(dateString);
            IbLiveDataManager.value.add(value);
            
        }
                */

        //System.out.println(field+" "+value);
    }
    
    public void recordThreadManager(int tickerId, int field, double value, boolean writeAccess){
        
        if(writeAccess == true){
        
            long timeMill = System.currentTimeMillis();
            realTimestamp = new java.sql.Timestamp(timeMill);
            realDate = new java.sql.Date(timeMill);
            longDate = new java.util.Date(timeMill);
            String dateString = format.format(longDate);

            sym.add(getSym(tickerId));
            type.add(getField(field));
            val.add(value);
            date.add(realDate);
            time.add(realTimestamp);
            detailedTime.add(dateString);
            

            if (field == 4 && IbLiveDataManager.runLiveData == 1){

                //IbLiveDataManager.symId.add(tickerId);
                IbLiveDataManager.time.add(realTimestamp);
                IbLiveDataManager.detailedTime.add(dateString);
                IbLiveDataManager.value.add(value);

            }
        } else if (writeAccess == false){
            //recordThreadManager(false);
        }
    }
    
    public static synchronized void recordThreadManager(String tbl){
        table = tbl;
        
            if (connection == 0) {
                mysql.mySqlConnect(0);
                mysql.createRealTimeData(table);
                connection = 1;
            }
            try {
                if (sym != null && !sym.isEmpty()) {
                    sqlQuery = "INSERT INTO "+table
                            + " VALUES ";
                    int savedSize = sym.size();

                    for (int x = 0; x < sym.size(); x++) {

                        if (x == 0) {
                            sqlQuery = sqlQuery + "(default, '"
                                    + sym.get(x) + "', '"
                                    + date.get(x) + "', '"
                                    + time.get(x) + "', '"
                                    + detailedTime.get(x) + "', "
                                    + val.get(x) + ", '"
                                    + type.get(x) + "')";
                        } else {
                            sqlQuery = sqlQuery + ", (default, '"
                                    + sym.get(x) + "', '"
                                    + date.get(x) + "', '"
                                    + time.get(x) + "', '"
                                    + detailedTime.get(x) + "', "
                                    + val.get(x) + ", '"
                                    + type.get(x) + "')";
                        }

                        processCount++;

                    //mysqlDB.storeRealTimeData(, , IbDataManager.time.get(x), IbDataManager.detailedTime.get(x), IbDataManager.type.get(x), IbDataManager.val.get(x));
                        /**
                        sym.remove(x);
                        date.remove(x);
                        time.remove(x);
                        detailedTime.remove(x);
                        type.remove(x);
                        val.remove(x);
                        */
                    }

                    sqlQuery = sqlQuery + ";";
                    //System.out.println(sqlQuery);
                    mysql.storeSqlQuery(sqlQuery);
                    sqlQuery = "";
                    sym.clear();
                    date.clear();
                    time.clear();
                    detailedTime.clear();
                    type.clear();
                    val.clear();

                }
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex);
            }
            
            //Show values in GUI
            
            guiM.setMainTextView(processCount + " Items saved in the database...");
            processCount = 0;
      
    }
    
    

    public static void setConnection() {
        connection = 1;
    }

    public String getSym(int tickId) {

        String symb = "";
        switch (tickId) {
            case 1:
                symb = "esusd";
                break;
            case 2:
                symb = "ymusd";
                break;
            case 3:
                symb = "nqusd";
                break;
            case 4:
                symb = "ztusd";
                break;
            case 5:
                symb = "zfusd";
                break;
            case 6:
                symb = "mgcusd";
                break;
            case 7:
                symb = "qgusd";
                break;
            case 8:
                symb = "ycusd";
                break;
            case 9:
                symb = "ywusd";
                break;
            case 10:
                symb = "ysusd";
                break;
            case 11:
                symb = "n225mjpy";
                break;
            case 12:
                symb = "audjpy";
                break;
            case 13:
                symb = "m6eusd";
                break;
            case 14:
                symb = "m6cusd";
                break;
            default:
                symb = "";
            //throw new IllegalArgumentException("Symbol is not registered "+symb);
        }
        return symb;
    }

    public String getField(int field) {
        String fld = "";
        switch (field) {
            case 0:
                fld = "bid_size";
                break;
            case 1:
                fld = "bid";
                break;
            case 2:
                fld = "ask";
                break;
            case 3:
                fld = "ask_size";
                break;
            case 4:
                fld = "last";
                break;
            case 5:
                fld = "last_size";
                break;
            case 6:
                fld = "high";
                break;
            case 7:
                fld = "low";
                break;
            case 8:
                fld = "volume";
                break;
            case 9:
                fld = "close";
                break;
            default:
                //System.out.println(field);
                fld = "14";
            //throw new IllegalArgumentException("Field is not registered "+fld);
        }
        return fld;

    }

    public static void setTickDataCapture() {
        tickDataCapture = 1;
    }

    public static int getTickDataCapture() {
        return tickDataCapture;
    }

    public void getRealData() {

    }

}
