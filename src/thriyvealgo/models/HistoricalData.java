/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.models;

import java.io.Serializable;

/**
 *
 * @author Christopher
 */
public class HistoricalData extends TickData implements Serializable{
    
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;
    private int count;
    private double wap;
    private boolean hasGaps;
    private int reqId;
    
    //Constructor
    public HistoricalData(){
        
        this.date = "";
        this.open = 0;
        this.close = 0;
        this.high = 0;
        this.low = 0;
        this.volume = 0;
        this.count =0;
        this.wap = 0;
        this.hasGaps = false;
        this.reqId = 0;
        
    }
    
    //Setter for date
    public void setDate(String date){
        
        this.date = date;
        
    }
    
    //Getter for date
    
    
    public String getDate(){
        
        return date;
        
        
    }
    
    //Setter for open
    public void setOpen(double open){
       
        this.open = open;
        
    }
    
    //Getter for open
    public double getOpen(){
        
        return open;
        
    }
    
    //Setter for high
    public void setHigh(double high){
        
        this.high = high;
        
    }
    
    //Getter for high
    public double getHigh(){
        return high;
    }
    
    //Setter for low
    public void setlow(double low){
        this.low = low;
    }
    
    //Getter for low
    public double getLow(){
        return low;
        
    }
    
    //Setter for close
    public void setClose(double close){
        this.close = close;
    }
    
    //Getter for close
    public double getClose(){
        return close;
        
    }
    
    //Setter for Volume
    public void setVolume(int volume){
        this.volume = volume;
    }
    
    //Getter for volume
    public int getVolume(){
        
        return volume;
    }
    
    //Setter for count
    public void setCount(int count){
        this.count = count;
    }
    
    //Getter for count
    public int getCount(){
        return count;
    }
    
    //Setter for wap
    public void setWAP(double wap){
        this.wap = wap;
    }
    
    //Getter for WAP
    public double getWAP(){
        return wap;
    }
    
    //Setter for HasGaps
    public void setHasGaps(boolean hasGaps){
        this.hasGaps = hasGaps;
    }
    
    //Getter for HasGaps
    public boolean getHasGaps(){
        
        return hasGaps;
        
    }
    
    public void setReqNum(int reqId){
        this.reqId = reqId;
    }
    
    public int getReqNum(){
        return reqId;
    }
   
    
    
}
