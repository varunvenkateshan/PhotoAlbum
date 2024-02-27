package controller;

import model.Album;
import model.Tag;
import model.Photo;
import model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import model.Serialize;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class SearchController {

    @FXML
    GridPane grid;

    public int row = 0;
    public int col = 0;

    public Stage mainStage;
    private User user;
    public int userIndex;
    public ArrayList<User> UsersList;
    public int z;
    public ArrayList<Album> allUserAlbums;
    public Album newAlbum;
    public LocalDate from;
    public LocalDate to;

    public void start(Stage mainstage, int userIndex) {
        this.mainStage = mainstage;
        this.userIndex = userIndex;

        newAlbum = new Album("");
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        allUserAlbums = user.getAlbums();
        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void backToAlbums() throws Exception {
        Stage appStage = this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/album.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        AlbumController controller = loader.getController();
        controller.start(appStage, this.userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void setImage() throws Exception {
        if (newAlbum.getPhotos().size() != 0) {
            TextInputDialog addAlbum = new TextInputDialog();
            addAlbum.initOwner(this.mainStage);
            addAlbum.setTitle("Create album from search");
            addAlbum.setHeaderText("Name of new album");
            Optional<String> result = addAlbum.showAndWait();
            if (result.isPresent()) {
                System.out.println("im here");
                newAlbum.setName(result.get().trim());
                try {
                    user.addAlbum(newAlbum);
                    newAlbum = new Album("");
                    clearPhotos();
                } catch (IllegalArgumentException error) {
                    System.out.println("the error is happening here!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    String content = error.getMessage();
                    alert.setContentText(content);
                    alert.showAndWait();
                }

            }
            Serialize.writeApp(UsersList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            String content = "Please search for photos before creating a new album!";
            alert.setContentText(content);
            alert.showAndWait();
        }

    }

    public void setter(int k) {
        z = k;
    }

    public void searchBy2Tag() {
        Dialog<Pair<String, String>> first = new Dialog<>();
        Dialog<Pair<String, String>> second = new Dialog<>();

        Dialog<Pair<Pair, Pair>> dialog = new Dialog<>();

        dialog.setTitle("Enter tag to search by");

        ButtonType done = new ButtonType("AND", ButtonBar.ButtonData.OK_DONE);
        ButtonType done2 = new ButtonType("OR", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, done2, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Key");
        TextField to = new TextField();
        to.setPromptText("Value");
        TextField from1 = new TextField();
        from1.setPromptText("Key2");
        TextField to1 = new TextField();
        to1.setPromptText("Value2");

        gridPane.add(new Label("Key:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Value:"), 2, 0);
        gridPane.add(to, 3, 0);
        gridPane.add(new Label("Key2:"), 4, 0);
        gridPane.add(from1, 5, 0);
        gridPane.add(new Label("Value2:"), 6, 0);
        gridPane.add(to1, 7, 0);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(() -> from.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                setter(1);
                return new Pair<Pair, Pair>(new Pair<>(from.getText(), to.getText()),
                        new Pair<>(from1.getText().toLowerCase(), to1.getText().toLowerCase()));
            }
            if (dialogButton == done2) {
                setter(2);
                return new Pair<Pair, Pair>(new Pair<>(from.getText(), to.getText()),
                        new Pair<>(from1.getText().toLowerCase(), to1.getText().toLowerCase()));
            }
            return null;
        });

        Optional<Pair<Pair, Pair>> result = dialog.showAndWait();

        result.ifPresent(pair -> {

            String temp1 = (pair.getKey().getKey()) + "";
            String key1 = temp1.trim();

            String temp7 = pair.getKey().getValue() + "";
            String value1 = temp7.trim();

            String temp3 = pair.getValue().getKey() + "";
            String key2 = temp3.trim();

            String temp4 = pair.getValue().getValue() + "";
            String value2 = temp4.trim();

            if (key1.equals("") || value1.equals("") || key2.equals("") || value2.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "1 or more tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            System.out.println(key1 + " " + value1 + " " + key2 + " " + value2);
            ArrayList<Album> albums = user.getAlbums();
            for (int i = 0; i < albums.size(); i++) {
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for (int j = 0; j < photos.size(); j++) {
                    Photo p = photos.get(j);
                    ArrayList<Tag> tags = p.getTags();
                    boolean temp = false;
                    boolean temp2 = false;
                    for (int k = 0; k < tags.size(); k++) {
                        System.out.println("z = " + z);
                        if (z == 1) {
                            System.out.println("I am in AND");
                            if (key1.equalsIgnoreCase(tags.get(k).getKey())
                                    && value1.equalsIgnoreCase(tags.get(k).getValue())) {
                                temp = true;
                            }
                            if (key2.equalsIgnoreCase(tags.get(k).getKey())
                                    && value2.equalsIgnoreCase(tags.get(k).getValue())) {
                                temp2 = true;
                            }
                            if (temp && temp2) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/view/individualsearch.fxml"));
                                try {
                                    if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                        newAlbum.getPhotos().add(p);
                                    } else {
                                        continue;
                                    }
                                    AnchorPane img = (AnchorPane) loader.load();
                                    IndividualSearchController searchView = loader.getController();
                                    searchView.start(mainStage, p);
                                    grid.add(searchView.search_grid, col, row);
                                    if (col == 2) {
                                        row++;
                                        col = 0;
                                    } else {
                                        col++;
                                    }

                                } catch (Exception q) {
                                    q.printStackTrace();
                                    break;
                                }
                                continue;
                            }
                        }
                        if (z == 2) {
                            System.out.println("I am in OR");
                            if ((key1.equalsIgnoreCase(tags.get(k).getKey())
                                    && value1.equalsIgnoreCase(tags.get(k).getValue()))
                                    || (key2.equalsIgnoreCase(tags.get(k).getKey())
                                            && value2.equalsIgnoreCase(tags.get(k).getValue()))) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/view/individualsearch.fxml"));
                                try {
                                    if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                        newAlbum.getPhotos().add(p);
                                    } else {
                                        continue;
                                    }
                                    AnchorPane img = (AnchorPane) loader.load();
                                    IndividualSearchController searchView = loader.getController();
                                    searchView.start(mainStage, p);
                                    grid.add(searchView.search_grid, col, row);
                                    if (col == 2) {
                                        row++;
                                        col = 0;
                                    } else {
                                        col++;
                                    }
                                } catch (Exception q) {
                                    q.printStackTrace();
                                    continue;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
            if (newAlbum.getPhotos().size() == 0) {
                System.out.println("Could not find tag");
            }
        });
    }

    public void searchBy1Tag() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Enter tag to search by");
        ButtonType done = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Key");
        TextField to = new TextField();
        to.setPromptText("Value");

        gridPane.add(new Label("Key:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Value:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> from.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                return new Pair<>(from.getText().toLowerCase(), to.getText().toLowerCase());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String temp3 = pair.getKey() + "";
            String key = temp3.trim();

            String temp4 = pair.getValue() + "";
            String value = temp4.trim();
            if (key.equals("") || value.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "1 or more tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            ArrayList<Album> albums = user.getAlbums();
            for (int i = 0; i < albums.size(); i++) {
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for (int j = 0; j < photos.size(); j++) {
                    Photo p = photos.get(j);
                    ArrayList<Tag> tags = p.getTags();
                    for (int k = 0; k < tags.size(); k++) {

                        if (key.equalsIgnoreCase(tags.get(k).getKey())
                                && value.equalsIgnoreCase(tags.get(k).getValue())) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/individualsearch.fxml"));
                            try {
                                if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                    newAlbum.getPhotos().add(p);
                                } else {
                                    continue;
                                }
                                AnchorPane img = (AnchorPane) loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }

                            } catch (Exception q) {
                                q.printStackTrace();
                            }
                        }
                    }
                }
            }

        });
    }

    public void clearPhotos() throws Exception {
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
    }

    public void clearSearch() {
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
        Album temp = new Album("");
        newAlbum = temp;
    }

    public void setterFrom(LocalDate date) {
        from = date;
    }

    public void setterTo(LocalDate date) {
        to = date;
    }

    public void searchByDate() throws Exception {
        TextInputDialog dateSearch = new TextInputDialog();
        dateSearch.initOwner(this.mainStage);
        TilePane r = new TilePane();
        dateSearch.getDialogPane().setContent(r);

        Label fromLabel = new Label("From:");
        Label toLabel = new Label("To:");
        DatePicker fromDate = new DatePicker();
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // get the date picker value
                setterFrom(fromDate.getValue());
            }
        };

        fromDate.setShowWeekNumbers(true);

        fromDate.setOnAction(event);
        DatePicker toDate = new DatePicker();

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                setterTo(toDate.getValue());
            }
        };

        toDate.setShowWeekNumbers(true);

        toDate.setOnAction(event2);
        r.getChildren().add(fromLabel);
        r.getChildren().add(fromDate);
        r.getChildren().add(toLabel);
        r.getChildren().add(toDate);
        Optional<String> result = dateSearch.showAndWait();
        if (result.isPresent()) {
            System.out.println(from + " " + to);
            if (from == null || to == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "2 Tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            if (from.isAfter(to)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "Please Fix the Order of the Start and End Date!";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            ArrayList<Album> albums = user.getAlbums();
            for (int i = 0; i < albums.size(); i++) {
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for (int j = 0; j < photos.size(); j++) {
                    Photo p = photos.get(j);
                    Date tempDate = p.getActualDate();
                    LocalDate date = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (from.isBefore(to)) {
                        if (from.isBefore(date) && to.isAfter(date) || from.isEqual(date) || to.isEqual(date)) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/individualsearch.fxml"));
                            try {
                                if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                    newAlbum.getPhotos().add(p);
                                } else {
                                    continue;
                                }
                                AnchorPane img = (AnchorPane) loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }

                            } catch (Exception q) {
                                q.printStackTrace();
                            }
                        }
                    }
                    if (from.isEqual(to)) {
                        if (from.isEqual(date) && to.isEqual(date)) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/individualsearch.fxml"));
                            try {
                                if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                    newAlbum.getPhotos().add(p);
                                } else {
                                    continue;
                                }
                                AnchorPane img = (AnchorPane) loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }
                            } catch (Exception q) {
                                q.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
