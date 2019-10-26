/* Course work
 * -----------
 * 
 * REASONER.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package agent;

import java.io.*;
import java.util.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
//import org.semanticweb.HermiT.Reasoner;


public class REASONER {
    private static OWLReasoner _reasoner;

    public REASONER() {};

    public REASONER(OWLOntology ontology)  {

//    PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
//    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
//    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
//    _reasoner = reasonerFactory.createReasoner(ontology);

    	
      _reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
     
   }

    public OWLReasoner getReasoner() {
        return _reasoner;
    }    

}
