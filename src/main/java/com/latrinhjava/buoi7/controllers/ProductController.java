package com.latrinhjava.buoi7.controllers;

import com.latrinhjava.buoi7.models.Product;
import com.latrinhjava.buoi7.services.CategoryService;
import com.latrinhjava.buoi7.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/product-list";
    }

//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("product", new Product());
//        model.addAttribute("categories", categoryService.getAllCategories());
//        return "products/add-product";
//    }

//    @PostMapping("/add")
//    public String addProduct(@Valid Product product, BindingResult result) {
//        if (result.hasErrors()) {
//            return "products/add-product";
//        }
//
//        for (MultipartFile image : images) {
//            if (!image.isEmpty()) {
//                try {
//                    String imageUrl = saveImageStatic(image);
//                    ProductImages productImage = new ProductImages();
//                    productImage.setPhoto("/images/" + imageUrl);
//                    productImage.setProduct(product); // Set the product reference
//                    product.getProductImages().add(productImage); // Add the image to the product
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    // Optionally add error handling for individual image failures
//                }
//            }
//        }

//        productService.addProduct(product);
//        return "redirect:/products";
//    }

    @GetMapping("/edit/{id}")
    public String showFormEditBook(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable int id, @Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id);
            return "products/update-product";
        }
//        if (images == null) {
//            productService.updateProduct(product);
//            return "redirect:/products";
//        } else {
//            product.removeAllProductImages();
//            for (MultipartFile image : images) {
//                if (!image.isEmpty()) {
//                    try {
//                        String imageUrl = saveImageStatic(image);
//                        ProductImages productImage = new ProductImages();
//                        productImage.setPhoto("/images/" + imageUrl);
//                        productImage.setProduct(product);
//                        product.addProductImages(productImage);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteProduct(@PathVariable int id) {
//        productService.deleteProductById(id);
//        return "redirect:/products";
//    }

    public String listProducts(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Product> products;
        if (query != null && !query.isEmpty()) {
            products = productService.searchProducts(query);
            model.addAttribute("query", query);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "products/search";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        return listProducts(query, model);
    }

//    private String saveImageStatic(MultipartFile image) throws IOException {
//        File saveFile = new ClassPathResource("static/images").getFile();
//        String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());
//        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
//        Files.copy(image.getInputStream(), path);
//        return fileName;
//    }
}