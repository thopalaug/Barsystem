package Controller;

import Model.DataSource;
import Model.Product;
import Model.SceneChanger;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductPanelController implements Initializable, ProductClass {

    @FXML
    TextField nameTextField;
    @FXML
    TextField priceTextField;

    Product product;

    @Override
    public void preloadProduct(Product product) {
        this.product = product;
        System.out.println(product.getName());
        nameTextField.setText(product.getName());
        priceTextField.setText(Integer.toString(product.getPrice()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void saveButtonPressed(javafx.event.ActionEvent event) throws IOException{

        String productName = nameTextField.getText();
        int productPrice = Integer.parseInt(priceTextField.getText());


        if(product != null){

            Task<Integer> taskUpdateProduct = new Task<>() {
                @Override
                protected Integer call() {
                    return DataSource.getInstance().updateProduct(product.getProductId(),productName,productPrice);
                }
            };
            new Thread(taskUpdateProduct).start();

        }else{
            Task<Integer> taskInsertProduct = new Task<>(){
                @Override
                protected Integer call() {
                    return DataSource.getInstance().insertProductIntoDB(productName,productPrice);
                }
            };
            new Thread(taskInsertProduct).start();
        }

        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, sceneChanger.adminWindow);
    }

    @FXML
    public void cancelButtonPressed(javafx.event.ActionEvent event) throws IOException {
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.changeScenes(event, sceneChanger.adminWindow);
    }


}
