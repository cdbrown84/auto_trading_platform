package thriyvealgo.utilities;

import java.io.*;
import java.util.ArrayList;

import thriyvealgo.models.Context;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class GuiManager {
	
	
	private String mainText;
	private PrintWriter out;
	private boolean setCreate = false;
	
	public GuiManager(){
		
		
	}
	
	public void createLogFile(){
		try {
			out = new PrintWriter(new FileOutputStream("log/test.txt"));
			setCreate = true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public synchronized void setMainTextView(String text){
		mainText = text;
		Platform.runLater(new Runnable() {
            public void run() {
            	
            	Context.getInstance().currentHomeController().setTa(mainText);
            	
            	
            }
         });
		
		notifyAll();
		
	}
	

}
