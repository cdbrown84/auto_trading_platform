/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thriyvealgo.datamanager;

import thriyvealgo.models.TickData;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class IbLiveDataObject{
    
    private int ticId;
    private int fld;
    private double val;
    private ThreadManager tm;
    
    public IbLiveDataObject(int tickerId, int field, double value, ThreadManager threadManager){
        this.ticId=tickerId;
        this.fld = field;
        this.val = value;
        this.tm = threadManager;
        run();
        
    }
    
    
    
    
    public void run(){
        TickData td = new TickData();
        
        td.setSym(ticId);
        td.setVal(val);
        td.setField(fld);
        td.setReqType(true);
        
        tm.tickManager(td);
        if (IbLiveDataManager.runLiveData == 1){
            tm.liveDataManager(td);
        }
        
        
    }
    
    
    
}
