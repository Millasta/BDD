package application;
	
import java.io.FileInputStream;
import java.sql.SQLException;

import Controler.ControlePrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(); 
			FileInputStream fxmlStream = new FileInputStream("src/View.fxml");
			Pane root = loader.load(fxmlStream);

			primaryStage.setTitle("TP3 BDD");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			ControlePrincipal controler = new ControlePrincipal();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}
}
