/* Course work
 * -----------
 * 
 * SenderAgent.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package myagent;

import java.io.IOException;

import agent.CognitiveAgent;
import agent.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class SenderAgent extends CognitiveAgent {

	Config myConfig = new Config("SenderAgent"); //name of config file // you can edit it :)
	
	protected String internalOnt = myConfig.getProperty("internalOnt");
	protected String ftpNameOfOntFile = myConfig.getProperty("ftpNameOfOntFile");
	protected String ftpURL = myConfig.getProperty("ftpURL");
	protected String ftpLogin = myConfig.getProperty("ftpLogin");
	protected String ftpPassword = myConfig.getProperty("ftpPassword"); 
	protected String serviceName = myConfig.getProperty("serviceName");

	
	public static void main() {
		SenderAgent d = new SenderAgent();
		d.setup();
	}
	
	protected void makeRequest() {
		
	}

	protected void sendFact() {
		/*
		 *for sniffer
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addUserDefinedParameter("type", "factTrue");
		message.addReceiver(new AID("CognitiveAgent", AID.ISLOCALNAME)); // Unknown -
																	// name of
																	// agent
																	// which
																	// gets
		
		
		//BOLDINO TESTS
		message.setPerformative(ACLMessage.QUERY_IF);
		message.setContent("Vasia");
		send(message);
		message.setContent("Baker");
		send(message);
		message.setContent("Transaction");
		send(message);
		message.setContent("Service");
		send(message);
		message.setContent("ProductOrService");
		send(message);
		message.setContent("Boldino-Service");
		send(message);
		message.setContent("Service001-excursion");
		send(message);

	
		//Add certain boldino services (1 time)
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factTrue");
		message.setContent("Service001-excursion-t8-ex002 instanceOf Boldino-Service");
		send(message);
		message.setContent("Service002-duel-t2-ex001 instanceOf Boldino-Service");
		send(message);
		message.setContent("Service003-costumeshow-t8-ex instanceOf Boldino-Service");
		send(message);
		message.setContent("Service004-lunch-t8-ex instanceOf Boldino-Service");
		send(message);


		
		//Use-case:
		//Check chosen services compatibility
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "is-valid");
		message.setContent("Service001-excursion-t8-ex002 Service002-duel-t2-ex001 Service003-costumeshow-t8-ex");
		send(message); //should be fail because of the time
		message.setContent("Service002-duel-t2-ex001 Service003-costumeshow-t8-ex Service004-lunch-t8-ex");
		send(message);
		message.setContent("Service001-excursion-t8-ex002 Service003-costumeshow-t8-ex Service004-lunch-t8-ex");
		send(message);
		
		
		
		
		
		
		
		
		/*
		//test of Pellet
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "service-test");
		message.setContent("Termobox");
		send(message);
		*/
		
		/*
		//test of mergeOntologies(String newOnt)
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "service-merge");
		message.setContent("http://x556uq/RequirementsOfOrderManagerToDelivery-reification2.owl");
		send(message);
		*/
		
		//TODO write different tests in different functions
		//or better use junit... 
		
		// ---------------------------------------------------------------------
		// Test of addInstance():
		// ---------------------------------------------------------------------
		
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factTrue");
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);
		message.setContent("Termobox002 instanceOf FoodTermobox");
		send(message);
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);
		message.setContent("Termobox001 instanceOf MedicalTermobox");
		send(message);

		// test multi triples:
		//outdated line: (!!!)
		//message.setContent("C-actProposition555 instanceOf C-actProposition,PizzaDelivered555 instanceOf PizzaDelivered,C-actProposition555 satisfies PizzaDelivered555");
		*/
		
		// ---------------------------------------------------------------------
		// Test of deleteInstance():
		// ---------------------------------------------------------------------
		//Case of only 1 instance
		//Note: better to test this deletion in different sessions
		//I mean add instance then exit from agent and then delete it
		
//		message.setPerformative(ACLMessage.INFORM);
//		message.removeUserDefinedParameter("type");
//		message.addUserDefinedParameter("type", "factTrue");
//		message.setContent("Termobox001 instanceOf FoodTermobox");
//		send(message);
//		
//		message.setPerformative(ACLMessage.INFORM);
//		message.removeUserDefinedParameter("type");
//		message.addUserDefinedParameter("type", "factFalse");
//		message.setContent("Termobox001 instanceOf FoodTermobox");
//		send(message);
		
		
		//Case of 2 instances
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factTrue");
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);	
		message.setContent("Termobox001 instanceOf MedicalTermobox");
		send(message);	

		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factFalse");
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);
		message.setContent("Termobox001 instanceOf MedicalTermobox");
		send(message);
		*/
		
		// ---------------------------------------------------------------------
		// Test of ontContains():
		// ---------------------------------------------------------------------
		//Case 1: ontContains()
		
//		message.setPerformative(ACLMessage.INFORM);
//		message.removeUserDefinedParameter("type");
//		message.addUserDefinedParameter("type", "factTrue");
//		message.setContent("Termobox001 instanceOf FoodTermobox");
//		send(message);
//		
//		message.setPerformative(ACLMessage.QUERY_IF);
//		//message.removeUserDefinedParameter("type");
//		message.setContent("Termobox001");
//		send(message);
//		message.setContent("FoodTermobox");
//		send(message);
		
		
		//Case 2: tests of ontContains(,,)
		//(axiom contains)
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factTrue");
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);
		
		message.setContent("Termobox001 FoodTermobox"); 
		send(message);
		message.setContent("Termobox005 FoodTermobox"); 
		send(message);
		message.setContent("Termobox001 MedicalTermobox"); 
		send(message);
		*/
		
		
		// ---------------------------------------------------------------------
		// Tests of inContradictions():
		// ---------------------------------------------------------------------
		
		//Test of (4)
		//TODO also turn on method in processMessage()
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "service-test");
		message.setContent("Termobox hasSubClass FoodTermobox");
		send(message);
		*/
		
		//Test of (5)
		
		//Test of (6)
		//TODO also turn on method in processMessage()
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "service-test");
		//message.setContent("Termobox001 isImportantFor FoodTermobox"); //???
		message.setContent("FoodTermobox hasSpecialInstance Termobox001"); //???
		send(message);
		*/
		
		//Test of (7)
		//TODO also turn on method in processMessage() 
		/*
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "factTrue");
		message.setContent("Termobox001 instanceOf FoodTermobox");
		send(message);
		message.setContent("Termobox002 instanceOf FoodTermobox");
		send(message);
		
		message.setPerformative(ACLMessage.INFORM);
		message.removeUserDefinedParameter("type");
		message.addUserDefinedParameter("type", "service-test");
		message.setContent("Termobox001 isPartOf Termobox002");
		send(message);
		*/
		
		//Test of (8)
		//...
	}

}
