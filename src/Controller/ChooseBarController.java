package Controller;

import Model.GetBarsFromDB;
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
import java.util.ResourceBundle;


public class ChooseBarController implements Initializable, ControllerClass {

    private static User user;
    private static String selectedBar;


    SceneChanger sceneChanger = new SceneChanger();
    ObservableList<String> listOfBars = FXCollections.observableArrayList();

    @FXML
    Label idLabel;

    @FXML
    ChoiceBox<String> barChoiceBox;

    public void populateChoiceBox(){
        GetBarsFromDB getBarsFromDB = new GetBarsFromDB();
        listOfBars = getBarsFromDB.call();
    }

    @FXML
    public void nextButtonPressed(ActionEvent event) throws IOException {
        sceneChanger.changeScenes(event, sceneChanger.barWindow, user);
    }

    @FXML
    public void backButtonPressed(ActionEvent event) throws IOException {
        sceneChanger.changeScenes(event, sceneChanger.logInWindow);
    }


    //Todo finne ut hvorfor selectoren ikke virker som den skal.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateChoiceBox();
        barChoiceBox.setItems(listOfBars);
        barChoiceBox.getSelectionModel().selectFirst();

        barChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValueValue, number, t1) ->
                selectedBar = barChoiceBox.getValue()
        );
        barChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValueValue, number, t1) ->
                System.out.println(barChoiceBox.getSelectionModel().getSelectedIndex())
        );
        System.out.println(selectedBar);

    }

    @Override
    public void preloadUserData(User user) {
        ChooseBarController.user = user;
        idLabel.setText("ID: " + user.getId());
    }

    public static String getSelectedBar() {
        return selectedBar;
    }
}
