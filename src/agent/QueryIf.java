/* Course work
 * -----------
 *
 * QueryIf.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package agent;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class QueryIf {
	// ---------------------------------------------------------------------
	// Methods which return true/false.
	// ---------------------------------------------------------------------


	// Returns true if ontology has requested class or instance.
	static boolean ontContains(IRI object, VarHolder varHolder) {
		OWLOntology ontology = varHolder.getOWLOntology();

		if (ontology.containsIndividualInSignature(object)) {
			return true;
		} else if (ontology.containsClassInSignature(object)) {
			return true;
		} else if (ontology.containsDatatypeInSignature(object)) {
			return true;
		} else {
			return false;
		}
	}

	// only for checking of link between class and individual!
	static boolean ontContains(Axiom myAxiom, VarHolder varHolder) {
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();

		OWLIndividual ind = factory.getOWLNamedIndividual(myAxiom.getEntity1());
		OWLClass owlClass = factory.getOWLClass(myAxiom.getEntity2());
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass, ind);

		return ontology.containsAxiom(axiom);
	}

}
