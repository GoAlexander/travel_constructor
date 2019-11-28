/* Course work
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.log4j.BasicConfigurator;


import java.util.ArrayList;
import java.util.List;

/*
 * Remember that name space (for queryStr) and path to ontology SHOULD be similar!
 */
public class KbRequest {
    private final boolean DEBUG = false;
    private final boolean LOGS = false;
    private OntModel model;
    private String internalOnt = null;
    public String ontPath = null;

    /*
     * This method receives destination of ontology (and name space (They SHOULD be similar!)).
     * In addition, it gets ftpURL, ftpLogin and ftpPassword to get access to ftp server
     * (only with these variables you can change ontology file on the server!)
     */
    public void setOntology(String internalOnt) {
        this.internalOnt = internalOnt;
    }

    public String getInternalOnt() {
        return internalOnt;
    }

    //---------------------------------------------------------------------
    //Methods to read data from ontology file.
    //---------------------------------------------------------------------

    public List<String> getInstances(String requestName) {
        // establish a connection
        try {
            if (internalOnt != null) { // throw exception???
                model = initializeOntModel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setQueryToGetInstance(requestName);
    }


    //WARNING! This method is proof of concept. With help of this, method you can query different information!
    public List<String> getInfo(String prefix, String option, String requestName) {
        // establish a connection
        try {
            if (internalOnt != null) { // throw exception???
                model = initializeOntModel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setQuery(prefix, option, requestName);
    }


    //TODO catch NullPointerException!!! (If nothing found!)
    public List<String> jgetInstances(String object) {
        List<String> results = new ArrayList<>();

        OntClass ontClass = model.getOntClass( internalOnt + "#" + object );
        Resource individual = null;
        for ( ExtendedIterator<? extends OntResource> individuals = ontClass.listInstances(); individuals.hasNext(); ) {  // Get each of its instances.
            individual = individuals.next();
            results.add(individual.getLocalName());
        }

        return results;
    }



    /*
     * This method initializes the ontology model.
     *
     * Output: OntModel (initialized ontology model)
     */
    private OntModel initializeOntModel() throws MalformedURLException, IOException {

        OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_DL_MEM); //old: OWL_MEM_MICRO_RULE_INF //? OWL_MEM_RULE_INF
        model = ModelFactory.createOntologyModel(ontModelSpec);

        // Alternatively you can write so (through inputStream):
        // uncomment all(!) code written below(!)
        /*
         * InputStream input = FileManager.get().open(filePath); InputStream
         * input = new URL(filePath).openStream();
         *
         * if (input == null) { throw new IllegalArgumentException("File: " +
         * filePath + " not found"); }
         */


        if (internalOnt == null) {
            throw new IllegalArgumentException("File not found");
        }

        /*
         * model.read(input, "");
         */
        model.read(internalOnt);

        return model;
    }

    //execute query and return results to the class which calls this functions
    private List<String> handleQuery(String queryStr){
        Query query;
        QueryExecution qe;
        ResultSet result;
        List<String> resultList = new ArrayList<String>();

        query = QueryFactory.create(queryStr);
        qe = QueryExecutionFactory.create(query, model);
        result = qe.execSelect();
        List<QuerySolution> list = ResultSetFormatter.toList(result);

        //for each element of list
        for (QuerySolution qs : list) {
            String resultStr = qs.get("x").toString();
            if (resultStr != null) {
                resultStr = resultStr.substring(resultStr.lastIndexOf("#") + 1);
            }
            resultList.add(resultStr);
        }

        return resultList;
    }

    //This method creates query.
    private List<String> setQuery(String prefix, String option, String requestName) {
        if (LOGS) {
            BasicConfigurator.configure(); //for logs!
        }

        //String NS = "http://www.semanticweb.org/xenia/ontologies/2015/10/untitled-ontology-9#";
        //String NS = "http://hp-note/Pizzeria_DEMO_myVersion.owl#";
        String queryStr = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX pr: <" + internalOnt +"#> "
                + "SELECT DISTINCT ?x FROM <" + internalOnt + "#> "
                + "WHERE { ?x " + prefix + ":" + option + " pr:" + requestName + ". "
                + "FILTER ( ?x != pr:" + requestName + ") ."
                + "FILTER ( ?x != owl:Nothing). }";

        return handleQuery(queryStr);
    }

    private List<String> setQueryToGetInstance(String requestName) {
        if (LOGS) {
            BasicConfigurator.configure(); //for logs!
        }

        //String NS = "http://www.semanticweb.org/xenia/ontologies/2015/10/untitled-ontology-9#";
        //String NS = "http://hp-note/Pizzeria_DEMO_myVersion.owl#";
        String queryStr = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX pr: <" + internalOnt +"#> "
                + "SELECT DISTINCT ?x FROM <" + internalOnt + "#> "
                + "WHERE { ?x " + "rdf" + ":" + "type" + " pr:" + requestName + ". "
                + "FILTER ( ?x != pr:" + requestName + ") ."
                + "FILTER ( ?x != owl:Nothing). }";

        return handleQuery(queryStr);
    }

    //---------------------------------------------------------------------
    //Methods which return true/false.
    //---------------------------------------------------------------------

    public boolean ontContains (String object) {
        //System.out.println(internalOnt + "#" + object);
        //Resource r = model.createResource(internalOnt + "#" + object);
        //TODO: temporary hardcode! Fix!

        try {
            initializeOntModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("http://localhost:8080/boldino.owl" + "#" + object);
        Resource r = model.createResource("http://localhost:8080/boldino.owl" + "#" + object);

        // these lines implement: does model contain r as a subject?
        return model.contains(r, null, (RDFNode) null);
    }

    //---------------------------------------------------------------------
    //Methods to add new knowledge.
    //---------------------------------------------------------------------
    //TODO in fact this is abstraction over inserting methods
    //so it is better to add these methods into section below

    public int integrateKnowledge(String strTriple) {
        String[] triple = strTriple(strTriple);

        //TODO make output of errors about triple[0] / [1] / [2]
        if(ontContains(triple[0]) && ontContains(triple[1]) && ontContains(triple[2])) { // if element ("predicate") exists in ont
            try {
                //jmakeLink(triple[0], triple[1], triple[2]);
                return makeLink(triple[0], triple[1], triple[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 1; //error
    }

    //TODO change place...
    private int integrateKnowledge(String subject, String predicate, String object) {
        //TODO make output of errors about triple[0] / [1] / [2]
        if(ontContains(subject) && ontContains(predicate) && ontContains(object)) { // if element ("predicate") exists in ont
            try {
                //jmakeLink(triple[0], triple[1], triple[2]);
                return makeLink(subject, predicate, object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 1; //error
    }


    public String processMessage(String message, String performative, String type) {
        if (message == null)
            return "1";

        //TODO make switch?
        //TODO IMPORTANT! It returns strings!!! not bool and int!  This is very bad!
        if (performative == "INFORM") { // if message is  -> transfer of fact (knowledge)
            return Integer.toString(addFacts(message));

        } else if (performative == "REQUEST") { // if message -> agent-sender wants to know something (request)
            System.out.println("Not implemented yet...");
            return "0";

        } else if (performative == "QUERY_IF") { // if message -> agent-sender wants to know this message is true or false
            return Boolean.toString( ontContains(message) ); // TODO make checking of single word in expression
        }
        else
            return "1"; //error
    }


    //this method is very similar to addFact() but for some facts in one String
    //Example of message: (without <>) <Vasia instanceOf Backer,Mario instanceOf Backer,Mia instanceOf Backer>
    public int addFacts (String strFacts) {
        if (!strFacts.contains(",")) // support of single fact
            return addFact(strFacts);

        //else:
        String[] triples = strTriples(strFacts);
        for (String triple : triples) {
            addFact(triple);
        }

        return 0; //TODO rewrite ending of this method!!!
        //TODO how to show multiple errors in case of multiple facts?!
    }


    private int addFact(String strFact) {
        String[] triple = strTriple(strFact);

        if (triple[1].equals("instanceOf")) {
            try {
                addInstance(triple[0], triple[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0; //all right

            //TODO other CONSTANT if... (not implemented yet)
        } else if(ontContains(triple[1])) { // the last "chance" -> element is custom
            //integrateKnowledge(strTriple);
            integrateKnowledge(triple[0], triple[1], triple[2]);
            return 0;
        }
        else
            return 1; //error
    }

    private String[] strTriple(String strTriple) {
        String[] triple = strTriple.split(" ");

        return triple;
    }

    private String[] strTriples(String strTriples) {
        String[] triples = strTriples.split(",");

        return triples;
    }


    //---------------------------------------------------------------------
    //Methods to insert data into ontology file.
    //---------------------------------------------------------------------

    private int addInstance(String subject, String object) throws IOException {
        //if <subject> exists in ontology - exit
        if (ontContains(subject)) {
            return 1; //"Error! This object already exists."
        }

        // establish a connection
        try {
            if (internalOnt != null) { // throw exception???
                model = initializeOntModel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setQueryToAddNew(subject, object);
    }

    private int writeOntToServer(String queryStr) throws IOException {

        //https://github.com/apache/jena/blob/master/jena-arq/src-examples/arq/examples/update/UpdateExecuteOperations.java
        UpdateRequest request= UpdateFactory.create();
        request.add(queryStr);
        // And perform the operations.
        UpdateAction.execute(request, model);
        if (DEBUG) {
            //SSE.write(model) ;
            model.write(System.out);
        }


        /*
         * If you can`t start web server you can try this.
         * Change </opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl> on your own path.
         *
         * PrintWriter pW = new PrintWriter("/opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl", "UTF-8");
         * model.write(pW);
         */
        //TODO: HARDCODE! FIX!
        //PrintWriter pW = new PrintWriter("/home/alexander/Files/Workspaces/IdeaProjects/tmp/boldino.owl", "UTF-8");
        PrintWriter pW = new PrintWriter(ontPath, "UTF-8");

        model.write(pW);

        return 0; //TODO maybe make some checking before say that all is good?
    }


    private int setQueryToAddNew(String subject, String object) throws IOException {
        if (LOGS) {
            BasicConfigurator.configure(); //for logs!
        }

        String queryStr = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX myOnt: <" + internalOnt +"#> \n"
                +"INSERT DATA  { "
                + "myOnt:" + subject +  " " + "rdf" + ":" + "type" + " " + "myOnt:" + object
                +" . }";

        return writeOntToServer(queryStr);
    }


    //---------------------------------------------------------------------
    //Methods to make links between instances
    //---------------------------------------------------------------------


    private int makeLink(String subject, String predicate, String object) throws IOException {
        if (LOGS) {
            BasicConfigurator.configure(); //for logs!
        }

        String queryStr = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX myOnt: <" + internalOnt +"#> \n"
                +"INSERT DATA  { "
                + "myOnt:" + subject +  " " + "myOnt" + ":" + predicate + " " + "myOnt:" + object
                +" . }";

        return writeOntToServer(queryStr);
    }


	/*
	 * Development of this method FROZEN. It was proof of concept.
	 * (See the documentation to reimplement this method.)
	private int jmakeLink(String subject, String predicate, String object) throws IOException {
		Resource rSubject = model.createResource(internalOnt + "#" + subject);
		Property rPredicate = model.createProperty(internalOnt + "#" + predicate);
		Resource rObject = model.createResource(internalOnt + "#" + object);

		model.add(rSubject, rPredicate, rObject);

		return 0; // add the error handler!
	}
	*/

}