package thriyvealgo.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import thriyvealgo.applicationmanager.BacktestManager;
import thriyvealgo.models.BackTestData;
import thriyvealgo.models.Context;
import thriyvealgo.models.StrategyValuePair;
import thriyvealgo.utilities.IbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BackTesterController implements Initializable{
	
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
	
	@FXML
	private ChoiceBox<StrategyValuePair>  cbStrategy = new ChoiceBox<StrategyValuePair>();
	
	
	private BackTestData btd = new BackTestData();
	private BacktestManager btm = new BacktestManager();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		cbStrategy.getItems().add(new StrategyValuePair(1, "Sample Strategy"));
		cbStrategy.getItems().add(new StrategyValuePair(2, "Random Generated Strategy"));
		cbStrategy.getItems().add(new StrategyValuePair(3, "Example Strategy ML"));
	}
	
	@FXML
	public void runBackTest(ActionEvent e){
		
		btd.setSymbol(tfSym.getText());
		btd.setCurrency(tfCur.getText());
		btd.setStrategyNum(cbStrategy.getValue().getKey());
        //IbConnection.startDate = tfStartDate.getText();
        //IbConnection.endDate = tfEndDate.getText();
        //IbConnection.barSize = tfPeriod.getText();
		Thread runBT = new Thread(){
			@Override
			public void run(){
				btm.backTest(btd);
			}
		};
        
        runBT.start();
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

}
