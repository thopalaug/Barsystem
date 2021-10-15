package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GetBarsFromDB extends Task {

    @Override
    public ObservableList<String> call(){
        return FXCollections.observableArrayList(
                DataSource.getInstance().getBarsFromDB()
        );
    }
}
