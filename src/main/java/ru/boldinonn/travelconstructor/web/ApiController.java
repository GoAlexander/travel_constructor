package ru.boldinonn.travelconstructor.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping
    public String hello() {
        return "Hello from Travel constructor";
    }

    @GetMapping("/ontContains")
    public boolean ontologyContains(String object) {
        return true;
    }

}
