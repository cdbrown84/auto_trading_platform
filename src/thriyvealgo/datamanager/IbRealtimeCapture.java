/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.datamanager;

import java.util.TimerTask;

import thriyvealgo.models.TickData;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class IbRealtimeCapture extends TimerTask {

    private final String table;
    private final TickData td;
    private final ThreadManager tm;

    public IbRealtimeCapture(String tableType, ThreadManager threadManager) {
        this.table = tableType.toLowerCase();
        this.td = new TickData();
        this.tm = threadManager;
    }

    @Override
    public void run() {
        
        
        td.setReqType(false);
        td.setTableName(table);
        //System.out.println(td.getReqType());
        tm.tickManager(td);
        
        
    }

}
