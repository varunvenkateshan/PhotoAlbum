package controller;

import model.Album;
import model.User;
import model.Serialize;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class AlbumController {

    public ArrayList<User> UsersList;
    public ArrayList<Album> albums;
    private User user;
    public int userIndex;
    public Stage mainStage;
    public int row = 0;
    public int col = 0;

    @FXML
    ScrollPane scroll;
    @FXML
    GridPane grid;
    @FXML
    Button logout_btn;
    @FXML
    Label username;

    // Start Album Controller
    public void start(Stage mainStage, int userIndex) {
        this.mainStage = mainStage;
        this.userIndex = userIndex;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        System.out.println(UsersList.toString());
        user = UsersList.get(userIndex);
        albums = user.getAlbums();
        username.setText("Welcome to the albums in your photo library, " + user.getUsername()
                + "! Double click on the album you wish to enter.");
        System.out.println(user.getUsername());
        System.out.println(user.numberOfAlbums());
        System.out.println(user.getAlbums());
        if (user.numberOfAlbums() > 0) {
            for (int i = 0; i < albums.size(); i++) {
                try {
                    populateAlbums(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Populate Album
    public void populateAlbums(int displayAlbumIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/individualalbum.fxml"));
        try {
            AnchorPane image = (AnchorPane) loader.load();
            IndividualAlbumController albumView = loader.getController();
            albumView.start(mainStage, displayAlbumIndex, userIndex);
            grid.add(albumView.album_grid, col, row);
            if (col == 2) {
                row++;
                col = 0;
            } else {
                col++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Log out
    public void logout() throws Exception {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirmation");
        String content1 = "Are you sure you want to logout?";
        alert1.setContentText(content1);
        Optional<ButtonType> result = alert1.showAndWait();
        if ((result).get() == ButtonType.OK) {
            Serialize.writeApp(UsersList);
            System.out.println(user.getAlbums());
            Stage appStage = (Stage) logout_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            LoginController controller = loader.getController();
            controller.start(appStage);
            appStage.setScene(new Scene(root));
            appStage.show();
        }
    }

    // Add Album
    public void addAlbum() throws Exception {
        TextInputDialog addAlbum = new TextInputDialog();
        addAlbum.initOwner(this.mainStage);
        addAlbum.setTitle("New Album");
        addAlbum.setHeaderText("Name of new album");
        Optional<String> result = addAlbum.showAndWait();
        if (result.isPresent()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/individualalbum.fxml"));
            try {
                AnchorPane image = (AnchorPane) loader.load();
                IndividualAlbumController albumView = loader.getController();
                Album newAlbum = new Album(result.get().trim());
                try {
                    user.addAlbum(newAlbum);
                    Serialize.writeApp(UsersList);
                    System.out.println(albums.size());
                    System.out.println(albums.toString());
                    albumView.start(mainStage, albums.size() - 1, userIndex);
                    grid.add(albumView.album_grid, col, row);
                    if (col == 2) {
                        row++;
                        col = 0;
                    } else {
                        col++;
                    }
                } catch (IllegalArgumentException error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    String content = error.getMessage();
                    alert.setContentText(content);
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Serialize.writeApp(UsersList);
    }

    // Edit Album
    public void editAlbum() throws Exception {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Album Name");
        ButtonType done = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        TextField from = new TextField();
        from.setPromptText("Old Name");
        TextField to = new TextField();
        to.setPromptText("New Name");
        gridPane.add(new Label("Old Name:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("New Name:"), 2, 0);
        gridPane.add(to, 3, 0);
        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(() -> from.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            Album a = user.getAlbumWithName(pair.getKey());
            try {
                user.editAlbum(pair.getKey() + "", pair.getValue() + "");
                System.out.println(a.getName());
                System.out.println("the old album was: " + pair.getKey() + "\n the new albums is: " + pair.getValue());
                try {
                    resetAlbums();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }

        });
    }

    //Delete Album
    public void deleteAlbum() throws Exception {
        TextInputDialog deletingAlbum = new TextInputDialog();
        deletingAlbum.initOwner(this.mainStage);
        deletingAlbum.setTitle("Delete Album");
        deletingAlbum.setHeaderText("Please enter the name of the album you would like to delete.");
        Optional<String> result = deletingAlbum.showAndWait();
        if (result.isPresent()) {
            System.out.println("the album being deleted is: " + result.get().trim());
            try {
                if (user.getAlbum(result.get().trim().toLowerCase()) == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    String content = "Album does not exist!";
                    alert.setContentText(content);
                    alert.showAndWait();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("You are deleting the selected photo!");
                alert.setContentText("Are you sure you want to delete this album? All photos cannot be recovered!");
                Optional<ButtonType> result1 = alert.showAndWait();
                if (result1.get() == ButtonType.OK) {
                    user.deleteAlbum(result.get().trim());
                    Serialize.writeApp(UsersList);
                    resetAlbums();
                } else {
                    return;
                }
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }
        }
    }

    //Delete Album Info
    public void resetAlbums() throws Exception {
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
        this.start(this.mainStage, userIndex);
    }

    //Search Photo Tags
    public void searchPhotos() throws Exception {
        Stage appStage = this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/search.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        SearchController controller = loader.getController();
        controller.start(this.mainStage, userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();

    }

}