/* Course work
 * -----------
 * 
 * VarHolder.java
 *  
 * Author: Alexander Dydychkin 
 * email: AlexanderDydychkin@yandex.ru
 * 
 */

package agent;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class VarHolder {
	boolean DEBUG;
	boolean LOGS;
	OWLOntologyManager manager;
	OWLOntology ontology;
	String internalOnt;
	
	String ftpNameOfOntFile;
	String ftpURL;
	String ftpLogin;
	String ftpPassword;
	
	public VarHolder(boolean DEBUG, boolean LOGS, OWLOntologyManager manager, OWLOntology ontology, 
			String internalOnt, String ftpNameOfOntFile, String ftpURL, String ftpLogin, String ftpPassword) {
		this.DEBUG = DEBUG;
		this.LOGS = LOGS;
		this.manager = manager;
		this.ontology = ontology;
		this.internalOnt = internalOnt;
		
		this.ftpNameOfOntFile = ftpNameOfOntFile;
		this.ftpURL = ftpURL;
		this.ftpLogin = ftpLogin;
		this.ftpPassword = ftpPassword;
	}
	
	public boolean getDebug(){
		return DEBUG;
	}
	
	public boolean getLogs(){
		return LOGS;
	}
	
	public OWLOntologyManager getOWLOntologyManager(){
		return manager;
	}
	
	public OWLOntology getOWLOntology(){
		return ontology;
	}
	
	public String getInternalOnt(){
		return internalOnt;
	}
	
	public String getFtpNameOfOntFile(){
		return ftpNameOfOntFile;
	}
	
	public String getFtpURL(){
		return ftpURL;
	}
	
	public String getFtpLogin(){
		return ftpLogin;
	}
	
	public String getFtpPassword(){
		return ftpPassword;
	}
}
