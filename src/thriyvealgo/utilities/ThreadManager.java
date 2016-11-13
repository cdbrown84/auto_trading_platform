/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.utilities;

import javafx.application.Platform;
import thriyvealgo.datamanager.IbDataManager;
import thriyvealgo.datamanager.IbLiveDataManager;
import thriyvealgo.models.Context;
import thriyvealgo.models.HistoricalData;
import thriyvealgo.models.TickData;

/**
 *
 * @author Christopher
 */
public class ThreadManager {

    private static int itemTracker = 0;
    private GuiManager guiM = new GuiManager();

    public synchronized void tickManager(TickData data) {

        if (data.getReqType() == true) {
            IbDataManager.sym.add(data.getSym());
            IbDataManager.type.add(data.getField());
            IbDataManager.val.add(data.getVal());
            IbDataManager.date.add(data.getRealDate());
            IbDataManager.time.add(data.getRealTimestamp());
            IbDataManager.detailedTime.add(data.getLongDate());

        } else if (data.getReqType() == false) {
            IbDataManager.recordThreadManager(data.getTableName());
            
            
            //guiM.setMainTextView("Items "+IbEWrapperiml.dataManagement);
            System.out.println(IbEWrapperiml.dataManagement);
            IbEWrapperiml.dataManagement = 0;

        }

        notify();
    }

    public synchronized void historicalManager(HistoricalData data) {
        int req = data.getReqNum();
        //System.out.println(data.getReqType());
        switch (req) {
            case 1:
                if (data.getOpen() != -1) {
                    IbDataManager.dateArray.add(data.getDate());
                    IbDataManager.openArray.add(data.getOpen());
                    IbDataManager.highArray.add(data.getHigh());
                    IbDataManager.lowArray.add(data.getLow());
                    IbDataManager.closeArray.add(data.getClose());
                    IbDataManager.volumeArray.add(data.getVolume());
                    IbDataManager.countArray.add(data.getCount());
                    IbDataManager.WAPArray.add(data.getWAP());
                    IbDataManager.hasGapsArray.add(data.getHasGaps());
                    //System.out.println(data.getOpen());

                }
                break;
            case 2:
                IbDataManager.storeHistoricalData();
                break;
            default:

        }
        notify();

    }

    public synchronized void liveDataManager(TickData data) {

        if (data.getReqType() != false && (data.getField().equals("last") || data.getField().equals("close"))) {
            IbLiveDataManager.sym.add(data.getSym());
            IbLiveDataManager.type.add(data.getField());
            IbLiveDataManager.value.add(data.getVal());
            IbLiveDataManager.detailedTime.add(data.getLongDate());

        } else if (data.getReqType() == false) {
            IbLiveDataManager.runData();
            IbLiveDataManager.sym.clear();
            IbLiveDataManager.type.clear();
            IbLiveDataManager.value.clear();
            IbLiveDataManager.time.clear();
            IbLiveDataManager.detailedTime.clear();
        }

    }

}
