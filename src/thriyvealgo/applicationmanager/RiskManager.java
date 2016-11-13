/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.applicationmanager;

import java.util.ArrayList;

import org.jquantlib.*;
import org.jquantlib.math.statistics.*;

import com.ib.client.Contract;

/**
 *
 * @author Christopher
 */
public class RiskManager {

    private int order = 0;
    private int accountBal;
    private double maxLoss = 0;
    private int decision = 0;
    private int orderSizeCal = 0;
    int contractLev;
    
    private static ArrayList<Contract> contractArray = new ArrayList<Contract>();
    private static ArrayList<Integer> positionArray = new ArrayList<Integer>();
    private static ArrayList<Double> marketPriceArray = new ArrayList<Double>();
    private static ArrayList<Double> marketValueArray = new ArrayList<Double>();
    private static ArrayList<Double> averageCostArray = new ArrayList<Double>();
    private static ArrayList<Double> unrealizedPNLArray = new ArrayList<Double>();
    private static ArrayList<Double> realizedPNLArray = new ArrayList<Double>();
    private static ArrayList<String> accountNameArray = new ArrayList<String>();
    
    private static double runningPNL = 0;

    public int getAccountBal() {

        accountBal = 20000;
        return accountBal;
    }
    
    public synchronized static void setAccountData(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName){
    	
    	if(!contractArray.isEmpty() || contractArray != null){
    		
    	    
    	    if(contractArray.size() == 0){
    	    	
    	    	contractArray.add(contract);
        	    positionArray.add(position);
        	    marketPriceArray.add(marketPrice);
        	    marketValueArray.add(marketValue);
        	    averageCostArray.add(averageCost);
        	    unrealizedPNLArray.add(unrealizedPNL);
        	    realizedPNLArray.add(realizedPNL);
        	    accountNameArray.add(accountName);
    	    } else if (contractArray.indexOf(contract) != -1){
    	    	
    	    	int x = contractArray.indexOf(contract);
    	    	
    			
    				positionArray.set(x, position);
    				marketPriceArray.set(x, marketPrice);
    				marketValueArray.set(x, marketValue);
    				averageCostArray.set(x, averageCost);
    				unrealizedPNLArray.set(x, unrealizedPNL);
    				realizedPNLArray.set(x, realizedPNL);
    				
    				
    			
        		
    	    	
    	    } else if (contractArray.indexOf(contract) == -1){
    	    	
    	    	contractArray.add(contract);
        	    positionArray.add(position);
        	    marketPriceArray.add(marketPrice);
        	    marketValueArray.add(marketValue);
        	    averageCostArray.add(averageCost);
        	    unrealizedPNLArray.add(unrealizedPNL);
        	    realizedPNLArray.add(realizedPNL);
        	    accountNameArray.add(accountName);
    	    	
    	    }
    	    
    	}  else {
    		
    		contractArray.add(contract);
    	    positionArray.add(position);
    	    marketPriceArray.add(marketPrice);
    	    marketValueArray.add(marketValue);
    	    averageCostArray.add(averageCost);
    	    unrealizedPNLArray.add(unrealizedPNL);
    	    realizedPNLArray.add(realizedPNL);
    	    accountNameArray.add(accountName);
    		
    	}
    
    }
    
    public static double calculatePnl(){
    	
    	runningPNL = 0;
    	
    	for (double tempPnl : unrealizedPNLArray){
    		runningPNL = runningPNL + tempPnl;
    	}
    	
    	for (double tempRelPnl : realizedPNLArray){
    		runningPNL = runningPNL + tempRelPnl;
    	}
    	
    	return runningPNL;
    	
    }

    public int orderSize(String contractType, double pnlTally, double price) {

        String symbol = contractType.toLowerCase();

        switch (symbol) {
            case "ymusd":
                contractLev = (int) (price * 5 / getAccountBal());
                if (contractLev > 10) {
                    order = 0;

                } else {
                    order = (int) Math.floor(10 / contractLev);

                }
                break;
            case "esusd":
                //ES Futures *50 contract size
                contractLev = (int) (price * 50 / getAccountBal());
                if (contractLev > 10) {
                    order = 0;

                } else {
                    order = (int) Math.floor(10 / contractLev);

                }
                break;
            case "none":
                //Futures emini
                //leverage = 10;
                break;
            case "nonnes":
                //Futures bonds
                //leverage = 10;
                break;

            default:
                throw new IllegalArgumentException("Strategy not registered: " + symbol);
        }
        //System.out.println(order);
        return order;
    }

    public int maxRisk(double pnlTally) {
        maxLoss = maxLoss();

        if (pnlTally <= maxLoss) {
            decision = 1;
        } else {
            decision = 0;
        }
        return decision;
    }

    public double maxLoss() {
        double maxLoss = getAccountBal() * -.05;
        return maxLoss;
    }

    public int normalize(int contractType, int orderSize) {
        switch (contractType) {
            case 0:

                orderSizeCal = orderSize / 1000;
                orderSizeCal = (int) orderSizeCal;
                orderSizeCal = orderSizeCal * 1000;
                break;
            case 1:
                //Futures grains
                break;
            case 2:
                //Futures emini

                break;
            case 3:
                //Futures bonds

                break;

            case 4:
                //Futures metals and energy

                break;
            case 5:
                //Stocks

                break;
            default:
                throw new IllegalArgumentException("Strategy not registered: ");
        }
        return orderSizeCal;
    }
}
