package controller;

import model.Album;
import model.Tag;
import model.User;
import model.Photo;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import model.Serialize;


/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class LoginController {
    
    public Stage mainStage;
    public  ArrayList<User> UsersList;

    @FXML TextField login;
    @FXML Button login_btn;

    
    public void start(Stage mainStage) throws Exception{
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This is the first time a user is using our app so we must initialize users list and add stock.");
            UsersList = new ArrayList<User>();
        }

        if (this.UsersList.isEmpty()) {
            File walkway = new File("data/walkway.jpeg");
            File childSkiing = new File("data/childSkiing.jpeg");
            File greenPasture = new File("data/greenPasture.jpeg");
            File manWorking = new File("data/manWorking.jpeg");
            File volleyBall = new File("data/volleyBall.jpeg");
            File womanWorking = new File("data/womanWorking.jpeg");
            this.UsersList.add(new User("stock"));
            User stockUser = UsersList.get(0);
            stockUser.addAlbum(new Album("Stock"));
            Album stockAlbum = stockUser.getAlbum("Stock");

            stockAlbum.addPhoto(new Photo(walkway));
            stockAlbum.addPhoto(new Photo(childSkiing));
            stockAlbum.addPhoto(new Photo(greenPasture));
            stockAlbum.addPhoto(new Photo(manWorking));
            stockAlbum.addPhoto(new Photo(volleyBall));
            stockAlbum.addPhoto(new Photo(womanWorking));

            Photo walkwayP = stockAlbum.photos.get(0);
            Photo childSkiingP = stockAlbum.photos.get(1);
            Photo greenPastureP = stockAlbum.photos.get(2);
            Photo manWorkingP = stockAlbum.photos.get(3);
            Photo volleyBallP = stockAlbum.photos.get(4);
            Photo womanWorkingP = stockAlbum.photos.get(5);

            walkwayP.setCaption("A beautiful natural walkway!");
            childSkiingP.setCaption("LESSS GOOOO!");
            greenPastureP.setCaption("Beautiful landscapes and almost looks like Windows XP");
            manWorkingP.setCaption("Lets get sturdy!");
            volleyBallP.setCaption("Wow! Look at this spike!");
            womanWorkingP.setCaption("I'm very focused at work!");

            walkwayP.addTag(new Tag("weather", "sunny", false));
            walkwayP.addTag(new Tag("person", "no one", true));

            childSkiingP.addTag(new Tag("weather","snowy", false));
            childSkiingP.addTag(new Tag("person","child", true));

            greenPastureP.addTag(new Tag("weather","sunny", false));
            greenPastureP.addTag(new Tag("person","no one", true));

            manWorkingP.addTag(new Tag("weather","N/A", false));
            manWorkingP.addTag(new Tag("person","man", true));

            volleyBallP.addTag(new Tag("weather","N/A", false));
            volleyBallP.addTag(new Tag("person","athlete", true));

            womanWorkingP.addTag(new Tag("weather","N/A", false));
            womanWorkingP.addTag(new Tag("person","woman", true));


            Serialize.writeApp(UsersList);
        }

        System.out.println(UsersList.toString());

        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void login() throws Exception{

        if((login.getText().trim()).equals("admin")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/admin.fxml"));
            try {

                AnchorPane root = (AnchorPane) loader.load();
                AdminController adminview = loader.getController();
                adminview.start(mainStage);
                Scene scene = new Scene(root);
                mainStage.setScene(scene);
                mainStage.show();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            String user_logging_in = login.getText();
            for (int i = 0; i<UsersList.size(); i++) {
                System.out.println(UsersList.get(i).getUsername());
                if (user_logging_in.trim().equals(UsersList.get(i).getUsername())) {
                    System.out.println("there is a match! The user is: " + user_logging_in);
                    Stage appStage=this.mainStage;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/album.fxml"));
                    AnchorPane root = (AnchorPane)loader.load();
                    AlbumController controller = loader.getController();
                    controller.start(appStage,i);
                    appStage.setScene(new Scene(root));
                    appStage.show();
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            String content = "Enter Valid Username -- Keep in mind usernames ARE case sensitive (leading and trailing spaces are irrelevant)";
            alert.setContentText(content);
            alert.showAndWait();
            login.clear();
        }
    }
}
