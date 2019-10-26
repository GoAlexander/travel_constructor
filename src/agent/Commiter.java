/* Course work
 * -----------
 * 
 * Commiter.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package agent;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

class Commiter {
	
	// ---------------------------------------------------------------------
	// Write to ontology file.
	// ---------------------------------------------------------------------
	// Dependency: <commons-net> for the ftp access
	
	static boolean writeOntToServer(VarHolder varHolder) throws IOException{
		String ftpNameOfOntFile = varHolder.getFtpNameOfOntFile();
		String ftpURL = varHolder.getFtpURL();
		String ftpLogin = varHolder.getFtpLogin();
		String ftpPassword = varHolder.getFtpPassword();
		OWLOntology ontology = varHolder.getOWLOntology();
		OWLOntologyManager manager = varHolder.getOWLOntologyManager();
		
		//If you can`t start web server you can try this.
		//Change </opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl> on your own path.
		//File fileformated = new File("/opt/lampp/htdocs/Pizzeria_DEMO_myVersion.owl");
		//manager.saveOntology(ontology, IRI.create(fileformated.toURI()));

		//Here we write on remote host!	
		FTPClient client = new FTPClient();
		OutputStream out = null;
		try {
		    client.connect(ftpURL); //here link to the ftp-server
		    client.login(ftpLogin, ftpPassword); //here login and password of ftp-server
		    client.enterLocalPassiveMode();
		    out = client.storeFileStream(ftpNameOfOntFile); //here name of ontology file on server
		    try {
				manager.saveOntology(ontology, out);
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		} finally {
		    try {
		        out.close();
		        client.logout();
		        client.disconnect();
		    } catch (Exception e) {
		    }
		}
				
		return true; //TODO maybe make some checking before say that all is good? 
	}

}
