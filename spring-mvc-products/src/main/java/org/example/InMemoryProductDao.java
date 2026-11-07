package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier
public class InMemoryProductDao implements ProductDao {
    private final List<Product> products = new CopyOnWriteArrayList<>();

    public InMemoryProductDao() {
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(products);
    }

    @Override
    public Product findById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Product product) {
        products.add(product);
    }

    @Override
    public void deleteById(Long id) {
        products.removeIf(productItem -> productItem.getId().equals(id));
    }
}
