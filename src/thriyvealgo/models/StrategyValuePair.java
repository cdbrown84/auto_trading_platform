package thriyvealgo.models;

public class StrategyValuePair {
	
	private final int key;
	private final String value;
	
   public StrategyValuePair(int key, String value) {
	   this.key = key;
	   this.value = value;
   }

  public int getKey()   {    return key;    }

  public String toString() {    return value;  }
	

}
