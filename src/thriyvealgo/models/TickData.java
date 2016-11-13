/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thriyvealgo.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static thriyvealgo.datamanager.IbDataManager.date;
import static thriyvealgo.datamanager.IbDataManager.detailedTime;
import static thriyvealgo.datamanager.IbDataManager.sym;
import static thriyvealgo.datamanager.IbDataManager.time;
import static thriyvealgo.datamanager.IbDataManager.type;
import static thriyvealgo.datamanager.IbDataManager.val;
import thriyvealgo.datamanager.IbDataManager;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class TickData implements Serializable{
    
    /**
   * The client's first name.
   * @serial
   */
    private String sym;
    private String type;
    private double val;
    private java.sql.Date date;
    private java.sql.Timestamp time;
    private String detailedTime;
    private java.util.Date longDate;
    private java.sql.Date realDate;
    private java.sql.Timestamp realTimestamp;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
    private long timeMill; 
    private String dateString;
    private IbDataManager dm = new IbDataManager();
    private boolean wAccess;
    private String table;
    private int ticId;
    private int fld;
    //private double val;
    private ThreadManager tm;
    
    public TickData(){
        this.timeMill = System.currentTimeMillis();
        setRealDate();
    }
    
    //Setter for Symbol
    public void setSym(int tickId){
        setTickId(tickId);
        this.sym = dm.getSym(tickId);
        
    }
    
    //Getter for Symbol
    public String getSym(){
        
        return this.sym;
    }
    
    private void setTickId(int tickId){
    	this.ticId = tickId;
    }
    
    public int getTickId(){
    	return this.ticId;
    }
    
    //Setter for field
    public void setField(int fieldType){
        
        this.type = dm.getField(fieldType);
        
    }
    
    //Getter for field
    public String getField(){
        
        return this.type;
    }
    
    //Setter for value
    public void setVal(double value){
        this.val = value;
    }
    
    //Getter for value
    public double getVal(){
        return this.val;
    }
    
    //Setter for Date auto called in the constructor
    private void setRealDate(){
        
        
        realDate = new java.sql.Date(timeMill);
        setRealTimestamp();
        setLongDate();
        
    }
    
    //Getter for Date
    public java.sql.Date getRealDate(){
        
        return this.realDate;
    }
    
    //Setter for Timestamp (auto implemented in setDate() method
    private void setRealTimestamp(){
        realTimestamp = new java.sql.Timestamp(timeMill);
    }
    
    //Getter for Timestamp
    public java.sql.Timestamp getRealTimestamp(){
        
        return this.realTimestamp;
    }
    
    //Setter for longdate and timestamp (auto implemented in setDate() method
    private void setLongDate(){
        longDate = new java.util.Date(timeMill);
        dateString = format.format(longDate);
        
        
        
    }
    
    //Getter for Long date and TimeStamp
    public String getLongDate(){
        
        return this.dateString;
    }
    
    
    
    //Set the table name
    public void setTableName(String tbl){
        this.table = tbl;
    }
    
    //Get the table name
    public String getTableName(){
        
        return this.table;
    }
    
    ///setter for Access request type
    public void setReqType(boolean writeAccess){
        
        this.wAccess = writeAccess;
        
    }
    
    //Getter for Access request type
    public boolean getReqType(){
        return this.wAccess;
    }
    
   
    
}
