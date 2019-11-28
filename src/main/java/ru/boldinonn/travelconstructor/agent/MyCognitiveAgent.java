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

public class MyCognitiveAgent extends CognitiveAgent {
    //Test merging:
    //protected String internalOnt = "http://x556uq/C-fact-Delivery-ootos.owl";
    //protected String ftpNameOfOntFile = "C-fact-Delivery-ootos.owl";

    //protected String internalOnt = "http://x556uq/RequirementsOfOrderManagerToDelivery-ootos.owl";
    //protected String ftpNameOfOntFile = "RequirementsOfOrderManagerToDelivery-ootos.owl";

    //static Config myConfig = new Config("MyCognitiveAgent");  //name of config file // you can edit it :)
    //protected static final String internalOnt = myConfig.getProperty("internalOnt");
    //protected static String serviceName = myConfig.getProperty("serviceName");


    public static void main(String[] args) {
        //MyCognitiveAgent d = new MyCognitiveAgent();
        //d.setup();
        KbRequest myKbRequest = new KbRequest();
        //myKbRequest.setOntology(internalOnt);
        myKbRequest.ontPath = "src/main/resources/public/boldino.owl";
        myKbRequest.setOntology("http://localhost:8080/boldino.owl");
        //System.out.println(myKbRequest.processMessage("Termobox001 instanceOf FoodTermobox", "INFORM", "factTrue"));
        //System.out.println(myKbRequest.processMessage("Termobox001 instanceOf FoodTermobox", "QUERY_IF", "factTrue"));
        //System.out.println(myKbRequest.processMessage("MedicalTermobox instanceOf Termobox", "QUERY_IF", "factTrue"));
        System.out.println("PRINT:" + myKbRequest.processMessage("Boldino-Service", "QUERY_IF", "factTrue"));
        //System.out.println(myKbRequest.processMessage("Service001-excursion-t8-ex002 instanceOf Boldino-Service", "QUERY_IF", "factTrue"));
        //System.out.println(myKbRequest.processMessage("Service001-excursion-t8-ex002 instanceOf Boldino-Service", "QUERY_IF", "factTrue"));


        //Get instances
        System.out.println("PRINT:" + "Request`s value: " + myKbRequest.getInstances("Boldino-Service"));
        //Add facts
        System.out.println("PRINT:" + "Add value (0 -> all fine): " + myKbRequest.addFacts("Service001-excursion-test1 instanceOf Boldino-Service"));
        System.out.println("PRINT:" + "Request`s value: " + myKbRequest.getInstances("Boldino-Service"));
    }

}