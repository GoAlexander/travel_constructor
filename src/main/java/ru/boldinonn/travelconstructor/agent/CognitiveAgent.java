/* Course work
 * -----------
 *
 * CognitiveAgent.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

/*This "abstract" class register in DF.
 * Make your own class with help of inheritance and do what you want. ;)
 * Resources you need to edit:
 * 	-String internalOnt;
 * 	-makeRequest()
 *
 * EDIT_IT! -> marked all methods or variables you need to edit.
 *
 * Note: Made with help of DFRegisterAgent.java from package yellowPages from Jade examples.
 * +source code of DummyAgent
 *
 */
public abstract class CognitiveAgent extends Agent {
    private final boolean DEBUG = true;
    // protected String file_path = null;

    protected String internalOnt; // EDIT_IT!
    protected String ftpNameOfOntFile; // EDIT_IT!
    protected String ftpURL; // EDIT_IT!
    protected String ftpLogin; // EDIT_IT!
    protected String ftpPassword; // EDIT_IT!
    protected String serviceName; // EDIT_IT!

    protected void setup() {
        // Read the name of the service to register as an argument
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            serviceName = args[0].toString();
        }

        // Register the service
        System.out.println("Agent " + getLocalName() + " registering service \"" + serviceName + " \" ");
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setName(serviceName);

            sd.setType("My Test"); // edit !
            // sd.addOntologies("http://hp-note/Pizzeria_DEMO_myVersion.owl");
            // only for sharing ontologies
            sd.addOntologies(internalOnt);
            if (DEBUG)
                System.out.println("Test: " + sd.getAllOntologies().next());

            // save ontology destination (ONLY for sharing ontologies)
            // Ont = sd.getAllOntologies().next().toString();

            dfd.addServices(sd);

            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        makeRequest();
        sendFact();
    }

    protected void makeRequest() { // EDIT_IT!
    }

    // http://stackoverflow.com/questions/28671830/jade-two-agents-communication
    // also read JADEProgramming-Tutorial-for-beginners.pdf (5.2 Sending
    // messages)
    protected void sendFact() { // EDIT_IT!

    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Printout a dismissal message
        System.out.println("Register agent " + getAID().getName() + " is terminating.");
    }

}
