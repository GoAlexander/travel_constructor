/* Course work
 * -----------
 *
 * VarHolder.java
 *
 * Author: Alexander Dydychkin
 * email: AlexanderDydychkin@yandex.ru
 *
 */

package ru.boldinonn.travelconstructor.agent;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class VarHolder {
    boolean DEBUG;
    boolean LOGS;
    OWLOntologyManager manager;
    OWLOntology ontology;
    String internalOnt;

    public VarHolder(boolean DEBUG, boolean LOGS, OWLOntologyManager manager, OWLOntology ontology,
                     String internalOnt) {
        this.DEBUG = DEBUG;
        this.LOGS = LOGS;
        this.manager = manager;
        this.ontology = ontology;
        this.internalOnt = internalOnt;
    }

    public boolean getDebug() {
        return DEBUG;
    }

    public boolean getLogs() {
        return LOGS;
    }

    public OWLOntologyManager getOWLOntologyManager() {
        return manager;
    }

    public OWLOntology getOWLOntology() {
        return ontology;
    }

    public String getInternalOnt() {
        return internalOnt;
    }
}
