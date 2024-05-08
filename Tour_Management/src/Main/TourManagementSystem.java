package Main;

import Model.Booking;
import Model.Hotel;
import Model.Tour;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TourManagementSystem {

    ArrayList<Tour> tourCatalog = new ArrayList<>();
    ArrayList<Hotel> hotels = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();

    // Phương thức để tạo một gói tour
    void createTourPackage() {
        Scanner sc = new Scanner(System.in); // Khởi tạo một đối tượng Scanner để đọc dữ liệu từ bàn phím
        // Khai báo các biến để lưu thông tin của tour
        int id;
        String name;
        String destination;
        String duration;
        String description;
        double price;
        String[] inclusions;
        String[] exclusions;
        boolean idExists;
        // Vòng lặp để đảm bảo ID tour không trùng lặp
        do {
            idExists = false; // Thiết lập biến kiểm tra ID tồn tại là false ban đầu
            System.out.print("Nhập ID tour: ");
            id = sc.nextInt(); // Nhập ID từ bàn phím
            // Kiểm tra xem ID đã tồn tại trong danh sách tour hay chưa
            for (Tour existingTour : tourCatalog) {
                if (existingTour.id == id) {
                    System.out.println("Tour có ID " + id + " đã tồn tại trong hệ thống.");
                    idExists = true; // Nếu ID đã tồn tại, đặt cờ này thành true và thoát khỏi vòng lặp
                    break;
                }
            }
        } while (idExists); // Lặp lại nếu ID đã tồn tại
        sc.nextLine(); // Đọc bỏ dòng trống
        // Nhập thông tin của tour từ bàn phím
        System.out.print("Nhập tên tour: ");
        name = sc.nextLine();
        System.out.print("Nhập điểm đến: ");
        destination = sc.nextLine();

        // Yêu cầu nhập và kiểm tra định dạng thời gian
        boolean validDuration;
        Date currentDate = new Date(); // Lấy thời gian hiện tại
        do {
            validDuration = true;
            System.out.print("Nhập thời gian (dd/mm/yyyy hh:mm): ");
            duration = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateFormat.setLenient(false);
            try {
                Date inputDate = dateFormat.parse(duration);
                if (inputDate.before(currentDate)) {
                    System.out.println("Thời gian không thể trở về quá khứ. Vui lòng nhập lại.");
                    validDuration = false;
                }
            } catch (ParseException e) {
                System.out.println("Định dạng thời gian không hợp lệ. Vui lòng nhập lại theo định dạng dd/mm/yyyy hh:mm.");
                validDuration = false;
            }
        } while (!validDuration);
        // Nhập mô tả của tour từ bàn phím
        System.out.print("Nhập mô tả: ");
        description = sc.nextLine();
        // Yêu cầu nhập và kiểm tra giá tour
        do {
            System.out.print("Nhập giá: ");
            while (!sc.hasNextDouble()) {
                System.out.println("Vui lòng nhập một số cho giá.");
                System.out.print("Nhập giá: ");
                sc.next(); // Đọc bỏ dữ liệu không hợp lệ
            }
            price = sc.nextDouble();
        } while (price <= 0); // Lặp lại nếu giá không hợp lệ

        sc.nextLine(); // Đọc bỏ dòng trống

        // Nhập số lượng và các điều khoản bao gồm của tour
        System.out.print("Nhập số lượng các điều khoản bao gồm: ");
        int numInclusions = sc.nextInt();
        sc.nextLine();
        inclusions = new String[numInclusions];
        for (int i = 0; i < numInclusions; i++) {
            System.out.print("Điều khoản bao gồm " + (i + 1) + ": ");
            inclusions[i] = sc.nextLine();
        }
        // Nhập số lượng và các điều khoản loại trừ của tour
        System.out.print("Nhập số lượng các điều khoản loại trừ: ");
        int numExclusions = sc.nextInt();
        sc.nextLine();
        exclusions = new String[numExclusions];
        for (int i = 0; i < numExclusions; i++) {
            System.out.print("Điều khoản loại trừ " + (i + 1) + ": ");
            exclusions[i] = sc.nextLine();
        }
        // Tạo đối tượng Tour mới và thêm vào danh sách tour
        Tour tour = new Tour(id, name, destination, duration, description, price, inclusions, exclusions);
        tourCatalog.add(tour);
        // In ra thông báo xác nhận việc tạo tour thành công
        System.out.println("Đã thêm tour thành công!");
    }

    void viewAndUpdateTours() {
        Scanner sc = new Scanner(System.in);
        while (true) {//dùng vòng lặp hiển thị menu
            System.out.println("+---Xem và chỉnh sửa Tour---+");
            System.out.println("| 1. Tìm kiếm Tour          |");
            System.out.println("| 2. Chỉnh sửa Tour         |");
            System.out.println("| 3. Quay lại Menu Chính    |");
            System.out.println("+---------------------------+");
            System.out.print("Nhập lựa chọn: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                System.out.print("Nhập tiêu chí tìm kiếm (1.Điểm đến/2.Thời gian/3.Giá): ");
                String search = sc.nextLine();
                ArrayList<Tour> filteredTours = new ArrayList<>();//khởi tạo danh sách cho các tour đã lọc
                if (search.equalsIgnoreCase("1")) {//sử dụng equalsIgnoreCase để so sánh lựa chọn 
                    System.out.print("Nhập điểm đến: ");
                    String destination = sc.nextLine();
                    for (Tour tour : tourCatalog) {
                        if (tour.destination.toLowerCase().contains(destination)) {//contains để so sánh không phân biệt hoa thường
                            filteredTours.add(tour);
                        }
                    }
                } else if (search.equalsIgnoreCase("2")) {
                    System.out.print("Nhập thời gian: ");
                    String duration = sc.nextLine();
                    for (Tour tour : tourCatalog) {
                        if (tour.duration == duration) {
                            filteredTours.add(tour);
                        }
                    }
                } else if (search.equalsIgnoreCase("2")) {
                    System.out.print("Nhập giá tối thiểu: ");
                    double minPrice = sc.nextDouble();
                    for (Tour tour : tourCatalog) {
                        if (tour.price >= minPrice) {
                            filteredTours.add(tour);
                        }
                    }
                }
                System.out.println("Các tour phù hợp:");//in ra
                System.out.printf("%-10s %-20s %-15s %-10s %-10s%n", "ID", "Tên Tour", "Điểm Đến", " Thời Gian         ", "Giá");
                System.out.println("------------------------------------------------------------------------------------------");
                for (Tour tour : filteredTours) {
                    System.out.printf("%-10s %-20s %-15s %-10s $%.2f%n", tour.id, tour.name, tour.destination, tour.duration, tour.price);
                }
            } else if (choice == 2) {
                System.out.print("Nhập ID tour để chỉnh sửa: ");
                int id = sc.nextInt();
                boolean found = false;
                for (Tour tour : tourCatalog) {//vòng lặp chạy từ đầu đến cuối danh sách
                    if (tour.id == id) {
                        found = true;
                        System.out.println("Chỉnh sửa: " + tour.name);

                        System.out.print("Nhập tên mới (hoặc NA để giữ nguyên): ");
                        String newName = sc.nextLine();
                        if (!newName.equals("NA")) {//Equals dùng để so sánh đúng tuyệt đối
                            tour.name = newName;
                        }
                        sc.nextLine();
                        System.out.print("Nhập điểm đến mới (hoặc NA để giữ nguyên): ");
                        String newDestination = sc.nextLine();
                        if (!newDestination.equals("NA")) {
                            tour.destination = newDestination;
                        }
                        // Yêu cầu nhập thời gian theo đúng định dạng dd/mm/yyyy hh:mm
                        System.out.print("Nhập thời gian mới (dd/mm/yyyy hh:mm) (hoặc NA để giữ nguyên): ");
                        String newDuration = sc.nextLine();
                        if (!newDuration.equals("NA")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            dateFormat.setLenient(false);//sử dụng thư viện dateFormat kiểm tra tính đúng sai của dòng vừa nhập ở trên
                            try {
                                dateFormat.parse(newDuration);//đúng sẽ chuyển thành duration mới sai in ra thông báo và nhập lại
                                tour.duration = newDuration;
                            } catch (ParseException e) {
                                System.out.println("Định dạng thời gian không hợp lệ. Thời gian được giữ nguyên.");
                            }
                        }
                        System.out.print("Nhập giá mới (hoặc NA để giữ nguyên): ");
                        String newPrice = sc.nextLine();
                        if (!newPrice.equals("NA")) {
                            try {
                                double price = Double.parseDouble(newPrice);
                                if (price > 0) {
                                    tour.price = price;
                                } else {
                                    System.out.println("Giá phải là một số dương. Giá được giữ nguyên.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Giá phải là một số. Giá được giữ nguyên.");
                            }
                        }
                        System.out.print("Nhập mô tả mới (hoặc NA để giữ nguyên): ");
                        String newDescription = sc.nextLine();
                        if (!newDescription.equals("NA")) {
                            tour.description = newDescription;
                        }
                        System.out.print("Nhập số lượng loại trừ mới: ");
                        int newNumExclusions = sc.nextInt();
                        sc.nextLine(); // Đọc bỏ dòng trống
                        String[] newExclusions = new String[newNumExclusions];
                        for (int i = 0; i < newNumExclusions; i++) {
                            System.out.print("Loại trừ " + (i + 1) + ": ");
                            newExclusions[i] = sc.nextLine();
                        }
                        tour.exclusions = newExclusions;
                        System.out.print("Nhập số lượng bao gồm mới: ");
                        int newNumInclusions = sc.nextInt();
                        sc.nextLine(); // Đọc bỏ dòng trống
                        String[] newInclusions = new String[newNumInclusions];
                        for (int i = 0; i < newNumInclusions; i++) {
                            System.out.print("Bao gồm " + (i + 1) + ": ");
                            newInclusions[i] = sc.nextLine();
                        }
                        tour.inclusions = newInclusions;
                        System.out.println("Đã cập nhật tour!");
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Không tìm thấy tour có ID " + id);
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    void deleteTour() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập ID của tour cần xóa: ");
        int id = sc.nextInt();
        Tour tourToDelete = null;
        for (Tour tour : tourCatalog) {
            if (tour.id == id) {
                tourToDelete = tour;
                break;
            }
        }
        if (tourToDelete == null) {
            System.out.println("Không tìm thấy tour!");
            return;
        }
        // Xác nhận xóa
        System.out.print("Bạn có chắc chắn muốn xóa tour " + tourToDelete.name + "? (Y/N): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            tourCatalog.remove(tourToDelete);
            System.out.println("Đã xóa tour thành công!");
        } else {
            System.out.println("Đã hủy xóa tour.");
        }
    }

    void addHotel() {
        Scanner scanner = new Scanner(System.in);
        int id;
        String name;
        String location;
        int roomsAvailable;
        double pricePerNight;
        boolean idExists;
        do {
            idExists = false;
            System.out.print("Nhập ID của khách sạn: ");
            id = scanner.nextInt();
            // Kiểm tra trùng lặp ID
            for (Hotel existingHotel : hotels) {
                if (existingHotel.id == id) {
                    System.out.println("Khách sạn có ID " + id + " đã tồn tại trong hệ thống.");
                    idExists = true;
                    break;
                }
            }
        } while (idExists); // Lặp lại nếu ID đã tồn tại

        scanner.nextLine(); // Đọc bỏ dòng trống

        System.out.print("Nhập tên khách sạn: ");
        name = scanner.nextLine();

        System.out.print("Nhập địa điểm: ");
        location = scanner.nextLine();

        // Yêu cầu nhập số phòng cho đến khi nhập đúng
        do {
            System.out.print("Nhập số phòng trống: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Vui lòng nhập số phòng là một số nguyên.");
                scanner.next(); // Đọc bỏ giá trị không hợp lệ
            }
            roomsAvailable = scanner.nextInt();
        } while (roomsAvailable <= 0);
        // Yêu cầu nhập giá đêm cho đến khi nhập đúng
        do {
            System.out.print("Nhập giá/đêm: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Vui lòng nhập giá là một số.");
                scanner.next(); // Đọc bỏ giá trị không hợp lệ
            }
            pricePerNight = scanner.nextDouble();
        } while (pricePerNight <= 0);

        // Tiếp tục nhập thông tin khách sạn và thêm vào hotels
        Hotel newHotel = new Hotel(id, name, location, roomsAvailable, null, pricePerNight);
        hotels.add(newHotel);
        System.out.println("Đã thêm khách sạn thành công!");
    }

    void updateAndManageHotels() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("+----Xem và chỉnh sửa khách sạn----+");
            System.out.println("| 1. Tìm kiếm khách sạn            |");
            System.out.println("| 2. Chỉnh sửa thông tin khách sạn |");
            System.out.println("| 3. Quay lại Menu Chính           |");
            System.out.println("+----------------------------------+");
            System.out.print("Nhập lựa chọn: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Nhập tiêu chí tìm kiếm (1.địa điểm/2.tiện ích/3.số phòng trống): ");
                String search = scanner.nextLine();

                ArrayList<Hotel> filteredHotels = new ArrayList<>();

                if (search.equalsIgnoreCase("1")) {
                    System.out.print("Nhập địa điểm: ");
                    String location = scanner.nextLine();
                    for (Hotel hotel : hotels) {
                        if (hotel.location.toLowerCase().contains(location)) {
                            filteredHotels.add(hotel);
                        }
                    }
                } else if (search.equalsIgnoreCase("2")) {
                    System.out.print("Nhập tiện ích: ");
                    String amenity = scanner.nextLine();
                    for (Hotel hotel : hotels) {
                        for (String a : hotel.amenities) {
                            if (a.toLowerCase().contains(amenity)) {
                                filteredHotels.add(hotel);
                                break;
                            }
                        }
                    }
                } else if (search.equalsIgnoreCase("3")) {
                    System.out.print("Nhập số phòng trống: ");
                    int rooms = scanner.nextInt();
                    for (Hotel hotel : hotels) {
                        if (hotel.roomsAvailable >= rooms) {
                            filteredHotels.add(hotel);
                        }
                    }
                }

                System.out.println("Các khách sạn phù hợp:");
                for (Hotel hotel : filteredHotels) {
                    System.out.println(hotel.id + " " + hotel.name + " " + hotel.location + " " + hotel.roomsAvailable + " phòng trống");
                }

            } else if (choice == 2) {
                System.out.print("Nhập ID khách sạn để chỉnh sửa: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số

                boolean found = false;

                for (Hotel hotel : hotels) {
                    if (hotel.id == id) {
                        found = true;
                        System.out.println("Chỉnh sửa: " + hotel.name);

                        System.out.print("Nhập tên mới (hoặc NA để giữ nguyên): ");
                        String newName = scanner.nextLine();
                        if (!newName.equals("NA")) {
                            hotel.name = newName;
                        }

                        System.out.print("Nhập địa điểm mới (hoặc NA để giữ nguyên): ");
                        String newLocation = scanner.nextLine();
                        if (!newLocation.equals("NA")) {
                            hotel.location = newLocation;
                        }

                        // Yêu cầu nhập số phòng trống và giá phòng mỗi đêm dưới dạng số
                        System.out.print("Nhập số phòng trống mới (hoặc NA để giữ nguyên): ");
                        String newRoomsAvailable = scanner.nextLine();
                        if (!newRoomsAvailable.equals("NA")) {
                            try {
                                int roomsAvailable = Integer.parseInt(newRoomsAvailable);
                                if (roomsAvailable >= 0) {
                                    hotel.roomsAvailable = roomsAvailable;
                                } else {
                                    System.out.println("Số phòng trống phải là một số không âm. Giá trị được giữ nguyên.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Số phòng trống phải là một số. Giá trị được giữ nguyên.");
                            }
                        }

                        System.out.print("Nhập giá phòng mỗi đêm mới (hoặc NA để giữ nguyên): ");
                        String newPricePerNight = scanner.nextLine();
                        if (!newPricePerNight.equals("NA")) {
                            try {
                                double pricePerNight = Double.parseDouble(newPricePerNight);
                                if (pricePerNight >= 0) {
                                    hotel.pricePerNight = pricePerNight;
                                } else {
                                    System.out.println("Giá phòng mỗi đêm phải là một số không âm. Giá trị được giữ nguyên.");
                                }
                            } catch (NumberFormatException e) {//dòng sử lý ngoại lệ
                                System.out.println("Giá phòng mỗi đêm phải là một số. Giá trị được giữ nguyên.");
                            }
                        }

                        System.out.println("Các tiện ích hiện tại: ");
                        for (String amenity : hotel.amenities) {
                            System.out.println("- " + amenity);
                        }

                        System.out.print("Bạn muốn thêm/sửa/xóa tiện ích? (add/edit/remove/none): ");
                        String amenityChoice = scanner.nextLine();
                        if (amenityChoice.equalsIgnoreCase("add")) {
                            System.out.print("Nhập số lượng tiện ích mới: ");
                            int newAmenitiesCount = scanner.nextInt();
                            scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số
                            String[] newAmenities = new String[newAmenitiesCount];
                            for (int i = 0; i < newAmenitiesCount; i++) {
                                System.out.print("Tiện ích thứ " + (i + 1) + ": ");
                                newAmenities[i] = scanner.nextLine();
                            }
                            hotel.amenities = newAmenities;
                        } else if (amenityChoice.equalsIgnoreCase("edit")) {
                            System.out.print("Nhập vị trí của tiện ích cần sửa: ");
                            int position = scanner.nextInt();
                            scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số
                            System.out.print("Nhập tiện ích mới: ");
                            String newAmenity = scanner.nextLine();
                            hotel.amenities[position - 1] = newAmenity;
                        } else if (amenityChoice.equalsIgnoreCase("remove")) {
                            System.out.print("Nhập vị trí của tiện ích cần xóa: ");
                            int position = scanner.nextInt();
                            scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số
                            String[] updatedAmenities = new String[hotel.amenities.length - 1];
                            int k = 0;
                            for (int i = 0; i < hotel.amenities.length; i++) {
                                if (i != position - 1) {
                                    updatedAmenities[k++] = hotel.amenities[i];
                                }
                            }
                            hotel.amenities = updatedAmenities;
                        }

                        System.out.println("Đã cập nhật thông tin khách sạn!");
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Không tìm thấy khách sạn có ID " + id);
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    void removeHotel() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập ID của khách sạn cần xóa: ");
        int id = scanner.nextInt();
        Hotel hotelToRemove = null;
        for (Hotel hotel : hotels) {
            if (hotel.id == id) {
                hotelToRemove = hotel;
                break;
            }
        }
        if (hotelToRemove == null) {
            System.out.println("Không tìm thấy khách sạn!");
            return;
        }
        // Kiểm tra xem có booking đang tồn tại cho khách sạn này không
        boolean hasBookings = checkHotelBookings(hotelToRemove);
        if (hasBookings) {
            System.out.println("Khách sạn đang có đặt phòng. Bạn có chắc chắn muốn xóa khách sạn " + hotelToRemove.name + "? (Y/N): ");
        } else {
            System.out.println("Bạn có chắc chắn muốn xóa khách sạn " + hotelToRemove.name + "? (Y/N): ");
        }
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            hotels.remove(hotelToRemove);
            System.out.println("Đã xóa khách sạn thành công!");
        } else {
            System.out.println("Đã hủy xóa khách sạn.");
        }
    }

    boolean checkHotelBookings(Hotel hotel) {
        for (Booking booking : bookings) {
            if (booking.bookedHotel.id == hotel.id) {
                return true;
            }
        }
        return false;
    }

    void customerBooking() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("+----Menu đặt tour và khách sạn----+");
            System.out.println("| 1. Xem danh sách các tour        |");
            System.out.println("| 2. Xem danh sách các khách sạn   |");
            System.out.println("| 3. Đặt tour và khách sạn         |");
            System.out.println("| 4. Quay lại Menu Chính           |");
            System.out.println("+----------------------------------+");
            System.out.print("Nhập lựa chọn: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                displayTours();
            } else if (choice == 2) {
                displayHotels();
            } else if (choice == 3) {
                bookTourAndHotel(scanner);
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    void displayTours() {
        System.out.println("Danh sách các tour:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-20s | %-15s | %-20s | %s%n", "ID", "Tên Tour", "Điểm Đến", "Thời Gian", "Giá");
        System.out.println("------------------------------------------------------------------------------------");
        for (Tour tour : tourCatalog) {
            System.out.printf("%-4d | %-20s | %-15s | %-20s | $%.2f%n", tour.id, tour.name, tour.destination, tour.duration, tour.price);
        }
        System.out.println("------------------------------------------------------------------------------------");
    }

    void displayHotels() {
        System.out.println("Danh sách các khách sạn:");
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-25s | %-15s | %-13s | %s%n", "ID", "Tên Khách Sạn", "Địa Điểm", "Phòng Trống", "Giá/Đêm");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (Hotel hotel : hotels) {
            System.out.printf("%-4d | %-25s | %-15s | %-13d | $%.2f%n", hotel.id, hotel.name, hotel.location, hotel.roomsAvailable, hotel.pricePerNight);
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    void bookTourAndHotel(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Nhập ID của tour bạn muốn đặt: ");
                int tourId = Integer.parseInt(scanner.nextLine());

                Tour selectedTour = null;
                for (Tour tour : tourCatalog) {//vòng lặp chạy từ đầu đến cuối danh sách tour
                    if (tour.id == tourId) {
                        selectedTour = tour;
                        break;
                    }
                }

                if (selectedTour == null) {
                    System.out.println("Không tìm thấy tour có ID " + tourId);
                    continue;
                }

                System.out.print("Nhập ID của khách sạn bạn muốn đặt: ");
                int hotelId = Integer.parseInt(scanner.nextLine());

                Hotel selectedHotel = null;
                for (Hotel hotel : hotels) {
                    if (hotel.id == hotelId) {
                        selectedHotel = hotel;
                        break;
                    }
                }
                if (selectedHotel == null) {
                    System.out.println("Không tìm thấy khách sạn có ID " + hotelId);
                    continue;
                }
                System.out.print("Nhập số lượng khách: ");
                int numberOfGuests = Integer.parseInt(scanner.nextLine());
                Booking newBooking = new Booking(bookings.size() + 1, selectedTour, selectedHotel, numberOfGuests);
                bookings.add(newBooking);
                System.out.println("Đã đặt tour và khách sạn thành công!");
                break; // Kết thúc vòng lặp khi đặt thành công
            } catch (NumberFormatException e) {
                System.out.println("ID và số lượng khách phải là một số nguyên. Vui lòng nhập lại.");
            }
        }
    }

    void manageBookings() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("+----Menu quản lý đặt tour và khách sạn----+");
            System.out.println("| 1. Xem danh sách đặt tour và khách sạn   |");
            System.out.println("| 2. Chỉnh sửa đặt tour và khách sạn       |");
            System.out.println("| 3. Hủy đặt tour và khách sạn             |");
            System.out.println("| 4. Quay lại Menu Chính                   |");
            System.out.println("+------------------------------------------+");
            System.out.print("Nhập lựa chọn: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewBookings();
                    break;
                case 2:
                    modifyBooking(scanner);
                    break;
                case 3:
                    cancelBooking(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    void viewBookings() {
        System.out.println("Danh sách đặt tour và khách sạn:");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-4s | %-25s | %-25s | %-13s | %s%n", "ID", "Tên Tour", "Tên Khách Sạn", "Số Lượng Khách", "Tổng Giá");
        System.out.println("----------------------------------------------------------");
        for (Booking booking : bookings) {
            System.out.printf("%-4d | %-25s | %-25s | %-13d | $%.2f%n",
                    booking.bookingId, booking.bookedTour.name, booking.bookedHotel.name, booking.numberOfGuests, booking.bookedTour.price * booking.numberOfGuests);
        }
        System.out.println("----------------------------------------------------------");
    }

    void modifyBooking(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Nhập ID của đặt tour và khách sạn bạn muốn chỉnh sửa: ");
                int bookingId = Integer.parseInt(scanner.nextLine());

                Booking bookingToModify = null;
                for (Booking booking : bookings) {
                    if (booking.bookingId == bookingId) {
                        bookingToModify = booking;
                        break;
                    }
                }
                if (bookingToModify == null) {
                    System.out.println("Không tìm thấy đặt tour và khách sạn có ID " + bookingId);
                    return;
                }
                System.out.println("Thông tin hiện tại của đặt tour và khách sạn:");
                System.out.println("Tour: " + bookingToModify.bookedTour.name);
                System.out.println("Khách sạn: " + bookingToModify.bookedHotel.name);
                System.out.println("Số lượng khách: " + bookingToModify.numberOfGuests);
                // Cập nhật thông tin đặt tour và khách sạn ở đây
                System.out.println("+-Chọn loại thông tin bạn muốn cập nhật-+");
                System.out.println("| 1. Tour                               |");
                System.out.println("| 2. Khách sạn                          |");
                System.out.println("| 3. Số lượng khách                     |");
                System.out.println("+---------------------------------------+");
                System.out.print("Nhập lựa chọn: ");
                int updateChoice = Integer.parseInt(scanner.nextLine());
                switch (updateChoice) {
                    case 1:
                        System.out.print("Nhập ID của tour mới hoặc nhập 0 để giữ nguyên: ");
                        int newTourId = Integer.parseInt(scanner.nextLine());
                        if (newTourId != 0) {
                            Tour newTour = null;
                            for (Tour tour : tourCatalog) {
                                if (tour.id == newTourId) {
                                    newTour = tour;
                                    break;
                                }
                            }
                            if (newTour == null) {
                                System.out.println("Không tìm thấy tour có ID " + newTourId);
                            } else {
                                bookingToModify.bookedTour = newTour;
                                System.out.println("Đã cập nhật tour thành công!");
                            }
                        }
                        break;
                    case 2:
                        System.out.print("Nhập ID của khách sạn mới hoặc nhập 0 để giữ nguyên: ");
                        int newHotelId = Integer.parseInt(scanner.nextLine());
                        if (newHotelId != 0) {
                            Hotel newHotel = null;
                            for (Hotel hotel : hotels) {
                                if (hotel.id == newHotelId) {
                                    newHotel = hotel;
                                    break;
                                }
                            }
                            if (newHotel == null) {
                                System.out.println("Không tìm thấy khách sạn có ID " + newHotelId);
                            } else {
                                bookingToModify.bookedHotel = newHotel;
                                System.out.println("Đã cập nhật khách sạn thành công!");
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Nhập số lượng khách mới hoặc nhập 0 để giữ nguyên: ");
                        int newNumberOfGuests = Integer.parseInt(scanner.nextLine());
                        if (newNumberOfGuests != 0) {
                            bookingToModify.numberOfGuests = newNumberOfGuests;
                            System.out.println("Đã cập nhật số lượng khách thành công!");
                        }
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
                break; // Kết thúc vòng lặp khi cập nhật thành công
            } catch (NumberFormatException e) {
                System.out.println("ID và số lượng khách phải là một số nguyên. Vui lòng nhập lại.");
            }
        }
    }

    void cancelBooking(Scanner scanner) {
        System.out.print("Nhập ID của đặt tour và khách sạn bạn muốn hủy: ");
        int bookingId = scanner.nextInt();

        Booking bookingToDelete = null;
        for (Booking booking : bookings) {
            if (booking.bookingId == bookingId) {
                bookingToDelete = booking;
                break;
            }
        }
        if (bookingToDelete == null) {
            System.out.println("Không tìm thấy đặt tour và khách sạn có ID " + bookingId);
            return;
        }
        System.out.print("Bạn có chắc chắn muốn hủy đặt tour và khách sạn " + bookingToDelete.bookedTour.name + " và " + bookingToDelete.bookedHotel.name + "? (Y/N): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            bookings.remove(bookingToDelete);
            System.out.println("Đã hủy đặt tour và khách sạn thành công!");
        } else {
            System.out.println("Đã hủy hành động.");
        }
    }

    void generateReports() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("+----------Menu tạo báo cáo----------+");
            System.out.println("| 1. Phân tích các tour              |");
            System.out.println("| 2. Cung cấp thông tin doanh thu    |");
            System.out.println("| 3. Quay lại Menu Chính             |");
            System.out.println("+------------------------------------+");

            System.out.print("Nhập lựa chọn: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    analyzeTours();
                    break;
                case 2:
                    provideRevenueInsights();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    void analyzeTours() {
        System.out.println("Phân tích các tour:");

        // Tính tổng số lượng tour
        int totalTours = tourCatalog.size();
        System.out.println("Tổng số tour: " + totalTours);

        // Tính trung bình giá của các tour
        double totalPrices = 0;
        for (Tour tour : tourCatalog) {
            totalPrices += tour.price;
        }
        double averagePrice = totalPrices / totalTours;
        System.out.println("Giá trung bình của các tour: $" + averagePrice);

        // Phân tích theo điểm đến
        Map<String, Integer> destinationCounts = new HashMap<>();
        for (Tour tour : tourCatalog) {
            String destination = tour.destination;
            destinationCounts.put(destination, destinationCounts.getOrDefault(destination, 0) + 1);
        }
        System.out.println("Phân tích theo điểm đến:");
        for (Map.Entry<String, Integer> entry : destinationCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " tour");
        }
    }

    void provideRevenueInsights() {
        System.out.println("Cung cấp thông tin doanh thu:");

        // Tính tổng doanh thu từ tour và khách sạn
        double totalRevenue = 0;
        for (Booking booking : bookings) {
            double tourRevenue = booking.bookedTour.price * booking.numberOfGuests;
            double hotelRevenue = booking.bookedHotel.pricePerNight * booking.numberOfGuests;
            totalRevenue += tourRevenue + hotelRevenue;
        }
        System.out.println("Tổng doanh thu: $" + totalRevenue);

        // Tính doanh thu trung bình từ mỗi booking
        double averageRevenuePerBooking = totalRevenue / bookings.size();
        System.out.println("Doanh thu trung bình mỗi booking: $" + averageRevenuePerBooking);
    }

    void dataManagement() {
        System.out.println("+------Quản lý dữ liệu------+");
        System.out.println("| 1. Lưu dữ liệu vào tệp    |");
        System.out.println("| 2. Tải dữ liệu từ tệp     |");
        System.out.println("| 3. Thoát ra menu chính    |");
        System.out.println("+---------------------------+");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập lựa chọn: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                saveDataToFile();
                break;
            case 2:
                loadDataFromFile();
                break;
            case 3:
                exitToMenu();
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    void saveDataToFile() {
        // Lưu trữ thông tin tour vào tệp tour.dat
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Output/tour.dat"))) {
            for (Tour tour : tourCatalog) {
                writer.write(tour.id + "," + tour.name + "," + tour.destination + "," + tour.duration + "," + tour.description + "," + tour.price);
                writer.newLine();
            }
            System.out.println("Dữ liệu tour đã được lưu trữ vào tệp tour.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu trữ dữ liệu tour: " + e.getMessage());
        }
        // Lưu trữ thông tin khách sạn vào tệp hotel.dat
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Output/hotel.dat"))) {
            for (Hotel hotel : hotels) {
                writer.write(hotel.id + "," + hotel.name + "," + hotel.location + "," + hotel.roomsAvailable + "," + hotel.pricePerNight);
                writer.newLine();
            }
            System.out.println("Dữ liệu khách sạn đã được lưu trữ vào tệp hotel.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu trữ dữ liệu khách sạn: " + e.getMessage());
        }
        // Lưu trữ thông tin đặt tour vào tệp booking.dat
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Output/booking.dat"))) {
            for (Booking booking : bookings) {
                writer.write(booking.bookingId + "," + booking.bookedTour.id + "," + booking.bookedHotel.id + "," + booking.numberOfGuests);
                writer.newLine();
            }
            System.out.println("Dữ liệu đặt tour đã được lưu trữ vào tệp booking.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu trữ dữ liệu đặt tour: " + e.getMessage());
        }
    }

    void loadDataFromFile() {
        // Tải dữ liệu từ tệp tour.dat
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Output/tour.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String destination = data[2];
                String duration = data[3]; // Thay đổi kiểu dữ liệu từ int thành String
                String description = data[4];
                double price = Double.parseDouble(data[5]);
                Tour tour = new Tour(id, name, destination, duration, description, price, null, null);
                tourCatalog.add(tour);
            }
            System.out.println("Dữ liệu tour đã được tải từ tệp tour.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi tải dữ liệu tour từ tệp: " + e.getMessage());
        }

        // Tải dữ liệu từ tệp hotel.dat
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Output/hotel.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String location = data[2];
                int roomsAvailable = Integer.parseInt(data[3]);
                double pricePerNight = Double.parseDouble(data[4]);
                Hotel hotel = new Hotel(id, name, location, roomsAvailable, null, pricePerNight);
                hotels.add(hotel);
            }
            System.out.println("Dữ liệu khách sạn đã được tải từ tệp hotel.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi tải dữ liệu khách sạn từ tệp: " + e.getMessage());
        }
        // Tải dữ liệu từ tệp booking.dat
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Output/booking.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int bookingId = Integer.parseInt(data[0]);
                int tourId = Integer.parseInt(data[1]);
                int hotelId = Integer.parseInt(data[2]);
                int numberOfGuests = Integer.parseInt(data[3]);
                Tour bookedTour = null;
                for (Tour tour : tourCatalog) {
                    if (tour.id == tourId) {
                        bookedTour = tour;
                        break;
                    }
                }
                Hotel bookedHotel = null;
                for (Hotel hotel : hotels) {
                    if (hotel.id == hotelId) {
                        bookedHotel = hotel;
                        break;
                    }
                }
                if (bookedTour != null && bookedHotel != null) {
                    Booking booking = new Booking(bookingId, bookedTour, bookedHotel, numberOfGuests);
                    bookings.add(booking);
                }
            }
            System.out.println("Dữ liệu đặt tour đã được tải từ tệp booking.dat");
        } catch (IOException e) {
            System.out.println("Lỗi khi tải dữ liệu đặt tour từ tệp: " + e.getMessage());
        }
    }

    void exitToMenu() {
        System.out.println("Trở lại menu chính.");
    }

    public static void main(String[] args) {
        TourManagementSystem app = new TourManagementSystem();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("+------------Menu------------+");
            System.out.println("| 1. Tạo Tour                |");
            System.out.println("| 2. Quản lý Tour            |");
            System.out.println("| 3. Xóa Tour                |");
            System.out.println("| 4. Thêm Khách sạn          |");
            System.out.println("| 5. Quản lý Khách sạn       |");
            System.out.println("| 6. Xóa Khách sạn           |");
            System.out.println("| 7. Đặt Tour                |");
            System.out.println("| 8. Quản lý Đặt Tour        |");
            System.out.println("| 9. Tạo Báo cáo             |");
            System.out.println("| 10. Quản lý dữ liệu        |");
            System.out.println("| 11. Thoát                  |");
            System.out.println("+----------------------------+");
            System.out.print("Nhập lựa chọn: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    app.createTourPackage();
                    break;
                case 2:
                    app.viewAndUpdateTours();
                    break;
                case 3:
                    app.deleteTour();
                    break;
                case 4:
                    app.addHotel();
                    break;
                case 5:
                    app.updateAndManageHotels();
                    break;
                case 6:
                    app.removeHotel();
                    break;
                case 7:
                    app.customerBooking();
                    break;
                case 8:
                    app.manageBookings();
                    break;
                case 9:
                    app.generateReports();
                    break;
                case 10:
                    app.dataManagement();
                    break;
                case 11:
                    System.out.println("Đang thoát......");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!!!");
            }
        }
    }
}
