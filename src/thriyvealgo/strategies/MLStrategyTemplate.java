package thriyvealgo.strategies;

import thriyvealgo.models.LiveStrategyData;

public interface MLStrategyTemplate {
	
	//Run command called by strategy Registry. Required for all Strategies
    public int run(int strategy, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps, String sym);
    
    //Run command using LiveStrategyData Object
    public int run(LiveStrategyData lsd);
    
    public void runDevelopment(String sym);
    
    public void loadData();
    
    public void loadData(int temp, String sym);
    
    public void patternStorage();
    
    public void currentPattern();
    
    public void patternRecognition();
    
    //Required for all strategies
    public int getDecision();

    //Required for all strategies
    public void setDecision(int dec);

       
    public void globalSettings(int psXVal, int psYVal, int psTVal, int psMVal, int psMinPatVal, int cpTVal, int cpMVal, int cpMinVal);
    
    public void liveCurrentPattern();
    
    public void livePatternStorage();
    
    public void livePatternRecognition();
    
    public boolean checkSym(String sym);

}
