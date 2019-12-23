package ru.boldinonn.travelconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.boldinonn.travelconstructor.agent.KbRequest;
import ru.boldinonn.travelconstructor.model.Service;

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

    @PostMapping("/service")
    public String service(@RequestBody Service service) {
        return kbRequest.addService(service.getSuperClass(), service.getSubClass());
    }



}
