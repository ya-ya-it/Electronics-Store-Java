package Views;

import Model.Camera;
import Model.Electronics;
import Model.Laptop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * This is the slowest way how the program could work, but it works...
 *
 * @author jwright
 */
public class TableOfProductsController implements Initializable {

    @FXML
    private TableView<Electronics> productTableView;
    @FXML
    private TableColumn<Electronics, Integer> productIDColumn;
    @FXML
    private TableColumn<Electronics, String> manufacturerColumn;
    @FXML
    private TableColumn<Electronics, String> productColumn;
    @FXML
    private TableColumn<Electronics, String> descriptionColumn;
    @FXML
    private TableColumn<Electronics, Double> priceColumn;
    @FXML
    private CheckBox cameraCheckBox;
    @FXML
    private CheckBox laptopCheckBox;
    @FXML
    private TextField searchTextBox;
    @FXML
    private Label rowsReturnedLabel;

    /**
     * Initializes the controller class.
     *
     * private String productName, description, manufacturer; private double
     * price;
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize elements on the view
        cameraCheckBox.setSelected(true);
        laptopCheckBox.setSelected(true);
        searchTextBox.setText("");

        //initialize table columns
        productIDColumn.setCellValueFactory(new PropertyValueFactory<Electronics, Integer>("productID"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("manufacturer"));
        productColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("productName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Electronics, Double>("price"));

        try {
            updateTableFromDB();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method will update the table without any filters
     *
     * @throws SQLException
     */
    public void updateTableFromDB() throws SQLException {
        ObservableList<Electronics> products = FXCollections.observableArrayList();
        products.addAll(populateLaptopsFromDB());
        products.addAll(populateCamerasFromDB());

        int count = 0;
        for (Electronics product : products) {
            count++;
        }
        rowsReturnedLabel.setText("Rows returned: " + count);
        productTableView.getItems().clear();
        productTableView.getItems().addAll(products);

    }

    /**
     * This method will return all laptops from the database
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<Electronics> populateLaptopsFromDB() throws SQLException {
        ObservableList<Electronics> laptops = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/comp1011Test3?useSSL=false", "root", "root");

            //2.  create a statement object
            statement = conn.createStatement();

            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM electronics "
                    + "WHERE productType = 'laptop'");

            //4.  create volunteer objects from each record
            while (resultSet.next()) {

                Laptop newLaptop = new Laptop(Double.parseDouble(resultSet.getString("screenSize")),
                        Double.parseDouble(resultSet.getString("memory")),
                        resultSet.getString("productName"),
                        resultSet.getString("description"),
                        resultSet.getString("manufacturer"),
                        Double.parseDouble(resultSet.getString("price")));
                newLaptop.setProductID(resultSet.getInt("productID"));

                laptops.add(newLaptop);

            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return laptops;
    }

    /**
     * This method will return all cameras from the database
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<Electronics> populateCamerasFromDB() throws SQLException {
        ObservableList<Electronics> cameras = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/comp1011Test3?useSSL=false", "root", "root");

            //2.  create a statement object
            statement = conn.createStatement();

            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM electronics "
                    + "WHERE productType = 'camera'");

            //4.  create volunteer objects from each record
            while (resultSet.next()) {

                Camera newCamera = new Camera(Double.parseDouble(resultSet.getString("resolutionInMP")),
                        resultSet.getString("productName"),
                        resultSet.getString("description"),
                        resultSet.getString("manufacturer"),
                        Double.parseDouble(resultSet.getString("price")));
                newCamera.setProductID(resultSet.getInt("productID"));

                cameras.add(newCamera);

            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return cameras;
    }

    /**
     * This method will update the table with filters
     *
     * @throws SQLException
     */
    public void updateTableWithSearchFilter() throws SQLException {
        int count = 0;
        ObservableList<Electronics> laptops = populateLaptopsFromDB();
        ObservableList<Electronics> cameras = populateCamerasFromDB();
        ObservableList<Electronics> resultsSearch = getSearchResult();;
        

        //if both checkboxes are unchecked
            if (!cameraCheckBox.isSelected() && !laptopCheckBox.isSelected()) {
                productTableView.getItems().clear();
                count = productTableView.getItems().size();
            } 
            
            //if the camera checkbox is unchecked

            else if (!cameraCheckBox.isSelected()) {
                if (!searchTextBox.getText().isEmpty()) {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(resultsSearch);
                    count = productTableView.getItems().size();
                } else {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(laptops);
                    count = productTableView.getItems().size();
                }
                
            } 

            //if the laptop checkbox is unchecked
            
            else if (!laptopCheckBox.isSelected()) {
                if (!searchTextBox.getText().isEmpty()) {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(resultsSearch);
                    count = productTableView.getItems().size();
                } else {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(cameras);
                    count = productTableView.getItems().size();
            
                }
            }
            
            //if both checkboxes are checked
            
            else {
                if (!searchTextBox.getText().isEmpty()) {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(resultsSearch);
                    count = productTableView.getItems().size();
                } else {
                    productTableView.getItems().clear();
                    productTableView.getItems().addAll(laptops);
                    productTableView.getItems().addAll(cameras);
                    count = productTableView.getItems().size();
                }
                
            }
        rowsReturnedLabel.setText("Rows returned: " + count);
    }
    
    /**
     * This method get items from the tableview
     * and compare the item description with the word from searchbox
     * @param search
     * @return 
     */
    
        public ObservableList<Electronics> getSearchResult() throws SQLException {

        ObservableList<Electronics> resultSearch = FXCollections.observableArrayList();
        ObservableList<Electronics> products = FXCollections.observableArrayList();
        products.addAll(populateLaptopsFromDB());
        products.addAll(populateCamerasFromDB());


        ObservableList<Electronics> laptops = populateLaptopsFromDB();
        ObservableList<Electronics> cameras = populateCamerasFromDB();
        if (!cameraCheckBox.isSelected()) {
            for (Electronics item : laptops) {
                if (item.getDescription().contains(searchTextBox.getText())) {
                        resultSearch.add(item);
                }
            }
        } //if the laptop checkbox is unchecked
        else if (!laptopCheckBox.isSelected()) {
            for (Electronics item : cameras) {
                if (item.getDescription().contains(searchTextBox.getText())) {
                        resultSearch.add(item);
                }
            }
        } //if both checkboxes are checked
        else {
            for (Electronics item : products) {
                if (item.getDescription().contains(searchTextBox.getText())) {
                        resultSearch.add(item);
                }
            }
        }

        return resultSearch;
    }

}
