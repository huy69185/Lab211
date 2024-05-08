package view;

import Control.FlightManagement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MVCmain {

    FlightManagement system = new FlightManagement();

    public static void main(String[] args) {
        FlightManagement system = new FlightManagement();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+-----HỆ THỐNG QUẢN LÝ CHUYẾN BAY----+");
                System.out.println("|1. Quản trị viên đăng nhập          |");
                System.out.println("|2. Menu nhân viên                   |");
                System.out.println("|3. Thoát                            |");
                System.out.println("+------------------------------------+");
                System.out.print("Nhập lựa chọn của bạn:");

                int initialChoice = scanner.nextInt();
                scanner.nextLine();

                switch (initialChoice) {
                    case 1:
                        if (system.login()) {
                            adminMenu(system);
                        }
                        break;
                    case 2:
                        guestMenu(system);
                        break;
                    case 3:
                        System.out.println("Cảm ơn vì đã sử dụng dích vụ của chúng tôi.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.Vui lòng nhập lại!!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }

        }
    }

    public static void manageCrewAssignments(FlightManagement system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+-----MENU QUẢN LÍ PHI HÀNH ĐOÀN----+");
                System.out.println("|1. Gán phi hành đoàn vào chuyến bay|");
                System.out.println("|2. Thêm phi hành đoàn mới          |");
                System.out.println("|3. Dánh sách phi hành đoàn         |");
                System.out.println("|4. Dánh sách nhân viên             |");
                System.out.println("|5. Quay lại menu chính             |");
                System.out.println("+-----------------------------------+");
                System.out.print("Nhập lựa chọn của bạn: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        system.assignCrewToFlight();
                        break;
                    case 2:
                        system.assignStaffToCrew();
                        break;
                    case 3:
                        system.printCrewInformation();
                        break;
                    case 4:
                        system.printStaffInformation();
                    case 5:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }
        }
    }

    public static void guestMenu(FlightManagement system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+---------MENU KHÁCH HÀNG--------+");
                System.out.println("|1. Đặt vé                       |");
                System.out.println("|2. Check-In                     |");
                System.out.println("|3. Hiện danh sách các chuyến bay|");
                System.out.println("|4. Quay lại menu chính          |");
                System.out.println("+--------------------------------+");
                System.out.print("Nhập lựa chọn của bạn:");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        system.makeReservation();
                        break;
                    case 2:
                        system.checkIn();
                        break;
                    case 3:
                        system.printFlightInformation();
                        break;
                    case 4:
                        system.logout();
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.Vui lòng nhập lại!!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }
        }
    }

    public static void adminMenu(FlightManagement system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+-----MENU CỦA NGƯỜI QUẢN LÝ-----+");
                System.out.println("|1. Quản lý chuyến bay           |");
                System.out.println("|2. Đặt vé                       |");
                System.out.println("|3. Check-In                     |");
                System.out.println("|4. Quản lý phi hàng đoàn        |");
                System.out.println("|5. Hiển thị danh sách chuyến bay|");
                System.out.println("|6. Lưu file                     |");
                System.out.println("|7. Tải file đã lưu              |");
                System.out.println("|8. Đăng xuất                    |");
                System.out.println("+--------------------------------+");
                System.out.print("Nhập lựa chọn của bạn:");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        system.addFlight();
                        break;
                    case 2:
                        system.makeReservation();
                        break;
                    case 3:
                        system.checkIn();
                        break;
                    case 4:
                        manageCrewAssignments(system);
                        break;
                    case 5:
                        system.printFlightInformation();
                        break;
                    case 6:
                        system.saveDataToFile();
                        break;
                    case 7:
                        system.loadDataFromFile();
                        break;
                    case 8:
                        system.logout();
                        return;
                    default:
                        System.out.println("Lựa chọn của bạn không hợp lệ! Vui lòng nhập lại");
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }
        }
    }
}
