/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.applicationmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import thriyvealgo.models.LiveStrategyData;
import thriyvealgo.utilities.ContractSpec;
import thriyvealgo.utilities.DbConnection;
import thriyvealgo.utilities.IbConnection;

/**
 *
 * @author Christopher
 */
public class ExecutionManager {

    private int position = 0;
    private double livePnL = 0;
    private double pnlTally = 0;
    private int maxRisk = 0;
    private String sym;
    private RiskManager risk;
    public static OrderManager orders;
    
    private LiveStrategyData lsdTemp;

    public ExecutionManager() {
        
        risk = new RiskManager();

    }

    public synchronized void executeTrade(LiveStrategyData lsd, int decision, OrderManager order) {
    	
    	this.orders = order;
    	tradeManagement(lsd, decision);
       
        //tradeCount++;

    }
    
    public int checkPosition(LiveStrategyData lsd){
    	position = orders.checkPosition(sym);
    	
    	return position;
    	
    }
    
    public void riskManager(LiveStrategyData lsd, int decision){
    	
    	livePnL = risk.calculatePnl();
        maxRisk = risk.maxRisk(livePnL);
        
      //Actions if the maxRisk has been exceeded
        if (maxRisk == 1) {
            if (orders.checkPosition(sym) < 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)){
            	//Go Exit Short only if there are existing positions
            	
            	//check exitsing positions
            	System.out.println(livePnL);
            	position = -orders.checkPosition(sym);
            	lsd.setQuantityReq(position);
            	decision = 4;
            	orders.submitOrder(decision, position, lsd);

            	System.out.println("Exiting short!!!!!!"+lsd.getSym()+lsd.getVal());

            } else if (orders.checkPosition(sym) > 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)){
                //System.out.println("No Trade taken!!");
            	//IbConnection.api.cancelOrder(lsdTemp.getOrderId());
            	System.out.println(livePnL);
            	position = orders.checkPosition(sym);
            	lsd.setQuantityReq(position);
            	decision = 3;
            	orders.submitOrder(decision, position, lsd);
            	
            	System.out.println("Exiting long!!!!!"+lsd.getSym()+lsd.getVal());
            }
        }
    }

    //Decides if should enter of exit a position based on the decision and risk parameters
    private synchronized void tradeManagement(LiveStrategyData lsd, int decision) {
        sym = lsd.getSym();
        int indexTemp = 0;
        
        for(int x = 0; x < OrderManager.lsdArray.size(); x++){
        	if (OrderManager.lsdArray.get(x).getSym() == lsd.getSym()){
        		
        		lsdTemp = OrderManager.lsdArray.get(x);
        		indexTemp = x;
        		
        	}
        }
        
        if (lsdTemp == null){
        	lsdTemp = lsd;
        } else {
        	IbConnection.api.reqOpenOrders();
        }
        
        riskManager(lsd, decision);
        
        if (maxRisk == 0) {
            switch (decision) {
                case 0:
                    //No Trade

                    //System.out.println("No Trade made");
                    break;
                case 1:
                    //Go long Statement or Exit Short
                	System.out.println("Current Profit/Loss on all trades is $"+livePnL);
                    if (orders.checkPosition(sym) == 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)) {
                    	//Go long only if there are no pending orders or existing positions
                    	
                        position = risk.orderSize(sym, pnlTally, lsd.getVal());
                        lsd.setQuantityReq(position);
                        orders.submitOrder(decision, position, lsd);
                        
                        position = orders.checkPosition(sym);
                      
                        System.out.println("Going Long!!!!!!" +lsd.getSym()+lsd.getVal());
                        
                    } else if(orders.checkPosition(sym) < 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)){
                    	//Go Exit Short only if there are existing positions
                    	
                    	//check exitsing positions
                    	position = -orders.checkPosition(sym);
                    	lsd.setQuantityReq(position);
                    	decision = 4;
                    	orders.submitOrder(decision, position, lsd);
                    	System.out.println("Exiting short!!!!!!"+lsd.getSym()+lsd.getVal());
                    	
                    } else if (orders.checkPosition(sym) == 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() != 0)){
                    	
                    	IbConnection.api.cancelOrder(lsdTemp.getOrderId());
                    	OrderManager.lsdArray.remove(indexTemp);
                    	lsdTemp.setQuantitySub(0);
                    	System.out.println("Cancelling short!!!!!"+lsd.getSym()+lsd.getVal());
                    	
                    }

                    break;
                case 2:
                    //Go Short Statements
                	System.out.println("Current Profit/Loss on all trades is $"+livePnL);
                    if (orders.checkPosition(sym) == 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)) {
                        
                        position = -risk.orderSize(sym, pnlTally, lsd.getVal());
                        lsd.setQuantityReq(position);
                        orders.submitOrder(decision, position, lsd);
                        position = orders.checkPosition(sym);
                        System.out.println("Going Short!!!!!!"+lsd.getSym()+lsd.getVal());
                    } else if(orders.checkPosition(sym) > 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() == 0)){
                                            	
                    	position = orders.checkPosition(sym);
                    	lsd.setQuantityReq(position);
                    	decision = 3;
                    	orders.submitOrder(decision, position, lsd);
                    	
                    	System.out.println("Exiting long!!!!!"+lsd.getSym()+lsd.getVal());
                    	
                    } else if (orders.checkPosition(sym) == 0 && (lsdTemp.getQuantityReq() == 0 && lsdTemp.getQuantitySub() != 0)){
                    	
                    	IbConnection.api.cancelOrder(lsdTemp.getOrderId());
                    	OrderManager.lsdArray.remove(indexTemp);
                    	lsdTemp.setQuantitySub(0);
                    	System.out.println("Cancelling long!!!!!"+lsd.getSym()+lsd.getVal());
                    	
                    } 

                    break;
                
                default:
                    throw new IllegalArgumentException("Invalid decision");
            }
        }
        
        notifyAll();

    }

    
   
}
