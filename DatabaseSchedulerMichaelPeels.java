package database.scheduler.michael.peels;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Michael Peels
 * username: test
 * password: test
 * 
 * alternate username: michael
 * alternate password: password1
 * 
 * username + password are case sensitive
 * 
 */
public class DatabaseSchedulerMichaelPeels extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Scheduler Pro - By Michael Peels");
        stage.setResizable(false);        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
