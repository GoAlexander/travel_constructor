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

    static Config myConfig = new Config("MyCognitiveAgent");  //name of config file // you can edit it :)
    protected static final String internalOnt = myConfig.getProperty("internalOnt");
    protected static String ftpNameOfOntFile = myConfig.getProperty("ftpNameOfOntFile");
    protected static String ftpURL = myConfig.getProperty("ftpURL");
    protected static String ftpLogin = myConfig.getProperty("ftpLogin");
    protected static String ftpPassword = myConfig.getProperty("ftpPassword");
    protected static String serviceName = myConfig.getProperty("serviceName");


    public static void main(String[] args) {
        MyCognitiveAgent d = new MyCognitiveAgent();
        //d.setup();
        KbRequest myKbRequest = new KbRequest();
        myKbRequest.setOntology(internalOnt, ftpNameOfOntFile, ftpURL, ftpLogin, ftpPassword);
        System.out.println(myKbRequest.processMessage("Termobox001 instanceOf FoodTermobox"));
    }

    protected void makeRequest() {

		/*
		//for sniffer
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
        KbRequest myKbRequest = new KbRequest();
        myKbRequest.setOntology(internalOnt, ftpNameOfOntFile, ftpURL, ftpLogin, ftpPassword);
        System.out.println(myKbRequest.processMessage("Termobox001 instanceOf FoodTermobox"));

    }

}