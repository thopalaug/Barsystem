package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



// This class handles all the calls to and from the Database.
public class DataSource {

    // Final static Database query String
    public static final String DB_NAME = "ProductDataBase.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\ThomasPalme\\Desktop\\JavaSpringMasterClass\\Barsystem\\" + DB_NAME;
    public static final String QUERY = "SELECT * FROM Products";
    public static final String INSERT_PRODUCT = "INSERT INTO Products (name, price) VALUES (?,?)";

    private static final DataSource instance = new DataSource();

    private DataSource(){

    }

    public static DataSource getInstance(){
        return instance;
    }




    // This methods calls the Database and returns the products.
    public List<Product> getProductsFromDB(){
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(QUERY) ){

            List<Product> productList = new ArrayList<>();
            while(resultSet.next()){
                Product product = new Product(resultSet.getString(1),resultSet.getInt(2));
                productList.add(product);
            }
            return productList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void insertProductIntoDB(String productName, int productPrice){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING); PreparedStatement preparedStatement =connection.prepareStatement(INSERT_PRODUCT)){

            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, productPrice);

            preparedStatement.executeUpdate();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
