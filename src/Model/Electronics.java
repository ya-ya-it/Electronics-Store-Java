package Model;

import java.sql.SQLException;

/**
 *
 * @author jwright
 */
public abstract class Electronics {
    private int productID;
    private String productName, description, manufacturer;
    private double price;

    public Electronics(String productName, String description, String manufacturer, double price) {
        this.productName = productName;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
    
    

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract void insertIntoDB() throws SQLException;
    
    }
