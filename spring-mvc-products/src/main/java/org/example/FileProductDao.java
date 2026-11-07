package org.example;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Primary
public class FileProductDao implements ProductDao {
    private final Path file = Path.of(System.getProperty("user.home"), "products.dat");

    private final List<Product> products = new CopyOnWriteArrayList<>();

    public FileProductDao() {
        products.addAll(load());
    }

    private List<Product> load() {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return (List<Product>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Не удалось прочитать " + file, e);
        }
    }

    private void write() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(new ArrayList<>(products));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось записать " + file, e);
        }
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
        write();
    }

    @Override
    public void deleteById(Long id) {
        products.removeIf(productItem -> productItem.getId().equals(id));
        write();
    }
}
