package com.genesys.gir.qa.testcopystream.Controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/hello")
    String sayHello() {
        return "Hello World!!!";
    }
}