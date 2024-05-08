package Model;


import Data.Product;


public class LongLastingProduct extends Product {

    public LongLastingProduct(String productCode, String productName) {
  super(productCode, productName, "Long Lasting Product");
  setDailyProduct(false);
}

    @Override
    public void displayInfo() {
        if (!headerDisplayed) {
            String header = "| Product Code | Product Name | Product Type |";
            System.out.println(header);
            headerDisplayed = true; // Đánh dấu rằng header đã được hiển thị
        }

        String code = getProductCode();
        String name = getProductName();
        String type = getProductType();

        // Tính độ dài tối đa của mỗi cột (có thể điều chỉnh theo nhu cầu)
        int maxCodeLength = 12;
        int maxNameLength = 12;
        int maxTypeLength = 12;

        // In thông tin sản phẩm với các độ dài cố định cho từng cột
        System.out.println("| " + padString(code, maxCodeLength) + " | " + padString(name, maxNameLength) + " | " + padString(type, maxTypeLength) + " |");
        headerDisplayed = false;
    }

  

// Phương thức để đệm chuỗi cho đến khi có độ dài mong muốn
    private String padString(String input, int length) {
        if (input.length() >= length) {
            return input;
        } else {
            StringBuilder paddedString = new StringBuilder(input);
            while (paddedString.length() < length) {
                paddedString.append(" ");
            }
            return paddedString.toString();
        }
    }
}
