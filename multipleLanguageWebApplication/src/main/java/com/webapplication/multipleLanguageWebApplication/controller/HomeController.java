package com.webapplication.multipleLanguageWebApplication.controller;

import com.webapplication.multipleLanguageWebApplication.model.Product;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(@RequestParam(name = "name", required = false) String name,
        Model model) {
        if (Strings.isEmpty(name)) {
            name = "Hello world";
        }
        String message = "Hi " + name + "!";

        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/product")
    public String getProducts(Model model) {
        List<Product> products = new LinkedList<>();
        products.add(new Product(1, "Uchiha Sasuke", 10));
        products.add(new Product(2, "Naruto", 12));
        products.add(new Product(3, "Tomori", 23));
        products.add(new Product(4, "Kanji", 23));
        model.addAttribute("products", products);
        return "Product";
    }
}
