/* Course work
 * -----------
 *
 * KbRequest.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/*
 * Remember that name space (for queryStr) and path to ontology SHOULD be similar!
 */
public class KbRequest {
    private final boolean DEBUG = true;
    private final boolean LOGS = false;
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private String internalOnt = null;

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
    public void setOntology(String internalOnt) {
        this.internalOnt = internalOnt;

        //initialize ontology
        if (internalOnt != null) {
            manager = initializeOntManager();
            try {
                ontology = initializeOnt();
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }

        //now create VarHolder
        varHolder = new VarHolder(DEBUG, LOGS, manager, ontology, internalOnt);
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
        File file = new File("/home/alexander/Files/Workspaces/IdeaProjects/tmp", "RequirementsOfOrderManagerToDelivery-reification.owl");

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);


        return ontology;
    }

    // ---------------------------------------------------------------------
    // Methods to process inputted commands and data.
    // ---------------------------------------------------------------------

    public String processMessage(String message, String performative, String type) {
        String output = null;
        boolean success = false;
        Axiom myAxiom;

        if (message == null)
            return "1"; //error

        myAxiom = new Axiom(message, internalOnt);
        success = addFact(myAxiom);

        output = "INFORM (factTrue):";
        output += success ? "no_errors" : "error"; // if(success == true) -> "no_errors"

        if (performative == "INFORM") { // if message is  -> transfer of fact (knowledge)
            switch (type) {
                case "factTrue":
                    myAxiom = new Axiom(message, internalOnt);
                    success = addFact(myAxiom);

                    output = "INFORM (factTrue):";
                    output += success ? "no_errors" : "error"; // if(success == true) -> "no_errors"


                    break;
                case "factFalse":  // if message -> agent-sender wants to delete entity (individual)
                    try {
                        myAxiom = new Axiom(message, internalOnt);
                        success = InformFactFalse.deleteInstance(myAxiom, varHolder);

                        output = "INFORM (factFalse):";
                        output += success ? "no_errors" : "error";

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "service-test":
                    //..
                    break;
            }

        } else if (performative == "REQUEST") { // if message -> agent-sender wants to know something (request)
            System.out.println("Not implemented yet...");
            return "1"; //error

        } else if (performative == "QUERY_IF") { // if message -> agent-sender wants to know this message is true or false
            myAxiom = new Axiom(IRI.create(internalOnt + "#" + message));
            myAxiom.setOntology(internalOnt);
            output = "QUERY_IF:" + Boolean.toString(processQueryIF(myAxiom));
        } else
            return "1"; //error

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
