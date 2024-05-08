package Model;

import static Data.Product.headerDisplayed;
import java.io.Serializable;



public class ImportExportSlip implements Serializable {
    private String productCode;
    private String Slipcode;
    private int quantity;
    private double price;
    private boolean isImport;

    public ImportExportSlip(String productCode,String Slipcode, int quantity, double price, boolean isImport) {
        this.productCode = productCode;
        this.Slipcode = Slipcode;
        this.quantity = quantity;
        this.price = price;
        this.isImport = isImport;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSlipcode() {
        return Slipcode;
    }

    public void setSlipcode(String Slipcode) {
        this.Slipcode = Slipcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isIsImport() {
        return isImport;
    }

    public void setIsImport(boolean isImport) {
        this.isImport = isImport;
    }

    
     public void displaySlipInfo() {
        if (!headerDisplayed) {
            System.out.println("Product Code      Slip Code         Quantity    Price per Unit    Type");
            headerDisplayed = true; // Đánh dấu rằng header đã được xuất hiện
        }

        // Sử dụng chuỗi định dạng cố định để đảm bảo các giá trị thẳng hàng với các ô tương ứng
        System.out.printf("%-18s%-18s%-12s%-18s%-6s%n", productCode, Slipcode, quantity, price, (isImport ? "Import" : "Export"));
    }
}