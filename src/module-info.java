module Barsystem {

    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires javafx.swing;

    opens View;
    opens Controller;
    opens Model;

}