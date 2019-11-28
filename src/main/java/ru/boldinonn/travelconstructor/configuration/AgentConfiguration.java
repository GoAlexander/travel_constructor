package ru.boldinonn.travelconstructor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.boldinonn.travelconstructor.agent.KbRequest;

@Configuration
public class AgentConfiguration {

    @Bean
    KbRequest getKbRequest() {
        KbRequest kbRequest = new KbRequest();
        kbRequest.setOntology("http://localhost:8080/boldino.owl");
        return kbRequest;
    }
}
