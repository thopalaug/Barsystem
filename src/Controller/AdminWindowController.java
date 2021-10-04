package Controller;

import Model.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable, ControllerClass {

    // FXML Fields
    @FXML
    private ListView<Product> productsListView = new ListView<>();
    @FXML
    Label userID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getProducts();
    }

    public void getProducts(){
        GetProductsFromDB getProductsFromDB = new GetProductsFromDB();
        productsListView.itemsProperty().bind(getProductsFromDB.valueProperty());
        new Thread(getProductsFromDB).start();
    }

    @Override
    public void preloadUserData(User user) {
        userID.setText("ID: " + user.getId());
    }


    @FXML
    public void newButtonPressed(javafx.event.ActionEvent event) throws IOException{
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, sceneChanger.ProductPanelWindow);
    }

    @FXML
    public void editButtonPressed(javafx.event.ActionEvent event) throws IOException{
        Product product = productsListView.getSelectionModel().getSelectedItem();
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, sceneChanger.ProductPanelWindow, product);
    }

    @FXML
    public void deleteButtonPressed(){
        Product product = productsListView.getSelectionModel().getSelectedItem();
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                return DataSource.getInstance().removeProductFromDb(product.getProductId());
            }
        };
        new Thread(task).start();
        refresh();
    }


    @FXML
    public void logOutButtonPressed(javafx.event.ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, sceneChanger.logInWindow);
    }

    @FXML
    public void refresh(){
        productsListView.refresh();
    }
}
