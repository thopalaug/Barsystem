package Model;

import Controller.ControllerClass;
import Controller.ProductClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {

    /* This class is used to change scenes in the application. It contains two different constructors,
    one basic with only the arguments for the event and the name of the view, and noe which also takes in a
    user.
    */

    // Fields for the different views used.
    public final String barWindow = "/View/BarWindow.fxml";
    public final String logInWindow = "/View/LogInWindow.fxml";
    public final String adminWindow = "/View/AdminWindow.fxml";
    public final String chooseBarWindow = "/View/ChooseBarWindow.fxml";
    public final String ProductPanelWindow = "/View/ProductPanelView.fxml";

    private static User loggedInUser;

    public void changeScenes(ActionEvent event, String viewName) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeScenes(ActionEvent event, String viewName, User user) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ControllerClass controllerClass = loader.getController();
        controllerClass.preloadUserData(user);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeScenes(ActionEvent event, String viewName, Product product) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ProductClass productClass = loader.getController();
        productClass.preloadProduct(product);


        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    public static void setLoggedInUser(User loggedInUser){
        SceneChanger.loggedInUser = loggedInUser;
    }


}
