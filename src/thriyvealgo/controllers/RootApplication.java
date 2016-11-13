/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thriyvealgo.ThriyveAlgo;
import thriyvealgo.applicationmanager.BacktestManager;
import thriyvealgo.datamanager.IbLiveDataManager;
import thriyvealgo.datamanager.IbRealtimeCapture;
import thriyvealgo.models.Context;
import thriyvealgo.strategies.ExampleStrategy;
import thriyvealgo.strategies.StrategyDevelopment;
import thriyvealgo.utilities.IbConnection;
import thriyvealgo.utilities.IbEWrapperiml;
import thriyvealgo.utilities.QuandlConnection;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class RootApplication implements Initializable {

    

    private IbConnection connector = new IbConnection();

    public QuandlConnection quand = new QuandlConnection();
    public BacktestManager backTest = new BacktestManager();
    //private IbRealtimeCapture dataCapture = new IbRealtimeCapture();
    Timer recordTimer;
    Timer timer;
    Timer tradeTimer;
    public ThreadManager tm = IbEWrapperiml.tmLive;
    private Stage primaryStage;
    public NumberAxis xAxis = new NumberAxis();
    public NumberAxis yAxis = new NumberAxis();
    public XYChart.Series series = new XYChart.Series();
    public LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
    
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	Context.getInstance().setRootApplication(this);
        //TO DO
        

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        System.exit(0);
    }

    

    @FXML
    public void connectTwsAction(ActionEvent e) throws InterruptedException {
        Context.getInstance().setRootApplication(this);
        Context.getInstance().currentHomeController().setTa("Connecting");
        //HomeController.getInstance().setTa("test");
    	
        connector.connectTws();
        

    }

    public void recordTickData(ActionEvent e) {

        connector.realTimeDataCapture();
        timer = new Timer();
        timer.schedule(new IbRealtimeCapture("tickdata", tm), 1000, 1000);
        //timer.schedule(new IbLiveDataManager(), 60000, 60000);

        //connector.realTimeDataCapture();
    }
    
    public void liveDataAction(ActionEvent e){
        
        
        
        connector.realTimeDataCapture();
        tradeTimer = new Timer();
        recordTimer = new Timer();
        IbLiveDataManager.runLiveData=1;
        IbConnection.api.reqAccountUpdates(true, "All");
        
        int time = 250;
        
        recordTimer.schedule(new IbRealtimeCapture("tickdata", tm), time*500, time*500);
        tradeTimer.schedule(new IbLiveDataManager(tm), time, time);
         
        
        
        //Test the speed of java
    }
    
    public void recordfivesecData(ActionEvent e){
        connector.realTimeBarAction();
        timer = new Timer();
        timer.schedule(new IbRealtimeCapture("fivesecdata", tm), 60000, 60000);
        System.out.println("Starting to record data");
    }

    public void getData(ActionEvent e) throws InterruptedException {
        connector.realTimeAction();

    }
    
    public void getStrategyDev(ActionEvent e){
    	
    	try {
    		FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(ThriyveAlgo.class.getResource("/views/HistoricalData.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage historicalDataStage = new Stage();
	        historicalDataStage.initModality(Modality.WINDOW_MODAL);
	        historicalDataStage.setTitle("Thriyve Algo - Development - Historical Data Settings");
	        historicalDataStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        historicalDataStage.setScene(scene);
			historicalDataStage.showAndWait();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    }
    
    public void getTestChart(ActionEvent e){
    	//StrategyDevelopment sdc = new StrategyDevelopment();
    	//sdc.testChart();
    	try {
    		FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(ThriyveAlgo.class.getResource("/views/StrategyDev.fxml"));
        	BorderPane page = (BorderPane)loader.load();
			Stage strategyDevStage = new Stage();
			strategyDevStage.initModality(Modality.WINDOW_MODAL);
			strategyDevStage.setTitle("Thriyve Algo - Development - Strategy Development");
			strategyDevStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        strategyDevStage.setScene(scene);
	        strategyDevStage.showAndWait();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	
    	
    }
    
    public void setLineChartData(XYChart.Series series){
    	this.lineChart.getData().retainAll();
    	this.lineChart.getData().addAll(series);
    }

    public void getDataHistorical(ActionEvent e) {
      
    	try {
    		FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(ThriyveAlgo.class.getResource("/views/HistoricalData.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage historicalDataStage = new Stage();
	        historicalDataStage.initModality(Modality.WINDOW_MODAL);
	        historicalDataStage.setTitle("Thriyve Algo - Development - Historical Data Settings");
	        historicalDataStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        historicalDataStage.setScene(scene);
			historicalDataStage.showAndWait();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
        
    }

    public void runBackTest(ActionEvent e) {

        System.out.println("Running back test...");
        try {
    		FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(ThriyveAlgo.class.getResource("/views/BackTest.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage historicalDataStage = new Stage();
	        historicalDataStage.initModality(Modality.WINDOW_MODAL);
	        historicalDataStage.setTitle("Thriyve Algo - Development - BackTester Settings");
	        historicalDataStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        historicalDataStage.setScene(scene);
			historicalDataStage.showAndWait();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //backTest.backTest(1);

    }

    public void contractSetter(ActionEvent e) {
        connector.setContract();
    }

    public void launchCommand(ActionEvent e) {
        Runtime rt = Runtime.getRuntime();
        try {
            String new_dir = "Testing";
            rt.exec("cmd.exe /c cd \"" + new_dir + "\" & start cmd.exe /k \"java -flag -flag -cp terminal-based-program.jar\"");
        } catch (IOException ex) {
            Logger.getLogger(RootApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnectTwsAction(ActionEvent e) {
        connector.disconnectTws();
    }

   
    
    
    
    

}
