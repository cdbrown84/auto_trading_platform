/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.utilities;

import com.ib.client.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import thriyvealgo.applicationmanager.OrderManager;
import thriyvealgo.applicationmanager.RiskManager;
import thriyvealgo.datamanager.IbDataManager;
import thriyvealgo.datamanager.IbHistoricalDataThread;
import thriyvealgo.datamanager.IbLiveDataObject;
import thriyvealgo.models.Context;

/**
 *
 * @author Christopher
 */
public class IbEWrapperiml implements EWrapper {

    public String dataReceived;
    public String dataReceived2;
    public String dataReceived3;
    public IbDataManager model = new IbDataManager();

    //variables from Historical data
   
    
   
    public static int dataManagement = 0;

    private Thread liveThread;
    private Thread historicalThread;
    
    public static ThreadManager tmLive = new ThreadManager();
    public static ThreadManager tmHist = new ThreadManager();
    
    private static GuiManager guiM = new GuiManager();
    public static int errorCd;

    /*
    EClientSocket m_s = new EClientSocket(this);

    public static void main(String[] args) {
        new IbEWrapperiml().run();
    }
    

    private void run() {
        m_s.eConnect("localhost", 7496, 0);
    }
    */

    @Override
    public void nextValidId(int orderId) {
    	
    	OrderManager.setInitialOrderId(orderId);

    }

    @Override
    public void error(Exception e) {
        dataReceived3 = null;
        dataReceived3 = "Error exception. " + e;
        /*
        try {
            throw e;
        } catch (Exception ex) {
            Logger.getLogger(IbEWrapperiml.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
      //Show values in GUI
        guiM.setMainTextView(dataReceived3);
        

    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {

        dataReceived3 = null;
        dataReceived3 = "Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n";
        guiM.setMainTextView(dataReceived3);
        
        System.out.println(dataReceived3);
        
        if(errorCode == 103){
        	errorCd = 103;
        }
        

    }

    @Override
    public void connectionClosed() {
    }

    @Override
    public void error(String str) {
    	String note = str+".";
    	guiM.setMainTextView(note);
    	System.out.println(note);
    	
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {

        dataReceived2 = null;
        dataReceived2 = "Tick Price. Ticker Id:" + tickerId + ", Field: " + field + ", Price: " + price + ", CanAutoExecute: " + canAutoExecute + "\n";
        dataReceived2.toString();
        
            
            //dataManagement++;

        //Save record to db
        if (IbDataManager.getTickDataCapture() == 1) {
            
            IbLiveDataObject ld = new IbLiveDataObject(tickerId, field, price, tmLive);
            ld = null;
            
            dataManagement++;
            
        }

    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        //System.out.println("Tick Size. Ticker Id:" +tickerId + "' Field: " + field+ ", Size: "+size+"\n");

        dataReceived = null;
        dataReceived = "Tick Size. Ticker Id:" + tickerId + ", Field: " + field + ", Size: " + size + "\n";
        dataReceived.toString();
        
        //dataManagement++;
        

        //Save record to db
        if (IbDataManager.getTickDataCapture() == 1) {
            
            IbLiveDataObject ld = new IbLiveDataObject(tickerId, field, size, tmLive);
            ld = null;
            
            dataManagement++;
            
            
           
        }
        

    }

    @Override
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        dataReceived = "TickId" + tickerId + " ,Field " + field + ", Implied Vol, " + impliedVol + " Delta " + delta + " Opt Price " + optPrice + " PV Dividend " + pvDividend + " gamma " + gamma
                + " vega " + vega + " theta " + theta + " UndPrice " + undPrice;

        //model.setTarea1(dataReceived);
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        dataReceived = "TickerId " + tickerId + " TickType " + tickType + " value " + value + " tickGeneric";

        //model.setTarea1(dataReceived);
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        dataReceived = "TickerId " + tickerId + " tickType " + tickType + " value " + value + "tickString";

        // model.setTarea1(dataReceived);
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact,
            double dividendsToExpiry) {
        dataReceived = " tickerId " + tickerId + " tickType " + tickType + " basisPoints " + basisPoints + " formattedBasisPoints " + formattedBasisPoints + " ImpliedFuture " + impliedFuture + " holdDays " + holdDays
                + " futureExpiry " + futureExpiry + " dividendImpact " + dividendImpact + " dividensToExpiry " + dividendsToExpiry;

        //model.setTarea1(dataReceived);
    }

    @Override
    public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        dataReceived = "OrderId " + orderId + " Status " + status + " Filled " + filled + " Remaining " + remaining + " AveFillPrice " + avgFillPrice + " permId " + permId + " ParentId " + parentId
                + " lastFillPrice " + lastFillPrice + " clientId " + clientId + " whyHeld " + whyHeld;

        model.setTarea1(dataReceived);
        //System.out.println(dataReceived);
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        dataReceived = "OrderId " + orderId + " Contract " + contract + " Order " + order + " OrderState " + orderState;
        OrderManager.setOpenOrders(orderId, contract, order, orderState);
        //System.out.println(dataReceived);
        //System.out.println(orderState.m_status);
        //model.setTarea1(dataReceived);
        
        
        
    }

    @Override
    public void openOrderEnd() {

        //System.out.println("yes");
        //model.setTarea1(dataReceived);
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        dataReceived = "Key " + key + " value " + value + " currency " + currency + " accountname " + accountName;

        //model.setTarea1(dataReceived);
    }

    @Override
    public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        String dataReceived4 = "Contract " + contract + " Position " + position + " MarketPrice " + marketPrice + " marketvalue " + marketValue + " AverageCost " + averageCost + " UnrealizedPNL " + unrealizedPNL + " realizedPNL "
                + realizedPNL + " AccountName " + accountName;

        RiskManager.setAccountData(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName);
        //System.out.println(dataReceived4);
    }

    @Override
    public void updateAccountTime(String timeStamp) {
        dataReceived = timeStamp;

        //model.setTarea1(dataReceived);
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        dataReceived = accountName;

        model.setTarea1(dataReceived);
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        dataReceived = "ReqId " + reqId + " ContractDetails " + contractDetails;

        model.setTarea1(dataReceived);
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        dataReceived = " reqId " + reqId + " ContractDetails " + contractDetails;

        model.setTarea1(dataReceived);
    }

    @Override
    public void contractDetailsEnd(int reqId) {

        System.out.println(reqId);

        //model.setTarea1(dataReceived);
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        dataReceived = "ReqId " + reqId + " contract " + contract + " Execution " + execution;

        model.setTarea1(dataReceived);
    }

    @Override
    public void execDetailsEnd(int reqId) {
        dataReceived = "reqId " + reqId;

        model.setTarea1(dataReceived);
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
        dataReceived = "TickerId " + tickerId + " position " + position + " operation " + operation + " side " + side + " price " + price + " size " + size;

        model.setTarea1(dataReceived);
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
        dataReceived = "Tickerid " + tickerId + " Position " + position + " MarketMaker " + marketMaker + " Operation " + operation + " side " + side + " price " + price + " Size " + size;

        model.setTarea1(dataReceived);
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        dataReceived = "MsgId " + msgId + " MsgType " + msgType + " Message " + message + " OrigExchange " + origExchange;

        model.setTarea1(dataReceived);
    }

    @Override
    public void managedAccounts(String accountsList) {
        dataReceived = accountsList;

    }

    @Override
    public void receiveFA(int faDataType, String xml) {
        dataReceived = "FADataType " + faDataType + " xml " + xml;

        model.setTarea1(dataReceived);
    }

    @Override
    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
        
        historicalThread = new IbHistoricalDataThread(date, open, high, low, close, volume, count, WAP, hasGaps, 1, tmHist);
        historicalThread.start();
        //System.out.println("Thread started");
        

    }

    @Override
    public void scannerParameters(String xml) {

        model.setTarea1(xml);
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        dataReceived = "ReqId " + reqId + " Rank " + rank + " ContractDetails " + contractDetails + " Distance " + distance + " Benchmark " + benchmark + " Projection " + projection + " legsStr " + legsStr;

        model.setTarea1(dataReceived);
    }

    @Override
    public void scannerDataEnd(int reqId) {

        //model.setTarea1(dataReceived);
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {

        dataReceived = "ReqId " + reqId + " Time " + time + " open " + open + " high " + high + " low " + low + " Close " + close + " volume " + volume + " wap " + wap + " Count " + count;

        model.setTarea1(dataReceived);
        
    }

    @Override
    public void currentTime(long time) {

        //System.out.println(time);
        guiM.setMainTextView("Time: "+time);
        
        
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        dataReceived = "ReqId " + reqId + " Data " + data;

        model.setTarea1(dataReceived);
    }

    @Override
    public void deltaNeutralValidation(int reqId, UnderComp underComp) {
        dataReceived = "ReqId " + reqId + " underComp " + underComp;

        model.setTarea1(dataReceived);
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        System.out.println(reqId);
        model.setTarea1(dataReceived);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        dataReceived = "ReqId " + reqId + " Market DataType " + marketDataType;

        model.setTarea1(dataReceived);
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        //dataReceived = commissionReport;
        System.out.println("commission report " + commissionReport);
        // model.setTarea1(dataReceived);
    }

    @Override
    public void position(String account, Contract contract, int pos, double avgCost) {
        dataReceived = "Account " + account + " contract " + contract + " Pos " + pos + " Avg Cost " + avgCost;

        model.setTarea1(dataReceived);
        //System.out.println(dataReceived);
        OrderManager.setPosition(account, contract, pos, avgCost);
        //System.out.println("called");
    }

    @Override
    public void positionEnd() {
        //System.out.println("yes");
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        dataReceived = "Req Id " + reqId + " account " + account + " Tag " + "Value " + value + " currency " + currency;

        model.setTarea1(dataReceived);
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        System.out.println(reqId);
        //model.setTarea1(dataReceived);
    }

    @Override
    public void verifyMessageAPI(String apiData) {
        dataReceived = apiData;
        System.out.println(apiData);
        model.setTarea1(dataReceived);
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        dataReceived = "Is Successful " + isSuccessful + "Error Text " + errorText;

        model.setTarea1(dataReceived);
    }

    @Override
    public void displayGroupList(int reqId, String groups) {
        dataReceived = "Reqid " + groups;

        model.setTarea1(dataReceived);
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        dataReceived = "reqId " + reqId + "Contract Info " + contractInfo;

        model.setTarea1(dataReceived);
    }

    public void setDataManagement(int x) {

        dataManagement = x;

    }

    public int getDataManagement() {

        return dataManagement;
    }

}
