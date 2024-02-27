package controller;

import model.Album;
import model.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Serialize;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class IndividualAlbumController {

    @FXML ImageView image;
    @FXML GridPane album_grid;
    @FXML Label album_name_text;
    @FXML Label number_of_pics;
    @FXML Label dates_of_pics;
    
    public Stage mainStage;
    public int albumIndex;
    public int userIndex;
    public ArrayList<User> UsersList;
    private User user;
    private Album album;

    public void start(Stage mainStage, int albumIndex, int userIndex) {
        this.albumIndex = albumIndex;
        this.userIndex = userIndex;
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        System.out.println(user.getUsername());
        System.out.println(user.getAlbums().size());
        this.album = user.getAlbums().get(albumIndex);
        this.album_grid.setId(UsersList.get(userIndex).getAlbums().get(albumIndex).getName());
        if (album.getPhotos().size() != 0) {
            image.setImage(new Image(album.getPhotos().get(album.getPhotos().size()-1).getFile().toURI().toString()));
        }
        album_grid.setVisible(true);
        number_of_pics.setText("Number of photos: " + (album.numPhotos() == 0 ? "0" : String.valueOf(album.numPhotos())));
        album_name_text.setText((album.getName()));
        album_name_text.setWrapText(true);
        dates_of_pics.setWrapText(true);
        if (album.getRangeOfPhotos() == null) {
            dates_of_pics.setText("");
        } else if (album.getRangeOfPhotos().size() == 1) {
            dates_of_pics.setText(album.getRangeOfPhotos().get(0).toString());
        } else {
            dates_of_pics.setText(album.getRangeOfPhotos().get(0).toString() + " -- " + album.getRangeOfPhotos().get(1).toString());
        }
        album_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println(album.getName() + " was clicked twice!");
                    try {
                        goToPhotosScene(album);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void goToPhotosScene(Album album) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(mainStage,this.albumIndex,this.userIndex);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }
}
