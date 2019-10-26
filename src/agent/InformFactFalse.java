/* Course work
 * -----------
 *
 * InformFactFalse.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package agent;

import java.io.IOException;
import java.util.Collections;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;

class InformFactFalse {
	// ---------------------------------------------------------------------
	// Methods to delete data in ontology file.
	// ---------------------------------------------------------------------

	static boolean deleteInstance(Axiom myAxiom, VarHolder varHolder) throws IOException {
		String internalOnt = varHolder.getInternalOnt();
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();

		//if <subject> does not exist in ontology - exit
		if (!QueryIf.ontContains(myAxiom.getEntity1(), varHolder)) {
			return false;
		}

		if (internalOnt != null) {
			OWLDataFactory factory = manager.getOWLDataFactory(); //TODO вынести в поле класса?

			// Delete connection between class and individual
			OWLClass person = factory.getOWLClass(myAxiom.getEntity2());
			OWLNamedIndividual ind = factory.getOWLNamedIndividual(myAxiom.getEntity1());

			OWLClassAssertionAxiom axiomToRemove = factory.getOWLClassAssertionAxiom(person, ind);
			manager.removeAxiom(ontology, axiomToRemove);

			if (varHolder.getDebug()) {
				System.out.println("DEBUG: ");
				System.out.println("Classes of individual after 'unconnected' one");
				System.out.println(EntitySearcher.getTypes(ind, ontology));
				System.out.println("DEBUG END. ");
			}

			// check if individual has any "connected" classes
			// if no -> delete it completely
			if(EntitySearcher.getTypes(ind, ontology).isEmpty()){
				return deleteInstanceCompletely(myAxiom, varHolder);
			}
			else {
				return Commiter.writeOntToServer(varHolder);
			}
		}

		return false;
	}

	//This method deletes object completely
	//This method fixes OWL API`s bug when you delete individual which has only one "connected" class
	private static boolean deleteInstanceCompletely(Axiom myAxiom, VarHolder varHolder) throws IOException {
		String internalOnt = varHolder.getInternalOnt();
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();

		// if <subject> does not exist in ontology - exit
		if (!QueryIf.ontContains(myAxiom.getEntity1(), varHolder)) {
			return false;
		}

		if (internalOnt != null) {
			OWLDataFactory factory = manager.getOWLDataFactory(); // TODO вынести в поле класса?
			OWLNamedIndividual ind = factory.getOWLNamedIndividual(myAxiom.getEntity1());

			// If no one connection exists -> delete entity
			ind = factory.getOWLNamedIndividual(myAxiom.getEntity1());
			OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontology));
			remover.visit(ind);
			manager.applyChanges(remover.getChanges()); // or ind.accept(remover);

			return Commiter.writeOntToServer(varHolder);
		}

		return false;
	}

}
