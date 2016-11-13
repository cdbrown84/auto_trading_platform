/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import thriyvealgo.datamanager.IbLiveDataObject;
import thriyvealgo.models.Context;
import thriyvealgo.strategies.ExampleStrategy;
import thriyvealgo.utilities.GuiManager;
import thriyvealgo.utilities.IbConnection;
import thriyvealgo.utilities.ThreadManager;

/**
 *
 * @author Christopher
 */
public class HomeController implements Initializable{
	
	public static ThreadManager tmLive = new ThreadManager();
    public int counter = 0;
    private ArrayList<String> data = new ArrayList();
    
    @FXML
    public TextArea taMain;
    
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Context.getInstance().setHomeController(this);

    	//taMain.appendText(" i wrk");
        
    }
   
    public void setTa(String text){
        
        taMain.appendText(text+"\n");
    }
    
    @FXML
    public void dataTester(ActionEvent e){
    	
    	/*
    	
    	GuiManager guiM = new GuiManager();
    	
    	
    	for(int x = 1; x<=1000000; x++){
    		
    	
    		IbLiveDataObject ld = new IbLiveDataObject(1, 1, 1, tmLive);
    		
    		counter++;
    		
    		
    		
    		
    		
    	}
    	guiM.setMainTextView("The counter is "+counter);
    	*/
    	
    	//IbConnection.api.reqAccountUpdates(true, "All");
    	Thread runData = new Thread(){
    		public void run() {
    			ExampleStrategy es = new ExampleStrategy();
    	    	
    	    	
    	    	//es.runDevelopment();
    			
    		}
    	};
    	runData.start();
    	
    	

    }
    
    
    
    
    
}
