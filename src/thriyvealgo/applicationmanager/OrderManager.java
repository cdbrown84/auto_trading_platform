/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thriyvealgo.applicationmanager;

import java.util.ArrayList;
import java.util.Random;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

import static java.lang.Math.abs;
import thriyvealgo.models.LiveStrategyData;
import thriyvealgo.utilities.IbConnection;
import thriyvealgo.utilities.IbEWrapperiml;

/**
 *
 * @author Christopher
 */
public class OrderManager {
    
    private Contract contract = new Contract();
    private Order order = new Order();
    
    private static ArrayList<String> accountArray = new ArrayList<String>();
    private static ArrayList<Contract> contractArray = new ArrayList<Contract>();
    private static ArrayList<Integer> posArray = new ArrayList<Integer>();
    private static ArrayList<Double> avgCostArray = new ArrayList<Double>();
    
    public static ArrayList<LiveStrategyData> lsdArray = new ArrayList<LiveStrategyData>();
    private static int orderIdGenerated = 0;

    private static LiveStrategyData lsd;
    

    private static int position = 0;
    
    public synchronized void submitOrder(int decision, int position, LiveStrategyData lsd){
    	
    	this.lsd =lsd;
    	
    	double last = lsd.getVal();
    	int tickerid = lsd.getTickId();
    	//Random rand = new Random();
    	//int orderIdGenerated = rand.nextInt(999999999)+1;
    	
    	//IbConnection.api.reqPositions();
        IbConnection.api.reqOpenOrders();
    	orderIdGenerated++;
    	lsd.setOrderId(orderIdGenerated);
    	
        
       switch (decision) {
                
                case 1:
                    //Go long Statement
                    
                	addOrder(lsd);
                	order.m_action = "BUY";
                    order.m_orderType = "LMT";
                    order.m_lmtPrice = last;

                    break;
                case 2:
                    //Go Short Statements
                	addOrder(lsd);
                	order.m_action = "SELL";
                    order.m_orderType = "LMT";
                    order.m_lmtPrice = last;
                    
                    break;
                case 3:
                    // Exit Long Statements
                	addOrder(lsd);
                	order.m_action = "SELL";
                    order.m_orderType = "MKT";
                    order.m_lmtPrice = last;
                    	
                    break;
                case 4:
                    //Exit Short Statements
                	addOrder(lsd);
                	order.m_action = "BUY";
                    order.m_orderType = "MKT";
                    order.m_lmtPrice = last;
                    
                    break;
                default:
                    //no trade
            }
       order.m_tif = "GTC";
       order.m_totalQuantity = abs(lsd.getQuantityReq());

       IbConnection.api.placeOrder(lsd.getOrderId(), getContract(lsd.getTickId()), order);
       
       notifyAll();
       
    }
    
    private void addOrder(LiveStrategyData lsd){
    	//IbConnection.api.reqOpenOrders();
    	
    	if (lsdArray.isEmpty() || lsdArray == null){
    		lsdArray.add(lsd);
    	}  else {
    		
    		for(int x = 0; x < lsdArray.size(); x++){
    			
    			System.out.println(lsdArray.get(x).getSym());
    			
    			if(lsdArray.get(x).getSym().equals(lsd.getSym()) && (lsdArray.get(x).getQuantityReq() == 0 || lsdArray.get(x).getQuantitySub() != 0) && checkPosition(lsd.getSym()) == 0){
    				
    				IbConnection.api.cancelOrder(lsdArray.get(x).getOrderId());
    				lsdArray.remove(x);
    				
    				
    			} else if(lsdArray.get(x).getSym().equals(lsd.getSym()) && lsdArray.get(x).getQuantityReq() != 0){
    				
    				lsdArray.remove(x);
    				
    			} else if (x +1 == lsdArray.size()){
    				
    				lsdArray.add(lsd);
    				break;
    			}
    			
    		}
    		
    	}
    	
    }
    
    private Contract getContract(int tickerid){
        
       contract.m_symbol = IbConnection.sym.get(tickerid-1);
       contract.m_secType = IbConnection.sec.get(tickerid-1);
       contract.m_expiry = IbConnection.exp.get(tickerid-1);
       contract.m_exchange = IbConnection.exc.get(tickerid-1);
       contract.m_currency = IbConnection.cur.get(tickerid-1);
       
       return contract;
        
    }
    
    public static void setOpenOrders(int orderId, Contract contract, Order order, OrderState orderState){
    	
    	String sym = contract.m_symbol+contract.m_currency;
    	//System.out.println(orderState.m_status);
    	sym = sym.toLowerCase();
    	//System.out.println(orderState.m_status+" "+sym);
    	
    	if(!lsdArray.isEmpty() || lsdArray != null) {
    	
	    	for (int x = 0; x < lsdArray.size(); x++){
	    		
	    		if(orderState.m_status.equals("Submitted") && sym.equals(lsdArray.get(x).getSym())){
	        		
	    			lsdArray.get(x).setQuantityReq(0);
	        		lsdArray.get(x).setQuantitySub(order.m_totalQuantity);
	        		lsdArray.get(x).setQuantityFilled(0);
	        		
	        	} else if(orderState.m_status.equals("Filled") && sym.equals(lsdArray.get(x).getSym())){
	        		
	        		lsdArray.get(x).setQuantityReq(0);
	        		lsdArray.get(x).setQuantitySub(0);
	        		lsdArray.get(x).setQuantityFilled(order.m_totalQuantity);
	        		
	        		
	        	} 
	    		
	    		if(sym.equals(lsdArray.get(x).getSym()) && orderState.m_status.equals("Filled") && checkPosition(sym) == 0){
	    			lsdArray.get(x).setQuantityReq(0);
	        		lsdArray.get(x).setQuantitySub(0);
	        		lsdArray.get(x).setQuantityFilled(0);
	    		}
	    		
	    	}
    	}
    	
    	

    }
    
    public static LiveStrategyData getLsd(String symbol){
    	
    	LiveStrategyData lsd = null;
    	
    	
    	for (LiveStrategyData tempLsd : lsdArray){
    		
    		if(tempLsd.getSym().equals(symbol)){
    			lsd = tempLsd;
    			//System.out.println("Found" +lsd);
    		}
    		
    	}
    	
    	
    	return lsd;
    	
    }
    
    public synchronized static void setPosition(String account, Contract contract, int pos, double avgCost){
    	
    	String symbol = contract.m_symbol;
    	//System.out.println("avg cost"+avgCost);
    	
    	if(pos != 0){
    		String tempSym = contract.m_symbol+contract.m_currency;
    		LiveStrategyData tempLsd;
    		tempSym = tempSym.toLowerCase();
    		tempLsd = getLsd(tempSym);
    		//System.out.println("the sym is " + tempSym);
    		//System.out.println(tempLsd);
    		if(tempLsd != null){
    			tempLsd.setOpenPosPrice(avgCost);
    			//System.out.println(avgCost);
    		
    		}
    	}
    	
    	//System.out.println(symbol);
    	if(!posArray.isEmpty() || posArray != null ){
    		//System.out.println("Part1");
    		if(posArray.size() == 0){
    			//System.out.println("Part1.2");
    			accountArray.add(account);
            	contractArray.add(contract);
            	posArray.add(pos);
            	avgCostArray.add(avgCost);
    		} else {
    			
    		
	    		for(int x = 0; x < posArray.size(); x++){
	    			//System.out.println("Part2");
	    			if (contractArray.get(x).m_symbol.equals(symbol)){
	        			//System.out.println("Part3");
	        			posArray.set(x, pos);
	        			
	        		} 
	        			
	        			
	        		
	        	}
	    		
    		}
    	} else {
    		//System.out.println("Part5");
    		accountArray.add(account);
        	contractArray.add(contract);
        	posArray.add(pos);
        	avgCostArray.add(avgCost);
    		
    	}
    	
    	if (contractArray.indexOf(contract) == -1){
			accountArray.add(account);
        	contractArray.add(contract);
        	posArray.add(pos);
        	avgCostArray.add(avgCost);
			
		}

    }
    
    public synchronized static int checkPosition(String sym){
    	
    	IbConnection.api.reqPositions();
    	if(!posArray.isEmpty() && posArray != null){
	    	
    		for(int x = 0; x < posArray.size(); x++){
	    		
    			String symbol = contractArray.get(x).m_symbol+contractArray.get(x).m_currency;
	    		symbol = symbol.toLowerCase();
	    		
	    		if(sym.equals(symbol)){
	    			position = posArray.get(x);
	    		}
	    	}
    		
    	}
    	return position;
    	
    }

       
    public static void resetPosition(){
    	accountArray.clear();
    	contractArray.clear();
    	posArray.clear();
    	avgCostArray.clear();
    }
    
    public static void setInitialOrderId(int orderId){
    	orderIdGenerated = orderId;
    }
    
    //Below method is not in use 
    public static double getFilledPrice(String symbol){
    	
    	double avgPrice = -1;
    	
    	for(int x = 0; x < posArray.size(); x++){
			//System.out.println("Part2");
			if (contractArray.get(x).m_symbol.equals(symbol)){
    			//System.out.println("Part3");
				avgPrice = avgCostArray.get(x);
				break;
    			
    		} 
    			
    			
    		
    	}
    	return avgPrice;
    }

    
    
}
