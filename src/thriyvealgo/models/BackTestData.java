package thriyvealgo.models;

public class BackTestData {
	
	private int strategyNum; 
	private String sym; 
	private String cur;
	
	public BackTestData(){
		
	}
	
	public void setStrategyNum(int strategyNum){
		this.strategyNum = strategyNum;
	}
	
	public int getStrategyNum(){
		return strategyNum;
	}
	
	public void setSymbol(String sym){
		this.sym = sym.toLowerCase();
	}
	
	public void setCurrency(String cur){
		this.cur = cur.toLowerCase();
	}
	
	public String getSymbol(){
		String symbol;
		symbol = sym+cur;
		return symbol;
	}
	
	public void setStartDate(){
		
	}
	
	public String getStartDate(){
		return "s";
	}
	
	public void setEndDate(){
		
	}
	
	public String getEndDate(){
		return "s";
	}
	
	public void setPeriod(){
		
	}
	
	public String getPeriod(){
		return "1 min";
	}

}
