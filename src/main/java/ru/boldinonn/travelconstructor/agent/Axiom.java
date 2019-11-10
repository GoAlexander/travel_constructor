/* Course work
 * -----------
 *
 * Axiom.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.HashSet;

public class Axiom {
    private final boolean DEBUG = true;

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private String internalOnt = null;

    private IRI entity1;
    private IRI predicate;
    private HashSet<String> properties;
    private IRI entity2;

    //entity1Type ?
    //predicateType ?
    //entity2Type ?

    public Axiom(IRI entity1, IRI predicate, HashSet<String> properties, IRI entity2) {
        this.entity1 = entity1;
        this.predicate = predicate;
        this.properties = properties;
        this.entity2 = entity2;
    }

    public Axiom(String str, String internalOnt) {
        String[] triple = strTriple(str);

        //TODO add parsing of property
        //String[] property = strTriple(str);
        //...

        this.entity1 = IRI.create(internalOnt + "#" + triple[0]);
        this.predicate = IRI.create(internalOnt + "#" + triple[1]);
        //this.properties = properties; //in progress...
        this.entity2 = IRI.create(internalOnt + "#" + triple[2]);
        ////this.property1 = triple[3];
        ////this.property1 = triple[4];
        this.internalOnt = internalOnt;

        if (DEBUG) {
            System.out.println("DEBUG->parsing of string (class Axiom):");
            System.out.println("\t" + str);
            System.out.println("\t" + triple[0]);
            System.out.println("\t" + triple[1]);
            System.out.println("\t" + triple[2]);
        }
    }

    //only for the ontContains() case etc
    public Axiom(IRI entity1) {
        this.entity1 = entity1;
    }


    private String[] strTriple(String strTriple) {
        return strTriple.split(" ");
    }


    // ---------------------------------------------------------------------
    // Initialization of ontology.
    // ---------------------------------------------------------------------
    public void setOntology(String internalOnt) {
        this.internalOnt = internalOnt;

        if (internalOnt != null) {
            manager = initializeOntManager();
            try {
                ontology = initializeOnt();
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }
    }

    private OWLOntologyManager initializeOntManager() {
        return OWLManager.createOWLOntologyManager();
    }

    private OWLOntology initializeOnt() throws OWLOntologyCreationException {
        if (internalOnt == null) {
            throw new IllegalArgumentException("File: " + internalOnt + " not found");
        }
        OWLOntology ontology = manager.loadOntology(IRI.create(internalOnt));

        return ontology;
    }


    // ---------------------------------------------------------------------
    // simple gets methods
    // ---------------------------------------------------------------------
    public IRI getEntity1() {
        return entity1;
    }

    public IRI getEntity2() {
        return entity2;
    }

    public IRI getPredicate() {
        return predicate;
    }


    public int getNumberOfObjects() {
        int counter = 0;
        if (entity1 != null) {
            counter++;
        }
        if (predicate != null) {
            counter++;
        }
        if (entity2 != null) {
            counter++;
        }

        return counter;
    }


    // ---------------------------------------------------------------------
    // Some analyzes (need to call setOntology())
    // ---------------------------------------------------------------------

    //	Returns the structure of axiom like:
    // class-instanceOf-class || class-subClassOf-class || ind-custom_predicate-ind || ...
    public String getStructure() {
        return getEntityType(entity1) + "-" + getPredicateType() + "-" + getEntityType(entity2);
    }

    public String getEntity1Type() {
        return getEntityType(entity1);
    }

    public String getEntity2Type() {
        return getEntityType(entity2);
    }

    //backend of methods getEntity1Type() and getEntity2Type()
    private String getEntityType(IRI entity) {
        if (ontology.containsClassInSignature(entity)) {
            return "Class";
        } else if (ontology.containsIndividualInSignature(entity)) {
            return "Individual";
        } else if (ontology.containsDatatypeInSignature(entity)) {
            return "Datatype";
        } else if (entity == null) {
            return null;
        } else {
            return "UnknownType";
        }
    }

    //TODO реализовать "кеширование" типа в полях класса!
    //in opposite to getEntity1Type() here will be real implementation
    public String getPredicateType() {
        if (predicate == null) {
            return null;
        }

        OWLDataFactory factory = manager.getOWLDataFactory();

        // 1. check on not standard kinds of ObjectProperty
        if (predicate.getShortForm().equals("instanceOf")) {
            return "instanceOf";
        } else if (predicate.getShortForm().equals("hasInstance")) {
            return "hasInstance";
        }
        if (predicate.getShortForm().equals("subClassOf")) {
            return "subClassOf";
        }

        // 2. Is ObjectProperty not standard? If yes -> error
        if (!ontology.containsObjectPropertyInSignature(predicate)) {
            return "UnknownType";
        }
        /*
        else {
        	// 2.1 Which kind of standard?
        	return "ObjectProperty";
        }
        */

        // 3. If ObjectProperty is standard -> which one type exactly?
        /*
         * ObjectPropertyAxiom := SubObjectPropertyOf |
         * EquivalentObjectProperties | DisjointObjectProperties |
         * InverseObjectProperties | ObjectPropertyDomain | ObjectPropertyRange
         * | FunctionalObjectProperty | InverseFunctionalObjectProperty |
         * ReflexiveObjectProperty | IrreflexiveObjectProperty |
         * SymmetricObjectProperty | AsymmetricObjectProperty |
         * TransitiveObjectProperty
         */
        if (ontology.containsObjectPropertyInSignature(predicate)) {
            //TODO сделать запросы в онтологию, чтобы понять, какого она типа относительно нашей онтологии
            //...
            return "ObjectProperty";
        }


        //TEST
        /*
		OWLObjectProperty test1 = factory.getOWLObjectProperty(IRI.create(internalOnt + "#" + "existentiallyDependentOfQua"));
		OWLSymmetricObjectPropertyAxiom test2 = factory.getOWLSymmetricObjectPropertyAxiom(test1);
		if (ontology.containsAxiom(test2)) {
        	System.out.println("existentiallyDependentOfQua -> SymmetricObjectProperty");
		}
		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
		*/
        //TODO погуглить как можно вызывать по циклу различные типы!
        //TEST inheresIn - symmetricProperty
		/*
		OWLSymmetricObjectPropertyAxiom objectPropertyAxiom  = factory.getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
		if (ontology.containsAxiom(objectPropertyAxiom)) {
        	return "SymmetricObjectProperty";
		}
		*/
		/*
		 * Что делать?
		 *     <owl:ObjectProperty rdf:about="http://x556uq/RequirementsOfOrderManagerToDelivery-reification.owl#inheresIn">
        <rdfs:subPropertyOf rdf:resource="http://x556uq/RequirementsOfOrderManagerToDelivery-reification.owl#existentiallyDependentOf"/>
        <rdf:type rdf:resource="http://www.w3.org/2002/07/owl#FunctionalProperty"/>
        <rdf:type rdf:resource="http://www.w3.org/2002/07/owl#AsymmetricProperty"/>
		 */

        // 4. Error
        return "UnknownType";
    }


    public boolean hasSamePropertiesOfPredicate() {
        //TODO сделать запросы в онтологию
        //TODO сравнить результаты с properties
        //если одинаковы, то true

        //containsAll(Collection c) — возвращает true, если все элементы содержатся в коллекции
        //equals(Object o) — проверяет, одинаковы ли коллекции
		/*
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLObjectProperty test1 = factory.getOWLObjectProperty(IRI.create(internalOnt + "#" + "inheresIn"));
		OWLObjectPropertyAxiom test2 = factory.getOWLAnnotationProperty(test1);

		OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(predicate);
		*/


        return false;
    }
}
