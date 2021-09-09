package Controller;

import Model.GetProductsFromDB;
import Model.Product;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BarWindowController implements Initializable, ControllerClass {

    // Private fields
    private ArrayList<Product> productsToSell = new ArrayList<>();
    private HashMap<Product, Integer> telleListeVarer = new HashMap<>();
    private HashMap<Product, Integer> telleListeBrekk = new HashMap<>();

    private int sum;
    private int sumKontant;
    private int sumBank;

    // FXML Fields
    @FXML
    private ListView<Product> productsListView = new ListView<>();
    @FXML
    private ListView<Product> productsToSellListView = new ListView<>();
    @FXML
    private TextField sumTextField;
    @FXML
    private ToggleButton deleteProductButton;
    @FXML
    private ToggleButton brekkButton;
    @FXML
    Label timeAndDate;
    @FXML
    Label nameOfBar;
    @FXML
    Label userID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sumKontant = 0;
        sumBank = 0;
        getProducts();
        sumTextField.setEditable(false);
        productsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nameOfBar.setText(ChooseBarController.selectedBar);
    }

    @Override
    public void preloadUserData(User user){
        userID.setText("ID: " + user.getId());
    }

    public void updateProductsToSell(){
        ObservableList<Product> observableList = FXCollections.observableArrayList(productsToSell);
        productsToSellListView.setItems(observableList);
    }

    //This method handles when a product is clicked on in the meny.
    @FXML
    public void handleProductsToSellMouseClicked(){
        Product product = productsListView.getItems().get(productsListView.getSelectionModel().getSelectedIndex());
        productsToSell.add(product);
        sum += product.getPrice();
        sumTextField.setText(Integer.toString(sum));
        updateProductsToSell();
    }

    @FXML
    public void handleDeleteProduct(){
        if(deleteProductButton.isSelected()) {
            Product product = productsToSellListView.getItems().get(productsToSellListView.getSelectionModel().getSelectedIndex());
            productsToSell.remove(product);
            sum -= product.getPrice();
            sumTextField.setText(Integer.toString(sum));
            updateProductsToSell();
        }
    }

    // This method uses the GetProductsFROmDB class to call the Database and get the products to bind them to the ListView.
    public void getProducts(){
        GetProductsFromDB getProductsFromDB = new GetProductsFromDB();
        productsListView.itemsProperty().bind(getProductsFromDB.valueProperty());
        new Thread(getProductsFromDB).start();
    }

    // This method handles when products are to be added to the list of products sold so far.
    public void addToTelleListe(ArrayList<Product> listOfProductsToAdd){
        for (Product product : listOfProductsToAdd){
            if(telleListeVarer.containsKey(product)){
                telleListeVarer.replace(product, telleListeVarer.get(product)+1);
            }else{
                telleListeVarer.put(product,1);
            }
        }
    }


    /*
    This method handles the payment. It calls the addToTelleListe method, and updates the total
     sum of everything sold so far, and cleans the register listView.
     */
    public void payment(){
        int check;
        if(brekkButton.isSelected()){
            check = JOptionPane.showConfirmDialog(null, "Ønsker du å brekke disse varene?");
        }else{
            check = JOptionPane.showConfirmDialog(null, "Ønsker du å gjennomføre transaksjonen?");
        }
        if(check == 0){
            addToTelleListe(productsToSell);
            sumTextField.clear();
            productsToSell.clear();
            updateProductsToSell();
            sum = 0;
        }
    }

    //This method opens a dialog window that shows which products have been sold so far.
    @FXML
    public void checkTelleliste(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger totalSale = new AtomicInteger();
        telleListeVarer.forEach((key, value) -> totalSale.addAndGet((value * key.getPrice())));
        telleListeVarer.forEach((key, value) -> sb.append(key).append(": ").append(value).append(" Total sum: ").append(value * key.getPrice()).append(",-\n"));
        sb.append("\nTotal sum alle varer: ").append(totalSale).append(",-");
        JOptionPane.showMessageDialog(null, sb);
    }


    // WIP
    @FXML
    public void testSettleButton(){
        String message = "Sum bank: " + sumBank + ",-\n" +
                "Sum kontant: " + sumKontant + ",-\n\n" +
                "Total salg: " + (sumKontant + sumBank) + ",-";
        JOptionPane.showMessageDialog(null, message);
    }


    // Methods to handle the payment buttons
    @FXML
    public void handleBank1Pressed(){
        sumBank += sum;
        payment();
    }

    @FXML
    public void handleBank2Pressed(){
        sumBank += sum;
        payment();
    }

    @FXML
    public void handleCashPressed(){
        sumKontant += sum;
        payment();
    }

}
