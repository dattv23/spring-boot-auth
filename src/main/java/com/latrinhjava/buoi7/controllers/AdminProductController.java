package com.latrinhjava.buoi7.controllers;

import com.latrinhjava.buoi7.models.Product;
import com.latrinhjava.buoi7.services.CategoryService;
import com.latrinhjava.buoi7.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("admin/products")
@Controller
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAllProducts());
        return "admin/products/index.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", categoryService.getAllCategories());
        return "admin/products/create";
    }

    @PostMapping("/create")
    public String create(@Valid Product newProduct, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("listCategory", categoryService.getAllCategories());
            return "admin/products/create";
        }
//        if (imageProduct != null && imageProduct.getSize() > 0) {
//            try {
//                File saveFile = new ClassPathResource("static/images").getFile();
//                String newImageFile = UUID.randomUUID() + ".png";
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
//                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                newProduct.setImage(newImageFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        productService.addProduct(newProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable int id, @Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id);
            return "admin/products/edit";
        }
//        if (imageProduct != null && imageProduct.getSize() > 0) {
//            try {
//                File saveFile = new ClassPathResource("static/images").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + editProduct.getImage());
//                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        productService.updateProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
}