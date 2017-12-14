package Model;

/**
 *
 * @author jwright
 */
public class Camera extends Electronics {
    private double resolutionInMP;

    public Camera(double resolutionInMP, String productName, String description, String manufacturer, double price) {
        super(productName, description, manufacturer, price);
        this.resolutionInMP = resolutionInMP;
    }

    public double getResolutionInMP() {
        return resolutionInMP;
    }

    public void setResolutionInMP(double resolutionInMP) {
        this.resolutionInMP = resolutionInMP;
    }

    @Override
    public void insertIntoDB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
