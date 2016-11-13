/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.datamanager;

import thriyvealgo.models.HistoricalData;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class IbHistoricalDataThread extends Thread{
    private final String date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final int volume;
    private final int count;
    private final double wap;
    private final boolean hasGaps;
    private final ThreadManager tm;
    private final int reqId;
    
    public IbHistoricalDataThread(String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, int reqId, ThreadManager threadManager){
        
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.count = count;
        this.wap = WAP;
        this.hasGaps = hasGaps;
        this.tm = threadManager;
        this.reqId = reqId;
        
    }
    
    @Override
    public void run(){
        //System.out.println("New thread");
        HistoricalData hd = new HistoricalData();
        hd.setClose(close);
        hd.setCount(count);
        hd.setDate(date);
        hd.setHasGaps(hasGaps);
        hd.setHigh(high);
        hd.setOpen(open);
        hd.setVolume(volume);
        hd.setWAP(wap);
        hd.setlow(low);
        hd.setReqNum(reqId);
        
        tm.historicalManager(hd);
        
    }
    
}
