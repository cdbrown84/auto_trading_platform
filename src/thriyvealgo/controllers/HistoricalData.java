package thriyvealgo.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import thriyvealgo.models.Context;
import thriyvealgo.utilities.IbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HistoricalData implements Initializable{
	
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void runHistoricalData(ActionEvent e){
		
		IbConnection.symbol = tfSym.getText();		
        IbConnection.expiry = tfExp.getText();
        IbConnection.security = tfSec.getText();
        IbConnection.currency = tfCur.getText();
        IbConnection.exchange = tfExc.getText();
        IbConnection.startDate = tfStartDate.getText();
        IbConnection.endDate = tfEndDate.getText();
        IbConnection.barSize = tfPeriod.getText();
        Context.getInstance().currentIbConnection().historicData();
        
        Node  source = (Node)  e.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
		
	}
	
	@FXML
	public void closeHistoricalWindow(ActionEvent e){
		Node  source = (Node)  e.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
	}

}
