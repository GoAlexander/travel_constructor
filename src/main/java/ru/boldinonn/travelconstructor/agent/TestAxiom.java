/* Course work
 * -----------
 *
 * TestAxiom.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.util.HashSet;

import org.semanticweb.owlapi.model.IRI;


//import agent.A

class TestAxiom {

    public static void main(String[] args) {
        String internalOnt = "http://x556uq/RequirementsOfOrderManagerToDelivery-reification.owl";


        IRI entity1 = IRI.create(internalOnt + "#" + "Termobox001");
        IRI predicate = IRI.create(internalOnt + "#" + "instanceOf");
        IRI entity2 = IRI.create(internalOnt + "#" + "FoodTermobox");
		
		/*
		Axiom myAxiom = new Axiom(entity1, predicate, entity2);
		myAxiom.setOntology(internalOnt);
		
		System.out.println(myAxiom.getEntity1Type());
		System.out.println(myAxiom.getPredicateType());
		System.out.println(myAxiom.getEntity2Type());
		System.out.println(myAxiom.getStructure());
		System.out.println();
		
		//---
		
		entity1 = IRI.create(internalOnt + "#" + "FoodTermobox");
		predicate = IRI.create(internalOnt + "#" + "hasInstance");
		entity2 = IRI.create(internalOnt + "#" + "Termobox001");
		
		Axiom myAxiom2 = new Axiom(entity1, predicate, entity2);
		myAxiom2.setOntology(internalOnt);
		
		System.out.println(myAxiom2.getEntity1Type());
		System.out.println(myAxiom2.getPredicateType());
		System.out.println(myAxiom2.getEntity2Type());
		System.out.println(myAxiom2.getStructure());
		System.out.println();
		
		//---
		
		entity1 = IRI.create(internalOnt + "#" + "AAA");
		predicate = IRI.create(internalOnt + "#" + "xxx");
		entity2 = IRI.create(internalOnt + "#" + "Termobox001");
		
		Axiom myAxiom3 = new Axiom(entity1, predicate, entity2);
		myAxiom3.setOntology(internalOnt);
		
		System.out.println(myAxiom3.getEntity1Type());
		System.out.println(myAxiom3.getPredicateType());
		System.out.println(myAxiom3.getEntity2Type());
		System.out.println(myAxiom3.getStructure());
		System.out.println();
		
		//---
		
		entity1 = IRI.create(internalOnt + "#" + "Termobox001");
		predicate = IRI.create(internalOnt + "#" + "isPartOf");
		entity2 = IRI.create(internalOnt + "#" + "Termobox002");
		
		Axiom myAxiom4 = new Axiom(entity1, predicate, entity2);
		myAxiom4.setOntology(internalOnt);
		
		System.out.println(myAxiom4.getEntity1Type());
		System.out.println(myAxiom4.getPredicateType());
		System.out.println(myAxiom4.getEntity2Type());
		System.out.println(myAxiom4.getStructure());
		System.out.println();
		
		//---
		
		entity1 = IRI.create(internalOnt + "#" + "Termobox002");
		predicate = IRI.create(internalOnt + "#" + "isPartOf");
		entity2 = IRI.create(internalOnt + "#" + "Termobox001");
		
		Axiom myAxiom5 = new Axiom(entity1, predicate, entity2);
		myAxiom5.setOntology(internalOnt);
		
		System.out.println(myAxiom5.getEntity1Type());
		System.out.println(myAxiom5.getPredicateType());
		System.out.println(myAxiom5.getEntity2Type());
		System.out.println(myAxiom5.getStructure());
		System.out.println();
		*/

        // ---

        entity1 = IRI.create(internalOnt + "#" + "Termobox002");
        predicate = IRI.create(internalOnt + "#" + "isPartOf");
        entity2 = IRI.create(internalOnt + "#" + "Termobox001");

        HashSet<String> properties = new HashSet<String>();
        properties.add("AsymmetricObjectProperty");
        properties.add("FunctionalObjectProperty");
        properties.add("ReflexiveObjectProperty");


        Axiom myAxiom5 = new Axiom(entity1, predicate, properties, entity2);
        myAxiom5.setOntology(internalOnt);

        System.out.println(myAxiom5.getEntity1Type());
        System.out.println(myAxiom5.getPredicateType());
        System.out.println(myAxiom5.getEntity2Type());
        System.out.println(myAxiom5.getStructure());
        System.out.println();

    }

}
