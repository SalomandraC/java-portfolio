package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService prodService;

    public ProductController(ProductService prodService) {
        this.prodService = prodService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", prodService.findAll());
        model.addAttribute("stats", prodService.categoryStats());
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "productForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Price must be number");
            return "productForm";
        }
        try {
            prodService.save(product);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "productForm";
        }
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Product product = prodService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукт с id=" + id + " не найден");
        }
        model.addAttribute("product", product);
        return "productDetails";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        prodService.deleteById(id);
        return "redirect:/products";
    }
}
