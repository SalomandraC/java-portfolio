package org.example;

import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    Product findById(Long id);

    void save(Product product);

    void deleteById(Long id);
}
