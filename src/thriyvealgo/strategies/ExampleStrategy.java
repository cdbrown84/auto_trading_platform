package thriyvealgo.strategies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
import thriyvealgo.applicationmanager.BacktestManager;
import thriyvealgo.models.Context;
import thriyvealgo.models.LiveStrategyData;
import thriyvealgo.utilities.DbConnection;
import thriyvealgo.utilities.GuiManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.*;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleInsets;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;


public class ExampleStrategy implements MLStrategyTemplate{
	
	private int decision = 0;
	private ArrayList<String> date = new ArrayList<String>();
	private ArrayList<Double> bid = new ArrayList<Double>();
	private ArrayList<Double> ask = new ArrayList<Double>();
	private ArrayList<ArrayList<Double>> patternAr = new ArrayList<ArrayList<Double>>();
	private ArrayList<Double> performanceAr = new ArrayList<Double>();
	private ArrayList<Double> patForRec = new ArrayList<Double>();
	private int toWhat = 0;
    private ArrayList<Double> allData = new ArrayList<Double>();
    private ArrayList<Double> avgLineSub;
    private double avgLine =0;
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private DbConnection mysql = new DbConnection();
    private GuiManager guiM = new GuiManager();
    
    //creating the chart
    private final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
    //private Series<Number, Number> testSeries = new Series<Number, Number>();
    
    private GuiManager gui = new GuiManager();
    
    private int psXVal = 0;
    private int psYVal = 0;
    private int psTVal = 0;
    private int psMVal = 0;
    private int psMinPatVal = 0;
	private int cpMVal = 0;
	private int cpTVal = 0;
	private int cpMinVal = 0;

	@Override
	public void globalSettings(int psXVal, int psYVal, int psTVal, int psMVal, int psMinPatVal, int cpTVal, int cpMVal, int cpMinVal){
		//for run method
		this.psXVal = psXVal;
		this.psYVal = psYVal;
		this.psTVal = psTVal;
		this.psMVal = psMVal;
		this.psMinPatVal = psMinPatVal;
		this.cpTVal = cpTVal;
		this.cpMinVal = cpMinVal;
		this.cpMVal = cpMVal;
	}
    
    @Override
	public int run(int strategy, String date, double open, double high,
			double low, double close, int volume, int count, double WAP,
			boolean hasGaps, String sym) {
		// TODO Auto-generated method stub
		
    	if(avgLineSub ==null){
    		avgLineSub = new ArrayList<>();
    	}
		avgLineSub.add(close);
		

        globalSettings(60, 31, 0, 29 , 30, 30, 0, 31);
        
        patternAr.clear();
	    performanceAr.clear();
	    patForRec.clear();
		
		if(avgLineSub.size()-1 > (psXVal+5)){
			livePatternStorage();
		    liveCurrentPattern();
		    livePatternRecognition(); 
		    //System.out.println(decision);
			
		}
		
		//System.out.println(avgLineSub.size());
		
		return decision;
	}

	@Override
	public int run(LiveStrategyData lsd) {
		
		if(checkSym(lsd.getSym())){
			
			if(avgLineSub ==null){
	    		avgLineSub = new ArrayList<>();
	    	}
			avgLineSub.add(lsd.getVal());
			
	
	        globalSettings(60, 31, 0, 29 , 30, 30, 0, 31);
	        
	        patternAr.clear();
		    performanceAr.clear();
		    patForRec.clear();
			
			if(avgLineSub.size()-1 > (psXVal+5)){
				livePatternStorage();
			    liveCurrentPattern();
			    livePatternRecognition(); 
			    //System.out.println(decision);
				
			}
			
			//System.out.println(avgLineSub.size());
			
		} else {
			decision = 0;
		}
			
		
		return decision;
	}
	
	@Override
	public void runDevelopment(String sym){
		
        loadData(1, sym);
        System.out.println("All data has been stored from the DB, processing");
        globalSettings(60, 31, 0, 29 , 30, 30, 0, 31);
        
        toWhat = 15000;
        while (toWhat < allData.size()){
        	
        	patternAr.clear();
		    performanceAr.clear();
		    patForRec.clear();
		    lineChart.getData().clear();
		    //gui.setLineChartData(lineChart);
		    
        
        	avgLineSub = new ArrayList<>(allData.subList(0, toWhat));   
		    patternStorage();
		    currentPattern();
		    patternRecognition();  
		    
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        
	        
	        try {
	        	System.out.println(" Enter Exit to end or....Click any key to continue....");
				String reader = br.readLine();
				if(reader.toLowerCase().equals("exit")){
					System.out.println("Development System has now ended!!!");
					break;
				}
				
		        
				//scene=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    toWhat++;
	
        }

	}
	
	@Override
	public void loadData(int temp, String sym){
		allData.clear();
		try {
            int num = 0;
            mysql.mySqlConnect(num);
            
            num = 2;
            String symbol = sym.toLowerCase();
            

            ResultSet results = mysql.getHistoricalData(symbol);
            if(results != null){
                
	            guiM.setMainTextView("Data Received from DB, Storing information for process...");
	            String name = symbol;
	    	    int count = 0;
	                while (results.next()) {
	                    if (results.getDouble("close") != -1) {
	
	                       
	                        allData.add(results.getDouble("close"));
	                        
	                        count++;
	                    }
	                }
            } else {
                System.out.println("No historical data to backtest. Please load historical data first or try another symbol");
                
            } 
		} catch (SQLException ex) {
            Logger.getLogger(BacktestManager.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	@Override
	public void loadData(){
		//Data from example
		Scanner data;
		try {
			String workingDir = System.getProperty("user.dir");
			data = new Scanner(new File(workingDir+"/data/GBPUSD1d.txt")).useDelimiter(",|\n");
			
			int x = 0;
		    // while loop
		    while (data.hasNext()) {
		      // find next line
		      
		      date.add(data.next());
		      double bidTemp = Double.parseDouble(data.next());
		      double askTemp = Double.parseDouble(data.next());
		      bid.add(bidTemp);
		      ask.add(askTemp);
		      avgLine = ((bidTemp+askTemp)/2);
		      allData.add(avgLine);
		      //System.out.println(avgLine);
		      
		      
		    }
		    
		    data.close();
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found \n"+e);
			
		}
 
	}
	
	//Taken from example
	
	private double percentageChange(double startPoint, double currentPoint){
		try{
	       double x = (((currentPoint)-startPoint)/(Math.abs(startPoint))*100.00);
	        if (x == 0.0){
	            return 0.00000001;
	        }
	        else{
	            return x;
	        }
		} catch(Exception e){
			
	        return 0.00000001;
		}
	}
	
	@Override
	public void patternStorage(){
		
		int x = avgLineSub.size() - psXVal;
		//System.out.println("avgline "+x);
		int y = psYVal;
		ArrayList<Double> p = new ArrayList<Double>();
		int t = psTVal;
		int m = psMVal;
		double currentPoint = 0;
		ArrayList<Double> outcomeRange = new ArrayList<Double>();
		
				
		while(y < x){

			ArrayList<Double> pattern = new ArrayList<Double>();
			while(t < psMinPatVal){
				p.add(t, percentageChange(avgLineSub.get(y - 30), avgLineSub.get(y - m)));
				t++;
				m--;
			}
			
			outcomeRange = new ArrayList(avgLineSub.subList(y+20, y+30));
			currentPoint = avgLineSub.get(y);
			
			double avgOutcome = 0;
			
			try{
				for (double outcome : outcomeRange){
					avgOutcome = avgOutcome+outcome;
					
				}
				avgOutcome = avgOutcome / outcomeRange.size();
				
			}
	        catch (Exception e){
	            
	            avgOutcome = 0;
	        }
	        t = psTVal;
	        m = psMVal;
	        
	        double futureOutcome = percentageChange(currentPoint, avgOutcome);
	        
	        
	        for (double eachP : p){
	        	pattern.add(eachP);
	        	
	        }

	        patternAr.add((pattern));
	        performanceAr.add(futureOutcome);
	        p.clear();
	        y++;
			
		}
	        
		System.out.println(patternAr.size());
		System.out.println(performanceAr.size());
	
	}
	
	@Override
	public void currentPattern(){
		
		ArrayList<Double> cp = new ArrayList<Double>();
	    int t = cpTVal;
	    int m = cpMVal;
	    while (t >= 1){
	        double cur = percentageChange(avgLineSub.get(avgLineSub.size()-31), avgLineSub.get(avgLineSub.size()-t));
	        cp.add(cur);
	        
	        m+=1;
	        t-=1;
	    }
	    //m=1;
	    for (double eachCp : cp){
	        
	        patForRec.add(eachCp);
	    }
	    System.out.println("Current pattern: "+patForRec);
		
	}
	
	@Override
	public void patternRecognition(){
		int patFound = 0;
	    ArrayList<ArrayList<Double>> plotPatAr = new ArrayList<ArrayList<Double>>();
	    ArrayList<Double> predictedOutcomesAr = new ArrayList<Double>();
	    ArrayList<Double> sim = new ArrayList<Double>();
	    int patDex = 0;
	    int tracker = 0;
	    final XYSeriesCollection dataset = new XYSeriesCollection();

	    for (ArrayList<Double> eachPattern : patternAr){
	    	
	        int t = 0;
	        while (t <= 29){
	            double changeP = Math.abs(percentageChange(eachPattern.get(t), patForRec.get(t)));
	            
	            double diff = 100 - changeP;
	            sim.add(t, (diff));
	            
	            t++;
	        }
	        t = 0;
	        double total = 0;
	        int count = 0;
	        for (double eachSim : sim){
	            total = total + eachSim;
	            count++;
	        }
	        double howSim = ((total)/(count));
	        
	        sim.clear();
	        
	        if (howSim > 70){
	        	//System.out.println(howSim);
	            patDex = patternAr.indexOf(eachPattern);
	            
	            patFound = 1;
	            plotPatAr.add(eachPattern);
	        }
	    }
	    
	    
        if(patFound == 1){
        	
        	//plot chart data
        	int l = 1;
        	int futurePoints =0;
        	for (ArrayList<Double> eachPatt : plotPatAr){
        		
        		futurePoints = patternAr.indexOf(eachPatt);
        	    
        		
                //double futurePoints = patternAr.indexOf(eachPatt);
                /*
                if performanceAr[futurePoints] > patForRec[29]:
                    pcolor = "#24bc00"
                else:
                    pcolor = "#d40000"
                    */
        		
        		predictedOutcomesAr.add(performanceAr.get(futurePoints));
                System.out.println(predictedOutcomesAr);
                //populating the series with data
        	    String name = "tracker "+tracker;
        	    XYSeries seriesTemp = new XYSeries(name);
        		for (double test : eachPatt){
        			
        			seriesTemp.add(l,test);
        			
	                l++;  
        		}
        		
        		
        		l = 1;
        		tracker++;	
        		 dataset.addSeries(seriesTemp);  
        	}
        	
        	String name = "tracker "+tracker;
    	    XYSeries seriesTemp = new XYSeries(name);
    		for (double test : patForRec){
    			
    			seriesTemp.add(l,test);    			
                l++;  
    		}
    		
    		
    		l = 1;
    		tracker++;	
    		
    		dataset.addSeries(seriesTemp);
    		
        	
        	System.out.println(futurePoints);
     	    //System.out.println(eachPatt);
        	
        	

        	ArrayList<Double> realOutcomeRange = new ArrayList<Double>(allData.subList(toWhat+20,toWhat+30));
        	
        	double realAvgOutcome = 0;
        	for(double ror : realOutcomeRange){
        		realAvgOutcome = realAvgOutcome + ror;
        	}
        	
	        realAvgOutcome = realAvgOutcome / realOutcomeRange.size();
	        double realMovement = percentageChange(allData.get(toWhat), realAvgOutcome);
	        
	        double predictedAvgOutcome = 0;
	        for(double pao : predictedOutcomesAr){
        		predictedAvgOutcome = predictedAvgOutcome + pao;
        	}
        	
	        predictedAvgOutcome = predictedAvgOutcome / predictedOutcomesAr.size();
	        
	        System.out.println(predictedOutcomesAr.size());
	        System.out.println(predictedOutcomesAr);
	        final int tempTracker = tracker-1;
	        
	        
	        String name1 = "real Movement";
	        String name2 = "Predicted Outcome";
	        String name3 = "Performance";
    	    XYSeries seriesTemp1 = new XYSeries(name1);
    	    XYSeries seriesTemp2 = new XYSeries(name2);
    	    XYSeries seriesTemp3 = new XYSeries(name3);
    	    
	        seriesTemp1.add(37, realMovement);
	        seriesTemp2.add(35, predictedAvgOutcome);
	        seriesTemp3.add(32, performanceAr.get(futurePoints));
	        dataset.addSeries(seriesTemp1);
	        dataset.addSeries(seriesTemp2);
	        dataset.addSeries(seriesTemp3);
	        
	        Thread displayChart = new Thread(){
	    		public void run() {
	    			
	    			float lineWidth = 8.9f;
	    			BasicStroke bs = new BasicStroke(lineWidth);
	    			JFreeChart chart = ChartFactory.createXYLineChart(
	            			"Algo Development", // chart title
	            			"Period", // x axis label
	            			"Price", // y axis label
	            			dataset, // data
	            			PlotOrientation.VERTICAL,
	            			false, // include legend
	            			true, // tooltips
	            			false // urls
	            			);
	    			XYPlot plot = (XYPlot) chart.getPlot();
	            	plot.setBackgroundPaint(Color.lightGray);
	            	plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
	            	plot.setDomainGridlinePaint(Color.white);
	            	plot.setRangeGridlinePaint(Color.white);
	            	XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
	            	//XYItemRenderer xyir = renderer.getRenderer();
	            	renderer.setSeriesStroke(tempTracker, bs);
	            	//renderer.setPaint(new Color(0,0,0,155));
	            	renderer.setSeriesPaint(tempTracker, new Color(255,0,255,255));
	            	renderer.setSeriesPaint(tempTracker+1, new Color(155,155,155,255));
	            	renderer.setSeriesPaint(tempTracker+2, new Color(155,0,155,255));
	            	renderer.setSeriesPaint(tempTracker+3, new Color(155,155,0,255));
	            	renderer.setShapesVisible(true);
	            	renderer.setShapesFilled(true);
	            	ChartPanel chartPanel = new ChartPanel(chart);
	            	ChartFrame chartFrame = new ChartFrame("Test", chart);
	            	chartFrame.setSize(800, 600);
	            	chartFrame.setVisible(true);
	    			
	    		}
	    	};
	    	displayChart.start();
	        
	
	    }

	}
	
	@Override
	public void livePatternRecognition(){
		
		int patFound = 0;
	    ArrayList<ArrayList<Double>> plotPatAr = new ArrayList<ArrayList<Double>>();
	    ArrayList<Double> predictedOutcomesAr = new ArrayList<Double>();
	    ArrayList<Double> sim = new ArrayList<Double>();
	    int patDex = 0;
	    int tracker = 0;

	    for (ArrayList<Double> eachPattern : patternAr){
	    	
	        int t = 0;
	        while (t <= 29){
	            double changeP = Math.abs(percentageChange(eachPattern.get(t), patForRec.get(t)));
	            
	            double diff = 100 - changeP;
	            sim.add(t, (diff));
	            
	            t++;
	        }
	        t = 0;
	        double total = 0;
	        int count = 0;
	        
	        for (double eachSim : sim){
	            total = total + eachSim;
	            count++;
	        }
	        double howSim = ((total)/(count));
	        
	        sim.clear();
	        
	        if (howSim > 40){
	        	//System.out.println(howSim);
	            patDex = patternAr.indexOf(eachPattern);
	            
	            patFound = 1;
	            plotPatAr.add(eachPattern);
	        }
	    }
	    
	    
        if(patFound == 1){
        	
        	//plot chart data
        	int l = 1;
        	int futurePoints =0;
        	for (ArrayList<Double> eachPatt : plotPatAr){
        		
        		futurePoints = patternAr.indexOf(eachPatt);
        	            		
        		predictedOutcomesAr.add(performanceAr.get(futurePoints));
                //System.out.println(predictedOutcomesAr);
                //populating the series with data
	 
        	}
        	
        	//System.out.println(futurePoints);
        		        
	        double predictedAvgOutcome = 0;
	        for(double pao : predictedOutcomesAr){
        		predictedAvgOutcome = predictedAvgOutcome + pao;
        	}
        	
	        predictedAvgOutcome = predictedAvgOutcome / predictedOutcomesAr.size();
	        
	        if(predictedAvgOutcome >= 0 ){
	        	
	        	setDecision(1);
	        	
	        } else if(predictedAvgOutcome < 0){
	        	setDecision(2);
	        }
	        
	        //System.out.println(predictedOutcomesAr.size());
	        //System.out.println(predictedOutcomesAr);
        } else {
        	setDecision(0);
        }
		
	}
	
	@Override
	public void livePatternStorage(){
		int x = avgLineSub.size() - psXVal;
		//System.out.println("avgline "+x);
		int y = psYVal;
		ArrayList<Double> p = new ArrayList<Double>();
		int t = psTVal;
		int m = psMVal;
		double currentPoint = 0;
		ArrayList<Double> outcomeRange = new ArrayList<Double>();
		
				
		while(y < x){

			ArrayList<Double> pattern = new ArrayList<Double>();
			while(t < psMinPatVal){
				p.add(t, percentageChange(avgLineSub.get(y - 30), avgLineSub.get(y - m)));
				t++;
				m--;
			}
			
			outcomeRange = new ArrayList(avgLineSub.subList(y+20, y+30));
			currentPoint = avgLineSub.get(y);
			
			double avgOutcome = 0;
			
			try{
				for (double outcome : outcomeRange){
					avgOutcome = avgOutcome+outcome;
					
				}
				avgOutcome = avgOutcome / outcomeRange.size();
				
			}
	        catch (Exception e){
	            
	            avgOutcome = 0;
	        }
	        t = psTVal;
	        m = psMVal;
	        
	        double futureOutcome = percentageChange(currentPoint, avgOutcome);
	        
	        
	        for (double eachP : p){
	        	pattern.add(eachP);
	        	
	        }

	        patternAr.add((pattern));
	        performanceAr.add(futureOutcome);
	        p.clear();
	        y++;
			
		}
	        
		//System.out.println(patternAr.size());
		//System.out.println(performanceAr.size());
	}
	
	@Override
	public void liveCurrentPattern(){
		
		ArrayList<Double> cp = new ArrayList<Double>();
	    int t = cpTVal;
	    int m = cpMVal;
	    while (t >= 1){
	        double cur = percentageChange(avgLineSub.get((avgLineSub.size()-1)-cpMinVal), avgLineSub.get((avgLineSub.size()-1)-t));
	        cp.add(cur);
	        
	        m+=1;
	        t-=1;
	    }
	    //m=1;
	    for (double eachCp : cp){
	        
	        patForRec.add(eachCp);
	    }
	    //System.out.println("Current pattern: "+patForRec);
		
	}

	@Override
	public int getDecision() {
		// TODO Auto-generated method stub
		return this.decision;
	}

	@Override
	public void setDecision(int dec) {
		// TODO Auto-generated method stub
		this.decision = dec;
	}

	@Override
    public boolean checkSym(String sym) {
        
        if (sym.equals("ymusd") || sym.equals("esusd")){
            return true;   
        } else{
            return false;
        }
        
        
    }

	

}
