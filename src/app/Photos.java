package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
*/
public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        LoginController listController = loader.getController();
        listController.start(primaryStage);

        primaryStage.setTitle("Photo Album: Varun Venkateshan and Yashwant Balaji");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
