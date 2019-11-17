/* Course work
 * -----------
 *
 * Commiter.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

class Commiter {

    // ---------------------------------------------------------------------
    // Write to ontology file.
    // ---------------------------------------------------------------------

    static boolean writeOntToServer(VarHolder varHolder) throws IOException {
        OWLOntology ontology = varHolder.getOWLOntology();
        OWLOntologyManager manager = varHolder.getOWLOntologyManager();

        //If you can`t start web server you can try this.
        //Change </opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl> on your own path.
        //File fileformated = new File("/opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl");
        //manager.saveOntology(ontology, IRI.create(fileformated.toURI()));
        File fileformated = new File(varHolder.getInternalOnt());

        try {
            manager.saveOntology(ontology, IRI.create(fileformated.toURI()));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

        return true;
    }

}
