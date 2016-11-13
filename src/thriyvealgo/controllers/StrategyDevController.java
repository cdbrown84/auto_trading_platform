package thriyvealgo.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import thriyvealgo.models.Context;
import thriyvealgo.models.SeriesArray;
import thriyvealgo.models.StrategyValuePair;
import thriyvealgo.strategies.ExampleStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StrategyDevController implements Initializable {
	
	@FXML
	private ChoiceBox<StrategyValuePair>  cbStrategy = new ChoiceBox<StrategyValuePair>();
	
	@FXML
	private TextField  tfSym;
	
	@FXML
	private TextField  tfExp;
	
	@FXML
	private TextField  tfEndDate;
	
	@FXML
	private TextField  tfStartDate;
	
	@FXML
	private TextField  tfCur;
	
	@FXML
	private TextField  tfExc;
	
	@FXML
	private TextField  tfPeriod;
	
	@FXML
	private TextField  tfSec;
		
	public StrategyDevController(){
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Context.getInstance().setStrategyDevController(this);
		cbStrategy.getItems().add(new StrategyValuePair(3, "Example Strategy ML"));
		
	}
	
	@FXML
	public void runExampleStrategy(ActionEvent e){
		
		Thread runData = new Thread(){
    		public void run() {
    			ExampleStrategy es = new ExampleStrategy();
    	    	
    	    	
    	    	//es.runDevelopment();
    			
    		}
    	};
    	runData.start();
		
	}
	
	@FXML
	public void runDevelopment(ActionEvent e){
		selectStrategy(cbStrategy.getValue().getKey());
		Node  source = (Node)  e.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	@FXML
	public void closeWindow(ActionEvent e){
		Node  source = (Node)  e.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	private void selectStrategy(int strategyId){
		
		final String symbol = tfSym.getText().toLowerCase()+tfCur.getText().toLowerCase();
		switch(strategyId){
			case 3:
				Thread runData = new Thread(){
		    		public void run() {
		    			ExampleStrategy es = new ExampleStrategy();
		    	    	
		    	    	
		    	    	es.runDevelopment(symbol);
		    			
		    		}
		    	};
		    	runData.start();
		    	break;
	    	default:
	    		System.out.println("The strategy selected is not available for advanced development");
				
		}
	}
	
	

}
