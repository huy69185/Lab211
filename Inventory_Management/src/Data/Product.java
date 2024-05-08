package Data;

import java.io.Serializable;

public abstract class Product implements Serializable {

    private String productCode;
    private String productName;
    private String productType;
    public static boolean headerDisplayed = false;

    public Product(String productCode, String productName, String productType) {
        this.productCode = productCode;
        this.productName = productName;
        this.productType = productType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public static void clearHeaderHistory() {
        headerDisplayed = false;
    }

    public abstract void displayInfo();

    private boolean isDailyProduct;

    public void setDailyProduct(boolean isDailyProduct) {
        this.isDailyProduct = isDailyProduct;
    }

    public boolean isDailyProduct() {
        return isDailyProduct;
    }
}
