/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Christopher
 */
//The Below are the original codes for using JavaFx with FXML for a GUI
public class ThriyveAlgo extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    public static FXMLLoader mainLoader;
    
    /**
     * @param args the command line arguments
     *
     *
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        this.primaryStage = stage;
        this.primaryStage.setTitle("Thriyve Algo - Development");
        
        initRootLayout();

        showHomeLayout();
        
        

    }

    private void initRootLayout(){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ThriyveAlgo.class.getResource("/views/RootApplication.fxml"));
        rootLayout = (BorderPane)loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private void showHomeLayout(){
        
        try {
            // Load person overview.
            mainLoader = new FXMLLoader();
            mainLoader.setLocation(ThriyveAlgo.class.getResource("/views/Home.fxml"));
            
            AnchorPane home = (AnchorPane) mainLoader.load();
            
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(home);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
