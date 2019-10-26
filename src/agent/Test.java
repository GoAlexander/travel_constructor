/* Course work
 * -----------
 * 
 * Test.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

class Test {
	
	// only for checking of link between class and individual!
	//TODO: check elements existing
	static String isValid(IRI element1, IRI element2, IRI element3, VarHolder varHolder) {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		OWLClass owlClass = factory.getOWLClass(element1);
		OWLIndividual ind = factory.getOWLNamedIndividual(element1);
		
		
		// Service001-excursion-t8-ex002
		// Service002-duel-t8-ex001
		// Service003-costumeshow-t8-ex
		
		String[] elements  = {element1.getShortForm(), element2.getShortForm(), element3.getShortForm()};
		
		boolean timeOK = false;
		ArrayList<String> time = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			Pattern timePattern = Pattern.compile("-t\\d+");
			Matcher timeMatcher = timePattern.matcher(elements[i]);
			
	        while(timeMatcher.find()) {
	            time.add(timeMatcher.group());
	            //System.out.println(timeMatcher.group());
	        }
		}
        timeOK = time.stream().distinct().limit(2).count() <= 1;

		
		boolean excludeOK = true;
		ArrayList<String> exclude = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			Pattern excludePattern = Pattern.compile("-ex\\d+");
			Matcher excludeMatcher = excludePattern.matcher(elements[i]);
			
	        while(excludeMatcher.find()) {
	        	exclude.add(excludeMatcher.group());
	        }
	        
		}
		
		if (timeOK && excludeOK) {
			return "> Choosed services are compatible!"; 
		}
		else if (timeOK == false) {
			return "> Choosed services are NOT compatible due a time!\n> Choose another services.";
		}
		else {
			return "> Choosed services are NOT compatible!\n> Choose another services.";
		}
	}
	
	// ---------------------------------------------------------------------
	// Merging ontologies
	// (not for agents use only for "human" use)
	// ---------------------------------------------------------------------
	
	/*
	 * This method merges all ontologies in manager and then creates new file with merged ontologies on the server.
	 */
	static void mergeOntologies(String newOnt, VarHolder varHolder) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		String internalOnt = varHolder.getInternalOnt();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		String ftpNameOfOntFile = varHolder.getFtpNameOfOntFile();
		String ftpURL = varHolder.getFtpURL();
		String ftpLogin = varHolder.getFtpLogin();
		String ftpPassword = varHolder.getFtpPassword();
		
		
		//одну онтологию мы уже имеем, а эта придет как параметр
		manager.loadOntologyFromOntologyDocument(IRI.create(newOnt)); 
		
		OWLOntologyMerger merger = new OWLOntologyMerger(manager); // Create our ontology merger
		// We merge all of the loaded ontologies. Since an OWLOntologyManager is
		// an OWLOntologySetProvider we just pass this in. We also need to
		// specify the URI of the new ontology that will be created.
		IRI mergedOntologyIRI = IRI.create("Merged" + internalOnt);
		OWLOntology merged = merger.createMergedOntology(manager, mergedOntologyIRI);	
		//for (OWLAxiom ax : merged.getAxioms()) { // Print out the axioms in the merged ontology.
		//	System.out.println(ax);
		//}
		
		FTPClient client = new FTPClient();
		InputStream input = null;
		try {
		    client.connect(ftpURL); //here link to the ftp-server
		    client.login(ftpLogin, ftpPassword); //here login and password of ftp-server
		    client.enterLocalPassiveMode();
		    
		    // create file in tmp folder
		    File fileformated = new File("./tmp/Merged" + ftpNameOfOntFile);
			manager.saveOntology(merged, IRI.create(fileformated.toURI()));
			
			// send file to the server
            input = new FileInputStream(fileformated);
            client.storeFile("Merged" + ftpNameOfOntFile, input);	 
		} finally {
		    try {
		    	input.close();
		        client.logout();
		        client.disconnect();
		    } catch (Exception e) {
		    }
		}
		
		//if (flag == true) {
			//удаляем лишнее из новой онтологии
			//manager.getOntologyStorers().clear();//но это затронет все онтологии... Нормально?
			
			//подменяем файл онтологии на сервере
			//...
			/* //не соответствует предложенной концепции, оставлено для примера
			try {
			    client.connect(ftpURL); //here link to the ftp-server
			    client.login(ftpLogin, ftpPassword); //here login and password of ftp-server
			    client.enterLocalPassiveMode();
			    out = client.storeFileStream(ftpNameOfOntFile); //here name of ontology file on server
			    String tmp = out.toString().replaceAll(internalOnt + "Merged", internalOnt);
			    System.out.println(tmp); //ERROR!
			    // out = tmp
			    out.write(tmp.getBytes(Charset.forName("UTF-8")));
				
			} finally {
			    try {
			        out.close();
			        client.logout();
			        client.disconnect();
			    } catch (Exception e) {
			    }
			}
			*/
			
			//инициализируем онтологию
			//ontology = initializeOnt();
		//}	
	
	}
	
	
	// ---------------------------------------------------------------------
	// Methods for testing
	// ---------------------------------------------------------------------
	
	//source code from this example: https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java#L911
	//see the comments in link
	static private void ontContainsWithReasoner(IRI object, VarHolder varHolder) {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		
		System.out.println("TEST: ontContainsWithReasoner(IRI object)");
	
        //OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();PelletReasonerFactory()
		OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
        
        reasoner.precomputeInferences();
        boolean consistent = reasoner.isConsistent();
        System.out.println("Consistent: " + consistent);
        System.out.println("\n");

        Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses(); //TODO from OWL API distribution?

        Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
        if (!unsatisfiable.isEmpty()) {
            System.out.println("The following classes are unsatisfiable: ");
            for (OWLClass cls : unsatisfiable) {
                System.out.println("    " + cls);
            }
        } else {
            System.out.println("There are no unsatisfiable classes");
        }
        System.out.println("\n");

        OWLDataFactory fac = manager.getOWLDataFactory();

        OWLClass vegPizza = fac.getOWLClass(object);

        NodeSet<OWLClass> subClses = reasoner.getSubClasses(vegPizza, true);

        Set<OWLClass> clses = subClses.getFlattened();
        System.out.println("Subclasses of vegetarian: ");
        for (OWLClass cls : clses) {
            System.out.println("    " + cls);
        }
        System.out.println("\n");

        OWLClass country = fac.getOWLClass(object);

        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(country,
                true);
 
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        System.out.println("Instances of pet: ");
        for (OWLNamedIndividual ind : individuals) {
            System.out.println("    " + ind);
        }
        System.out.println("\n");
        
        /*
        //TODO IMPORTANT! IMPLEMENT IN THE FUTURE!
        OWLNamedIndividual mick = fac.getOWLNamedIndividual(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#Mick"));
        // Let's get the pets of Mick Get hold of the has_pet property which has
        // a full IRI of <http://owl.man.ac.uk/2005/07/sssw/people#has_pet>
        OWLObjectProperty hasPet = fac.getOWLObjectProperty(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#has_pet"));
        // Now ask the reasoner for the has_pet property values for Mick
        NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(mick, hasPet);
        Set<OWLNamedIndividual> values = petValuesNodeSet.getFlattened();
        System.out.println("The has_pet property values for Mick are: ");
        for (OWLNamedIndividual ind : values) {
            System.out.println("    " + ind);
        }
        */
	}
	
	
	// ---------------------------------------------------------------------
	// Methods from algorithm
	// ---------------------------------------------------------------------
	
	//Methods to test inContradiction()	
	static boolean toTestClassCustomClass(IRI entity1, IRI predicate, IRI entity2, VarHolder varHolder) throws IOException {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass owlClass1 = factory.getOWLClass(entity1);
		OWLClass owlClass2 = factory.getOWLClass(entity2);
		
		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
		OWLClassExpression owlClassExpression = factory.getOWLObjectSomeValuesFrom(owlObjectProperty, owlClass2);
		OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(owlClass1, owlClassExpression);			
		manager.addAxiom(ontology, axiom);
        
        return Commiter.writeOntToServer(varHolder);
	}
	static boolean toTestIndCustomInd(IRI entity1, IRI predicate, IRI entity2, VarHolder varHolder) throws IOException {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLNamedIndividual owlIndividual1 = factory.getOWLNamedIndividual(entity1);
		OWLNamedIndividual owlIndividual2 = factory.getOWLNamedIndividual(entity2);
		
		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
		OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(owlObjectProperty, owlIndividual1, owlIndividual2);
		manager.addAxiom(ontology, axiom);
        
        return Commiter.writeOntToServer(varHolder);
	}

	static boolean toTestClassCustomInd(IRI entity1, IRI predicate, IRI entity2, VarHolder varHolder) throws IOException {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass owlClass1 = factory.getOWLClass(entity1);
		OWLNamedIndividual owlIndividual2 = factory.getOWLNamedIndividual(entity2);
		
		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
		OWLClassExpression owlClassExpression = factory.getOWLObjectSomeValuesFrom(owlObjectProperty, owlClass1);
		OWLAxiom axiom = factory.getOWLClassAssertionAxiom(owlClassExpression, owlIndividual2);
		
		manager.addAxiom(ontology, axiom);
        
        return Commiter.writeOntToServer(varHolder);
	}
	
	//TODO (!!!) think about input parameters -> it can be a special class (axiom-class) or string...
	//TODO need to rewrite this method. It can be more optimized
	static int inContradiction(IRI entity1, IRI predicate, IRI entity2, VarHolder varHolder) {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		String entity1Nature = ""; //TODO to class! make it as EntityType.CLASS
		String entity2Nature = ""; //TODO to class!
		
		//predicate.getShortForm(); // (1)
		
		//(2)+(3) определить вид элементов
		//TODO выделить этот блок к отдельную функцию ?!
		OWLClass owlClass1 = factory.getOWLClass(entity1);
		OWLClass owlClass2 = factory.getOWLClass(entity2);
		OWLNamedIndividual owlIndividual1 = factory.getOWLNamedIndividual(entity1);
		OWLNamedIndividual owlIndividual2 = factory.getOWLNamedIndividual(entity2);
		
		Set<OWLClass> classes = ontology.getClassesInSignature();
		Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
		
		if (classes.contains(owlClass1)) {
			entity1Nature = "Class";
		} else if (individuals.contains(owlIndividual1)) {
			entity1Nature = "Individual";
		}
		if (classes.contains(owlClass2)) {
			entity2Nature = "Class";
		} else if (individuals.contains(owlIndividual2)) {
			entity2Nature = "Individual";
		}
		
		if (varHolder.getDebug()) {
			System.out.println("DEBUG: ");
			System.out.println("Axiom: ");
			System.out.println(entity1.getShortForm() + " " + predicate.getShortForm() + " " + entity2.getShortForm());
			System.out.println("Classified entyties: ");
			System.out.println("1: " + entity1Nature);
			System.out.println("2: " + predicate.getShortForm());
			System.out.println("3: " + entity2Nature);
			System.out.println("DEBUG END. ");
		}
		
		//(4)
		if ((entity1Nature.equals("Class") && entity2Nature.equals("Class"))
				&& !(predicate.getShortForm().equals("instanceOf")
						|| predicate.getShortForm().equals("hasInstance"))) {
			OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
			OWLClassExpression owlClassExpression = factory.getOWLObjectSomeValuesFrom(owlObjectProperty, owlClass2);
			//order: Termobox hasSubClass FoodTermobox
			OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(owlClass1, owlClassExpression);		
					
		    if (varHolder.getDebug()) {
		    	System.out.println("DEBUG: ");
				System.out.println("From  (4.1): ...");
				System.out.println("DEBUG END. ");
		    }
			if(ontology.containsAxiom(axiom)){
				return 0;  // success
			} else
				return 1;
		}

		// (5)
		if (((entity1Nature.equals("Class") && entity2Nature.equals("Individual"))
				|| (entity1Nature.equals("Individual") && entity2Nature.equals("Class")))
				&& (predicate.getShortForm().equals("instanceOf")
						|| predicate.getShortForm().equals("hasInstance"))) {
			
			// (5.1) Termobox001 instanceOf FoodTermobox
			if (entity1Nature.equals("Individual") && entity2Nature.equals("Class")) {	 
			    OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass2, owlIndividual1); 
			    if (varHolder.getDebug()) {
			    	System.out.println("DEBUG: ");
					System.out.println("From  (5.1): ...");
					System.out.println("DEBUG END. ");
			    }
				if(ontology.containsAxiom(axiom)){
					return 0;  // success
				} else
					return 1;
			}
			
			// (5.2) FoodTermobox hasInstance Termobox001 
			if (entity1Nature.equals("Class") && entity2Nature.equals("Individual")) {	 
			    OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass1, owlIndividual2);
			    if (varHolder.getDebug()) {
			    	System.out.println("DEBUG: ");
					System.out.println("From  (5.2): ...");
					System.out.println("DEBUG END. ");
			    }
				if(ontology.containsAxiom(axiom)){
					return 0;  // success
				} else
					return 1;
			}
		}

		// (6)
		//TODO need discussions
		if (((entity1Nature.equals("Class") && entity2Nature.equals("Individual"))
				|| (entity1Nature.equals("Individual") && entity2Nature.equals("Class")))
				&& !(predicate.getShortForm().equals("instanceOf")
						|| predicate.getShortForm().equals("hasInstance"))) {
			
			return 1; //error
			/*
			if (entity1Nature.equals("Class") && entity2Nature.equals("Individual")) {
				OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
				OWLClassExpression owlClassExpression = factory.getOWLObjectSomeValuesFrom(owlObjectProperty, owlClass1);
				OWLAxiom axiom = factory.getOWLClassAssertionAxiom(owlClassExpression, owlIndividual2);

				if (DEBUG) {
					System.out.println("DEBUG: ");
					System.out.println("From  (6.1): ...");
					System.out.println("DEBUG END. ");
				}
				if (ontology.containsAxiom(axiom)) {
					return 0; // success
				} else
					return 1;
			}
			
			if (entity1Nature.equals("Individual") && entity2Nature.equals("Class")) {	
				System.out.println("Not ready yet (6.2)");
			}
			*/
		}
		
		
		// (7)
		if ((entity1Nature.equals("Individual") && entity2Nature.equals("Individual"))
				&& !(predicate.getShortForm().equals("instanceOf")
						|| predicate.getShortForm().equals("hasInstance"))) {
			OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
			//order: owlIndividual1 -> owlObjectProperty -> owlIndividual2
			OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(owlObjectProperty, owlIndividual1, owlIndividual2);
		    if (varHolder.getDebug()) {
		    	System.out.println("DEBUG: ");
				System.out.println("From  (7.1): ...");
				System.out.println("DEBUG END. ");
		    }
			if(ontology.containsAxiom(axiom)){
				return 0;  // success
			} else
				return 1;
		}
		
		
		// (8)
		// Beginning of unusual cases:
		if (((entity1Nature.equals("Class") && entity2Nature.equals("Class"))
				|| (entity1Nature.equals("Individual") && entity2Nature.equals("Individual")))
				&& (predicate.getShortForm().equals("instanceOf")
						|| predicate.getShortForm().equals("hasInstance"))) {
			
			// Подразумеваем, что на первом месте - instance, на третьем - type
			if (predicate.getShortForm().equals("instanceOf")) {
			    OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass2, owlIndividual1); 
			    if (varHolder.getDebug()) {
			    	System.out.println("DEBUG: ");
					System.out.println("From  (8.1): ...");
					System.out.println("DEBUG END. ");
			    }
				if(ontology.containsAxiom(axiom)){
					return 0;  // success
				} else
					return 1;
			}
			
			// Подразумеваем, что на первом месте - type, на третьем - instance
			if (predicate.getShortForm().equals("hasInstance")) {
			    OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass1, owlIndividual2);
			    if (varHolder.getDebug()) {
			    	System.out.println("DEBUG: ");
					System.out.println("From  (8.2): ...");
					System.out.println("DEBUG END. ");
			    }
				if(ontology.containsAxiom(axiom)){
					return 0; // success
				} else
					return 1;
			}
		}
		
		return 1; // error
	}
		

}
