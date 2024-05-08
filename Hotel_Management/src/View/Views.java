
package View;

import java.util.Scanner;


public class Views {
    public void mainMenu() {
        System.out.println("+-----Hotel Management System-----+");
        System.out.println("| 1. Add new hotel                |");
        System.out.println("| 2. Check hotel existence        |");
        System.out.println("| 3. Update hotel information     |");
        System.out.println("| 4. Delete hotel                 |");
        System.out.println("| 5. Search hotel                 |");
        System.out.println("| 6. Display hotel list           |");
        System.out.println("| 7. Delete by name prefix        |");
        System.out.println("| 8. Exit                         |");
        System.out.println("+---------------------------------+");
        System.out.print("Enter your choice: ");
    }
    public void exitProgram() {
        System.out.println("Exiting the program. Thank you!");
        System.exit(0);
    }
    public void printSeparatorLine() {
        System.out.println("+------------+----------------------+----------------------+----------------------------------------------------+-----------------+--------+");
    }
    public void searchMenu(){
        System.out.println("+-----Choose search criteria-----+");
        System.out.println("| 1. Search by Hotel ID          |");
        System.out.println("| 2. Search by Hotel Name        |");
        System.out.println("| 3. Search by Keyword           |");
        System.out.println("| 4. Search by ID Range          |");
        System.out.println("+--------------------------------+");
        System.out.print("Enter your choice: ");
    }
}
