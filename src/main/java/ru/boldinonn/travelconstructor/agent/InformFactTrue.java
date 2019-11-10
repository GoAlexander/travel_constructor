/* Course work
 * -----------
 *
 * InformFactTrue.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.io.IOException;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class InformFactTrue {
    // ---------------------------------------------------------------------
    // Methods to insert data into ontology file.
    // ---------------------------------------------------------------------

    static boolean addInstance(Axiom myAxiom, VarHolder varHolder) throws IOException {
        String internalOnt = varHolder.getInternalOnt();
        OWLOntology ontology = varHolder.getOWLOntology();
        OWLOntologyManager manager = varHolder.getOWLOntologyManager();

        if (!QueryIf.ontContains(myAxiom.getEntity2(), varHolder)) { // check object (entity2)
            return false;
        }

        if (internalOnt != null) { // throw exception???
            if (varHolder.getDebug()) {
                System.out.println("TEST FROM addInstance(): " + myAxiom.getEntity2());
            }

            OWLDataFactory factory = manager.getOWLDataFactory();

            OWLClass owlClass = factory.getOWLClass(myAxiom.getEntity2());
            OWLIndividual ind = factory.getOWLNamedIndividual(myAxiom.getEntity1());
            OWLAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass, ind);
            manager.addAxiom(ontology, axiom);

            return Commiter.writeOntToServer(varHolder);
        }

        return false;
    }

}