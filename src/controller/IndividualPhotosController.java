package controller;

import model.Photo;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class IndividualPhotosController {

    @FXML ImageView ind_photos_image;
    @FXML GridPane photo_grid;
    @FXML Label ind_caption_label;

    Stage mainStage;


    public void start(Stage mainStage, Photo photo, ImageView image, Label caption, Label date, Label tags) {
        this.mainStage = mainStage;
        ind_caption_label.setText((photo.getCaption() == null ? "" : photo.getCaption()));
        ind_caption_label.setWrapText(true);
        ind_photos_image.setImage(new Image(photo.getFile().toURI().toString()));
        photo_grid.setVisible(true);

        photo_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) {
                    System.out.println(photo.getCaption() + " was clicked!");
                    displayPhoto(photo, image, caption, date, tags);
                }
            }
        });
    }

    public void displayPhoto(Photo photo, ImageView image, Label caption, Label date, Label tags) {
        caption.setWrapText(true);
        tags.setWrapText(true);
        image.setId(photo.getFile().getAbsolutePath());
        image.setImage(new Image(photo.getFile().toURI().toString()));
        caption.setText((photo.getCaption() == null ? "Caption: " : photo.getCaption()));
        date.setText((photo.getDate() == null ? "Date: " : photo.getDate()));
        tags.setText(photo.getTags() == null ? "Tags: " : photo.getDisplayTags());
    }

}

