package thriyvealgo.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thriyvealgo.applicationmanager.ExecutionManager;
import thriyvealgo.models.LiveStrategyData;

public class RandomStrategy implements StrategyTemplate{
	
	private int decision = 0;
	private Random random = new Random();
	private String symbol;
	private List<String> dateArray = new ArrayList<>();
    private List<Double> openArray = new ArrayList<>();
    private List<Double> highArray = new ArrayList<>();
    private List<Double> lowArray = new ArrayList<>();
    private List<Double> closeArray = new ArrayList<>();
    int position = 0;


	@Override
	public int run(int strategy, String date, double open, double high,
			double low, double close, int volume, int count, double WAP,
			boolean hasGaps, String sym) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int run(LiveStrategyData lsd) {
		// TODO Auto-generated method stub
		
		symbol = lsd.getSym();
   	 	position = ExecutionManager.orders.checkPosition(symbol);
		
   	 if(checkSym(lsd.getSym())){
         dateArray.add(lsd.getDate());
         closeArray.add(lsd.getVal());
         decision = random.nextInt(3);
     
     
     } else {
         decision = 0;
     }
     return decision;
		
		
	}

	@Override
	public boolean checkSym(String sym) {
		// TODO Auto-generated method stub
		 if (sym.equals("ymusd") || sym.equals("esusd")){
	            return true;   
	        } else{
	            return false;
	        }
	}

	@Override
	public void strategy(List<Double> closeArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDecision() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDecision(int dec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetPosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runDevelopment() {
		// TODO Auto-generated method stub
		
	}

}
