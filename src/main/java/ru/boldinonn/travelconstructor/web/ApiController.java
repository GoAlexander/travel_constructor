package ru.boldinonn.travelconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.boldinonn.travelconstructor.agent.KbRequest;
import ru.boldinonn.travelconstructor.model.Service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ApiController {

    private static final List<String> providers = Arrays.asList("Музей Болдино", "Столовая", "Полёты на шаре", "Костюмированные шоу");

    private static final Map<String, List<ServiceOfProvider>> servicesMap = initMap();

    private static Map<String, List<ServiceOfProvider>> initMap() {
        Map<String, List<ServiceOfProvider>> map = new HashMap<>();
        ServiceOfProvider museumTicket = new ServiceOfProvider("Экскурсия «Усадьба Пушкиных в Болдине»",
                1000,
                Arrays.asList(
                        OffsetDateTime.parse("2022-05-03T10:00:00+03:00"),
                        OffsetDateTime.parse("2022-05-03T12:00:00+03:00"))
        );

        ServiceOfProvider photo = new ServiceOfProvider("Фотосессия 1 час",
                2500,
                Arrays.asList(
                        OffsetDateTime.parse("2022-05-03T10:00:00+03:00"),
                        OffsetDateTime.parse("2022-05-03T11:00:00+03:00"),
                        OffsetDateTime.parse("2022-05-03T15:00:00+03:00"))
        );

        ServiceOfProvider gift = new ServiceOfProvider("Экскурсия «В мире литературных героев «Повестей Белкина»",
                1200,
                Arrays.asList(
                        OffsetDateTime.parse("2022-05-03T10:00:00+03:00"),
                        OffsetDateTime.parse("2022-05-03T11:00:00+03:00"),
                        OffsetDateTime.parse("2022-05-03T14:00:00+03:00"))
        );

        map.put(providers.get(0), Arrays.asList(museumTicket, photo, gift));

        return map;
    }

    @Autowired
    private KbRequest kbRequest;

    @GetMapping
    public String hello() {
        return "Hello from Travel constructor API";
    }

    @GetMapping("/processMessage")
    public String ontologyContains(@RequestParam("message") String message, @RequestParam("performative") String performative, @RequestParam("type") String type) {
        return kbRequest.processMessage(message, performative, type);
    }

    @GetMapping("/providers")
    public List<String> getProviders() {
        //TODO add ontology call
        return providers; 
    }

    @GetMapping("/servicesOf/{provider}")
    public List<ServiceOfProvider> getServicesOf(@PathVariable(value = "provider") String provider) {
        return servicesMap.get(provider);
        //TODO add ontology call
        //return kbRequest.listServicesOf(provider);
    }

    @PostMapping("/addServiceToCart")
    public CartItem addServiceToCart(@RequestBody CartItem cartItem) {
        return cartItem;
    }

    @GetMapping("/getOrder")
    public String getOrder() {
        //TODO add ontology call
        return "Order successfully created!";
    }

    @PostMapping("/service")
    public String service(@RequestBody Service service) {
        return kbRequest.addService(service.getSuperClass(), service.getSubClass());
    }



}
