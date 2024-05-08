/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab3;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author NGUYEN
 */
public class Vehicle implements Serializable {

    int ID_Vehicle;
    String Name_Vehicle;
    String color_Vehicle;
    long price_Vehicle;
    String brand_Vehicle;
    String type;
    String productYear;

    public Vehicle(int ID_Vehicle, String Name_Vehicle, String color_Vehicle, long price_Vehicle, String brand_Vehicle, String type, String productYear) {
        this.ID_Vehicle = ID_Vehicle;
        this.Name_Vehicle = Name_Vehicle;
        this.color_Vehicle = color_Vehicle;
        this.price_Vehicle = price_Vehicle;
        this.brand_Vehicle = brand_Vehicle;
        this.type = type;
        this.productYear = productYear;
    }

    public int getID_Vehicle() {
        return ID_Vehicle;
    }

    public void setID_Vehicle(int ID_Vehicle) {
        this.ID_Vehicle = ID_Vehicle;
    }

    public String getName_Vehicle() {
        return Name_Vehicle;
    }

    public void setName_Vehicle(String Name_Vehicle) {
        this.Name_Vehicle = Name_Vehicle;
    }

    public String getColor_Vehicle() {
        return color_Vehicle;
    }

    public void setColor_Vehicle(String color_Vehicle) {
        this.color_Vehicle = color_Vehicle;
    }

    public long getPrice_Vehicle() {
        return price_Vehicle;
    }

    public void setPrice_Vehicle(long price_Vehicle) {
        this.price_Vehicle = price_Vehicle;
    }

    public String getBrand_Vehicle() {
        return brand_Vehicle;
    }

    public void setBrand_Vehicle(String brand_Vehicle) {
        this.brand_Vehicle = brand_Vehicle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductYear() {
        return productYear;
    }

    public void setProductYear(String productYear) {
        this.productYear = productYear;
    }

    @Override
    public String toString() {
        return ID_Vehicle + "," + Name_Vehicle + "," + color_Vehicle + "," + price_Vehicle + "," + brand_Vehicle + "," + type + "," + productYear;
    }

}
