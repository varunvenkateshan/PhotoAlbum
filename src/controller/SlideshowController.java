package controller;
import model.Album;
import model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.Serialize;

import java.util.ArrayList;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class SlideshowController {
    
    private User user;
    private Album album;
    public Stage mainStage;
    public ArrayList<User> UsersList;
    public int userIndex;
    public int albumIndex;
    public int photo;
    @FXML ImageView Image;
    @FXML Label slideshow_caption;
    @FXML Label slideshow_date;
    @FXML Label slideshow_tags;

    public void start(Stage mainStage, int userIndex, int albumIndex){
        this.userIndex = userIndex;
        this.albumIndex = albumIndex;
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        this.album = user.getAlbums().get(albumIndex);
        slideshow_tags.setWrapText(true);
        slideshow_date.setWrapText(true);
        slideshow_caption.setWrapText(true);
        this.photo = 0;
        if (this.album.getPhotos().size() != 0) {
            Image.setImage(new Image(album.getPhotos().get(photo).getFile().toURI().toString()));
            slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
            slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
            slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
        }
    }

    public void backToPhotos() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(this.mainStage, this.albumIndex, this.userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void previousPhoto() {
        if (this.album.getPhotos().size() != 0) {
            if (this.photo != 0) {
                this.photo--;
                Image.setImage(new Image(this.album.getPhotos().get(this.photo).getFile().toURI().toString()));
                slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
                slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
                slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
            }
        }
    }

    public void nextPhoto() {
        if (this.album.getPhotos().size() != 0) {
            if (this.photo != this.album.getPhotos().size() - 1) {
                this.photo++;
                Image.setImage(new Image(this.album.getPhotos().get(this.photo).getFile().toURI().toString()));
                slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
                slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
                slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
            }
        }
    }

}

