/* Course work
 * -----------
 *
 * MyCognitiveAgent.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyCognitiveAgent {

    public static void main(String[] args) {

        //FIXME!
        Set<String> artifactoryLoggers = new HashSet<>(Arrays.asList("org.apache.http", "groovyx.net.http", "org.apache.jena.shared.LockMRSW", "org.apache.jena.riot.system.stream.StreamManager", "org.apache.jena.util.FileManager"));
        for(String log:artifactoryLoggers) {
            ch.qos.logback.classic.Logger artLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(log);
            artLogger.setLevel(ch.qos.logback.classic.Level.INFO);
            artLogger.setAdditive(false);
        }

        KbRequest myKbRequest = new KbRequest();
        myKbRequest.ontPath = "src/main/resources/public/boldino.owl"; //FIXME!
        myKbRequest.setOntology("http://localhost:8080/boldino.owl");
        //System.out.println(myKbRequest.processMessage("Termobox001 instanceOf FoodTermobox", "INFORM", "factTrue"));
        //System.out.println(myKbRequest.processMessage("Service001-excursion-t8-ex002 instanceOf Boldino-Service", "QUERY_IF", "factTrue"));
        //System.out.println(myKbRequest.processMessage("Service001-excursion-t8-ex002 instanceOf Boldino-Service", "QUERY_IF", "factTrue"));

        // Test functions:
        // 1) Check if object exists in ontology
        System.out.println("PRINT1:" + myKbRequest.processMessage("Boldino-Service", "QUERY_IF", "factTrue"));
        System.out.println("PRINT2:" + myKbRequest.processMessage("Service001-excursion-t8-ex002", "QUERY_IF", "factTrue"));

        // 2) Add new service
        System.out.println("PRINT3:" + myKbRequest.addService("Boldino-Service", "duel"));

        // 3) List services of
        System.out.println("PRINT4:" + myKbRequest.listServicesOf("Boldino-Service"));



        System.out.println("========================================");
        //Get instances
        System.out.println("PRINT:" + "Request`s value: " + myKbRequest.getInstances("Boldino-Service"));
        //Add facts
        System.out.println("PRINT:" + "Add value (0 -> all fine): " + myKbRequest.addFacts("Service001-excursion-test1 instanceOf Boldino-Service"));
        System.out.println("PRINT:" + "Request`s value: " + myKbRequest.getInstances("Boldino-Service"));
    }

}
