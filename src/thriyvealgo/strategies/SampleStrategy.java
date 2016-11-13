/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.strategies;

import thriyvealgo.indicators.Indicators;
import thriyvealgo.models.LiveStrategyData;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.util.ArrayList;
import java.util.List;

import thriyvealgo.applicationmanager.ExecutionManager;

/**
 *
 * @author Christopher
 */
public class SampleStrategy implements StrategyTemplate {

    /**
     * Decision codes are the following 0 = No trade 1 = Go Long 2 = Go Short 3
     * = Exit Long 4 = Exit Short
     */
    private int decision = 0;

    // All data from database, required
    private List<String> dateArray = new ArrayList<>();
    private List<Double> openArray = new ArrayList<>();
    private List<Double> highArray = new ArrayList<>();
    private List<Double> lowArray = new ArrayList<>();
    private List<Double> closeArray = new ArrayList<>();
    private List<Integer> volumeArray = new ArrayList<>();
    private List<Integer> countArray = new ArrayList<>();
    private List<Double> WAPArray = new ArrayList<>();
    private List<Boolean> hasGapsArray = new ArrayList<>();
    private String symbol;

    //Get all indicators
    Indicators indicators = new Indicators();

    //sma period initial settings
    int setting = 5;

    //initial settings for position and decision monitor
    int currentDecision = 0;
    int position = 0;

    //Run command called by strategy Registry. Required for all Strategies
    @Override
    public int run(int strategy, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, String sym) {
        dateArray.add(date);
        openArray.add(open);
        highArray.add(high);
        lowArray.add(low);
        closeArray.add(close);
        volumeArray.add(volume);
        countArray.add(count);
        WAPArray.add(WAP);
        hasGapsArray.add(hasGaps);

        strategy(closeArray);
        //System.out.println(decision);
        return decision;
    }
    
     @Override
    public int run(LiveStrategyData lsd) {
    	 
    	 symbol = lsd.getSym();
    	 position = ExecutionManager.orders.checkPosition(symbol);
    	 //System.out.println("This position is "+position);
        
        if(checkSym(lsd.getSym())){
            dateArray.add(lsd.getDate());
            closeArray.add(lsd.getVal());
        
        
        strategy(closeArray);
        } else {
            decision = 0;
        }
        return decision;
    }

    //Strategy code goes in here
    @Override
    public void strategy(List<Double> closeArray) {
        int tPeriods = closeArray.size();

        if (closeArray.size() > setting) {
            double sma = indicators.sma(setting, closeArray.size(), closeArray);
            if (sma > closeArray.get(tPeriods - 1)) {
                //System.out.println("The Simple Moving Average is: "+sma+" Which is above current price");
                setDecision(1);
            } else if(sma < closeArray.get(tPeriods - 1)) {
                //System.out.println("The Simple Moving Average is: "+sma+" Which is below current price");
                setDecision(2);
            } else {
            	setDecision(0);
            }

        }

    }

    //Required for all strategies
    @Override
    public int getDecision() {

        return decision;

    }

    //Required for all strategies
    @Override
    public void setDecision(int dec) {

        this.decision = dec;

       
    }

    //Required for all strategies
    @Override
    public void resetPosition() {
        position = 0;
        decision = 0;
        dateArray.clear();
        openArray.clear();
        highArray.clear();
        lowArray.clear();
        closeArray.clear();
        volumeArray.clear();
        countArray.clear();
        WAPArray.clear();
        hasGapsArray.clear();
    }

    @Override
    public boolean checkSym(String sym) {
        
        if (sym.equals("ymusd") || sym.equals("esusd")){
            return true;   
        } else{
            return false;
        }
        
        
    }

	@Override
	public void runDevelopment() {
		// TODO Auto-generated method stub
		
	}

   
}
