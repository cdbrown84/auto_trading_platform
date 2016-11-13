/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.applicationmanager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import thriyvealgo.controllers.RootApplication;
import thriyvealgo.models.BackTestData;
import thriyvealgo.models.Context;
import thriyvealgo.utilities.ContractSpec;
import thriyvealgo.utilities.DbConnection;
import thriyvealgo.utilities.GuiManager;
import thriyvealgo.utilities.IbConnection;

/**
 *
 * @author Christopher
 */


public class BacktestManager {

    public BacktestManager() {

    }

    DbConnection mysql = new DbConnection();
    StrategyManager strategy = new StrategyManager();
    ContractSpec contract = new ContractSpec();
    DescriptiveStatistics stats = new DescriptiveStatistics();
    private int num = 0;
    private GuiManager guiM = new GuiManager();

    private final List<String> dateArray = new ArrayList<>();
    private List<Double> openArray = new ArrayList<>();
    private List<Double> highArray = new ArrayList<>();
    private List<Double> lowArray = new ArrayList<>();
    private List<Double> closeArray = new ArrayList<>();
    private List<Integer> volumeArray = new ArrayList<>();
    private List<Integer> countArray = new ArrayList<>();
    private List<Double> WAPArray = new ArrayList<>();
    private List<Boolean> hasGapsArray = new ArrayList<>();
    private List<Double> pointsArray = new ArrayList<>();
    private List<Double> pnlArray = new ArrayList<>();
    private int decision = 0;
    private int tDecision = 0;
    private int position = 0;
    private double entryPrice = 0;
    private double exitPrice = 0;
    private double points = 0;
    private double pointsTally = 0;
    private double maxPeak = 0;
    private double maxDrawdown = 0;
    private double maxPnLPeak = 0;
    private double maxPnLDrawdown = 0;
    private double livePnL = 0;
    private double lLoss = 0;
    private double lGain = 0;
    private double lPnlLoss = 0;
    private double lPnlGain = 0;
    private double sharpeRatio = 0;

    private int accountBalance = 0;
    private int orderSize = 0;
    private double pnlTally = 0;
    private int returnPercent = 0;
    private int maxDrawdownPercent = 0;
    private int maxPnLPeakPercent = 0;
    private int tradeCount = 0;
    private int maxRisk = 0;
    private int lTrade = 0;
    private int gTrade = 0;
    private double perTradeGain = 0;
    private double perTradeLoss = 0;
    private XYSeries series;
    private double lowerBound = 0;
    private double upperBound = 0;
    private double tempRange = 0;

    private RiskManager risk = new RiskManager();

    private DecimalFormat df2 = new DecimalFormat("###.##");
    
    final XYSeriesCollection dataset = new XYSeriesCollection();
    //Main backTest method which is called to run the back test
    public void backTest(BackTestData btd) {
    	
    	

        double actionPrice = 0;
        boolean initial = false;

        try {
            //Here is where the entire back test will run

            //First step is to get the data and run in loop
            mysql.mySqlConnect(num);
            
            num = 2;
            String symbol = btd.getSymbol();
            

            ResultSet results = mysql.getHistoricalData(symbol);
            if(results != null){
                
            guiM.setMainTextView("Data Received from DB, Storing information for process...");
            String name = symbol;
    	    series = new XYSeries(name);
    	    int count = 0;
                while (results.next()) {
                    if (results.getDouble("close") != -1) {

                        dateArray.add(results.getString("date"));
                        openArray.add(results.getDouble("open"));
                        highArray.add(results.getDouble("high"));
                        lowArray.add(results.getDouble("low"));
                        closeArray.add(results.getDouble("close"));
                        volumeArray.add(results.getInt("volume"));
                        countArray.add(results.getInt("count"));
                        WAPArray.add(results.getDouble("WAP"));
                        hasGapsArray.add(results.getBoolean("hasGaps"));
                        series.add(count, results.getDouble("close"));
                        if(initial == false){
                        	upperBound = results.getDouble("close");
                        	lowerBound = results.getDouble("close");
                        	initial = true;
                    	}else if(results.getDouble("close") > upperBound){
                        	upperBound = results.getDouble("close");
                        	tempRange = results.getDouble("close");
                        	
                        } else if(results.getDouble("close") < lowerBound) {
                        	lowerBound = results.getDouble("close");
                        	//tempRange = closeArray.get(k);
                        }
                        count++;
                    }
                }
                dataset.addSeries(series);
            } else {
                System.out.println("No historical data to backtest. Please load historical data first or try another symbol");
                
            }
            
            if(!dateArray.isEmpty()){

                for (int k = 0; k < dateArray.size(); k++) {

                    //System.out.println(dateArray.get(k));
                    //Get Decision 
                    decision = strategy.strategyListRun(btd.getStrategyNum(), dateArray.get(k), openArray.get(k), highArray.get(k), lowArray.get(k), closeArray.get(k),
                            volumeArray.get(k), countArray.get(k), WAPArray.get(k), hasGapsArray.get(k), symbol);

                    //Check if at the end of the list 
                    if (k == dateArray.size()) {
                        actionPrice = closeArray.get(k + 1);
                    } else {
                        actionPrice = closeArray.get(k);
                    }
                    
                    
                    //Action Trade
                    tradeManagement(decision, dateArray.get(k), openArray.get(k), highArray.get(k), lowArray.get(k), closeArray.get(k), actionPrice, symbol);
                    if(maxRisk == 1){
                    	break;
                    }

                }
                getPerformance();
                printResults();
            }

        } catch (SQLException ex) {
            Logger.getLogger(BacktestManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Decides if should enter of exit a position based on the decision and risk parameters
    public void tradeManagement(int decision, String date, double open, double high, double low, double close, double actionPrice, String sym) {
        // method will decided what to do with the trade
        //System.out.println("Position is "+position);
        if (position > 0) {
            livePnL = (close * position) - entryPrice;

        } else if (position < 0) {
            livePnL = entryPrice + (close * position);

        } else {

        }
        //System.out.println("Pnl Tally is $"+livePnL+", and the entry price "+entryPrice+" the current price is "+close*position);
        maxRisk = risk.maxRisk((livePnL * contract.contractSize(sym)) + pnlTally);
        //System.out.println(maxRisk);

        if (maxRisk == 1) {
            if (position > 0) {

                tradeCount++;
                exitPrice = actionPrice * position;
                position = 0;
                points = 0;
                points = livePnL;
                //System.out.println("Exiting Long Position... "+points);
                positionManagement(points, sym);

            } else if (position < 0) {

                tradeCount++;
                position = 0;
                exitPrice = actionPrice * -position;
                points = 0;
                points = livePnL;
                //System.out.println("Exiting Short Position... $"+points);
                positionManagement(points, sym);

            } else {

            }

        } else if (maxRisk == 0) {
            switch (decision) {
                case 0:
                    //No Trade

                    //System.out.println("No Trade made");
                    break;
                case 1:
                    //Go long Statement
                    if (position == 0) {
                        position = risk.orderSize(sym, pnlTally, close);
                        entryPrice = actionPrice * position;

                        //System.out.println("Going Long!!!!!!");
                    } else if(position < 0){
                        
                    	tradeCount++;

                        exitPrice = actionPrice * -position;
                        points = 0;
                        points = entryPrice + (actionPrice * position);
                        //System.out.println(actionPrice+" "+position);
                        //System.out.println("Exiting Short Position... $"+points);
                        positionManagement(points, sym);
                        entryPrice = 0;
                        position = 0;
                    }

                    break;
                case 2:
                    //Go Short Statements
                    if (position == 0) {
                        position = -risk.orderSize(sym, pnlTally, close);
                        entryPrice = actionPrice * -position;
                        //System.out.println("Going Short!!!!!!");
                    } else if(position > 0 ){
                        
                    	tradeCount++;
                        exitPrice = actionPrice * position;

                        points = 0;
                        points = (actionPrice * position) - entryPrice;

                        //System.out.println("Exiting Long Position... "+points);
                        positionManagement(points, sym);
                        entryPrice = 0;
                        position = 0;
                    }

                    break;
                
                default:
                    throw new IllegalArgumentException("Invalid decision");
            }
        }
        System.out.println(pnlTally+" Trade count is: "+tradeCount);
        
        

    }

    //gets the current commissions level
    public double commissions() {
        return 3;
    }

    //Tracks performance in points and pnl
    public void positionManagement(double points, String sym) {

        pointsTally = pointsTally + points;
        pnlTally = pnlTally + ((points * contract.contractSize(sym)) - commissions());
        stats.addValue(pnlTally);

        pointsArray.add(points);
        pnlArray.add((points * contract.contractSize(sym)) - commissions());

    }

    //After the strategy has recorded trades get the performance 
    public void getPerformance() {

        double ptTracker = 0;
        double tempPoints = 0;
        double tempPoints2 = 0;
        double pnlTracker = 0;
        double tempPnlPoints = 0;
        double tempPnlPoints2 = 0;
        
        Thread displayChart = new Thread(){
    		@SuppressWarnings("deprecation")
			public void run() {
    			
    			
    			JFreeChart chart = ChartFactory.createXYLineChart(
            			"Algo Backtester", // chart title
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
            	NumberAxis range = (NumberAxis) plot.getRangeAxis();
            	range.setRange(lowerBound*.99, upperBound*1.01);
            	XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            	
            	renderer.setShapesVisible(true);
            	renderer.setShapesFilled(true);
            	ChartPanel chartPanel = new ChartPanel(chart);
            	ChartFrame chartFrame = new ChartFrame("Thriyve Algo - Market Results", chart);
            	chartFrame.setSize(1200, 800);
            	chartFrame.setVisible(true);
            	
    			
    		}
    	};
    	displayChart.start();
        

        accountBalance = risk.getAccountBal();
        returnPercent = (int) ((pnlTally / accountBalance) * 100);

        for (int x = 0; x < pnlArray.size(); x++) {

            //Count winning trades not factoring in commissions
            if (pnlArray.get(x) > 0) {
                gTrade++;
            } else if (pnlArray.get(x) < 0) {
                lTrade++;
            } else {
            	
            }

            //largest points gain and loss
            if (pointsArray.get(x) < lLoss) {
                lLoss = pointsArray.get(x);

            } else if (pointsArray.get(x) > lGain) {
                lGain = pointsArray.get(x);

            }

            //largest pnl gain and loss
            if (pnlArray.get(x) < lPnlLoss) {
                lPnlLoss = pnlArray.get(x);
            } else if (pnlArray.get(x) > lPnlGain) {
                lPnlGain = pnlArray.get(x);
            }

            //peak to drawdown in points
            tempPoints = tempPoints + pointsArray.get(x);
            if ((pointsArray.get(x) > ptTracker || pointsArray.get(x) > 0) && (maxPeak <= tempPoints && tempPoints2 < tempPoints)) {

                maxPeak = maxPeak + pointsArray.get(x);
            } else if ((pointsArray.get(x) < ptTracker || pointsArray.get(x) < 0) && (maxDrawdown >= tempPoints && tempPoints2 > tempPoints)) {
                maxDrawdown = maxDrawdown + pointsArray.get(x);
            }
            
            tempPoints2 = tempPoints;
            ptTracker = pointsArray.get(x);

            //peak to drawdown for pnl
            tempPnlPoints = tempPnlPoints + pnlArray.get(x);
            if ((pnlArray.get(x) > pnlTracker || pnlArray.get(x) > 0) && (maxPnLPeak <= tempPnlPoints && tempPnlPoints2 < tempPnlPoints)) {

                maxPnLPeak = maxPnLPeak + pnlArray.get(x);
            } else if ((pnlArray.get(x) < pnlTracker || pnlArray.get(x) < 0) && (maxPnLDrawdown >= tempPnlPoints && tempPnlPoints2 > tempPnlPoints)) {
                maxPnLDrawdown = maxPnLDrawdown + pnlArray.get(x);
                //System.out.println(maxPnLDrawdown);
            }
            tempPnlPoints2 = tempPnlPoints;
            pnlTracker = pnlArray.get(x);

        }

        maxDrawdownPercent = (int) ((maxPnLDrawdown / accountBalance) * 100);
        maxPnLPeakPercent = (int) ((maxPnLPeak / accountBalance) * 100);
        double mean = stats.getMean();
        double riskFreeReturn = 0.02;
        double std = stats.getStandardDeviation();
        sharpeRatio = (mean - (riskFreeReturn / 12)) / std * Math.sqrt(12);
        perTradeGain = ((double) gTrade / (double) tradeCount) * 100;
        perTradeLoss = ((double) lTrade / (double) tradeCount) * 100;

    }

    //Print the results to the console
    public void printResults() {
        System.out.println("***********************************************BACKTEST RESULTS***************************************************");
        System.out.println("Points: " + pointsTally + " MaxPeak: " + maxPeak + " MaxDrawdown: " + maxDrawdown + " Largest Gain: " + lGain + " Largest Loss: " + lLoss);
        System.out.println("PnL: $" + pnlTally + " MaxPeak: $" + maxPnLPeak + " MaxDrawdown: $" + maxPnLDrawdown + " Largest PNLGain: " + lPnlGain + " Largest PNL Loss: " + lPnlLoss);
        System.out.println("Total number of trades: " + tradeCount + " Total Commission Costs: $" + tradeCount * commissions());
        System.out.println("Returns: " + returnPercent + "% Max Returns: " + maxPnLPeakPercent + "% Max Drawdown: " + maxDrawdownPercent + "%");
        System.out.println("Sharpe Ratio " + sharpeRatio);
        System.out.println("Number of Winning trades: " + gTrade + " Percentage Winning: " + perTradeGain + "%" + " Number of Lossing Trades: " + lTrade + " Percentage Lossing: " + perTradeLoss + "%");
        
        guiM.setMainTextView(
        		
        		"***********************************************BACKTEST RESULTS*************************************************** \n"+
        		"Points: " + pointsTally + " MaxPeak: " + maxPeak + " MaxDrawdown: " + maxDrawdown + " Largest Gain: " + lGain + " Largest Loss: " + lLoss+"\n"+
        		"PnL: $" + pnlTally + " MaxPeak: $" + maxPnLPeak + " MaxDrawdown: $" + maxPnLDrawdown + " Largest PNLGain: " + lPnlGain + " Largest PNL Loss: " + lPnlLoss+"\n"+
        		"Total number of trades: " + tradeCount + " Total Commission Costs: $" + tradeCount * commissions()+"\n"+
        		"Returns: " + returnPercent + "% Max Returns: " + maxPnLPeakPercent + "% Max Drawdown: " + maxDrawdownPercent + "%"+"\n"+
        		"Sharpe Ratio " + sharpeRatio+"\n"+
        		"Number of Winning trades: " + gTrade + " Percentage Winning: " + perTradeGain + "%" + " Number of Lossing Trades: " + lTrade + " Percentage Lossing: " + perTradeLoss + "%"
        		
        		);
        
       resetVariables();
    }


    //Reset all Variables
    public void resetVariables() {
        decision = 0;
        tDecision = 0;
        position = 0;
        entryPrice = 0;
        exitPrice = 0;
        points = 0;
        pointsTally = 0;
        maxPeak = 0;
        maxDrawdown = 0;
        maxPnLPeak = 0;
        maxPnLDrawdown = 0;
        sharpeRatio = 0;

        accountBalance = 0;
        orderSize = 0;
        pnlTally = 0;
        returnPercent = 0;
        maxDrawdownPercent = 0;
        maxPnLPeakPercent = 0;
        livePnL = 0;
        num = 0;
        tradeCount = 0;
        lLoss = 0;
        lGain = 0;
        lPnlLoss = 0;
        lPnlGain = 0;

        lTrade = 0;
        gTrade = 0;
        perTradeGain = 0;
        perTradeLoss = 0;
        maxRisk = 0;

        dateArray.clear();
        openArray.clear();
        highArray.clear();
        lowArray.clear();
        closeArray.clear();
        volumeArray.clear();
        countArray.clear();
        WAPArray.clear();
        hasGapsArray.clear();
        strategy.reset();
        pointsArray.clear();
        pnlArray.clear();
        stats.clear();
        

    }

}
