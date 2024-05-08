package Model;

import Entity.Hotel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import View.Views;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HotelManager {

    private ArrayList<Hotel> hotels;
    private static int hotelCounter;
    private static final String FILE_NAME = "src/Output/Hotel.dat";
    Views views = new Views();

    public HotelManager() {
        // Gọi phương thức loadHotelData() để tải dữ liệu về khách sạn và gán vào biến hotels
        hotels = loadHotelData();
        // Gọi phương thức initializeHotelCounter() để thiết lập giá trị ban đầu cho biến hotelCounter
        initializeHotelCounter();
    }

    private void initializeHotelCounter() {
        if (!hotels.isEmpty()) {
            // Tạo một danh sách Integer để chứa các giá trị chuyển đổi từ ID của khách sạn
            List<Integer> hotelIds = new ArrayList<>();

            // Lặp qua danh sách khách sạn và thêm giá trị chuyển đổi từ ID vào danh sách
            for (Hotel hotel : hotels) {
                hotelIds.add(Integer.parseInt(hotel.getHotelId().substring(1)));
            }

            // Sử dụng Collections.max để tìm giá trị lớn nhất trong danh sách số nguyên
            int maxId = Collections.max(hotelIds);

            // Gán giá trị tìm được cộng thêm 1 vào biến hotelCounter
            hotelCounter = maxId + 1;
        } else {
            // Nếu danh sách khách sạn trống, gán giá trị ban đầu là 1
            hotelCounter = 1;
        }
    }

    // Phương thức để thêm mới một khách sạn vào danh sách
    public void addHotel() {
        // Khởi tạo đối tượng Scanner để đọc dữ liệu từ người dùng
        Scanner scanner = new Scanner(System.in);

        // Khai báo biến để lưu trữ ID của khách sạn
        String hotelId;

        // Tạo ID mới tự động và kiểm tra xem ID đã tồn tại trong danh sách chưa
        do {
            hotelId = generateHotelId();
        } while (hotelIdExists(hotelId));

        // Hiển thị thông báo yêu cầu nhập thông tin khách sạn cho ID vừa được tạo
        System.out.println("Enter Hotel information for ID " + hotelId + ":");

        // Khai báo và khởi tạo biến để lưu trữ tên khách sạn
        String hotelName;

        // Yêu cầu người dùng nhập tên khách sạn và kiểm tra tính hợp lệ của tên
        do {
            System.out.print("Hotel Name: ");
            hotelName = scanner.nextLine().trim();
            if (hotelName.isEmpty()) {
                System.out.println("Hotel name cannot be empty. Please enter a valid name.");
            }
        } while (hotelName.isEmpty());

        // Khai báo và khởi tạo biến để lưu trữ số lượng phòng khách sạn
        int roomAvailable;

        // Yêu cầu người dùng nhập số lượng phòng và kiểm tra tính hợp lệ
        do {
            System.out.print("Rooms Available: ");
            String roomInput = scanner.nextLine().trim();

            // Kiểm tra xem người dùng đã nhập một số nguyên dương hợp lệ hay không
            if (!roomInput.isEmpty() && roomInput.matches("\\d+")) {
                roomAvailable = Integer.parseInt(roomInput);//ParseInt chuyển đổi kiểu chuỗi thành kiểu integer
                if (roomAvailable <= 0) {
                    System.out.println("Invalid input. Please enter a valid positive number for rooms.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number for rooms.");
                roomAvailable = -1;  // Đặt giá trị mặc định để lặp lại vòng lặp
            }
        } while (roomAvailable <= 0);

        // Yêu cầu nhập địa chỉ và thiết lập giá trị mặc định nếu địa chỉ trống
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) {
            address = "N/A";
        }

        // Yêu cầu nhập số điện thoại và thiết lập giá trị mặc định nếu số điện thoại trống
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        if (phone.isEmpty()) {
            phone = "N/A";
        }

        // Khai báo biến để lưu trữ đánh giá và khởi tạo giá trị là null
        Double rating = null;

        // Yêu cầu nhập đánh giá và kiểm tra tính hợp lệ
        do {
            System.out.print("Rating (1 - 6): ");
            String ratingInput = scanner.nextLine().trim();

            // Kiểm tra xem người dùng đã nhập một giá trị đánh giá hợp lệ hay không
            if (!ratingInput.isEmpty() && ratingInput.matches("[1-6](\\.\\d+)?")) {
                rating = Double.parseDouble(ratingInput);//PareseDouble chuyển đổi chuỗi thành kiểu double
                if (rating < 1 || rating > 6) {
                    System.out.println("Invalid input. Rating must be between 1 and 5.");
                    rating = null;  // Đặt giá trị mặc định để lặp lại vòng lặp
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number for rating.");
                rating = null;  // Đặt giá trị mặc định để lặp lại vòng lặp
            }
        } while (rating == null);

        // Tạo đối tượng Hotel mới và thêm vào danh sách khách sạn
        Hotel newHotel = new Hotel(hotelId, hotelName, roomAvailable, address, phone, rating);
        hotels.add(newHotel);

        // Tăng giá trị biến đếm khách sạn và lưu dữ liệu khách sạn
        hotelCounter++;
        saveHotelData(hotels);

        // Hiển thị thông báo thành công
        System.out.println("Hotel added successfully!");
    }

    private boolean hotelIdExists(String hotelId) {
        for (Hotel hotel : hotels) {
            if (hotel.getHotelId().equals(hotelId)) {
                return true;
            }
        }
        return false;
    }

    private String generateHotelId() {//StringFormat
        return "H" + String.format("%02d", hotelCounter);
    }

    public void checkHotelExists() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Hotel ID to check: ");
        String hotelId = scanner.nextLine();

        if (hotelExists(hotelId)) {
            System.out.println("Exist Hotel");
        } else {
            System.out.println("No Hotel Found!");
        }
    }

    private boolean hotelExists(String hotelId) {
        for (Hotel hotel : hotels) {
            if (hotel.getHotelId().equalsIgnoreCase(hotelId)) {
                return true;
            }
        }
        return false;
    }

    public void updateHotelInfo() {
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập ID của khách sạn cần cập nhật
        System.out.print("Enter Hotel ID to update: ");
        String hotelIdToUpdate = scanner.nextLine();

        // Kiểm tra xem khách sạn có tồn tại không
        if (!hotelExists(hotelIdToUpdate)) {
            System.out.println("Hotel with ID " + hotelIdToUpdate + " does not exist!");
            return;
        }

        // Lấy thông tin của khách sạn cần cập nhật
        Hotel existingHotel = getHotelById(hotelIdToUpdate);

        // Hiển thị thông báo yêu cầu nhập thông tin mới
        System.out.println("Enter new information:");

        // Yêu cầu người dùng nhập tên khách sạn và kiểm tra tính hợp lệ của tên
        System.out.print("Hotel Name: ");
        String newHotelName = scanner.nextLine();
        if (!newHotelName.isEmpty()) {
            existingHotel.setHotelName(newHotelName);
        }

        // Yêu cầu người dùng nhập số lượng phòng và kiểm tra tính hợp lệ
        System.out.print("Rooms Available: ");
        String roomInput = scanner.nextLine();
        if (!roomInput.isEmpty() && roomInput.matches("\\d+")) {
            int newRoomAvailable = Integer.parseInt(roomInput);
            if (newRoomAvailable > 0) {
                existingHotel.setRoomAvailable(newRoomAvailable);
            } else {
                System.out.println("Invalid input. Please enter a valid positive number for rooms.");
            }
        }

        // Yêu cầu người dùng nhập địa chỉ và giữ nguyên giá trị hiện tại nếu địa chỉ trống
        System.out.print("Address: ");
        String newAddress = scanner.nextLine();
        if (!newAddress.isEmpty()) {
            existingHotel.setAddress(newAddress);
        }

        // Yêu cầu người dùng nhập số điện thoại và giữ nguyên giá trị hiện tại nếu số điện thoại trống
        System.out.print("Phone: ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            existingHotel.setPhone(newPhone);
        }

        // Yêu cầu người dùng nhập đánh giá và giữ nguyên giá trị hiện tại nếu đánh giá trống
        System.out.print("Rating (0 - 6): ");
        String ratingInput = scanner.nextLine();
        if (!ratingInput.isEmpty() && ratingInput.matches("[0-6](\\.\\d+)?")) {
            double newRating = Double.parseDouble(ratingInput);
            existingHotel.setRating(newRating);
        } else {
            System.out.println("Invalid input. Please enter a valid number for rating.");
        }

        // Hiển thị thông báo thành công
        System.out.println("Hotel information updated successfully!");
    }

    // Phương thức để xóa một khách sạn từ danh sách
    public void deleteHotel() {
        // Tạo đối tượng Scanner để đọc dữ liệu từ người dùng
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập ID của khách sạn cần xóa
        System.out.print("Enter Hotel ID to delete: ");
        String hotelId = scanner.nextLine();

        // Lấy đối tượng khách sạn cần xóa thông qua phương thức getHotelById
        Hotel hotelToDelete = getHotelById(hotelId);

        // Kiểm tra xem khách sạn có tồn tại không
        if (hotelToDelete == null) {
            System.out.println("Hotel does not exist!");
            return;
        }

        // Yêu cầu người dùng xác nhận việc xóa khách sạn
        System.out.print("Are you sure you want to delete this hotel? (yes/no): ");
        String confirm = scanner.nextLine();

        // Kiểm tra xác nhận của người dùng
        if (confirm.equalsIgnoreCase("yes")) {
            // Nếu xác nhận là "yes", xóa khách sạn khỏi danh sách
            hotels.remove(hotelToDelete);

            // Lưu lại dữ liệu sau khi xóa
            saveHotelData(hotels);

            // Hiển thị thông báo xóa thành công
            System.out.println("Hotel deleted successfully!");
        } else {
            // Nếu xác nhận không là "yes", thông báo hủy bỏ xóa
            System.out.println("Deletion canceled.");
        }
    }

    public void searchHotel() {
        Scanner scanner = new Scanner(System.in);
        views.searchMenu();
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchHotelById();
                break;
            case 2:
                searchHotelByName();
                break;
            default:
                System.out.println("Invalid choice! Please enter 1 or 2.");
        }
    }

    // Phương thức để tìm kiếm khách sạn theo ID
    private void searchHotelById() {
        // Tạo đối tượng Scanner để đọc dữ liệu từ người dùng
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập ID của khách sạn cần tìm kiếm
        System.out.print("Enter Hotel ID to search: ");
        String searchId = scanner.nextLine();

        // Tạo một danh sách để lưu trữ kết quả tìm kiếm
        ArrayList<Hotel> result = new ArrayList<>();

        // Duyệt qua danh sách khách sạn và thêm vào danh sách kết quả nếu ID khớp
        for (Hotel hotel : hotels) {
            if (hotel.getHotelId().equalsIgnoreCase(searchId)) {
                result.add(hotel);
            }
        }

        // Hiển thị kết quả tìm kiếm
        displaySearchResult(result);
    }

    // Phương thức để tìm kiếm khách sạn theo tên
    private void searchHotelByName() {
        // Tạo đối tượng Scanner để đọc dữ liệu từ người dùng
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập tên của khách sạn cần tìm kiếm
        System.out.print("Enter Hotel Name to search: ");
        String searchName = scanner.nextLine();

        // Tạo một danh sách để lưu trữ kết quả tìm kiếm
        ArrayList<Hotel> result = new ArrayList<>();

        // Duyệt qua danh sách khách sạn và thêm vào danh sách kết quả nếu tên khớp
        for (Hotel hotel : hotels) {
            if (hotel.getHotelName().equalsIgnoreCase(searchName)) {
                result.add(hotel);
            }
        }

        // Hiển thị kết quả tìm kiếm
        displaySearchResult(result);
    }

    // Phương thức để hiển thị kết quả tìm kiếm khách sạn
    private void displaySearchResult(ArrayList<Hotel> result) {
        // Kiểm tra xem danh sách kết quả có trống không
        if (result.isEmpty()) {
            System.out.println("No matching hotels found.");
        } else {
            // Sắp xếp danh sách kết quả theo ID khách sạn theo thứ tự giảm dần
            Collections.sort(result, Comparator.comparing(Hotel::getHotelId).reversed());

            // Hiển thị tiêu đề kết quả tìm kiếm
            System.out.println("Search Result:");

            // Duyệt qua danh sách kết quả và hiển thị thông tin của từng khách sạn
            for (Hotel hotel : result) {
                hotel.display();
            }
        }
    }

    // Phương thức để hiển thị danh sách khách sạn
    // Hiển thị danh sách tất cả các khách sạn
    void displayHotelList() {
        if (!hotels.isEmpty()) {
            // Sắp xếp danh sách khách sạn theo ID trước khi hiển thị
            hotels.sort(Comparator.comparing(Hotel::getHotelId));

            System.out.println("Hotel List:");

            // Hiển thị tiêu đề cho danh sách khách sạn
            System.out.printf("| %-10s | %-20s | %-20s | %-80s | %-15s | %-5s |%n",
                    "Hotel ID", "Hotel Name", "Rooms Available", "Address", "Phone", "Rating");

            // Hiển thị dòng phân tách
            printSeparatorLine();

            // Hiển thị thông tin từng khách sạn trong danh sách
            for (Hotel hotel : hotels) {
                int addressFieldWidth = 80; // Điều chỉnh giá trị này dựa trên nhu cầu của bạn
                // Định dạng địa chỉ để hiển thị đẹp hơn
                String formattedAddress = formatLongString(hotel.getAddress(), addressFieldWidth);

                // Hiển thị thông tin khách sạn
                System.out.printf("| %-10s | %-20s | %-20s | %-80s | %-15s | %-5s  |%n",
                        hotel.getHotelId(), hotel.getHotelName(), hotel.getRoomAvailable(),
                        formattedAddress, hotel.getPhone(), hotel.getRating());
                // Hiển thị dòng phân tách
                printSeparatorLine();
            }
        } else {
            System.out.println("No hotels available.");
        }
    }

// Định dạng một chuỗi dài để hiển thị đẹp hơn
    private String formatLongString(String longString, int fieldWidth) {
        StringBuilder result = new StringBuilder();
        int index = 0;

        while (index < longString.length()) {
            int endIndex = Math.min(index + fieldWidth, longString.length());
            String substring = longString.substring(index, endIndex);
            int actualWidth = substring.length(); // Xem xét chiều rộng thực tế của chuỗi con
            result.append(String.format("%-" + actualWidth + "s", substring));

            // Nếu có nhiều hơn một dòng, thêm dòng mới
            if (index + fieldWidth < longString.length()) {
                result.append("\n");
            }
            index += fieldWidth;
        }

        return result.toString();
    }

// Hiển thị dòng phân tách
    private void printSeparatorLine() {
        System.out.println("+------------+----------------------+----------------------+----------------------------------------------------------------------------------+-----------------+--------+");
    }

// Phương thức để lấy đối tượng Hotel dựa trên ID
    private Hotel getHotelById(String hotelId) {
        // Duyệt qua danh sách khách sạn
        for (Hotel hotel : hotels) {
            // So sánh ID và trả về đối tượng Hotel nếu khớp
            if (hotel.getHotelId().equalsIgnoreCase(hotelId)) {
                return hotel;
            }
        }

        // Trả về null nếu không tìm thấy
        return null;
    }

    // Phương thức để nạp dữ liệu khách sạn từ tệp vào danh sách
    public static ArrayList<Hotel> loadHotelData() {
        // Tạo một danh sách để lưu trữ các đối tượng khách sạn
        ArrayList<Hotel> hotels = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            // Đọc từng dòng từ tệp
            String line;
            while ((line = reader.readLine()) != null) {
                // Tách thông tin của khách sạn từ dòng dữ liệu
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Tạo đối tượng Hotel từ thông tin tách được và thêm vào danh sách
                    Hotel hotel = new Hotel(parts[0], parts[1], Integer.parseInt(parts[2]),
                            parts[3], parts[4], Double.parseDouble(parts[5]));
                    hotels.add(hotel);
                } else {
                    // In thông báo lỗi nếu định dạng dữ liệu không đúng
                    System.err.println("Invalid data format in the file: " + line);
                }
            }
        } catch (IOException e) {
            // Xử lý ngoại lệ IOException (đọc tệp)
            // Ghi log nếu cần hoặc in thông báo lỗi cụ thể
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Xử lý ngoại lệ NumberFormatException (ép kiểu)
            // Ghi log nếu cần hoặc in thông báo lỗi cụ thể
            System.err.println("Error parsing integer from file.");
            e.printStackTrace();
        }

        // Trả về danh sách các đối tượng khách sạn
        return hotels;
    }

    // Phương thức để lưu dữ liệu khách sạn từ danh sách vào tệp
    public static void saveHotelData(ArrayList<Hotel> hotels) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Duyệt qua danh sách khách sạn và ghi thông tin của mỗi khách sạn vào tệp
            for (Hotel hotel : hotels) {
                // Tạo chuỗi dữ liệu từ thông tin của khách sạn và thêm vào tệp
                String line = hotel.getHotelId() + "," + hotel.getHotelName() + "," + hotel.getRoomAvailable()
                        + "," + hotel.getAddress() + "," + hotel.getPhone() + "," + hotel.getRating();
                writer.write(line);
                writer.newLine(); // Thêm dòng mới sau mỗi khách sạn để tạo dòng mới trong tệp
            }
        } catch (IOException e) {
            // Xử lý ngoại lệ IOException (ghi tệp)
            // Ghi log nếu cần hoặc in thông báo lỗi cụ thể
            e.printStackTrace();
        }
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            views.mainMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addHotel();
                    break;
                case 2:
                    checkHotelExists();
                    break;
                case 3:
                    updateHotelInfo();
                    break;
                case 4:
                    deleteHotel();
                    break;
                case 5:
                    searchHotel();
                    break;
                case 6:
                    displayHotelList();
                    break;
                case 7:
                    views.exitProgram();
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 7.");
            }
        } while (choice != 7);
    }

}
