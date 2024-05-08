package Data;

import Model.WarehouseItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Model.WarehouseItem;

public class Warehouse implements Serializable {
    private List<WarehouseItem> warehouseItems;

    public Warehouse() {
        this.warehouseItems = new ArrayList<>();
    }

    public List<WarehouseItem> getWarehouseItems() {
        return warehouseItems;
    }

    public void addWarehouseItem(WarehouseItem item) {
        warehouseItems.add(item);
    }

    // Thêm phương thức để lấy thông tin sản phẩm trong kho bằng mã sản phẩm
    public WarehouseItem getWarehouseItem(String productCode) {
        for (WarehouseItem item : warehouseItems) {
            if (item.getProductCode().equals(productCode)) {
                return item;
            }
        }
        return null;
    }
}
