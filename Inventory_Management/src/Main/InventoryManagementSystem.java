package Main;


import Model.ImportExportSlip;
import Data.Warehouse;
import Data.Product;
import Model.DailyProduct;
import Model.LongLastingProduct;
import Model.WarehouseItem;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class InventoryManagementSystem {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static List<Product> products = new ArrayList<>();
    private static List<ImportExportSlip> importSlips = new ArrayList<>();
    private static List<ImportExportSlip> exportSlips = new ArrayList<>();
    private static Warehouse warehouse = new Warehouse();
    private static Set<String> usedImportCodes = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueRunning = true;
        while (continueRunning) {
            System.out.println("Inventory Management System:");
            System.out.println("1. Manage Products.");
            System.out.println("2. Manage Warehouse.");
            System.out.println("3. Report.");
            System.out.println("4. Store Data to Files.");
            System.out.println("5. Show Data to Files.");
            System.out.println("6. Close the Application.");
            System.out.print("Enter your choice: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (mainChoice) {
                case 1:
                    manageProductsMenu(scanner);
                    break;
                case 2:
                    manageWarehouseMenu(scanner);
                    break;
                case 3:
                    generateReportsMenu(scanner);
                    break;
                case 4:
                    saveDataToFile();
                    break;
                case 5:
                    chooseDataFileToDisplay();
                    break;
                case 6:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

//Manage Products.
    private static void manageProductsMenu(Scanner scanner) {
        boolean continueManagingProducts = true;

        while (continueManagingProducts) {
            System.out.println("_______Manage Products_______");
            System.out.println("1. Add a Product");
            System.out.println("2. Update Product Information");
            System.out.println("3. Delete Product");
            System.out.println("4. Show All Products");
            System.out.println("5. Back to Main Menu");
            System.out.println("_____________________________");
            System.out.print("Enter your choice: ");
            int productsChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (productsChoice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    updateProduct(scanner);
                    break;
                case 3:
                    deleteProduct(scanner);
                    break;
                case 4:
                    displayProducts();
                    break;
                case 5:
                    continueManagingProducts = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    public static void addProduct(Scanner scanner) {
        System.out.print("Enter product code: ");
        String productCode = scanner.nextLine();
        if (isProductCodeDuplicate(productCode)) {
            System.out.println("Product code already exists.");
            return;
        }
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter product type (1 for Daily, 2 for Long Lasting): ");
        int productTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product product;
        if (productTypeChoice == 1) {
            product = new DailyProduct(productCode, productName);
        } else if (productTypeChoice == 2) {
            product = new LongLastingProduct(productCode, productName);
        } else {
            System.out.println("Invalid product type choice.");
            return;
        }

        products.add(product);
        System.out.println("Product added successfully.");
    }

    public static boolean isProductCodeDuplicate(String productCode) {
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                return true;
            }
        }
        return false;
    }

    public static void updateProduct(Scanner scanner) {
        displayProducts();
        System.out.print("Enter product code to update: ");
        String productCode = scanner.nextLine();
        Product foundProduct = getProductByCode(productCode);
        if (foundProduct == null) {
            System.out.println("Product does not exist.");
            return;
        }

        System.out.println("Current Product Information:");
        foundProduct.displayInfo();

        System.out.println("Enter new product information (leave blank to keep current value):");
        System.out.print("Enter product name: ");
        String newProductName = scanner.nextLine();
        if (!newProductName.isEmpty()) {
            foundProduct.setProductName(newProductName);
        }

        String newProductType;
        do {
            System.out.print("Enter product type (1 for Daily Product / 2 for Long Lasting Product): ");
            String newProductTypeChoice = scanner.nextLine().trim();
            if (newProductTypeChoice.equals("1")) {
                newProductType = "Daily Product";
                break;
            } else if (newProductTypeChoice.equals("2")) {
                newProductType = "Long Lasting Product";
                break;
            } else {
                System.out.println("Invalid input. Please choose 1 for Daily Product or 2 for Long Lasting Product.");
            }
        } while (true);

        foundProduct.setProductType(newProductType);

        System.out.println("Product information updated successfully.");
    }

    public static void deleteProduct(Scanner scanner) {
    System.out.print("Enter product code to delete: ");
    String productCode = scanner.nextLine();
    Product foundProduct = getProductByCode(productCode);
    
    if (foundProduct == null) {
        System.out.println("Product does not exist.");
        return;
    }

    // Kiểm tra xem sản phẩm đã tham gia quá trình nhập hoặc xuất chưa
    boolean isProductInvolvedInTransactions = isProductInvolvedInTransactions(productCode);
    if (isProductInvolvedInTransactions) {
        System.out.println("Cannot delete the product because it has been involved in import or export transactions.");
        return;
    }

    // Remove the product from the products list
    products.remove(foundProduct);
    System.out.println("Product deleted successfully.");
}

public static boolean isProductInvolvedInTransactions(String productCode) {
    for (ImportExportSlip importSlip : importSlips) {
        if (importSlip.getProductCode().equals(productCode)) {
            return true;
        }
    }
    for (ImportExportSlip exportSlip : exportSlips) {
        if (exportSlip.getProductCode().equals(productCode)) {
            return true;
        }
    }
    return false;
}

    public static void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("List of Products:");
            for (Product product : products) {
                product.displayInfo();
            }

        }
    }

// Manage Warehouse.
    private static void manageWarehouseMenu(Scanner scanner) {
        boolean continueManagingWarehouse = true;
        while (continueManagingWarehouse) {
            System.out.println("_____Manage Warehouse_____");
            System.out.println("1. Import Products");
            System.out.println("2. Export Products");
            System.out.println("3. Show Warehouse Status");
            System.out.println("4. Back to Main Menu");
            System.out.println("_________________________");
            System.out.print("Enter your choice: ");
            int warehouseChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (warehouseChoice) {
                case 1:
                    do {
                        importProducts(scanner);
                        System.out.print("Do you want to import another product? (yes/no): ");
                        String continueImport = scanner.nextLine().toLowerCase();
                        if (!continueImport.equals("yes")) {
                            break;
                        }
                    } while (true);
                    break;
                case 2:
                    do {
                        exportProducts(scanner);
                        System.out.print("Do you want to export another product? (yes/no): ");
                        String continueExport = scanner.nextLine().toLowerCase();
                        if (!continueExport.equals("yes")) {
                            break;
                        }
                    } while (true);
                case 3:
                    displayWarehouseStatus();
                    break;
                case 4:
                    continueManagingWarehouse = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    public static void importProducts(Scanner scanner) {

        System.out.print("Enter product code to import: ");
        String productCode = scanner.nextLine();
        Product foundProduct = getProductByCode(productCode);

        if (foundProduct == null) {
            System.out.println("Product does not exist.");
            return;
        }
        String importCodeRegex = "\\d{7}";

        String Slipcode;
        do {
            String prevSlipCode = getPreviousSlipCode(productCode);
            if (prevSlipCode == null) {
                System.out.print("Enter import code (7-digit number): ");
                Slipcode = scanner.nextLine();
            } else {
                int newCode = Integer.parseInt(prevSlipCode) + 1;
                Slipcode = String.format("%07d", newCode);
                System.out.println("Enter import code (7-digit number): " + Slipcode);
            }

            if (!Slipcode.matches(importCodeRegex)) {
                System.out.println("Invalid import code. It should be a 7-digit number.");
            }
        } while (!Slipcode.matches(importCodeRegex));

        if (checkDuplicateSlipCode(Slipcode)) {
            System.out.println("Export code already exists.");
            return;
        }

        System.out.print("Enter import quantity: ");
        int importQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter import price per unit: ");
        double importPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Product product = getProductByCode(productCode);
        Date manufacturingDate;
        Date expirationDate;
        if (product.isDailyProduct()) {
            manufacturingDate = new Date();
            expirationDate = new Date();
        } else {

            do {
                System.out.print("Enter manufacturing date (dd-mm-yyyy): ");
                String manufacturingDateStr = scanner.nextLine();
                manufacturingDate = parseDate(manufacturingDateStr);

                if (manufacturingDate == null) {
                    // Ngày không hợp lệ, yêu cầu nhập lại
                    continue;
                }

                Date currentDate = new Date(); // Ngày hiện tại
                if (manufacturingDate.after(currentDate)) {
                    System.out.println("Manufacturing date must be before or equal to the current date.");
                }

            } while (manufacturingDate == null || manufacturingDate.after(new Date()));

            do {
                System.out.print("Enter expiration date (dd-mm-yyyy): ");
                String expirationDateStr = scanner.nextLine();
                expirationDate = parseDate(expirationDateStr);

                if (expirationDate == null) {
                    // Ngày không hợp lệ, yêu cầu nhập lại
                    continue;
                }

                if (expirationDate.before(manufacturingDate)) {
                    System.out.println("Expiration date must be after the manufacturing date.");
                }
            } while (expirationDate == null || expirationDate.before(manufacturingDate));
        }
        // Update warehouse item
        WarehouseItem warehouseItem = warehouse.getWarehouseItem(productCode);
        if (warehouseItem == null) {
            warehouseItem = new WarehouseItem(productCode, importPrice, importQuantity, manufacturingDate, expirationDate);
            warehouse.addWarehouseItem(warehouseItem);
        } else {
            warehouseItem.setPrice(importPrice);
            warehouseItem.setQuantity(warehouseItem.getQuantity() + importQuantity);
            warehouseItem.setManufacturingDate(manufacturingDate);
            warehouseItem.setExpirationDate(expirationDate);
        }

        // Create import slip
        ImportExportSlip importSlip = new ImportExportSlip(productCode, Slipcode, importQuantity, importPrice, true);
        importSlips.add(importSlip);

        System.out.println("Product imported successfully.");
    }

    public static String getPreviousSlipCode(String productCode) {
        String prevImportCode = null;

        // Duyệt danh sách phiếu nhập kho để tìm mã của sản phẩm cần tìm
        for (ImportExportSlip importSlip : importSlips) {
            if (importSlip.getProductCode().equals(productCode)) {
                if (prevImportCode == null || importSlip.getSlipcode().compareTo(prevImportCode) > 0) {
                    prevImportCode = importSlip.getSlipcode();
                }
            }
        }

        return prevImportCode;
    }

    public static boolean checkDuplicateSlipCode(String Slipcode) {
        if (usedImportCodes.contains(Slipcode)) {
            return true; // Mã nhập khẩu đã tồn tại
        } else {
            usedImportCodes.add(Slipcode); // Thêm mã nhập khẩu vào danh sách đã sử dụng
            return false; // Mã nhập khẩu không trùng lặp
        }
    }

    public static void exportProducts(Scanner scanner) {
        System.out.print("Enter product code to export: ");
        String productCode = scanner.nextLine();
        Product foundProduct = getProductByCode(productCode);
        if (foundProduct == null) {
            System.out.println("Product does not exist.");
            return;
        }

        String importCodeRegex = "\\d{7}";

        String Slipcode;
        do {
            String prevSlipCode = getPreviousSlipCode(productCode);
            if (prevSlipCode == null) {
                System.out.print("Enter import code (7-digit number): ");
                Slipcode = scanner.nextLine();
            } else {
                int newCode = Integer.parseInt(prevSlipCode) + 1;
                Slipcode = String.format("%07d", newCode);
                System.out.println("Enter import code (7-digit number): " + Slipcode);
            }

            if (!Slipcode.matches(importCodeRegex)) {
                System.out.println("Invalid import code. It should be a 7-digit number.");
            }
        } while (!Slipcode.matches(importCodeRegex));

        if (checkDuplicateSlipCode(Slipcode)) {
            System.out.println("Export code already exists.Generating a new import code...");
            try {
                int oldImportCode = Integer.parseInt(Slipcode);
                int newImportCode = oldImportCode + 1;
                Slipcode = String.format("%07d", newImportCode);
                System.out.println("New import code: " + Slipcode);
            } catch (NumberFormatException e) {
                System.out.println("Error generating a new import code.");
                return;
            }
        }

        System.out.print("Enter export quantity: ");
        int exportQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter export price per unit: ");
        double exportPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline\

        Date manufacturingDate;
        Date expirationDate;
        Product product = getProductByCode(productCode);
        if (product.isDailyProduct()) {
            manufacturingDate = new Date();
            expirationDate = new Date();
        } else {

            do {
                System.out.print("Enter manufacturing date (dd-mm-yyyy): ");
                String manufacturingDateStr = scanner.nextLine();
                manufacturingDate = parseDate(manufacturingDateStr);

                if (manufacturingDate == null) {
                    // Ngày không hợp lệ, yêu cầu nhập lại
                    continue;
                }

                Date currentDate = new Date(); // Ngày hiện tại
                if (manufacturingDate.after(currentDate)) {
                    System.out.println("Manufacturing date must be before or equal to the current date.");
                }

            } while (manufacturingDate == null || manufacturingDate.after(new Date()));

            do {
                System.out.print("Enter expiration date (dd-mm-yyyy): ");
                String expirationDateStr = scanner.nextLine();
                expirationDate = parseDate(expirationDateStr);

                if (expirationDate == null) {
                    // Ngày không hợp lệ, yêu cầu nhập lại
                    continue;
                }

                if (expirationDate.before(manufacturingDate)) {
                    System.out.println("Expiration date must be after the manufacturing date.");
                }
            } while (expirationDate == null || expirationDate.before(manufacturingDate));
        }
        // Update warehouse item
        WarehouseItem warehouseItem = warehouse.getWarehouseItem(productCode);
        if (warehouseItem == null) {
            warehouseItem = new WarehouseItem(productCode, exportPrice, exportQuantity, manufacturingDate, expirationDate);
            warehouse.addWarehouseItem(warehouseItem);
        } else {
            warehouseItem.setPrice(exportPrice);
            warehouseItem.setQuantity(warehouseItem.getQuantity() - exportQuantity);
            warehouseItem.setManufacturingDate(manufacturingDate);
            warehouseItem.setExpirationDate(expirationDate);
        }

        // Create export slip
        ImportExportSlip exportSlip = new ImportExportSlip(productCode, Slipcode, exportQuantity, exportPrice, true);
        exportSlips.add(exportSlip);

        System.out.println("Product exported successfully.");
    }

    public static void displayWarehouseStatus() {
        List<WarehouseItem> sortedItems = warehouse.getWarehouseItems().stream()
                .sorted(Comparator.comparing(WarehouseItem::getProductCode))
                .collect(Collectors.toList());

        System.out.println("Warehouse Status:");
        for (WarehouseItem item : sortedItems) {
            Product product = getProductByCode(item.getProductCode());
            System.out.println("|Product Code:                                               |" + item.getProductCode());
            System.out.println("|Product Name:                                               |" + product.getProductName());
            System.out.println("|Import Price:                                               |" + item.getPrice());
            System.out.println("|Quantity:                                                   |" + item.getQuantity());
            System.out.println("|Manufacturing Date:                                         |" + dateFormat.format(item.getManufacturingDate()));
            System.out.println("|Expiration Date:                                            |" + dateFormat.format(item.getExpirationDate()));
            System.out.println("|____________________________________________________________|");
        }
    }

    public static Product getProductByCode(String productCode) {
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                return product;
            }
        }
        return null;
    }

    public static Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use dd-mm-yyyy.");
            return null;
        }
    }

//  Report  
    private static void generateReportsMenu(Scanner scanner) {
        boolean continueGeneratingReports = true;
        while (continueGeneratingReports) {
            System.out.println("_________Generate Reports_________");
            System.out.println("1. Products that have expired");
            System.out.println("2. The products that the store is selling");
            System.out.println("3. Products that are running out of stock");
            System.out.println("4. List Import Slips");
            System.out.println("5. List Export Slips");
            System.out.println("6. Back to Main Menu");
            System.out.println("_________________________________________");
            System.out.print("Enter your choice: ");
            int reportsChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (reportsChoice) {
                case 1:
                    displayExpiredProducts();
                    break;
                case 2:
                    displaySellingProducts();
                    break;
                case 3:
                    displayRunningOutProducts();
                    break;
                case 4:
                    listImportSlips();
                    break;
                case 5:
                    listExportSlips();
                    break;
                case 6:
                    continueGeneratingReports = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    public static void listImportSlips() {
        if (importSlips.isEmpty()) {
            System.out.println("No import slips found.");
        } else {
            System.out.println("List of Import Slips:");
            for (ImportExportSlip slip : importSlips) {
                slip.displaySlipInfo();
            }
        }
    }

    public static void listExportSlips() {
        if (exportSlips.isEmpty()) {
            System.out.println("No export slips found.");
        } else {
            System.out.println("List of Export Slips:");
            for (ImportExportSlip slip : exportSlips) {
                slip.displaySlipInfo();
            }
        }
    }

    public static void displayRunningOutProducts() {
        System.out.println("Products Running Out:");
        for (Product product : products) {
            WarehouseItem warehouseItem = warehouse.getWarehouseItem(product.getProductCode());
            if (warehouseItem != null && warehouseItem.getQuantity() <= 3) {
                product.displayInfo();
                System.out.println("Quantity in Warehouse: " + warehouseItem.getQuantity());
                System.out.println("_________________________");
            } else {
                System.out.println("No product.");
            }
        }
    }

    public static void displaySellingProducts() {
        System.out.println("Selling Products:");
        for (Product product : products) {
            WarehouseItem warehouseItem = warehouse.getWarehouseItem(product.getProductCode());
            if (warehouseItem != null && warehouseItem.getQuantity() > 1) {
                product.displayInfo();
                System.out.println("Quantity in Warehouse: " + warehouseItem.getQuantity());
                System.out.println("_________________________");
            } else {
                System.out.println("No product.");
            }
        }
    }

    public static void displayExpiredProducts() {
        System.out.println("Expired Products:");
        Date currentDate = new Date();
        for (WarehouseItem warehouseItem : warehouse.getWarehouseItems()) {
            if (warehouseItem.getExpirationDate() != null && currentDate.after(warehouseItem.getExpirationDate())) {
                Product product = getProductByCode(warehouseItem.getProductCode());
                if (product != null) {
                    product.displayInfo();
                    System.out.println("Expiration Date: " + dateFormat.format(warehouseItem.getExpirationDate()));
                    System.out.println("_________________________");
                } else {
                    System.out.println("No product.");
                }
            }
        }
    }

    //  Store Data to Files  
    public static void saveDataToFile() {
        try {
            ObjectOutputStream productsOut = new ObjectOutputStream(new FileOutputStream("src//Output//product.dat"));
            productsOut.writeObject(products);
            productsOut.close();

            ObjectOutputStream importSlipsOut = new ObjectOutputStream(new FileOutputStream("src//Output//importSlips.dat"));
            importSlipsOut.writeObject(importSlips);
            importSlipsOut.close();

            ObjectOutputStream exportSlipsOut = new ObjectOutputStream(new FileOutputStream("src//Output//exportSlips.dat"));
            exportSlipsOut.writeObject(exportSlips);
            exportSlipsOut.close();

            System.out.println("Data stored to files successfully.");
        } catch (IOException e) {
            System.out.println("Error while storing data to files.");
            e.printStackTrace(); // In thông tin lỗi
        }
    }

//    Show Data to Files
    public static void chooseDataFileToDisplay() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a data file to display:");
        System.out.println("1. Products Data (products.dat)");
        System.out.println("2. Import Slips Data (importSlips.dat)");
        System.out.println("3. Export Slips Data (exportSlips.dat)");
        System.out.print("Enter your choice: ");
        int fileChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String filePath = null;
        String fileType = null;

        switch (fileChoice) {
            case 1:
                filePath = "src//OutPut//product.dat";
                fileType = "Products";
                break;
            case 2:
                filePath = "src//OutPut//importSlips.dat";
                fileType = "Import Slips";
                break;
            case 3:
                filePath = "src//OutPut//exportSlips.dat";
                fileType = "Export Slips";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        displayDataFromFile(filePath, fileType);
    }

    public static void displayDataFromFile(String filePath, String fileType) {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filePath));
            List<?> savedData = (List<?>) objectIn.readObject();
            objectIn.close();

            System.out.println("Saved " + fileType + ":");
            for (Object data : savedData) {
                if (data instanceof Product) {
                    ((Product) data).displayInfo();
                } else if (data instanceof ImportExportSlip) {
                    ((ImportExportSlip) data).displaySlipInfo();
                }
                System.out.println("_________________________");
            }
        } catch (Exception e) {
            System.out.println("Error while reading " + fileType + " data file.");
        }
    }

    private static String generateNewImportCode() {
        if (importSlips.isEmpty()) {
            // Nếu danh sách importSlips trống, bạn có thể tạo mã importCode đầu tiên
            return "0000001";
        } else {
            // Lấy mã importCode cuối cùng trong danh sách
            String lastImportCode = importSlips.get(importSlips.size() - 1).getSlipcode();

            try {
                // Chuyển đổi mã importCode cuối cùng thành số nguyên
                int lastCodeInt = Integer.parseInt(lastImportCode);

                // Tăng giá trị lên 1 để tạo mã mới
                lastCodeInt++;

                // Chuyển đổi lại thành chuỗi và định dạng mã
                return String.format("%07d", lastCodeInt);
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu không thể chuyển đổi thành số nguyên
                System.out.println("Error generating a new import code.");
                return null;
            }
        }
    }

    private static String generateNewExportCode() {
        if (importSlips.isEmpty()) {
            // Nếu danh sách importSlips trống, bạn có thể tạo mã importCode đầu tiên
            return "0000001";
        } else {
            // Lấy mã importCode cuối cùng trong danh sách
            String lastExportCode = exportSlips.get(exportSlips.size() - 1).getSlipcode();

            try {
                // Chuyển đổi mã importCode cuối cùng thành số nguyên
                int lastCodeInt = Integer.parseInt(lastExportCode);

                // Tăng giá trị lên 1 để tạo mã mới
                lastCodeInt++;

                // Chuyển đổi lại thành chuỗi và định dạng mã
                return String.format("%07d", lastCodeInt);
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu không thể chuyển đổi thành số nguyên
                System.out.println("Error generating a new import code.");
                return null;
            }
        }
    }
}
