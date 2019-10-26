/* Course work
 * -----------
 *
 * KbRequest.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.BasicConfigurator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import jade.lang.acl.ACLMessage;

/*
 * Remember that name space (for queryStr) and path to ontology SHOULD be similar!
 */
public class KbRequest {
	private final boolean DEBUG = true;
	private final boolean LOGS = false;
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private String internalOnt = null;
	protected String ftpNameOfOntFile = null;
	protected String ftpURL = null;
	protected String ftpLogin = null;
	protected String ftpPassword = null;

	private VarHolder varHolder; //to store all variables for static methods

	//for logs
	Logger logger = Logger.getLogger(this.getClass().getName());
	SimpleFormatter formatter = new SimpleFormatter();
	FileHandler fh;

	Date currentTime = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");

	BufferedWriter bw; //for logs of DB

	/*
	 * This method receives destination of ontology (and name space (They SHOULD
	 * be similar!)). In addition, it gets ftpURL, ftpLogin and ftpPassword to
	 * get access to ftp server (only with these variables you can change
	 * ontology file on the server!)
	 */
	public void setOntology(String internalOnt, String ftpNameOfOntFile, String ftpURL, String ftpLogin,
							String ftpPassword) {
		this.internalOnt = internalOnt;
		this.ftpNameOfOntFile = ftpNameOfOntFile;
		this.ftpURL = ftpURL;
		this.ftpLogin = ftpLogin;
		this.ftpPassword = ftpPassword;

		//initialize ontology
		if (internalOnt != null) {
			manager = initializeOntManager();
			try {
				ontology = initializeOnt();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}

		//initialize agent logs
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("./logs/" + "AgentLog" + ".log", true); //TODO name the agent!
			logger.setUseParentHandlers(false); // to disable output to the console
			logger.addHandler(fh);
			fh.setFormatter(formatter);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		logger.info("\n\nAgent started at " + sdf.format(currentTime) +
				"\n================================================");

		//initialize ontology logs
		try {
			bw = new BufferedWriter(new FileWriter("./logs/" + ftpNameOfOntFile + ".log", true));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//now create VarHolder
		varHolder = new VarHolder(DEBUG, LOGS, manager, ontology, internalOnt, ftpNameOfOntFile, ftpURL, ftpLogin, ftpPassword);
	}


	// ---------------------------------------------------------------------
	// Initialization of ontology.
	// ---------------------------------------------------------------------

	private OWLOntologyManager initializeOntManager() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		return manager;
	}

	private OWLOntology initializeOnt() throws OWLOntologyCreationException {
		if (internalOnt == null) {
			throw new IllegalArgumentException("File: " + internalOnt + " not found");
		}
		OWLOntology ontology = manager.loadOntology(IRI.create(internalOnt));

		return ontology;
	}

	// ---------------------------------------------------------------------
	// Methods to process inputed commands and data.
	// ---------------------------------------------------------------------

	public String processMessage(ACLMessage message) {
		String output = null;
		boolean success = false;
		Axiom myAxiom;

		if (message == null)
			return "1"; //error

		if (message.getPerformative() == ACLMessage.INFORM) { // if message is  -> transfer of fact (knowledge)
			switch (message.getUserDefinedParameter("type")) {
				case "factTrue":
					myAxiom = new Axiom(message.getContent(), internalOnt);
					success = addFact(myAxiom);

					output = "INFORM (factTrue):";
					output += success ? "no_errors" : "error"; // if(success == true) -> "no_errors"


					break;
				case "factFalse":  // if message -> agent-sender wants to delete entity (individual)
					try {
						myAxiom = new Axiom(message.getContent(), internalOnt);
						success = InformFactFalse.deleteInstance(myAxiom, varHolder);

						output = "INFORM (factFalse):";
						output += success ? "no_errors" : "error";

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				case "service-merge":
					try {
						Test.mergeOntologies(message.getContent(), varHolder);
						output = "INFORM (service-merge):" + "Merging with <" + message.getContent() + "> completed!";
					} catch (OWLOntologyCreationException | OWLOntologyStorageException | IOException e) {
						e.printStackTrace();
					}

					// only for methods which are in testing stage
					break;
				case "service-test":
					//ontContainsWithReasoner(IRI.create(internalOnt + "#" + message.getContent())); //it is to test Pellet!
					String[] triple = strTriple(message.getContent()); //TODO rewrite with AXIOM class!

					IRI entity1 = IRI.create(internalOnt + "#" + triple[0]);
					IRI predicate = IRI.create(internalOnt + "#" + triple[1]);
					IRI entity2 = IRI.create(internalOnt + "#" + triple[2]);
					//To test. Begin.
					//try {
					//(4)
					//toTestClassCustomClass(entity1, predicate, entity2);
					//(6)
					//toTestClassCustomInd(entity1, predicate, entity2);
					//(7)
					//toTestIndCustomInd(entity1, predicate, entity2);
					//} catch (IOException e) {
					//e.printStackTrace();
					//}
					//To test. End.
					System.out.println(Test.inContradiction(entity1, predicate, entity2, varHolder)); //TODO tmp

					break;
				case "is-valid":
					// check if elements can be "together"
					String[] elements = strTriple(message.getContent()); //TODO rewrite with AXIOM class!

					IRI element1 = IRI.create(internalOnt + "#" + elements[0]);
					IRI element2 = IRI.create(internalOnt + "#" + elements[1]);
					IRI element3 = IRI.create(internalOnt + "#" + elements[2]);

					System.out.println("Called is-valid():");
					System.out.println("    Received message: " + message.getContent());
					System.out.println(Test.isValid(element1, element2, element3, varHolder));

					break;
			}

		} else if (message.getPerformative() == ACLMessage.REQUEST) { // if message -> agent-sender wants to know something (request)
			System.out.println("Not implemented yet...");
			return "1"; //error

		} else if (message.getPerformative() == ACLMessage.QUERY_IF) { // if message -> agent-sender wants to know this message is true or false
			myAxiom = new Axiom(IRI.create(internalOnt + "#" + message.getContent()));
			myAxiom.setOntology(internalOnt);
			output = "QUERY_IF:" + Boolean.toString( processQueryIF(myAxiom) );
		}
		else
			return "1"; //error

		output = sdf.format(currentTime) + "-" + message.getSender() + "-" + "\"" + message.getContent() + "\"" + output;
		logger.info(output);

		//log database
		if (message.getPerformative() == ACLMessage.INFORM && success == true) {
			try {
				bw.write("INFORM(" + message.getUserDefinedParameter("type") + "): " + message.getContent() + "\n");
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return output; //output for logs
	}

	private boolean addFact(Axiom myAxiom) {
		try {
			return InformFactTrue.addInstance(myAxiom, varHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO add DataType (если тип аксиомы такой-то)
		//тип аксиомы отпределяется спец. функцией из класса Axiom
		//for this method add some analyzing to Axiom class
		return false; // error
	}

	// For ontContains():
	private boolean processQueryIF(Axiom myAxiom) {
		if (myAxiom.getNumberOfObjects() == 3) {
			return QueryIf.ontContains(myAxiom, varHolder);
		} else if (myAxiom.getNumberOfObjects() == 1) {
			return QueryIf.ontContains(myAxiom.getEntity1(), varHolder);
		}

		return false; // error
	}

	private String[] strTriple(String strTriple) {
		String[] triple = strTriple.split(" ");
		return triple;
	}

}
