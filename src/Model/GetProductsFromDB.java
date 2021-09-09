package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

//This class is a Task which returns the products from the DataSource as an ObservableArrayList
public class GetProductsFromDB extends Task {
    @Override
    public ObservableList<Product> call(){
        return FXCollections.observableArrayList(
                DataSource.getInstance().getProductsFromDB()
        );
    }

}
