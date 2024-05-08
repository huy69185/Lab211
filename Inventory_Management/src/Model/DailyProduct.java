package Model;

import Data.Product;

public class DailyProduct extends Product {

    public DailyProduct(String productCode, String productName) {
        super(productCode, productName, "Daily Product");
        setDailyProduct(true);
    }

    @Override
    // Tạo một biến boolean để kiểm tra xem header đã được hiển thị hay chưa

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
