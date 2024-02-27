package controller;

import model.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/*
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class IndividualSearchController {
    @FXML GridPane search_grid;
    @FXML ImageView ind_search_image;
    @FXML Label ind_search_label;

    Stage mainStage;

    public void start(Stage mainStage, Photo photo){
        this.mainStage = mainStage;
        ind_search_label.setText((photo.getCaption() == null ? "Caption: " : "Caption: " + photo.getCaption()));
        ind_search_image.setImage(new Image(photo.getFile().toURI().toString()));
        search_grid.setVisible(true);
    }
}
