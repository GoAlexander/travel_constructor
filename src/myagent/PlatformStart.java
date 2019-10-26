/* Course work
 * -----------
 * 
 * PlatformStart.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package myagent;

import agent.CognitiveAgent;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/*
 * Class for test of CognitiveAgent.java.
 * It starts Jade platform.
 */
class PlatformStart extends CognitiveAgent {

	public static void main(String[] args) throws InterruptedException, StaleProxyException { //delete overhead???
		
		// Get a hold to the JADE runtime
		Runtime rt = Runtime.instance();
		
		// Launch the Main Container (with the administration GUI on top)
		// listening on port 8888
		System.out.println(">>>>>>>>>>>>>>> Launching the platform Main Container...");
		Profile pMain = new ProfileImpl(null, 8888, null); //port 8888
		pMain.setParameter(Profile.GUI, "true");
		ContainerController mainCtrl = rt.createMainContainer(pMain);
		
		java.util.List<AgentController> agentContainer = new java.util.ArrayList<AgentController>();		
		
		// Create and start an agent of class RegisterAgent
		System.out.println(">>>>>>>>>>>>>>> Starting up a RegisterAgent...");
		// Create a new agent, a DummyAgent
		// and pass it a reference to an Object
		Object reference = new Object();
		Object arguments[] = new Object[1];
		arguments[0]=reference;
		
		//Here you can start new agents
		//AgentController snifferAgentCtrl = mainCtrl.createNewAgent("Sniffer","jade.tools.sniffer.Sniffer", null);
		AgentController registerAgentCtrl = mainCtrl.createNewAgent("CognitiveAgent", myagent.MyCognitiveAgent.class.getName(), arguments);
		//sender agent
		AgentController registerSenderAgent = mainCtrl.createNewAgent("SenderAgent", myagent.SenderAgent.class.getName(), arguments);
		
		//snifferAgentCtrl.start();
		registerAgentCtrl.start();
		registerSenderAgent.start();
		Thread.sleep(20000);
		agentContainer.add(registerAgentCtrl);
		
		System.out.println("Test.");

	}

}
