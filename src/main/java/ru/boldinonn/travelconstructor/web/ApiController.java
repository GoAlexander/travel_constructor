package ru.boldinonn.travelconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.boldinonn.travelconstructor.agent.KbRequest;

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

}
