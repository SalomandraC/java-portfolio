package org.example;

public class CategoryStats {
    private int count;
    private double total;

    public void add(Product product) {
        count++;
        total += product.getPrice();
    }

    public int getCount() {
        return count;
    }

    public double getTotal() {
        return total;
    }
}
