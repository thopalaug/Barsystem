package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



// This class handles all the calls to and from the Database.
public class DataSource {

    // Final static Database query String
    private static final String DB_NAME = "ProductDataBase.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\ThomasPalme\\Desktop\\JavaSpringMasterClass\\Barsystem\\" + DB_NAME;
    private static final String SELECT_FROM_PRODUCTS = "SELECT * FROM Products";
    private static final String INSERT_PRODUCT = "INSERT INTO Products (name, price) VALUES (?,?)";
    private static final String UPDATE_PRODUCT = "UPDATE Products SET name = ?, price = ? WHERE product_id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM Products WHERE product_id = ?";
    public static final String INSERT_SALE = "INSERT INTO Sales (bestilling, sum, payment_method, user) VALUES (?,?,?,?)";
    public static final String SELECT_FROM_BARER = "SELECT * FROM Barer";


    private static final DataSource instance = new DataSource();

    private DataSource(){
    }

    public static DataSource getInstance(){
        return instance;
    }


    // This methods calls the Database and returns the products.
    public List<Product> getProductsFromDB(){
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_FROM_PRODUCTS) ){

            List<Product> productList = new ArrayList<>();
            while(resultSet.next()){
                Product product = new Product(resultSet.getString(2),resultSet.getInt(3));
                product.setProductId(resultSet.getInt(1));
                productList.add(product);
            }
            return productList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    // This methods calls the Database and returns the different bars.
    public List<String> getBarsFromDB(){
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_FROM_BARER);
            ResultSet resultSet = preparedStatement.executeQuery()){

            List<String> listOfBars = new ArrayList<>();
            while(resultSet.next()){
                listOfBars.add(resultSet.getString("name"));
            }
            return listOfBars;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    // This method handles the call to the database to insert a product
    public int insertProductIntoDB(String productName, int productPrice){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)){

            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, productPrice);

            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 1;
    }

    // This method handles the call to the database to insert a sale
    public int insertSaleIntoDB(String order, int sum, String paymentMethod, String user){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SALE)){

            preparedStatement.setString(1, order);
            preparedStatement.setInt(2, sum);
            preparedStatement.setString(3, paymentMethod);
            preparedStatement.setString(4, user);

            preparedStatement.executeUpdate();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 1;
    }

    // This method handles the call to the database to remove a product
    public int removeProductFromDb(int productId){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)){
            preparedStatement.setInt(1,productId);

            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return 1;
    }

    // This method handles the call to the database to update the name and price of a product
    public int updateProduct(int productId, String productName, int productPrice){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)){
            preparedStatement.setString(1,productName);
            preparedStatement.setInt(2,productPrice);
            preparedStatement.setInt(3,productId);

            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return 1;
    }

}
