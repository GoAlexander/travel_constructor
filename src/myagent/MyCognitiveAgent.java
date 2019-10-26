/* Course work
 * -----------
 * 
 * MyCognitiveAgent.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package myagent;

import java.io.IOException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import agent.CognitiveAgent;
import agent.Config;
import agent.KbRequest;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MyCognitiveAgent extends CognitiveAgent {	

	//Test merging:
	//protected String internalOnt = "http://x556uq/C-fact-Delivery-ootos.owl";
	//protected String ftpNameOfOntFile = "C-fact-Delivery-ootos.owl";
	
	//protected String internalOnt = "http://x556uq/RequirementsOfOrderManagerToDelivery-ootos.owl";
	//protected String ftpNameOfOntFile = "RequirementsOfOrderManagerToDelivery-ootos.owl";

	Config myConfig = new Config("MyCognitiveAgent");  //name of config file // you can edit it :)
	protected final String internalOnt = myConfig.getProperty("internalOnt");
	protected String ftpNameOfOntFile = myConfig.getProperty("ftpNameOfOntFile");
	protected String ftpURL = myConfig.getProperty("ftpURL");
	protected String ftpLogin = myConfig.getProperty("ftpLogin");
	protected String ftpPassword = myConfig.getProperty("ftpPassword");
	protected String serviceName = myConfig.getProperty("serviceName");
	

	public static void main() {
		MyCognitiveAgent d = new MyCognitiveAgent();
		d.setup();
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
				
		addBehaviour(new CyclicBehaviour() { //TODO make it more abstract?! Add to CognitiveAgent?!
			public void action() {				
				ACLMessage message = receive();
			    
				if (message != null) {
					 System.out.println(myKbRequest.processMessage(message) );
				}
				else
					block();
			}
		});
		
	}
	
}
