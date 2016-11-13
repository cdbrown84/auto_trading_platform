/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.models;


import thriyvealgo.controllers.BackTesterController;
import thriyvealgo.controllers.HomeController;
import thriyvealgo.controllers.RootApplication;
import thriyvealgo.controllers.StrategyDevController;
import thriyvealgo.utilities.IbConnection;

/**
 *
 * @author Christopher
 */
public class Context {
    
    private final static Context instance = new Context();
    private RootApplication rootApplication = new RootApplication();
    private HomeController homeController;
    private IbConnection ibConnection;
    private BackTesterController backTesterController;
    private StrategyDevController strategyDevController;
    
    
    
    public static Context getInstance() {
        return instance;
    }

    

    public HomeController currentHomeController() {
        return homeController;
    }
    
    public void setHomeController(HomeController hc){
        this.homeController = hc;
    }
    
    public void setBackTesterController(BackTesterController btc){
    	this.backTesterController = btc;
    }
    
    public BackTesterController getBackTesterController(){
    	return backTesterController;
    }
    
    public RootApplication currentRootApplication(){
        return rootApplication;
    }
    
    public void setRootApplication(RootApplication ra){
        this.rootApplication = ra;
    }
    
    public void setIbConnection(IbConnection ibc){
    	this.ibConnection = ibc;
    }
    
    public IbConnection currentIbConnection(){
    	return ibConnection;
    }
    
    public StrategyDevController currentStrategyDevController() {
        return strategyDevController;
    }
    
    public void setStrategyDevController(StrategyDevController sdc){
        this.strategyDevController = sdc;
    }
}
