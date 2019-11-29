package ru.boldinonn.travelconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.boldinonn.travelconstructor.agent.KbRequest;

import java.util.List;

@RestController
public class ApiController {

    @Autowired
    private KbRequest kbRequest;

    @GetMapping
    public String hello() {
        return "Hello from Travel constructor";
    }

    @GetMapping("/processMessage")
    public String ontologyContains(@RequestParam("message") String message, @RequestParam("performative") String performative, @RequestParam("type") String type) {
        return kbRequest.processMessage(message, performative, type);
    }

    @GetMapping("/listServicesOf")
    public List<String> listServicesOf(@RequestParam String superClass) {
        return kbRequest.listServicesOf(superClass);
    }

    @PostMapping("/addService")
    public String addService(@RequestParam("superClass") String superClass, @RequestParam("subClass") String subClass) {
        return kbRequest.addService(superClass, subClass);
    }



}
