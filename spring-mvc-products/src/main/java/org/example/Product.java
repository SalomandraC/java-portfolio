package org.example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Product  implements Serializable {
    private Long id;
    private String name;
    private Double price;
    private String[] categories = new String[0];

    public Product() {
    }

    public Product(Long id, String name, Double price, String[] categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categories = categories.clone();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String[] getCategories() {
        return categories.clone();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCategories(String[] categories) {
        this.categories = categories.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.02f", name, Arrays.toString(categories), price);
    }
}
