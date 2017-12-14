
package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author jwright
 */
public class Laptop extends Electronics{
    private double screenSize, memory;

    public Laptop(double screenSize, double memory, String productName, String description, String manufacturer, double price) {
        super(productName, description, manufacturer, price);
        this.screenSize = screenSize;
        this.memory = memory;
    }

    public double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    @Override
    public void insertIntoDB() throws SQLException{
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/comp1011Test3?useSSL=false", "root", "root");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO electronics (productType, manufacturer, productName, description, price, screenSize, memory)"
                    + "VALUES (?,?,?,?,?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
                     
            //4. Bind the values to the parameters
            preparedStatement.setString(1, "laptop");
            preparedStatement.setString(2, super.getManufacturer());
            preparedStatement.setString(3, super.getProductName());
            preparedStatement.setString(4, super.getDescription());
            preparedStatement.setDouble(5, super.getPrice());
            preparedStatement.setDouble(6, super.getPrice());
            preparedStatement.setDouble(7, screenSize);
            preparedStatement.setDouble(8, memory);
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }

}