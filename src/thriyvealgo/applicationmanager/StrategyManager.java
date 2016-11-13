/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.applicationmanager;

import thriyvealgo.models.LiveStrategyData;
import thriyvealgo.strategies.ExampleStrategy;
import thriyvealgo.strategies.RandomStrategy;
import thriyvealgo.strategies.SampleStrategy;
import thriyvealgo.utilities.GuiManager;

import java.util.Random;

/**
 *
 * @author Christopher
 */
public class StrategyManager {
	
	private GuiManager guiM = new GuiManager();

    Random random = new Random();
    SampleStrategy testStrategy = new SampleStrategy();
    RandomStrategy randomStrategy = new RandomStrategy();
    ExampleStrategy exampleStrategy = new ExampleStrategy();

    public int strategyListRun(int strategy, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, String sym) {

        //list case for all strategies and call the individual strategy methods
        int decision = 0;
        switch (strategy) {
            case 0:
                break;
            case 1:

                decision = testStrategy.run(strategy, date, open, high, low, close, volume, count, WAP, hasGaps, sym);
                
                break;
            case 2:
                decision = random.nextInt(3);
                break;
            case 3:
                decision = exampleStrategy.run(strategy, date, open, high, low, close, volume, count, WAP, hasGaps, sym);
                break;
            default:
                System.out.println(strategy);
                throw new IllegalArgumentException("Strategy not registered: " + strategy);
        }
        return decision;

    }
    
    public int strategyListRun(LiveStrategyData lsd){
        
        int decision = 0;
        int strategyNum = lsd.getStrategyNum();
        switch (strategyNum) {
            case 0:
                break;
            case 1:

                decision = testStrategy.run(lsd);
                //decision = random.nextInt(5);
                break;
            case 2:
                decision = random.nextInt(2);
                break;
                
            case 3:
                decision = randomStrategy.run(lsd);
                break;
                
            default:
                System.out.println(lsd.getStrategyNum());
                throw new IllegalArgumentException(" Strategy not registered: " + strategyNum);
        }
        guiM.setMainTextView("Sym: "+lsd.getSym()+" Price: "+lsd.getVal()+ " Strategy decision: "+decisionName(decision));
        //System.out.println(decision);
        return decision;
       
    }

    public void reset() {
        testStrategy.resetPosition();

    }
    
    public String decisionName(int decision){
    	
    	String dec;
    	switch (decision) {
        case 0:
            //No Trade
        	dec = "No Trade";
            //System.out.println("No Trade made");
            break;
        case 1:
            //Go long Statement
            dec = "Go Long";
            break;
        case 2:
            //Go Short Statements
            dec = "Go Short";

            break;
        case 3:
            // Exit Long Statements
            dec = "Exiting Long";

            break;
        case 4:
            //Exit Short Statements
            dec = "Exiting short";

            break;
        default:
            throw new IllegalArgumentException("Invalid decision");
            
            
    	}
    	
    	return dec;
    	
    }

}
