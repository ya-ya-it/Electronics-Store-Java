package Views;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jwright
 */
public class Main extends Application{
    
    public static void main(String[] args)
    {
        launch(args);    
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TableOfProducts.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Products");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
