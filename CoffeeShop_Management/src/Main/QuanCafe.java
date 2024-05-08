package Main;

import Model.DoUong;
import Model.NguyenLieu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class QuanCafe {

    private static int nextNguyenLieuId = 1;
    private static int nextDoUongId = 1;
    private static int nextDoUongDaPhucVuId = 1;

    Map<Integer, NguyenLieu> nguyenLieus;
    Map<Integer, DoUong> menu;
    private ArrayList<String> doUongDaPhucVu;

    public QuanCafe() {
        nguyenLieus = new HashMap<>();
        menu = new HashMap<>();
        this.doUongDaPhucVu = new ArrayList<>();
    }

    public void themNguyenLieu(String ten, double soLuong) {
        int newId = nextNguyenLieuId;
        while (nguyenLieus.containsKey(newId)) {
            newId++;
        }
        nextNguyenLieuId = newId + 1;

        nguyenLieus.put(newId, new NguyenLieu(newId, ten, soLuong));
    }

    public void capNhatNguyenLieu(int id, double soLuongMoi) {
        NguyenLieu nguyenLieu = nguyenLieus.get(id);
        if (nguyenLieu != null) {
            nguyenLieu.soLuong = soLuongMoi;
        }
    }

    public void xoaNguyenLieu(int id) {
        NguyenLieu nguyenLieu = nguyenLieus.get(id);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bạn có chắc chắn muốn xóa nguyên liệu này? (yes/no)");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            nguyenLieus.remove(id);
            System.out.println("Nguyên liệu đã được xóa.");
        } else {
            System.out.println("Thao tác xóa đã bị hủy.");
        }

    }

    public void hienThiTatCaNguyenLieu() {
        if (nguyenLieus.isEmpty()) {
            System.out.println("Không có nguyên liệu.");
            return;
        }
        System.out.println("Danh sách nguyên liệu:");
        System.out.println("| ID |      Tên      |  Số lượng  |");
        System.out.println("+----+---------------+------------+");
        for (NguyenLieu nguyenLieu : nguyenLieus.values()) {
            System.out.printf("| %2d | %-13s |  %.2f     |%n", nguyenLieu.id, nguyenLieu.ten, nguyenLieu.soLuong);
        }
        System.out.println("+----+---------------+------------+");
    }

    public void themDoUongVaoMenu(String ten, List<String> tenNguyenLieu, List<Double> soLuongNguyenLieu) {
        // Kiểm tra và tạo ID mới nếu có trùng lặp
        int newId = nextDoUongId;
        while (menu.containsKey(newId)) {
            newId++;
        }
        nextDoUongId = newId + 1;

        ArrayList<NguyenLieu> nguyenLieuList = new ArrayList<>();
        List<String> nguyenLieuKhongDu = new ArrayList<>();

        // Kiểm tra ngay lập tức xem có đủ nguyên liệu không
        for (int i = 0; i < tenNguyenLieu.size(); i++) {
            String tenNL = tenNguyenLieu.get(i);
            double soLuong = soLuongNguyenLieu.get(i);

            int id = -1;
            for (NguyenLieu nl : nguyenLieus.values()) {
                if (nl.ten.toLowerCase().contains(tenNL.toLowerCase())) {
                    id = nl.id;
                    if (nl.soLuong < soLuong) {
                        nguyenLieuKhongDu.add(tenNL);
                        break;
                    }
                }
            }

            if (id == -1) {
                System.out.println("Nguyên liệu '" + tenNL + "' không tồn tại.");
                return; // Thoát khỏi phương thức nếu có nguyên liệu không tồn tại
            }
        }

        // Nếu có nguyên liệu không đủ
        if (!nguyenLieuKhongDu.isEmpty()) {
            System.out.println("Không đủ nguyên liệu để thêm đồ uống vào menu!");
            System.out.println("Nguyên liệu không đủ: " + nguyenLieuKhongDu);
            return; // Thoát khỏi phương thức
        }

        // Nếu đủ nguyên liệu, tạo đối tượng DoUong và thêm vào menu
        for (int i = 0; i < tenNguyenLieu.size(); i++) {
            String tenNL = tenNguyenLieu.get(i);
            double soLuong = soLuongNguyenLieu.get(i);

            int id = -1;
            for (NguyenLieu nl : nguyenLieus.values()) {
                if (nl.ten.toLowerCase().contains(tenNL.toLowerCase())) {
                    id = nl.id;
                    break;
                }
            }

            nguyenLieuList.add(new NguyenLieu(id, tenNL, soLuong));
        }

        menu.put(newId, new DoUong(newId, ten, nguyenLieuList));
        System.out.println("Đã thêm đồ uống vào menu!!!");
    }

    public void capNhatThongTinDoUong(int id, List<String> tenNguyenLieu, List<Double> soLuongNguyenLieu) {
        DoUong doUong = menu.get(id);
        if (doUong != null) {
            List<NguyenLieu> nguyenLieuList = new ArrayList<>();
            for (int i = 0; i < tenNguyenLieu.size(); i++) {
                String tenNL = tenNguyenLieu.get(i);
                double soLuong = soLuongNguyenLieu.get(i);
                int nguyenLieuId = -1;
                for (NguyenLieu nl : nguyenLieus.values()) {
                    if (nl.ten.equals(tenNL)) {
                        nguyenLieuId = nl.id;
                        break;
                    }
                }
                if (nguyenLieuId != -1) {
                    nguyenLieuList.add(new NguyenLieu(nguyenLieuId, tenNL, soLuong));
                } else {
                    System.out.println("Nguyên liệu '" + tenNL + "' không tồn tại.");
                    return;
                }
            }
            doUong.nguyenLieu = nguyenLieuList;
        } else {
            System.out.println("Không tìm thấy đồ uống.");
        }
    }

    public void xoaDoUongKhoiMenu(int id) {
        DoUong doUong = menu.get(id);
        if (doUong != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Bạn có chắc chắn muốn xóa đồ uống này? (yes/no)");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                menu.remove(id);
                System.out.println("Đồ uống đã được xóa khỏi menu.");
            } else {
                System.out.println("Thao tác xóa đã bị hủy.");
            }
        } else {
            System.out.println("Không tìm thấy đồ uống.");
        }
    }

    public void hienThiMenu() {
        if (menu.isEmpty()) {
            System.out.println("Không có đồ uống.");
            return;
        }
        System.out.println("Menu:");
        System.out.println("| ID |    Tên đồ uống    |");
        System.out.println("+----+-------------------+");
        for (DoUong doUong : menu.values()) {
            System.out.printf("| %2d | %-17s |%n", doUong.id, doUong.ten);
        }
        System.out.println("+----+-------------------+");
    }

    public void phucVuDoUong(int id) {
        DoUong doUong = menu.get(id);
        if (doUong != null) {
            boolean coDuNguyenLieu = true;
            for (NguyenLieu nguyenLieu : doUong.nguyenLieu) {
                if (nguyenLieu.soLuong < nguyenLieu.soLuong) {
                    coDuNguyenLieu = false;
                    break;
                }
            }
            if (coDuNguyenLieu) {
                System.out.println("Phục vụ " + doUong.ten);
                for (NguyenLieu nguyenLieu : doUong.nguyenLieu) {
                    double soLuongCan = nguyenLieu.soLuong;
                    double soLuongHienCo = nguyenLieus.get(nguyenLieu.id).soLuong;
                    double soLuongConLai = soLuongHienCo - soLuongCan;
                    capNhatNguyenLieu(nguyenLieu.id, soLuongConLai);
                    doUongDaPhucVu.add(doUong.ten);
                }
            } else {
                System.out.println("Không đủ nguyên liệu để phục vụ " + doUong.ten);
            }
        } else {
            System.out.println("Không tìm thấy đồ uống.");
        }
    }

    public void baoCaoNguyenLieu() {
        System.out.println("Báo cáo Nguyên liệu:");
        for (NguyenLieu nguyenLieu : nguyenLieus.values()) {
            System.out.println(nguyenLieu.ten + ": " + nguyenLieu.soLuong);
        }
    }

    public void baoCaoDoUongHetNguyenLieu() {
        System.out.println("Các đồ uống hết nguyên liệu:");
        for (DoUong doUong : menu.values()) {
            boolean hetNguyenLieu = true;
            for (NguyenLieu nguyenLieu : doUong.nguyenLieu) {
                if (nguyenLieu.soLuong <= 0) {
                    hetNguyenLieu = false;
                    break;
                }
            }
            if (!hetNguyenLieu) {
                System.out.println(doUong.ten);
            }
        }
    }

    public void hienThiCacDoUong() {
        if (menu.isEmpty()) {
            System.out.println("Không có đồ uống.");
            return;
        }
        System.out.println("Danh sách đồ uống:");
        System.out.println("| ID |    Tên đồ uống    |");
        System.out.println("+----+-------------------+");
        for (DoUong doUong : menu.values()) {
            System.out.printf("| %2d | %-17s |%n", doUong.id, doUong.ten);
        }
        System.out.println("+----+-------------------+");
    }

    public void luuDuLieuVaoFile() {
        try (BufferedWriter writerNguyenLieu = new BufferedWriter(new FileWriter("src/OutPut/nguyenlieu.dat"));
                BufferedWriter writerDoUong = new BufferedWriter(new FileWriter("src/OutPut/douong.dat"));
                BufferedWriter writerDoUongDaPhucVu = new BufferedWriter(new FileWriter("src/OutPut/douongdaphucvu.dat"))) {

            // Ghi dữ liệu nguyên liệu
            writerNguyenLieu.write("Nguyên liệu:\n");
            for (NguyenLieu nguyenLieu : nguyenLieus.values()) {
                writerNguyenLieu.write(nguyenLieu.id + ": " + nguyenLieu.ten + ": " + nguyenLieu.soLuong + "\n");
            }

            // Ghi dữ liệu đồ uống
            for (DoUong doUong : menu.values()) {
                writerDoUong.write(doUong.id + ": " + doUong.ten + "\n");
            }

            // Ghi dữ liệu đồ uống đã phục vụ
            writerDoUongDaPhucVu.write("Đồ uống đã phục vụ:\n");
            for (String tenDoUong : doUongDaPhucVu) {
                writerDoUongDaPhucVu.write(tenDoUong + "\n");
            }

            System.out.println("Dữ liệu đã được lưu vào các file nguyenlieu.dat, douong.dat và douongdaphucvu.dat");
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi khi lưu dữ liệu vào file.");
            e.printStackTrace();
        }
    }

    public void loadDuLieuTuFile() {
        try (BufferedReader readerNguyenLieu = new BufferedReader(new FileReader("src/OutPut/nguyenlieu.dat"));
                BufferedReader readerDoUong = new BufferedReader(new FileReader("src/OutPut/douong.dat"));
                BufferedReader readerDoUongDaPhucVu = new BufferedReader(new FileReader("src/OutPut/douongdaphucvu.dat"))) {

            // Clear dữ liệu cũ trước khi load dữ liệu từ file
            nguyenLieus.clear();
            menu.clear();
            doUongDaPhucVu.clear();

            // Load dữ liệu nguyên liệu
            String line;
            boolean isMenuSection = false; // Biến đánh dấu khi đã đọc tới phần menu
            while ((line = readerNguyenLieu.readLine()) != null && !line.isEmpty() && !line.startsWith("Menu:")) {
                if (line.equals("Nguyên liệu:")) {
                    continue; // Bỏ qua dòng tiêu đề
                }
                String[] parts = line.split(": ");
                int id = Integer.parseInt(parts[0]);
                String ten = parts[1];
                double soLuong = Double.parseDouble(parts[2]);
                nguyenLieus.put(id, new NguyenLieu(id, ten, soLuong));
            }

            // Load dữ liệu đồ uống
            while ((line = readerDoUong.readLine()) != null) {
                String[] parts = line.split(":");
                int id = Integer.parseInt(parts[0].trim());
                String ten = parts[1].trim();
                menu.put(id, new DoUong(id, ten, new ArrayList<>()));
            }

            // Load dữ liệu đồ uống đã phục vụ
            while ((line = readerDoUongDaPhucVu.readLine()) != null) {
                if (!line.equals("Đồ uống đã phục vụ:")) {
                    doUongDaPhucVu.add(line.trim());
                }
            }

            System.out.println("Dữ liệu đã được tải từ các file.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Đã xảy ra lỗi khi tải dữ liệu từ file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QuanCafe quanCafe = new QuanCafe();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("+-------Hệ thống quản lý Quán Cafe-------+");
            System.out.println("| 1. Quản lý nguyên liệu                 |");
            System.out.println("| 2. Quản lý công thức đồ uống           |");
            System.out.println("| 3. Pha chế đồ uống                     |");
            System.out.println("| 4. Phục vụ đồ uống                     |");
            System.out.println("| 5. Báo cáo                             |");
            System.out.println("| 6. Lưu trữ dữ liệu vào tệp             |");
            System.out.println("| 7. Tải dữ liệu lên                     |");
            System.out.println("| 8. Thoát                               |");
            System.out.println("+----------------------------------------+");
            System.out.print("Nhập lựa chọn của bạn: ");
            int luaChon = scanner.nextInt();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng

            switch (luaChon) {
                case 1:
                    menuNguyenLieu(quanCafe, scanner);
                    break;
                case 2:
                    menuDoUong(quanCafe, scanner);
                    break;
                case 3:
                   menuPhaCheDoUong(quanCafe, scanner);
                    break;
                case 4:
                    phucVuDoUong(quanCafe, scanner);
                    break;
                case 5:
                    baoCao(quanCafe);
                    break;
                case 6:
                    quanCafe.luuDuLieuVaoFile();
                    break;
                case 7:
                    quanCafe.loadDuLieuTuFile();
                    break;
                case 8:
                    System.out.println("Đang thoát khỏi chương trình. Tạm biệt!");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số từ 1 đến 6.");
            }
        }
    }

    public static void menuNguyenLieu(QuanCafe quanCafe, Scanner scanner) {
        while (true) {
            System.out.println("+---------Quản lý Nguyên liệu---------+");
            System.out.println("| 1. Thêm một nguyên liệu             |");
            System.out.println("| 2. Cập nhật thông tin nguyên liệu   |");
            System.out.println("| 3. Xóa nguyên liệu                  |");
            System.out.println("| 4. Hiển thị tất cả nguyên liệu      |");
            System.out.println("| 5. Quay lại Menu chính              |");
            System.out.println("+-------------------------------------+");
            System.out.print("Nhập lựa chọn của bạn: ");
            int luaChon = scanner.nextInt();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng

            switch (luaChon) {
                case 1:
                    System.out.print("Nhập tên nguyên liệu: ");
                    String ten = scanner.nextLine();
                    System.out.print("Nhập số lượng: ");
                    double soLuong = scanner.nextDouble();
                    quanCafe.themNguyenLieu(ten, soLuong);
                    System.out.println("Thêm nguyên liệu thành công!");
                    break;
                case 2:
                    System.out.print("Nhập ID của nguyên liệu: ");
                    int id = scanner.nextInt();
                    if (!quanCafe.nguyenLieus.containsKey(id)) {
                        System.out.println("Không tìm thấy nguyên liệu.");
                        break;
                    }
                    System.out.print("Nhập số lượng mới: ");
                    double soLuongMoi = scanner.nextDouble();
                    quanCafe.capNhatNguyenLieu(id, soLuongMoi);
                    System.out.println("Cập nhật thành công!!!");
                    break;
                case 3:
                    System.out.print("Nhập ID của nguyên liệu: ");
                    int xoaId = scanner.nextInt();
                    if (!quanCafe.nguyenLieus.containsKey(xoaId)) {
                        System.out.println("Không tìm thấy nguyên liệu.");
                        break;
                    }
                    quanCafe.xoaNguyenLieu(xoaId);
                    break;

                case 4:
                    quanCafe.hienThiTatCaNguyenLieu();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số từ 1 đến 5.");
            }
        }
    }

    public static void menuDoUong(QuanCafe quanCafe, Scanner scanner) {
        while (true) {
            System.out.println("+------Quản lý Công thức Đồ uống-----+");
            System.out.println("| 1. Thêm một đồ uống vào Menu       |");
            System.out.println("| 2. Cập nhật thông tin đồ uống      |");
            System.out.println("| 3. Xóa đồ uống khỏi Menu           |");
            System.out.println("| 4. Hiển thị Menu                   |");
            System.out.println("| 5. Quay lại Menu chính             |");
            System.out.println("+------------------------------------+");
            System.out.print("Nhập lựa chọn của bạn: ");
            int luaChon = scanner.nextInt();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng

            switch (luaChon) {
                case 1:
                    themDoUongVaoMenu(quanCafe, scanner);
                    break;
                case 2:
                    menuCapNhatDoUong(quanCafe, scanner);
                    break;
                case 3:
                    System.out.print("Nhập ID của đồ uống cần xóa: ");
                    int xoaId = scanner.nextInt();
                    quanCafe.xoaDoUongKhoiMenu(xoaId);
                    break;
                case 4:
                    quanCafe.hienThiMenu();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số từ 1 đến 5.");
            }
        }
    }

    public static void themDoUongVaoMenu(QuanCafe quanCafe, Scanner scanner) {
        System.out.print("Nhập tên đồ uống: ");
        String ten = scanner.nextLine();

        List<String> tenNguyenLieu = new ArrayList<>();
        List<Double> soLuongNguyenLieu = new ArrayList<>();

        boolean duNguyenLieu = true; // Biến kiểm tra xem có đủ nguyên liệu cho đồ uống không
        List<String> nguyenLieuKhongDu = new ArrayList<>(); // Danh sách lưu trữ nguyên liệu không đủ

        while (duNguyenLieu) {
            System.out.print("Nhập tên nguyên liệu (hoặc nhập 'xong' để kết thúc): ");
            String tenNL = scanner.nextLine();

            if (tenNL.equalsIgnoreCase("xong")) {
                break;
            }

            tenNguyenLieu.add(tenNL);

            System.out.print("Nhập số lượng: ");
            double soLuong = scanner.nextDouble();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng

            int id = -1;
            for (NguyenLieu nl : quanCafe.nguyenLieus.values()) {
                if (nl.ten.toLowerCase().contains(tenNL.toLowerCase())) {
                    id = nl.id;
                    if (nl.soLuong < soLuong) {
                        duNguyenLieu = false;
                        nguyenLieuKhongDu.add(tenNL);
                        break;
                    }
                }
            }

            if (id == -1) {
                System.out.println("Nguyên liệu '" + tenNL + "' không tồn tại.");
                duNguyenLieu = false;
                break;
            }

            soLuongNguyenLieu.add(soLuong);
        }

        if (duNguyenLieu) {
            quanCafe.themDoUongVaoMenu(ten, tenNguyenLieu, soLuongNguyenLieu);
        } else {
            System.out.println("Không đủ nguyên liệu để thêm đồ uống vào menu!");
            System.out.println("Nguyên liệu không đủ: " + nguyenLieuKhongDu);
            System.out.println("Quay lại menu chính...");
            // Gọi phương thức hiển thị menu chính hoặc trở về màn hình chính
        }
    }

    public static void menuCapNhatDoUong(QuanCafe quanCafe, Scanner scanner) {
        System.out.print("Nhập ID của đồ uống: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Tiêu thụ ký tự xuống dòng
        if (!quanCafe.menu.containsKey(id)) {
            System.out.println("Không tìm thấy đồ uống.");
            return;
        }
        System.out.print("Nhập tên đồ uống mới: ");
        String ten = scanner.nextLine();
        List<String> tenNguyenLieu = new ArrayList<>();
        List<Double> soLuongNguyenLieu = new ArrayList<>();
        while (true) {
            System.out.print("Nhập tên nguyên liệu (hoặc nhập 'xong' để kết thúc): ");
            String tenNL = scanner.nextLine();
            if (tenNL.equalsIgnoreCase("xong")) {
                break;
            }
            tenNguyenLieu.add(tenNL);
            System.out.print("Nhập số lượng: ");
            double soLuong = scanner.nextDouble();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng
            soLuongNguyenLieu.add(soLuong);
        }
        quanCafe.capNhatThongTinDoUong(id, tenNguyenLieu, soLuongNguyenLieu);
    }

    public static void phucVuDoUong(QuanCafe quanCafe, Scanner scanner) {
        System.out.print("Nhập ID của đồ uống cần phục vụ: ");
        int id = scanner.nextInt();
        quanCafe.phucVuDoUong(id);
    }

    public static void baoCao(QuanCafe quanCafe) {
        while (true) {
            System.out.println("+------------------Báo cáo------------------+");
            System.out.println("| 1. Hiển thị nguyên liệu hiện có           |");
            System.out.println("| 2. Hiển thị đồ uống đã hết nguyên liệu    |");
            System.out.println("| 3. Hiển thị các đồ uống                   |");
            System.out.println("| 4. Quay lại Menu chính                    |");
            System.out.println("+-------------------------------------------+");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập lựa chọn của bạn: ");
            int luaChon = scanner.nextInt();
            scanner.nextLine(); // Tiêu thụ ký tự xuống dòng

            switch (luaChon) {
                case 1:
                    quanCafe.baoCaoNguyenLieu();
                    break;
                case 2:
                    quanCafe.baoCaoDoUongHetNguyenLieu();
                    break;
                case 3:
                    quanCafe.hienThiCacDoUong();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số từ 1 đến 4.");
            }
        }
    }

    public void phaCheDoUong(int id, double soLuongCanPhaChe) {
        DoUong doUong = menu.get(id);
        if (doUong == null) {
            System.out.println("Đồ uống không tồn tại.");
            return;
        }

        System.out.println("Pha chế đồ uống: " + doUong.ten);
        System.out.println("Số lượng cần pha chế: " + soLuongCanPhaChe);
        System.out.println("Thành phần của đồ uống:");

        boolean coDuNguyenLieu = true;
        String nguyenLieuKhongDu = "";

        for (NguyenLieu nguyenLieu : doUong.nguyenLieu) {
            double soLuongCan = nguyenLieu.soLuong * soLuongCanPhaChe; // Tính số lượng cần dựa trên tỷ lệ số lượng đồ uống
            double soLuongHienCo = nguyenLieus.get(nguyenLieu.id).soLuong;

            if (soLuongHienCo < soLuongCan) {
                coDuNguyenLieu = false;
                nguyenLieuKhongDu = nguyenLieu.ten;
                break;
            }
        }

        if (coDuNguyenLieu) {
            for (NguyenLieu nguyenLieu : doUong.nguyenLieu) {
                double soLuongCan = nguyenLieu.soLuong * soLuongCanPhaChe;
                double soLuongHienCo = nguyenLieus.get(nguyenLieu.id).soLuong;
                double soLuongConLai = soLuongHienCo - soLuongCan;
                capNhatNguyenLieu(nguyenLieu.id, soLuongConLai);
            }
            System.out.println("Pha chế đồ uống thành công.");
        } else {
            System.out.println("Không đủ nguyên liệu để pha chế đồ uống.");
            System.out.println("Số lượng cần pha chế của " + nguyenLieuKhongDu + " không đủ trong kho.");
        }
    }

    public static void menuPhaCheDoUong(QuanCafe quanCafe, Scanner scanner) {
        System.out.print("Nhập ID của đồ uống cần pha chế: ");
        int id = scanner.nextInt();
        System.out.print("Nhập số lượng đồ uống cần pha chế: ");
        double soLuongCanPhaChe = scanner.nextDouble();
        quanCafe.phaCheDoUong(id, soLuongCanPhaChe);
    }

}
