package Model;


import Model.NguyenLieu;
import java.util.ArrayList;
import java.util.List;

public class DoUong {

    public int id;
    public String ten;
    public List<NguyenLieu> nguyenLieu;

    public DoUong(int id, String ten, ArrayList<NguyenLieu> nguyenLieu) {
        this.id = id;
        this.ten = ten;
        this.nguyenLieu = nguyenLieu;
    }
      public void themNguyenLieu(NguyenLieu nguyenLieu) {
        this.nguyenLieu.add(nguyenLieu);
    }
}