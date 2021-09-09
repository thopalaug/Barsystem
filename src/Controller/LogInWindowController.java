package Controller;

import Model.SceneChanger;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogInWindowController implements Initializable {

    // Fields

    @FXML
    public TextField idTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorMessageLabel;
    @FXML
    public Label helpInfo;

    private static final String DB_NAME = "UserDataBase.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\ThomasPalme\\Desktop\\JavaSpringMasterClass\\Barsystem\\" + DB_NAME;
    private static final String QUERY = "SELECT * FROM Users WHERE id = ?";
    private static final String login = "Admin id: admin, pw admin\n User id: 5, pw: 5";

    /*
    This method is used to check if the user input for id and password exists in the database.
    If the input is valid it changes to the window where the user can select which bar being used.
    */
    public void loginButtonPressed(ActionEvent event) throws IOException {

        Connection conn;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        User user = null;

        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = conn.prepareStatement(QUERY);
            preparedStatement.setString(1, idTextField.getText());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                user = new User(resultSet.getString("id"),resultSet.getString("password"), resultSet.getInt("admin"));
            }
            SceneChanger sceneChanger = new SceneChanger();

            if(user.getPassword().equals(passwordField.getText()) && user.getAdmin() == 1){
                SceneChanger.setLoggedInUser(user);
                sceneChanger.changeScenes(event, sceneChanger.adminWindow, user);

            }else if(user.getPassword().equals(passwordField.getText())){
                SceneChanger.setLoggedInUser(user);
                sceneChanger.changeScenes(event, sceneChanger.chooseBarWindow, user);
            }else{
                errorMessageLabel.setText("Feil ID eller passord");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessageLabel.setText("");
        helpInfo.setText(login);
    }
}
