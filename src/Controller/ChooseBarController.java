package Controller;

import Model.SceneChanger;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class ChooseBarController implements Initializable, ControllerClass {

    public static final String DB_NAME = "ProductDataBase.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\ThomasPalme\\Desktop\\JavaSpringMasterClass\\Barsystem\\" + DB_NAME;
    public static final String QUERY = "SELECT * FROM Barer";

    private static User user;
    public static String selectedBar;

    SceneChanger sceneChanger = new SceneChanger();
    ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    Label idLabel;


    @FXML
    ChoiceBox<String> barChoiceBox;

    public void populateChoiceBox(){

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
        PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
        ResultSet resultSet = preparedStatement.executeQuery()){

            while(resultSet.next()){
                list.add(resultSet.getString("name"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void nextButtonPressed(ActionEvent event) throws IOException {
        sceneChanger.changeScenes(event, sceneChanger.barWindow, user);
    }

    @FXML
    public void backButtonPressed(ActionEvent event) throws IOException {
        sceneChanger.changeScenes(event, sceneChanger.logInWindow);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateChoiceBox();
        barChoiceBox.setItems(list);
        barChoiceBox.getSelectionModel().select(0);

        barChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValueValue, number, t1) ->
                selectedBar = barChoiceBox.getSelectionModel().getSelectedItem()
        );
    }

    @Override
    public void preloadUserData(User user) {
        ChooseBarController.user = user;
        idLabel.setText("ID: " + user.getId());
    }
}
