package Model;

import java.io.Serializable;

 public class Tour implements Serializable{

    public int id;
    public String name;
    public String destination;
    public String duration;
    public String description;
    public double price;
    public String[] inclusions;
    public String[] exclusions;

    public Tour(int id, String name, String destination, String duration, String description, double price, String[] inclusions, String[] exclusions) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
    }
}