package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import model.User;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Serialize;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class AdminController {

    @FXML
    TextField add;
    @FXML
    TextField delete;
    @FXML
    Button logout_btn;
    @FXML
    ListView<String> user_listview;

    public Stage mainstage;
    public ArrayList<User> UsersList;
    public ObservableList<String> obsList = FXCollections.observableArrayList();

    // Start Admin Controller
    public void start(Stage mainstage) {
        this.mainstage = mainstage;
        try {
            this.UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("eof exception");
            if (e instanceof EOFException)
                this.UsersList = new ArrayList<User>();
        }

        if (this.UsersList.isEmpty()) {
            this.UsersList.add(new User("Stock"));
        }

        updateListView();
        mainstage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    // Log out Admin
    public void logout() throws Exception {

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirmation");
        String content1 = "Are you sure you want to logout?";
        alert1.setContentText(content1);
        Optional<ButtonType> result = alert1.showAndWait();
        if ((result).get() == ButtonType.OK) {
            Serialize.writeApp(UsersList);
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

    // Update List View
    public void updateListView() {
        obsList.clear();
        for (int i = 0; i < UsersList.size(); i++) {
            obsList.add(UsersList.get(i).username);
        }
        user_listview.setItems(obsList);
    }

    // Add User
    public void add() {
        if (add.getText().toLowerCase().equals("admin")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            String content = "You cannot create an Admin user";
            alert.setContentText(content);
            alert.showAndWait();
            add.clear();
            return;
        }
        if (add.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            String content = "Please enter an Input";
            alert.setContentText(content);
            alert.showAndWait();
            add.clear();
            return;
        }
        for (int i = 0; i < UsersList.size(); i++) {
            if (((UsersList.get(i).username.toLowerCase()).equals(add.getText().trim().toLowerCase()))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "No Two Users Can NOT Have The Same Name Even If They Have Different Cases";
                alert.setContentText(content);
                alert.showAndWait();
                add.clear();
                return;
            }
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirmation");
        String content1 = "Are you sure you want to add?";
        alert1.setContentText(content1);
        Optional<ButtonType> result = alert1.showAndWait();
        if ((result).get() == ButtonType.OK) {
            UsersList.add(new User(add.getText().trim()));
            add.clear();
            updateListView();
        } else {
            add.clear();
            return;
        }
    }

    // Delete User
    public void delete() {
        if (delete.getText().toLowerCase().equals("admin")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "You can not delete an Admin user";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }
        if (delete.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "Please Make An Input";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }
        if (delete.getText().toLowerCase().equals("stock")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            String content = "You Can Not Delete This User";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }
        for (int i = 0; i < UsersList.size(); i++) {
            if ((UsersList.get(i).username.toLowerCase()).equals(delete.getText().trim().toLowerCase())) {
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Confirmation");
                String content1 = "Are you sure you want to delete?";
                alert1.setContentText(content1);
                Optional<ButtonType> result = alert1.showAndWait();
                if ((result).get() == ButtonType.OK) {
                    UsersList.remove(i);
                    i--;
                    delete.clear();
                    updateListView();
                    return;
                } else {
                    delete.clear();
                    return;
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Error");
        String content = delete.getText() + " has never been added to the list of users, this is NOT case sensitive";
        alert.setContentText(content);
        delete.clear();
        alert.showAndWait();
    }
}
