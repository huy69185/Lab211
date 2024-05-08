package lab3;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehicleManagementSystem {

    private List<Vehicle> vehicles;

    public VehicleManagementSystem(String fName) {
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Scanner scanner) {
        System.out.println("-----Infomation vehicle: -----");
        System.out.print("Enter vehicle ID: ");
        int id = scanner.nextInt();
        if (!CheckID(id)) {
            return;
        }
        scanner.nextLine();
        boolean check = true;
        String name = "";
        while (check) {
            System.out.print("Enter vehicle name: ");

            name = scanner.nextLine();
            String rule = "[a-zA-Z]+";
            if (!name.matches(rule)) {
                System.out.println("Invalid name. Please re-enter!");
                check = true;
            } else {
                check = false;
            }
        }
        boolean check2 = true;
        String color = "";
        while (check2) {
            System.out.print("Enter vehicle color: ");
            color = scanner.nextLine();
            String rule = "[a-zA-Z]+";
            if (!color.matches(rule)) {
                System.out.println("Invalid name. Please re-enter!");
                check2 = true;
            } else {
                check2 = false;
            }
        }

        System.out.print("Enter vehicle price: ");
        long price = scanner.nextLong();
        System.out.print("Enter vehicle brand: ");
        scanner.nextLine();
        String brand = scanner.nextLine();
        String type = getTypeFromInput(scanner);
        scanner.nextLine();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy");
        String productYear;
        while (true) {
            System.out.print("Enter vehicle product year(yyyy): ");
            productYear = scanner.nextLine();
            try {
                format.parse(productYear);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Format value is not valid. Please re-enter!");
            }
        }

        Vehicle newVehicle = new Vehicle(id, name, color, price, brand, type, productYear);
        vehicles.add(newVehicle);
        System.out.println("Vehicle is added successfully!");
    }

    public boolean CheckID(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getID_Vehicle() == id) {
                System.out.println("Vehicle exists in the system.");
                return false;
            }
        }
        return true;
    }

    public void checkExistingVehicle(Scanner scanner) {
        System.out.println("Enter vehicle ID to check: ");
        int checkID = scanner.nextInt();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getID_Vehicle() == checkID) {
                System.out.println("Vehicle exists in the system.");
                return;
            }
        }
        System.out.println("Vehicle does not exist in the system.");
    }

    public void updateVehicle(Scanner scanner) {
        displayAllVehicles();
        System.out.println("-----Infomation new vehicle: -----");
        System.out.print("Enter vehicle ID to update: ");
        int updateID = scanner.nextInt();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getID_Vehicle() == updateID) {
                Scanner sc = new Scanner(System.in);
                boolean check = true;
                String newName = "";
                while (check) {
                    System.out.print("Enter new vehicle name: ");

                    newName = sc.nextLine();
                    String rule = "[a-zA-Z]+";
                    if (!newName.matches(rule)) {
                        System.out.println("Invalid name. Please re-enter!");
                        check = true;
                    } else {
                        check = false;
                    }
                }
                boolean check2 = true;
                String newColor = "";
                while (check2) {
                    System.out.print("Enter new vehicle color: ");
                    newColor = sc.nextLine();
                    String rule = "[a-zA-Z]+";
                    if (!newColor.matches(rule)) {
                        System.out.println("Invalid name. Please re-enter!");
                        check2 = true;
                    } else {
                        check2 = false;
                    }
                }
                System.out.print("Enter new Price_Vehicle: ");
                long newPrice = sc.nextLong();
                System.out.print("Enter new Brand_Vehicle: ");
                sc.nextLine();
                String newBrand = sc.nextLine();
                String newType = getTypeFromInput(scanner);
                scanner.nextLine();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy");
                String newProductYear;
                while (true) {
                    System.out.print("Enter vehicle product year(yyyy): ");
                    newProductYear = scanner.nextLine();
                    try {
                        format.parse(newProductYear);
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Format value is not valid. Please re-enter!");
                    }
                }
                vehicle.setName_Vehicle(newName);
                vehicle.setColor_Vehicle(newColor);
                vehicle.setPrice_Vehicle(newPrice);
                vehicle.setBrand_Vehicle(newBrand);
                vehicle.setType(newType);
                vehicle.setProductYear(newProductYear);

                System.out.println("Vehicle is updated successfully!");
                return;
            }
        }
        System.out.println("Vehicle not found in the system.");
    }

    public void deleteVehicle(Scanner scanner) {
        displayAllVehicles();
        System.out.print("Enter vehicle ID to delete: ");
        int deleteID = scanner.nextInt();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getID_Vehicle() == deleteID) {
                vehicles.remove(vehicle);
                System.out.println("Vehicle deleted successfully!");
                return;
            }
        }
        System.out.println("Vehicle not found in the system.");
    }

    public void searchVehicleByID(int ID_Vehicle) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getID_Vehicle() == ID_Vehicle) {
                System.out.println("Infomation vehicle:");
                System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
                System.out.println("| ID_Vehicle  | Name_Vehicle      | Color_Vehicle | Price_Vehicle | Brand_Vehicle | Type                | Product_Year |");
                System.out.printf("| %-11d | %-17s | %-13s | %-13d | %-13s | %-19s | %-12s |\n",
                        vehicle.getID_Vehicle(), vehicle.getName_Vehicle(), vehicle.getColor_Vehicle(), vehicle.getPrice_Vehicle(), vehicle.getBrand_Vehicle(), vehicle.getType(), vehicle.getProductYear());
                System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
                return;
            }
        }
        System.out.println("Vehicle not found in the system.");
    }

    public void searchVehicleByName(String name) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getName_Vehicle().equalsIgnoreCase(name)) {
                System.out.println("Infomation vehicle:");
                System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
                System.out.println("| ID_Vehicle  | Name_Vehicle      | Color_Vehicle | Price_Vehicle | Brand_Vehicle | Type                | Product_Year |");
                System.out.printf("| %-11d | %-17s | %-13s | %-13d | %-13s | %-19s | %-12s |\n",
                        vehicle.getID_Vehicle(), vehicle.getName_Vehicle(), vehicle.getColor_Vehicle(), vehicle.getPrice_Vehicle(), vehicle.getBrand_Vehicle(), vehicle.getType(), vehicle.getProductYear());
                System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
                return;
            }
        }
        System.out.println("Vehicle not found in the system.");
    }

    public void displayAllVehicles() {

        System.out.println("Infomation vehicle:");
        System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
        System.out.println("| ID_Vehicle  | Name_Vehicle      | Color_Vehicle | Price_Vehicle | Brand_Vehicle | Type                | Product_Year |");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("| %-11d | %-17s | %-13s | %-13d | %-13s | %-19s | %-12s |\n",
                    vehicle.getID_Vehicle(), vehicle.getName_Vehicle(), vehicle.getColor_Vehicle(), vehicle.getPrice_Vehicle(), vehicle.getBrand_Vehicle(), vehicle.getType(), vehicle.getProductYear());
        }
        System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
    }

    public void saveToFile() {
        try {
            BufferedWriter writerDat = new BufferedWriter(new FileWriter("src/Output/vehicle.dat"));
            for (Vehicle vehicle : vehicles) {
                writerDat.write(vehicle.toString());
                writerDat.newLine();
            }
            writerDat.close();

            System.out.println("Vehicles are saved to files successfully!");
        } catch (IOException e) {
            System.out.println("Error saving vehicles to files: " + e.getMessage());
        }
    }

    public void printVehiclesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Output/vehicle.dat"));
            String line;
            System.out.println("Infomation vehicle:");
            System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
            System.out.println("| ID_Vehicle  | Name_Vehicle      | Color_Vehicle | Price_Vehicle | Brand_Vehicle | Type                | Product_Year |");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    continue;
                }
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String color = parts[2].trim();
                long price = Long.parseLong(parts[3].trim());
                String brand = parts[4].trim();
                String type = parts[5].trim();
                String productYear = parts[6].trim();

                Vehicle vehicle = new Vehicle(id, name, color, price, brand, type, productYear);
                System.out.printf("| %-11d | %-17s | %-13s | %-13d | %-13s | %-19s | %-12s |\n", id, name, color, price, brand, type, productYear);
            }
            System.out.println("+-------------+-------------------+---------------+---------------+---------------+---------------------+--------------+");
            reader.close();

        } catch (IOException e) {
            System.out.println("Error printing vehicles from file: " + e.getMessage());
        }
    }

    public void Seach(Scanner scanner) {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter your choice: ");
        int searchChoice = scanner.nextInt();
        switch (searchChoice) {
            case 1:
                System.out.print("Enter vehicle ID to search: ");
                int searchID = scanner.nextInt();
                searchVehicleByID(searchID);
                break;

            case 2:
                System.out.print("Enter vehicle name to search: ");
                scanner.nextLine();
                String searchName = scanner.nextLine();
                searchVehicleByName(searchName);
                break;

            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    public String getTypeFromInput(Scanner scanner) {
        System.out.println("+------Type of vehicle------+");
        System.out.println("| 1. Bicycle                |");
        System.out.println("| 2. Motorbike              |");
        System.out.println("| 3. Car                    |");
        System.out.println("| 4. Electric bicycle       |");
        System.out.println("| 5. Electric motorbike     |");
        System.out.println("| 6. Electric Car           |");
        System.out.println("+---------------------------+");
        System.out.println("Choose a type:");

        int typeChoice = scanner.nextInt();
        String type = "";

        switch (typeChoice) {
            case 1:
                type = "Bicycle";
                break;
            case 2:
                type = "Motorbike";
                break;
            case 3:
                type = "Car";
                break;
            case 4:
                type = "Electric bicycle";
                break;
            case 5:
                type = "Electric motorbike";
                break;
            case 6:
                type = "Electric Car";
                break;
            default:
                System.out.println("Invalid choice. Type set to unknown.");
                type = "Unknown";
                break;
        }

        System.out.println("Type: " + type);
        return type;
    }

    public void loadFromFile() {
        String fileName = "src/Output/vehicle.dat";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; 
                }
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    System.out.println("Invalid data format in line: " + line);
                    continue;
                }
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String color = parts[2].trim();
                long price = Long.parseLong(parts[3].trim());
                String brand = parts[4].trim();
                String type = parts[5].trim();
                String productYear = parts[6].trim();

                Vehicle vehicle = new Vehicle(id, name, color, price, brand, type, productYear);
                vehicles.add(vehicle); 
            }
             System.out.println("Load file succesfull!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}
