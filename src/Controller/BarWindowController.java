package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.IOException;
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
    private boolean brekk;
    private boolean kontant = false;
    private User user;

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
        nameOfBar.setText(ChooseBarController.getSelectedBar());
    }

    @Override
    public void preloadUserData(User user){
        this.user = user;
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

    public void addToBrekkListe(ArrayList<Product> listOfProductsToBrekk){
        for(Product product : listOfProductsToBrekk){
            if(telleListeBrekk.containsKey(product)){
                telleListeBrekk.replace(product,telleListeBrekk.get(product)+1);
            }else{
                telleListeBrekk.put(product,1);
            }
        }
    }

    @FXML
    public void checkBrekkButton(){
        brekk = brekkButton.isSelected();
    }

    /*
    This method handles the payment. It calls the addToTelleListe method, and updates the total
     sum of everything sold so far, and cleans the register listView. The sales is added to a database.
     */
    public void payment(boolean kontant){
        if(brekk && JOptionPane.showConfirmDialog(null, "Ønsker du å brekke disse varene?") == 0){
            addToBrekkListe(productsToSell);
            sumTextField.clear();
            productsToSell.clear();
            updateProductsToSell();
            sum = 0;
        }else if(JOptionPane.showConfirmDialog(null, "Ønsker du å gjennomføre transaksjonen?") == 0){
            String paymentMethod = "bank";
            if(kontant)
                paymentMethod = "kontant";
            String finalPaymentMethod = paymentMethod;
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call(){
                    return DataSource.getInstance().insertSaleIntoDB(productsToSellToString(productsToSell), sum, finalPaymentMethod, user.getId());
                }
            };
            new Thread(task).start();

            addToTelleListe(productsToSell);
            sumTextField.clear();
            productsToSell.clear();
            updateProductsToSell();
            sum = 0;

        }
    }

    public String productsToSellToString(ArrayList<Product> productsToSell){
        StringBuilder sb = new StringBuilder();

        for(Product product : productsToSell){
            sb.append(product.toString());
            sb.append(", ");
        }
        return sb.toString();
    }

    //This method opens a dialog window that shows which products have been sold so far.
    @FXML
    public void checkTelleliste(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger totalSale = new AtomicInteger();
        telleListeVarer.forEach((key, value) -> totalSale.addAndGet((value * key.getPrice())));
        telleListeVarer.forEach((key, value) -> sb.append(key).append(": ").append(value).append(" Total sum: ").append(value * key.getPrice()).append(",-\n"));
        sb.append("\nTotal sum alle varer: ").append(totalSale).append(",-\n");
        sb.append("\nTotal varer brukket:\n");
        telleListeBrekk.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        JOptionPane.showMessageDialog(null, sb);
    }


    //This method gives the user a pop-up window with the sums for the current sales.
    @FXML
    public void testSettleButton(){
        String message = "Sum bank: " + sumBank + ",-\n" +
                "Sum kontant: " + sumKontant + ",-\n\n" +
                "Total salg: " + (sumKontant + sumBank) + ",-";
        JOptionPane.showMessageDialog(null, message);
    }

    // WIP
    @FXML
    public void settleAndLogOutButton(javafx.event.ActionEvent event) throws IOException {
        if(JOptionPane.showConfirmDialog(null, "Ønsker du å gjennomføre oppgjør og logge ut?") == 0){
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.changeScenes(event, sceneChanger.logInWindow);
        }
    }


    // Methods to handle the payment buttons
    @FXML
    public void handleBank1Pressed(){
        sumBank += sum;
        payment(kontant);
    }

    @FXML
    public void handleBank2Pressed(){
        sumBank += sum;
        payment(kontant);
    }

    @FXML
    public void handleCashPressed(){
        kontant = true;
        sumKontant += sum;
        payment(true);
        kontant = false;
    }

}
