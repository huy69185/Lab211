package Control;

import Model.User;
import Model.InfoCustomer;
import Model.CrewAssignment;
import Model.Reservation;
import Model.Flight;
import Model.FlightTimeInfo;
import Model.Staff;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlightManagement {

    private ArrayList<Flight> flights;
    private List<FlightTimeInfo> flightTimeInfoList;
    private ArrayList<Reservation> reservations;
    private ArrayList<CrewAssignment> crewAssignments;
    private ArrayList<User> users;
    private ArrayList<InfoCustomer> infoCustomers;
    private ArrayList<Staff> staffs;
    private Set<String> assignedNormalSeats;
    private Set<String> assignedVIPSeats;
    private Set<String> assignedSeats;
    Set<String> adminReservedSeats;
    private User loggedInUser;
    InfoCustomer infoCus = new InfoCustomer();

    public FlightManagement() {
        flights = new ArrayList<>();
        reservations = new ArrayList<>();
        crewAssignments = new ArrayList<>();
        users = new ArrayList<>();
        infoCustomers = new ArrayList<>();
        staffs = new ArrayList<>();
        assignedNormalSeats = new HashSet<>();
        assignedVIPSeats = new HashSet<>();
        assignedSeats = new HashSet<>();
        adminReservedSeats = new HashSet<>();
        // Thêm tài khoản admin
        users.add(new User("admin", "123456", true));
        users.add(new User("staff", "123456", true));
        users.add(new User("1", "1", true));
        flightTimeInfoList = new ArrayList<>();
        loadFlightTimeInfoFromFile("src/Database/flightTime.txt");
        loadCrewFromFile("src/Output/crewInfo.txt");
        loadStaffFromFile("src/Database/staffInfo.txt");
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("Đăng nhập vào hệ thống thành công!");
                return true;
            }
        }

        loggedInUser = null;
        System.out.println("Đăng nhập vào hệ thống thất bại!!!");
        return false;
    }

    public void logout() {
        loggedInUser = null;
        System.out.println("Đăng xuất thành công!");
    }

    public void addFlight() {
        if (crewAssignments.size() == flights.size()) {
            System.out.println("Không đủ phi hành đoàn để thực hiện chuyến bay này. Hãy thêm phi hành đoàn trước khi thêm chuyến bay.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Nhập thông tin chuyến bay:");
        String flightNumberPattern = "^F[0-9A-Za-z]{4}$";
        Pattern pattern = Pattern.compile(flightNumberPattern);
        String flightNumber;

        while (true) {
            System.out.print("Mã chuyến bay (Fxyzt): ");
            flightNumber = scanner.nextLine().trim();
            Matcher matcher = pattern.matcher(flightNumber);

            if (!matcher.matches()) {
                System.out.println("Mã chuyến bay không đúng cú pháp. Vui lòng nhập lại (e.g., F1234).");
            } else if (flightExists(flightNumber)) {
                System.out.println("Mã chuyến bay đã được sử dụng. Vui lòng chọn một mã chuyến bay khác.");
            } else {
                break;
            }
        }

        System.out.println("Thông tin điểm đi có sẵn:");
        List<String> printedDepartureCities = new ArrayList<>();

        for (FlightTimeInfo flightTimeInfoList : flightTimeInfoList) {
            String departureCity = flightTimeInfoList.getDepartureCity();
            if (!printedDepartureCities.contains(departureCity)) {
                printedDepartureCities.add(departureCity);
            }
        }

        System.out.println("+----------------------+");
        for (int i = 0; i < printedDepartureCities.size(); i++) {
            String departureCityShow = printedDepartureCities.get(i);
            System.out.printf("| %-20s |%n", departureCityShow);
            System.out.println("+----------------------+");
        }

        String departureCity;
        while (true) {
            System.out.print("Nơi khởi hành: ");
            departureCity = scanner.nextLine();
            FlightTimeInfo flightDepartureCityInfo = findMatchingFlightPlaceInfo(departureCity);

            if (flightDepartureCityInfo == null) {
                System.out.println("Không tìm thấy địa điểm trên!! Vui lòng nhập lại!");
            } else {
                break;
            }
        }

        String destinationCity;
        System.out.println("Thông tin điểm đến có sẵn:");
        List<String> printedDestinationCity = new ArrayList<>();

        for (FlightTimeInfo flightTimeInfoList : flightTimeInfoList) {
            destinationCity = flightTimeInfoList.getDestinationCity();
            if (!printedDestinationCity.contains(destinationCity)) {
                printedDestinationCity.add(destinationCity);
            }
        }

        System.out.println("+----------------------+");
        for (int i = 0; i < printedDestinationCity.size(); i++) {
            String destinationCityShow = printedDestinationCity.get(i);
            System.out.printf("| %-20s |%n", destinationCityShow);
            System.out.println("+----------------------+");
        }

        while (true) {
            System.out.print("Điểm đến: ");
            destinationCity = scanner.nextLine();

            if (!departureCity.equalsIgnoreCase(destinationCity)) {
                break;
            } else {
                System.out.println("Thành phố đến không thể là nơi khởi hành");
            }
        }

        FlightTimeInfo flightDestinationCityInfo = findMatchingFlightPlaceInfo(destinationCity);

        if (flightDestinationCityInfo == null) {
            System.out.println("Không tìm thấy địa điểm trên!! Vui lòng nhập lại!");
            return;
        }

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
//        Date parsedDate = null;
//        Date currentDate = new Date();
//
//        while (parsedDate == null || parsedDate.before(currentDate)) {
//            System.out.print("Ngày khởi hành (yyyy-MM-dd): ");
//            String dateFlight = scanner.nextLine();
//
//            try {
//                parsedDate = dateFormat.parse(dateFlight);
//
//                if (!isValidDate(parsedDate) || parsedDate.before(currentDate)) {
//                    System.out.println("Ngày không hợp lệ hoặc trong quá khứ. Vui lòng nhập lại!!!");
//                    parsedDate = null;
//                }
//            } catch (ParseException e) {
//                System.out.println("Ngày không đúng cú pháp. Vui lòng nhập lại!!!");
//            }
//        }
        LocalDate currentDate = LocalDate.now();
        LocalDate parsedDate = null;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (parsedDate == null || parsedDate.isBefore(currentDate)) {
            System.out.print("Ngày khởi hành (yyyy-MM-dd): ");
            String dateFlight = scanner.nextLine();

            try {
                parsedDate = LocalDate.parse(dateFlight, dateFormat);

                if (!isValidDate(parsedDate) || parsedDate.isBefore(currentDate)) {
                    System.out.println("Ngày không hợp lệ hoặc trong quá khứ. Vui lòng nhập lại!!!");
                    parsedDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Ngày không đúng cú pháp. Vui lòng nhập lại!!!");
            }
        }

        FlightTimeInfo flightTimeInfo = findMatchingFlightTimeInfo(departureCity, destinationCity);

        if (flightTimeInfo == null) {
            System.out.println("Không tìm thấy thời gian bay của chuyến bay trên!!! Vui lòng nhập lại | thêm hoặc cập nhật!!!");
            return;
        }

        departureCity = flightTimeInfo.getDepartureCity();
        destinationCity = flightTimeInfo.getDestinationCity();

        double flightTime = flightTimeInfo.getFlightTime();
        System.out.println("Thời gian bay: " + flightTime + "giờ");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);
        String departureTime = null;

        while (departureTime == null) {
            System.out.print("Thời gian khởi hành (HH:mm): ");
            departureTime = scanner.nextLine();

            try {
                timeFormat.parse(departureTime);
            } catch (ParseException e) {
                System.out.println("Thời gian nhập vào không đúng cú pháp. Vui lòng nhập lại!!!");
                departureTime = null;
            }
        }

        String arrivalTime = calculateArrivalTime(departureTime, flightTime);

        int availableVIPSeats = 0;
        int availableNormalSeats = 0;

        while (true) {
            System.out.print("Số ghế hạng thương gia: ");
            System.out.flush();

            try {
                availableVIPSeats = Integer.parseInt(scanner.nextLine());
                if (availableVIPSeats < 0) {
                    System.out.println("Số ghế không thể là số âm. Vui lòng nhập lại!!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Số ghế phải là số nguyên. Vui lòng nhập lại!!!");
            }
        }

        while (true) {
            System.out.print("Số ghế thường: ");
            System.out.flush();
            try {
                availableNormalSeats = Integer.parseInt(scanner.nextLine());

                if (availableNormalSeats < 0) {
                    System.out.println("Số ghế không thể là số âm. Vui lòng nhập lại!!!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Số ghế phải là số nguyên. Vui lòng nhập lại!!!");
            }
        }

        Flight flight = new Flight(flightNumber, departureCity, destinationCity, parsedDate.toString(), departureTime, arrivalTime, availableVIPSeats, availableNormalSeats);
        flights.add(flight);
        System.out.println("Thêm chuyến bay thành công!!!");
    }

//    public static boolean isValidDate(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int year = calendar.get(Calendar.YEAR);
//
//        int minDay = 1;
//        int maxDay = 31;
//        int minMonth = 1;
//        int maxMonth = 12;
//        int minYear = 1903;
//        int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1000;
//
//        return (day >= minDay && day <= maxDay)
//                && (month >= minMonth && month <= maxMonth)
//                && (year >= minYear && year <= maxYear);
//    }
    public static boolean isValidDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1903, 1, 1);
        LocalDate maxDate = LocalDate.now().plusYears(2000);
        return !date.isBefore(minDate) && !date.isAfter(maxDate);
    }

    private FlightTimeInfo findMatchingFlightPlaceInfo(String place) {
        for (FlightTimeInfo flightTimeInfo : flightTimeInfoList) {
            if (partialMatch(flightTimeInfo.getDepartureCity(), place) || partialMatch(flightTimeInfo.getDestinationCity(), place)) {
                return flightTimeInfo;
            }
        }
        return null;
    }

    private FlightTimeInfo findMatchingFlightTimeInfo(String departureCity, String destinationCity) {
        for (FlightTimeInfo flightTimeInfo : flightTimeInfoList) {
            if (partialMatch(flightTimeInfo.getDepartureCity(), departureCity) && partialMatch(flightTimeInfo.getDestinationCity(), destinationCity)) {
                return flightTimeInfo;
            }
        }
        return null;
    }

    private boolean partialMatch(String str1, String str2) {
        return str1.toLowerCase().contains(str2.toLowerCase()) || str2.toLowerCase().contains(str1.toLowerCase()) || str1.toUpperCase().contains(str2.toLowerCase()) || str1.toLowerCase().contains(str2.toUpperCase()) || str2.toLowerCase().contains(str1.toUpperCase()) || str2.toUpperCase().contains(str1.toLowerCase()) || str2.toUpperCase().contains(str1.toUpperCase());
    }

    private String calculateArrivalTime(String departureTime, double flightTime) {

        String[] departureTimeParts = departureTime.split(":");
        if (departureTimeParts.length != 2) {
            throw new IllegalArgumentException("Định dạng thời gian khởi hành không hợp lệ!!!");
        }

        int departureHours = Integer.parseInt(departureTimeParts[0]);
        int departureMinutes = Integer.parseInt(departureTimeParts[1]);

        int totalMinutesDeparture = departureHours * 60 + departureMinutes;

        int totalMinutesArrival = totalMinutesDeparture + (int) (flightTime * 60);

        int arrivalHours = totalMinutesArrival / 60;
        int arrivalMinutes = totalMinutesArrival % 60;

        return String.format("%02d:%02d", arrivalHours, arrivalMinutes);
    }

    public void makeReservation() {
        printAvailableFlight();
        if (flights.isEmpty()) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Đặt vé và đặt chỗ:");
        String passengerName;
        System.out.println("Tên hành khách:");
        while (true) {
            passengerName = scanner.nextLine();
            if (passengerName.matches("[A-Za-z]+")) {
                break;
            } else {
                System.out.println("Không hợp lệ. Vui lòng nhập lại:");
            }
        }
        String identityCard;

        System.out.println("CCCD: ");
        while (true) {
            identityCard = scanner.nextLine();
            boolean isMatch = false;
            if (identityCard.matches("\\d{12}")) {
                for (InfoCustomer infoCustomer : infoCustomers) {
                    if (identityCard.equals(infoCustomer.getIdentityId()) && passengerName.equals(infoCustomer.getNameCus())) {
                        isMatch = true;
                        return;
                    }
                }
                if (isMatch) {
                    System.out.println("Số căn cước của bạn đã mua vé ở chúng tôi với cái tên khác");
                } else {
                    break;
                }
            } else {
                System.out.println("Không hợp lệ. Vui lòng nhập lại:");
            }
        }

        InfoCustomer infoCustomer = new InfoCustomer(identityCard, identityCard);
        infoCustomers.add(infoCustomer);
        System.out.print("Thông tin liên hệ: ");
        String contactDetails = scanner.nextLine();
        System.out.print("Mã chuyến bay: ");
        String flightNumber = scanner.nextLine();
        Flight selectedFlight = null;

        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                selectedFlight = flight;
                break;
            }
        }

        if (selectedFlight == null) {
            System.out.println("Không thể đặt vé cho chuyến bay này hoặc chuyến bay không tồn tại.");
            return;
        }
        System.out.print("Loại vé (VIP/Normal): ");
        String ticketType;
        while (true) {
            ticketType = scanner.nextLine().toLowerCase();
            if (ticketType.equalsIgnoreCase("vip") || ticketType.equalsIgnoreCase("normal")) {
                break;
            } else {
                System.out.println("Không hợp lệ. Vui lòng nhập lại:");
            }
        }
        boolean isVIP = ticketType.equalsIgnoreCase("vip");

        if (loggedInUser != null) {
            if (ticketType.equals("vip") && selectedFlight.getAvailableVIPSeats() > 0) {
                String regex = "V\\d+";
                Pattern pattern = Pattern.compile(regex);
                if (adminReservedSeats.size() > 0) {
                    System.out.println("Danh sách chỗ ngồi VIP đã được đặt cho chuyến bay " + flightNumber + ":");
                    for (String seatNumber : adminReservedSeats) {
                        if (seatNumber.startsWith("V") && !checkSeatRightFlight(flightNumber, seatNumber)) {
                            System.out.print(seatNumber + " ");
                        }
                    }
                }
                int staticVipSeat;
                int seatPlus = 0;
                for (String seatNumber : adminReservedSeats) {
                    if (seatNumber.startsWith("V") && !checkSeatRightFlight(flightNumber, seatNumber)) {
                        seatPlus++;
                    }
                }
                staticVipSeat = selectedFlight.getAvailableVIPSeats() + seatPlus;
                while (true) {
                    System.out.print("Chọn chỗ ngồi (V1-V" + staticVipSeat + "): ");
                    String seatVIPNumber = scanner.nextLine();
                    Matcher matcher = pattern.matcher(seatVIPNumber);
                    if (matcher.matches()) {
                        int seatNumber;
                        try {
                            seatNumber = Integer.parseInt(seatVIPNumber.substring(1));
                        } catch (NumberFormatException e) {
                            System.out.println("Mã vé không hợp lệ. Vui lòng nhập lại!");
                            return;
                        }

                        if (seatNumber <= selectedFlight.getAvailableVIPSeats()) {
                            if (checkSeatAvailable(flightNumber, seatVIPNumber)) {
                                String reservationID = generateVIPReservationID();
                                Reservation reservation = new Reservation(reservationID, passengerName, identityCard, contactDetails, flightNumber, isVIP, seatVIPNumber);
                                reservations.add(reservation);
                                adminReservedSeats.add(seatVIPNumber);
                                selectedFlight.setAvailableVIPSeats(selectedFlight.getAvailableVIPSeats() - 1);
                                System.out.println("Đặt vé VIP thành công!\n");
                                System.out.println("Thông tin vé:");
                                System.out.println("Mã chuyến bay: " + selectedFlight.getFlightNumber());
                                System.out.println("Nơi khởi hành: " + selectedFlight.getDepartureCity());
                                System.out.println("Nơi đến: " + selectedFlight.getDestinationCity());
                                System.out.println("Mã đặt vé: " + reservationID);
                                System.out.println("Mã ghế: " + seatVIPNumber);
                                return;
                            } else {
                                System.out.println("Mã ghế này đã có người đặt. Vui lòng chọn ghế khác.");
                            }
                        } else {
                            System.out.println("Mã ghế không hợp lệ. Vui lòng nhập lại!");
                        }
                    } else {
                        System.out.println("Mã vé không đúng định dạng. Vui lòng nhập lại!");
                    }
                }
            } else if (ticketType.equals("normal") && selectedFlight.getAvailableNormalSeats() > 0) {
                String regex = "R\\d+";
                Pattern pattern = Pattern.compile(regex);
                if (adminReservedSeats.size() > 0) {
                    System.out.println("Danh sách chỗ ngồi thuong đã được đặt cho chuyến bay " + flightNumber + ":");
                    for (String seatNumber : adminReservedSeats) {
                        if (seatNumber.startsWith("R") && !checkSeatRightFlight(flightNumber, seatNumber)) {
                            System.out.print(seatNumber + " ");
                        }
                    }
                }
                int staticNorSeat;
                int seatPlusNor = 0;
                staticNorSeat = selectedFlight.getAvailableNormalSeats() + seatPlusNor;
                for (String seatNumber : adminReservedSeats) {
                    if (seatNumber.startsWith("R") && !checkSeatRightFlight(flightNumber, seatNumber)) {
                        seatPlusNor++;
                    }
                }
                while (true) {
                    System.out.print("Chọn chỗ ngồi (R1-R" + staticNorSeat + "): ");
                    String seatNormalNumber = scanner.nextLine();
                    Matcher matcher = pattern.matcher(seatNormalNumber);
                    if (matcher.matches()) {
                        int seatNumber;
                        try {
                            seatNumber = Integer.parseInt(seatNormalNumber.substring(1));
                        } catch (NumberFormatException e) {
                            System.out.println("Mã ghế không hợp lệ. Vui lòng nhập lại!");
                            return;
                        }
                        if (seatNumber <= selectedFlight.getAvailableNormalSeats()) {
                            if (checkSeatAvailable(flightNumber, seatNormalNumber)) {
                                String reservationID = generateReservationID();
                                Reservation reservation = new Reservation(reservationID, passengerName, identityCard, contactDetails, flightNumber, false, seatNormalNumber);
                                reservations.add(reservation);
                                adminReservedSeats.add(seatNormalNumber);
                                selectedFlight.setAvailableNormalSeats(selectedFlight.getAvailableNormalSeats() - 1);
                                System.out.println("Đặt vé VIP thành công!\n");
                                System.out.println("Thông tin vé:");
                                System.out.println("Mã chuyến bay: " + selectedFlight.getFlightNumber());
                                System.out.println("Nơi khởi hành: " + selectedFlight.getDepartureCity());
                                System.out.println("Nơi đến: " + selectedFlight.getDestinationCity());
                                System.out.println("Mã đặt vé: " + reservationID);
                                System.out.println("Mã ghế: " + seatNormalNumber);
                                return;
                            } else {
                                System.out.println("Mã ghế này đã có người đặt. Vui lòng chọn ghế khác.");
                            }
                        } else {
                            System.out.println("Mã ghế không hợp lệ. Vui lòng nhập lại!");
                        }
                    } else {
                        System.out.println("Mã ghế không đúng định dạng. Vui lòng nhập lại!");
                    }
                }
            } else {
                System.out.println("Không thể đặt vé cho loại chỗ này hoặc loại chỗ không còn trống.");
            }

        } else if (loggedInUser == null) {
            if ((isVIP && selectedFlight.getAvailableVIPSeats() > 0) || (!isVIP && selectedFlight.getAvailableNormalSeats() > 0)) {

                String seatNumber;
                if (isVIP) {
                    if (selectedFlight.getAvailableVIPSeats() > 0) {
                        seatNumber = generateVIPRandomSeat(selectedFlight.getAvailableVIPSeats(), adminReservedSeats, assignedSeats);
                    } else {
                        System.out.println("Không còn vé VIP trống.");
                        return;
                    }
                } else {
                    if (selectedFlight.getAvailableNormalSeats() > 0) {
                        seatNumber = generateRandomSeat(selectedFlight.getAvailableNormalSeats(), adminReservedSeats, assignedSeats);
                    } else {
                        System.out.println("Không còn vé thường trống.");
                        return;
                    }
                }

                String reservationID = isVIP ? generateVIPReservationID() : generateReservationID();
                Reservation reservation = new Reservation(reservationID, passengerName, identityCard, contactDetails, flightNumber, isVIP, seatNumber);
                reservations.add(reservation);

                if (isVIP) {
                    selectedFlight.setAvailableVIPSeats(selectedFlight.getAvailableVIPSeats() - 1);
                    System.out.println("Đặt vé thành công!");
                    System.out.println("Mã đặt vé của bạn là: " + reservationID);
                    System.out.println("Mã ghế của bạn là " + seatNumber);
                    adminReservedSeats.add(seatNumber);
                } else {
                    selectedFlight.setAvailableNormalSeats(selectedFlight.getAvailableNormalSeats() - 1);
                    System.out.println("Đặt vé thành công!");
                    System.out.println("Mã đặt vé của bạn là: " + reservationID);
                    System.out.println("Mã ghế của bạn là " + seatNumber);
                    adminReservedSeats.add(seatNumber);

                }
            } else {
                System.out.println("Không thể đặt vé cho loại chỗ này hoặc loại chỗ không còn trống.");
            }
        }
    }

    private boolean checkSeatAvailable(String flightNum, String seat) {
        for (Reservation reservation : reservations) {
            if (reservation.getSeatNumber().equals(seat) && reservation.getFlightNumber().equals(flightNum)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSeatRightFlight(String flightNum, String seat) {
        for (Reservation reservation : reservations) {
            if (reservation.getSeatNumber().equals(seat) && reservation.getFlightNumber().equals(flightNum)) {
                return false;
            }
        }
        return true;
    }

    private String generateRandomSeat(int totalSeats, Set<String> adminReservedSeats, Set<String> assignedSeats) {
        Random random = new Random();
        int maxAvailableSeat = totalSeats - adminReservedSeats.size();
        String seatNumber;

        if (maxAvailableSeat == 0) {
            return "No available seats";
        }

        do {
            seatNumber = "R" + (random.nextInt(maxAvailableSeat) + 1);
        } while (adminReservedSeats.contains(seatNumber) || assignedSeats.contains(seatNumber));

        assignedSeats.add(seatNumber);
        return seatNumber;
    }

    private String generateVIPRandomSeat(int totalSeats, Set<String> adminReservedSeats, Set<String> assignedSeats) {
        Random random = new Random();
        int maxAvailableSeat = totalSeats - adminReservedSeats.size();
        String seatNumber;

        if (maxAvailableSeat == 0) {

            return "No available VIP seats";
        }

        do {
            seatNumber = "V" + (random.nextInt(maxAvailableSeat) + 1);
        } while (adminReservedSeats.contains(seatNumber) || assignedSeats.contains(seatNumber));

        assignedSeats.add(seatNumber);
        return seatNumber;
    }

    public void checkIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Kiểm tra và đặt chỗ:");
        System.out.print("Mã vé hoặc CCCD: ");
        String inputCode = scanner.nextLine();

        Reservation selectedReservation = null;
        for (Reservation reservation : reservations) {
            if (reservation.getReservationID().equalsIgnoreCase(inputCode) || reservation.getIdentityCard().equalsIgnoreCase(inputCode)) {
                selectedReservation = reservation;
                break;
            }
        }

        if (selectedReservation != null) {
            Flight selectedFlight = null;
            for (Flight flight : flights) {
                if (flight.getFlightNumber().equalsIgnoreCase(selectedReservation.getFlightNumber())) {
                    selectedFlight = flight;
                    break;
                }
            }
            if (selectedFlight != null) {
                System.out.println("Thông tin chuyến bay:");
                System.out.println("Mã chuyến bay: " + selectedFlight.getFlightNumber());
                System.out.println("Mã vé: " + selectedReservation.getReservationID());
                System.out.println("Nơi khởi hành: " + selectedFlight.getDepartureCity());
                System.out.println("Nơi đến: " + selectedFlight.getDestinationCity());
                System.out.println("Thời gian khởi hành: " + selectedFlight.getDepartureTime());
                System.out.println("Thời gian đến: " + selectedFlight.getArrivalTime());
            } else {
                System.out.println("Không thể check in hoặc chuyến bay không tồn tại.");
            }
        } else {
            System.out.println("Mã vé hoặc CCCD không hợp lệ hoặc không tồn tại.");
        }
    }

    public void assignCrewToFlight() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Gán phi hành đoàn vào chuyến bay ");
        System.out.println();
        System.out.println("Danh sách chuyến bay có sẵn:");

        List<Flight> flightsWithoutCrew = new ArrayList<>();

        for (Flight flight : flights) {
            if (flight.getCrewAssignment() == null) {
                System.out.println(flight.getFlightNumber() + " - " + flight.getDepartureCity() + " đến " + flight.getDestinationCity());
                flightsWithoutCrew.add(flight);
            }
        }
        System.out.println();
        if (flightsWithoutCrew.isEmpty()) {
            System.out.println("Không có chuyến bay nào cần gán phi hành đoàn.");
            return;
        }

        System.out.print("Nhập mã chuyến bay để gán phi hành đoàn (hoặc 0 để quay lại): ");
        String flightNumberInput = scanner.nextLine();

        if (flightNumberInput.equals("0")) {
            return;
        }

        Flight selectedFlight = null;
        for (Flight flight : flightsWithoutCrew) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumberInput)) {
                selectedFlight = flight;
                break;
            }
        }

        if (selectedFlight == null) {
            System.out.println("Không tìm thấy chuyến bay với mã chuyến bay này hoặc chuyến bay đã có phi hành đoàn.");
            return;
        }

        System.out.println("Danh sách phi hành đoàn có sẵn:");

        for (CrewAssignment assignment : crewAssignments) {
            System.out.println(assignment.getCrewNumber() + " - " + assignment.getPilot());
        }

        System.out.print("Nhập mã phi hành đoàn để gán vào chuyến bay (hoặc 0 để quay lại): ");
        String crewNumberInput = scanner.nextLine();

        if (crewNumberInput.equals("0")) {
            return;
        }

        CrewAssignment selectedCrewAssignment = null;
        for (CrewAssignment assignment : crewAssignments) {
            if (assignment.getCrewNumber().equalsIgnoreCase(crewNumberInput)) {
                selectedCrewAssignment = assignment;
                break;
            }
        }
        if (selectedCrewAssignment == null) {
            System.out.println("Không tìm thấy phi hành đoàn với mã phi hành đoàn này.");
            return;
        }
        for (Flight flight : flights) {
            if (selectedCrewAssignment == flight.getCrewAssignment()) {
                System.out.println("Phi hành đoàn đã được gán cho chuyến bay khác");
                return;
            }
        }
        selectedFlight.setCrewAssignment(selectedCrewAssignment);
        System.out.println("Phi hành đoàn đã được gán vào chuyến bay thành công.");
        printAvailableFlight();
    }

    private void loadStaffFromFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String staffId = parts[0].trim();
                    String staffName = parts[1].trim();
                    String staffPosition = parts[2].trim();
                    staffs.add(new Staff(staffId, staffName, staffPosition));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc dữ liệu từ file: " + e.getMessage());
        }
    }

    private void loadFlightTimeInfoFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String departureCity = parts[0].trim();
                    String destinationCity = parts[1].trim();
                    double flightTime = Double.parseDouble(parts[2].trim());
                    flightTimeInfoList.add(new FlightTimeInfo(departureCity, destinationCity, flightTime));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void loadCrewFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String crewNumber = parts[0];
                    String pilot = parts[1];
                    String coPilot = parts[2];
                    String flightAttendant = parts[3];
                    String doctor = parts[4];
                    String aviationTechnician = parts[5];
                    String groundStaff = parts[6];

                    crewAssignments.add(new CrewAssignment(crewNumber, pilot, coPilot, flightAttendant, doctor, aviationTechnician, groundStaff));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignStaffToCrew() {
        Scanner scanner = new Scanner(System.in);
        String crewNumber;

        while (true) {
            System.out.print("Mã phi hành đoàn (Cxxx, xxx is a number): ");
            crewNumber = scanner.nextLine();
            if (!crewNumber.matches("^C\\d{3}$")) {
                System.out.println("Mã phi hành đoàn không đúng định dạng. Vui lòng nhập lại (e.g., Crew123).");
            } else if (crewAssignmentExists(crewNumber)) {
                System.out.println("Mã phi hành đoàn đã được sử dụng");
            } else {
                break;
            }
        }

        printStaffInformation();

        List<String> staffCodes = new ArrayList<>();
        List<Staff> selectedStaffList = new ArrayList<>();

        for (String role : new String[]{"cơ trưởng", "cơ phó", "tiếp viên", "nhân viên bác sĩ", "nhân viên kĩ thuật", "nhân viên mặt đất"}) {
            boolean validInput = false;
            String staffCode = null;

            while (!validInput) {
                System.out.print("Mã " + role + ": ");
                staffCode = scanner.nextLine();
                Staff selectedStaff = getStaffByCode(staffCode);

                if (selectedStaff == null) {
                    System.out.println("Không tìm thấy nhân viên với mã nhân viên " + staffCode + ". Vui lòng nhập lại!!!");
                } else if (!selectedStaff.getStaffRole().equalsIgnoreCase(role)) {
                    System.out.println("Nhân viên với mã " + staffCode + " không thể làm vai trò " + role + ". Vui lòng nhập lại!!!");
                } else if (staffCodes.contains(staffCode)) {
                    System.out.println("Nhân viên với mã " + staffCode + " đã được sử dụng cho một vai trò khác. Vui lòng nhập lại!!!");
                } else {
                    validInput = true;
                }
            }

            staffCodes.add(staffCode);
            selectedStaffList.add(getStaffByCode(staffCode));
        }

        CrewAssignment crewAssignment = new CrewAssignment(
                crewNumber,
                selectedStaffList.get(0).getStaffName(),
                selectedStaffList.get(1).getStaffName(),
                selectedStaffList.get(2).getStaffName(),
                selectedStaffList.get(3).getStaffName(),
                selectedStaffList.get(4).getStaffName(),
                selectedStaffList.get(5).getStaffName()
        );

        crewAssignments.add(crewAssignment);
        System.out.println("Phi hành đoàn đã được thêm.");
    }

    private Staff getStaffByCode(String code) {
        for (Staff staff : staffs) {
            if (staff.getStaffId().equalsIgnoreCase(code)) {
                return staff;
            }
        }
        return null;
    }

//    public void printAvailableFlight() {
//        if (flights.isEmpty()) {
//            System.out.println("\nKhông có chuyến bay nào vào thời điểm hiện tại.\n");
//        } else {
//            Collections.sort(flights, Comparator.comparing(Flight::getDepartureTime).reversed());
//
//            System.out.println("Thông tin chuyến bay đang có sẵn:");
//            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");
//            System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16s | %-16s | %-20s |%n",
//                    "Mã chuyến bay", "Nơi khởi hành", "Nơi đến", "Khởi hành", "Đến", "Số ghế thường", "Số ghế VIP", "Cơ trưởng");
//
//            for (Flight flight : flights) {
//                System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");
//
//                String crewAssignmentInfo = "Chưa có";
//                CrewAssignment crewAssignment = flight.getCrewAssignment();
//                if (crewAssignment != null) {
//                    crewAssignmentInfo = crewAssignment.getPilot();
//                }
//
//                System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16d | %-16d | %-20s |%n",
//                        flight.getFlightNumber(), flight.getDepartureCity(), flight.getDestinationCity(),
//                        flight.getDepartureTime(), flight.getArrivalTime(), flight.getAvailableNormalSeats(), flight.getAvailableVIPSeats(), crewAssignmentInfo);
//            }
//            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");
//        }
//    }
    public void printAvailableFlight() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateString = currentDate.format(dateFormatter);

        System.out.println("Thông tin chuyến bay có trong ngày hôm nay (" + currentDateString + "):");

        List<Flight> todayFlights = new ArrayList<>();

        for (Flight flight : flights) {
            String flightDateString = flight.getDateFlight();
            if (currentDateString.equals(flightDateString)) {
                todayFlights.add(flight);
            }
        }

        if (todayFlights.isEmpty()) {
            System.out.println("Không có chuyến bay nào vào thời điểm hiện tại.");
        } else {
            Collections.sort(flights, Comparator.comparing(Flight::getDepartureTime).reversed());
            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");
            System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16s | %-16s | %-20s |%n",
                    "Mã chuyến bay", "Nơi khởi hành", "Nơi đến", "Khởi hành", "Đến", "Số ghế thường", "Số ghế VIP", "Cơ trưởng");

            for (Flight flight : todayFlights) {
                System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");

                String crewAssignmentInfo = "Chưa có";
                CrewAssignment crewAssignment = flight.getCrewAssignment();
                if (crewAssignment != null) {
                    crewAssignmentInfo = crewAssignment.getPilot();
                }

                System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16d | %-16d | %-20s |%n",
                        flight.getFlightNumber(), flight.getDepartureCity(), flight.getDestinationCity(),
                        flight.getDepartureTime(), flight.getArrivalTime(), flight.getAvailableNormalSeats(), flight.getAvailableVIPSeats(), crewAssignmentInfo);
            }
            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+----------------------+");
        }
    }

    public void printFlightInformation() {
        if (flights.isEmpty()) {
            System.out.println("\nKhông có chuyến bay nào vào thời điểm hiện tại.\n");
        } else {
            Collections.sort(flights, Comparator.comparing(Flight::getDepartureTime).reversed());

            System.out.println("Thông tin chuyến bay đang có sẵn:");
            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+------------------+----------------------+");
            System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16s | %-16s | %-16s | %-20s |%n",
                    "Mã chuyến bay", "Nơi khởi hành", "Nơi đến", "Khởi hành", "Đến", "Số ghế thường", "Số ghế VIP", "Ngày bay", "Cơ trưởng");

            for (Flight flight : flights) {
                System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+------------------+----------------------+");

                String crewAssignmentInfo = "Chưa có";
                CrewAssignment crewAssignment = flight.getCrewAssignment();
                if (crewAssignment != null) {
                    crewAssignmentInfo = crewAssignment.getPilot();
                }

                System.out.printf("| %-13s | %-20s | %-20s | %-10s | %-10s | %-16d | %-16d | %-16s | %-20s |%n",
                        flight.getFlightNumber(), flight.getDepartureCity(), flight.getDestinationCity(),
                        flight.getDepartureTime(), flight.getArrivalTime(), flight.getAvailableNormalSeats(), flight.getAvailableVIPSeats(), flight.getDateFlight(), crewAssignmentInfo);
            }
            System.out.println("+---------------+----------------------+----------------------+------------+------------+------------------+------------------+------------------+----------------------+");
        }
    }

    public void printCrewInformation() {
        if (crewAssignments.isEmpty()) {
            System.out.println("\nKhông có chuyến bay nào vào thời điểm hiện tại.\n");
        } else {
            System.out.println("Thông tin phi hành đoàn đang có sẵn:");
            System.out.println("+------------------+----------------------+----------------------+----------------------+----------------------+----------------------+------------------+");
            System.out.printf("| %-15s | %-20s | %-20s | %-20s | %-20s | %-20s | %-16s | %n",
                    "Mã phi hành đoàn", "Tên cơ trưởng", "Tên cơ phó", "Tên tiếp viên", "Tên nhân bác sĩ", "Tên nv kĩ thuật", "Tên nv mặt đất");

            for (CrewAssignment crewAssignment : crewAssignments) {
                System.out.println("+------------------+----------------------+----------------------+----------------------+----------------------+----------------------+------------------+");

                System.out.printf("| %-16s | %-20s | %-20s | %-20s | %-20s | %-20s | %-16s | %n",
                        crewAssignment.getCrewNumber(), crewAssignment.getPilot(), crewAssignment.getCoPilot(),
                        crewAssignment.getFlightAttendant(), crewAssignment.getDoctor(),
                        crewAssignment.getAviationTechnician(), crewAssignment.getGroundStaff());
            }
            System.out.println("+------------------+----------------------+----------------------+----------------------+----------------------+----------------------+------------------+");
        }
    }

    public void printStaffInformation() {
        if (staffs.isEmpty()) {
            System.out.println("\nKhông có nhân viên nào.\n");
        } else {
            System.out.println("Thông tin nhân viên đang có sẵn:");
            System.out.println("+----------------------+--------------------------+-------------------------+------------------------+--------------------------+--------------------------+");
            System.out.printf("| %-20s | %-24s | %-23s | %-22s | %-24s | %-24s | %n",
                    "Cơ trưởng", "Cơ phó", "Tiếp viên", "Bác sĩ", "NV kỹ thuật", "NV mặt đất");
            List<String> pilots = new ArrayList<>();
            List<String> coPilots = new ArrayList<>();
            List<String> flightAttendants = new ArrayList<>();
            List<String> doctors = new ArrayList<>();
            List<String> aviationTechnicians = new ArrayList<>();
            List<String> groundStaffs = new ArrayList<>();
            for (Staff staffInfo : staffs) {
                switch (staffInfo.getStaffRole()) {
                    case "cơ trưởng":
                        pilots.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    case "cơ phó":
                        coPilots.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    case "tiếp viên":
                        flightAttendants.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    case "nhân viên bác sĩ":
                        doctors.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    case "nhân viên kĩ thuật":
                        aviationTechnicians.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    case "nhân viên mặt đất":
                        groundStaffs.add(staffInfo.getStaffId() + " - " + staffInfo.getStaffName());
                        break;
                    default:
                        break;
                }
            }
            int maxEntries = Math.max(pilots.size(), Math.max(coPilots.size(),
                    Math.max(flightAttendants.size(), Math.max(doctors.size(),
                            Math.max(aviationTechnicians.size(), groundStaffs.size())))));
            for (int i = 0; i < maxEntries; i++) {
                System.out.println("+----------------------+--------------------------+-------------------------+------------------------+--------------------------+--------------------------+");
                System.out.printf("|%-21s | %-24s | %-23s | %-22s | %-24s | %-24s | %n",
                        i < pilots.size() ? pilots.get(i) : "",
                        i < coPilots.size() ? coPilots.get(i) : "",
                        i < flightAttendants.size() ? flightAttendants.get(i) : "",
                        i < doctors.size() ? doctors.get(i) : "",
                        i < aviationTechnicians.size() ? aviationTechnicians.get(i) : "",
                        i < groundStaffs.size() ? groundStaffs.get(i) : ""
                );
            }

            System.out.println("+----------------------+--------------------------+-------------------------+------------------------+--------------------------+--------------------------+");
        }
    }

    private String generateReservationID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "R" + randomNumber;
    }

    private String generateVIPReservationID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "V" + randomNumber;
    }

    public void saveDataToFile() {
        try {
            FileWriter flightsCSV = new FileWriter("src//Output//flights.txt");
            for (Flight flight : flights) {
                flightsCSV.write(flight.toString() + "\n");
            }
            flightsCSV.close();

            FileWriter reservationsCSV = new FileWriter("src//Output//reservations.txt");
            for (Reservation reservation : reservations) {
                reservationsCSV.write(reservation.toString() + "\n");
            }
            reservationsCSV.close();

            FileWriter crewAssignmentsCSV = new FileWriter("src//Output//crewInfo.txt");
            for (CrewAssignment crewAssignment : crewAssignments) {
                crewAssignmentsCSV.write(crewAssignment.toString() + "\n");
            }
            crewAssignmentsCSV.close();

            FileWriter usersCSV = new FileWriter("src//Output//account.txt");
            for (User user : users) {
                usersCSV.write(user.toString() + "\n");
            }
            usersCSV.close();

            FileWriter infoCus = new FileWriter("src//Database//infoCus.txt");
            for (InfoCustomer infoCustomer : infoCustomers) {
                infoCus.write(infoCustomer.toString() + "\n");
            }
            usersCSV.close();

            System.out.println("Dữ liệu đã được lưu vào các tệp dat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromFile() {
        try {
            FileReader fileReader = new FileReader("src/Output/flights.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Set<String> existingFlightNumbers = new HashSet<>();
            Set<String> existingReservationsID = new HashSet<>();
            Set<String> existingCrewNumber = new HashSet<>();
            Set<String> existingAcount = new HashSet<>();
            Set<String> existingInfo = new HashSet<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String flightNumber = parts[0];

                    if (existingFlightNumbers.contains(flightNumber)) {
                        continue;
                    }

                    String departureCity = parts[1];
                    String destinationCity = parts[2];
                    String dateFlight = parts[3];
                    String departureTime = parts[4];
                    String arrivalTime = parts[5];
                    int availableNormalSeats = Integer.parseInt(parts[6]);
                    int availableVIPSeats = Integer.parseInt(parts[7]);
                    Flight flight = new Flight(flightNumber, departureCity, destinationCity, dateFlight, departureTime, arrivalTime, availableNormalSeats, availableVIPSeats);

                    if (parts.length == 15) {
                        String crewNumber = parts[8];
                        String pilot = parts[9];
                        String coPilot = parts[10];
                        String flightAttendant = parts[11];
                        String doctor = parts[12];
                        String aviationTechnician = parts[13];
                        String groundStaff = parts[14];
                        CrewAssignment crewAssignment = new CrewAssignment(crewNumber, pilot, coPilot, flightAttendant, doctor, aviationTechnician, groundStaff);
                        flight.setCrewAssignment(crewAssignment);
                    }
                    boolean exists = false;
                    for (Flight flightcheck : flights) {
                        if (flightcheck.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        flights.add(flight);
                        existingFlightNumbers.add(flightNumber);
                    }

                }
            }
            bufferedReader.close();

            FileReader reservationReader = new FileReader("src/Output/reservations.txt");
            bufferedReader = new BufferedReader(reservationReader);

            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String reservationID = parts[0];
                    if (existingReservationsID.contains(reservationID)) {
                        continue;
                    }
                    String passengerName = parts[1];
                    String identityCard = parts[2];
                    String contactDetails = parts[3];
                    String flightNumber = parts[4];
                    boolean isVip = Boolean.parseBoolean(parts[5]);
                    String seatNumber = parts[6];

                    Reservation reservation = new Reservation(reservationID, passengerName, identityCard, contactDetails, flightNumber, isVip, seatNumber);
                    boolean exists = false;
                    for (Reservation reservationcheck : reservations) {
                        if (reservationcheck.getReservationID().equalsIgnoreCase(reservationID)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        reservations.add(reservation);
                        existingReservationsID.add(reservationID);
                        adminReservedSeats.add(seatNumber);
                    }

                }
            }
            bufferedReader.close();

            FileReader crewAssignmentReader = new FileReader("src/Output/crewInfo.txt");
            bufferedReader = new BufferedReader(crewAssignmentReader);

            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String crewNumber = parts[0];
                    if (existingCrewNumber.contains(crewNumber)) {
                        continue;
                    }
                    String pilot = parts[1];
                    String coPilot = parts[2];
                    String flightAttendant = parts[3];
                    String doctor = parts[4];
                    String aviationTechnician = parts[5];
                    String groundStaff = parts[6];
                    CrewAssignment crewAssignment = new CrewAssignment(crewNumber, pilot, coPilot, flightAttendant, doctor, aviationTechnician, groundStaff);
                    boolean exists = false;
                    for (CrewAssignment crewAssignmentcheck : crewAssignments) {
                        if (crewAssignmentcheck.getCrewNumber().equalsIgnoreCase(crewNumber)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        crewAssignments.add(crewAssignment);
                        existingCrewNumber.add(crewNumber);
                    }

                }
            }
            bufferedReader.close();
            FileReader userReader = new FileReader("src/Output/account.txt");
            bufferedReader = new BufferedReader(userReader);

            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    if (existingAcount.contains(username)) {
                        continue;
                    }
                    String password = parts[1];
                    boolean isAdmin = Boolean.parseBoolean(parts[2]);
                    User user = new User(username, password, isAdmin);
                    boolean exists = false;
                    for (User usercheck : users) {
                        if (usercheck.getUsername().equalsIgnoreCase(username)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        users.add(user);
                        existingAcount.add(username);
                    }

                }
            }
            bufferedReader.close();

            FileReader infoCusreader = new FileReader("src/Database/infoCus.txt");
            bufferedReader = new BufferedReader(infoCusreader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String identityId = parts[0];
                    String nameCus = parts[1];

                    if (!existingInfo.contains(identityId) && !infoCustomersContains(infoCustomers, identityId, nameCus)) {
                        InfoCustomer infoCustomer = new InfoCustomer(identityId, nameCus);
                        infoCustomers.add(infoCustomer);
                        existingInfo.add(identityId);
                    }
                }
            }

            bufferedReader.close();

            System.out.println("Dữ liệu chuyến bay đã được nạp từ tệp txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean flightExists(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean crewAssignmentExists(String crewNumber) {
        for (CrewAssignment assignment : crewAssignments) {
            if (assignment.getCrewNumber().equals(crewNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean infoCustomersContains(List<InfoCustomer> infoCustomers, String identityId, String nameCus) {
        for (InfoCustomer infoCustomer : infoCustomers) {
            if (infoCustomer.getIdentityId().equalsIgnoreCase(identityId) && infoCustomer.getNameCus().equalsIgnoreCase(nameCus)) {
                return true;
            }
        }
        return false;
    }
}
