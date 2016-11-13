/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.models;


/**
 *
 * @author Christopher
 */
public class LiveStrategyData extends HistoricalData{
    private int strategyNum = 0;
    private int activePosition = 0;
    private int orderId = 0;
    private int quantityReq = 0;
    private int quantityFilled = 0;
    private int quantitySub = 0;
    private double openPosPrice = -1;
    private double closePosPrice = -1;
    
    
    public LiveStrategyData(){
        
    }
    
    public void setStrategyNum(int strategyNum){
        
        this.strategyNum = strategyNum;
        
    }
    
    public int getStrategyNum(){
        
        return this.strategyNum;
    }
    
    public void setActivePosition(int position){
    	
    	this.activePosition = position;
    	
    }
    
    public int getActivePosition(){
    	return this.activePosition;
    }
    
    public void setOrderId(int orderId){
    	this.orderId = orderId;
    }
    
    public int getOrderId(){
    	return this.orderId;
    }
    
    public void setQuantityReq(int quantityReq){
    	this.quantityReq = quantityReq;
    }
    
    public int getQuantityReq(){
    	return quantityReq;
    }
    
    public void setQuantitySub(int quantitySub){
    	this.quantitySub = quantitySub;
    }
    
    public int getQuantitySub(){
    	return quantitySub;
    }
    
    public void setQuantityFilled(int quantityFilled){
    	this.quantityFilled = quantityFilled;
    }
    
    public int getQuantityFilled(){
    	return quantityFilled;
    }
    
    public void setOpenPosPrice(double openPosPrice){
    	this.openPosPrice = openPosPrice;
    }
    
    public double getOpenPosPrice(){
    	return openPosPrice;
    }
    
    public void setClosePosPrice(double closePosPrice){
    	this.closePosPrice = closePosPrice;
    }
    
    public double getClosePosPrice(){
    	return closePosPrice;
    }
    
}
