
package lab3;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        VehicleManagementSystem vehicleManagementSystem = new VehicleManagementSystem("src/Output/vehicles.dat");
        vehicleManagementSystem.loadFromFile();
        Scanner sc = new Scanner(System.in);
        int choice;
        boolean check = true;

        do {
            System.out.println("");
            System.out.println("+----Vehicle Management System Menu----+");
            System.out.println("| 1. Add new vehicle                   |");
            System.out.println("| 2. Check existing vehicle            |");
            System.out.println("| 3. Update vehicle                    |");
            System.out.println("| 4. Delete vehicle                    |");
            System.out.println("| 5. Search vehicle                    |");
            System.out.println("| 6. Display all vehicles              |");
            System.out.println("| 7. Save vehicles to file             |");
            System.out.println("| 8. Print vehicles from file          |");
            System.out.println("| 9. Quit                              |");
            System.out.println("+--------------------------------------+");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    vehicleManagementSystem.addVehicle(sc); 
                    break;
                case 2:
                    vehicleManagementSystem.checkExistingVehicle(sc);
                    break;
                case 3:
                    vehicleManagementSystem.updateVehicle(sc);
                    break;
                case 4:

                    vehicleManagementSystem.deleteVehicle(sc);
                    break;
                case 5:
                    vehicleManagementSystem.Seach(sc);

                    break;
                case 6:
                    vehicleManagementSystem.displayAllVehicles();
                    break;
                case 7:
                    vehicleManagementSystem.saveToFile();
                    break;
                case 8:
                    vehicleManagementSystem.printVehiclesFromFile();
                    break;
                case 9:
                    System.out.println("Existing1"
                            + "...");
                    check = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (check == true);
    }
}
