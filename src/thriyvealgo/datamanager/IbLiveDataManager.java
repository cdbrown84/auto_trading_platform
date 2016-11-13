/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.datamanager;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.ib.client.Order;

import javafx.application.Platform;
import thriyvealgo.applicationmanager.ExecutionManager;
import thriyvealgo.applicationmanager.OrderManager;
import thriyvealgo.applicationmanager.StrategyManager;
import thriyvealgo.models.Context;
import thriyvealgo.models.LiveStrategyData;
import thriyvealgo.models.TickData;
import thriyvealgo.utilities.GuiManager;
import thriyvealgo.utilities.IbConnection;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class IbLiveDataManager extends TimerTask {

    public static List<String> sym = new ArrayList<>();
    public static List<String> type = new ArrayList<>();
    public static List<Double> value = new ArrayList<>();
    public static List<java.sql.Timestamp> time = new ArrayList<>();
    public static List<String> detailedTime = new ArrayList<>();
    public static int runLiveData = 0;
    public static boolean clear = false;
    private ThreadManager tm;
    private static final IbDataManager dm = new IbDataManager();
    private static String symName;
    private static List<StrategyManager> sm = new ArrayList<>();
    private static ExecutionManager emLocal;
    private static LiveStrategyData lsd;
    private static boolean starter = false;
    
    private static GuiManager guiM = new GuiManager();
    
    private static OrderManager order = new OrderManager();
    
    public IbLiveDataManager(ThreadManager tm){
        this.tm = tm;
    }

    @Override
    public void run() {

    	
        TickData td = new TickData();
        td.setReqType(false);
        tm.liveDataManager(td);
        //IbConnection.api.reqAccountUpdates(true, "All");
        //System.out.println("task run");

        

    }

    public static void runData() {
        int elementId = value.size() - 1;
        int lastElementId;
        int counter = 0;
        if (starter == false){
            setSM();
            starter = true;
        }
        
        //System.out.println("***********************************************************************");
        guiM.setMainTextView("***********************************************************************");      
        int decision;
        
        for (int x = 1; x <= 14; x++){
            symName = dm.getSym(x);
            int symNameIndex = sym.lastIndexOf(symName);
            
            if (sym.lastIndexOf(symName) != -1) {

                lsd = new LiveStrategyData();
                lsd.setSym(x);
                lsd.setVal(value.get(symNameIndex));
                lsd.setStrategyNum(1);
                decision = sm.get(x-1).strategyListRun(lsd);
                emLocal = new ExecutionManager();
                emLocal.executeTrade(lsd, decision, order);
                final String output = sym.get(symNameIndex) + " " + detailedTime.get(symNameIndex) + " "+type.get(symNameIndex)+" " + value.get(symNameIndex);
                guiM.setMainTextView(output); 
                
            }
        } 
        guiM.setMainTextView("***********************************************************************");   
        
        //System.out.println("***********************************************************************");
        
        /*
        if (!value.isEmpty()) {

            System.out.println(sym.get(elementId)+" "+detailedTime.get(elementId)+" "+type.get(elementId)+" "+value.get(elementId));
        }
                */
    }
    
    private static void setSM(){
        for(int x = 0; x < 14; x++){
            
            StrategyManager sm2 = new StrategyManager();
            sm.add(sm2);
            
        }
    }

}
