package org.example;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductDao productDao;
    private Long nextId;

    public ProductService(@Qualifier("InMemoryProductDao") ProductDao productDao) {
        this.productDao = productDao;
        long maxId = productDao.findAll().stream()
                .mapToLong(Product::getId)
                .max()
                .orElse(0);
        this.nextId = maxId + 1;
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(Long id) {
        return productDao.findById(id);
    }

    public Product save(Product prod) {
        if (prod.getPrice() == null) {
            throw new IllegalArgumentException("Укажите цену продукта");
        }
        if (prod.getPrice() < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (prod.getId() == null) {
            prod.setId(nextId++);
        }
        productDao.save(prod);
        return prod;
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    public Map<String, CategoryStats> categoryStats() {
        Map<String, CategoryStats> result = new TreeMap<>();
        for (Product product : productDao.findAll()) {
            for (String category : product.getCategories()) {
                result.computeIfAbsent(category, c -> new CategoryStats()).add(product);
            }
        }
        return result;
    }
}
