package br.com.mybar.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {

    @GetMapping("/")
    public String getHome()
    {
        return "Application for managing a bar";
    }
}
