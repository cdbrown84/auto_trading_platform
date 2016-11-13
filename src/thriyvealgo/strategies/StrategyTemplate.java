/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.strategies;

import thriyvealgo.indicators.Indicators;
import thriyvealgo.models.LiveStrategyData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christopher
 */
public interface StrategyTemplate {

    //Run command called by strategy Registry. Required for all Strategies
    public int run(int strategy, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, String sym);
    
    //Run command using LiveStrategyData Object
    public int run(LiveStrategyData lsd);
    
    //Used for the development
    public void runDevelopment();
    
    public boolean checkSym(String sym);

    //Actual strategy code
    public void strategy(List<Double> closeArray);

    //Required for all strategies
    public int getDecision();

    //Required for all strategies
    public void setDecision(int dec);

    //Required for all strategies
    public void resetPosition();

}
