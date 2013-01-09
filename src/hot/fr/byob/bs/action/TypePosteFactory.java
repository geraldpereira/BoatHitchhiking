package fr.byob.bs.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;

import fr.byob.bs.model.TypePoste;

@Startup
@Name("typePosteFactory")
@Scope(ScopeType.APPLICATION)
public class TypePosteFactory {

	@Logger
	private Log log;
	
	@Out(required = false)
	public List<TypePoste> typePostes;
	
	@Factory("typePostes")
	public void getListePostes(){
		log.info("Factoring types postes list");
		typePostes = Arrays.asList( TypePoste.values()); 
	}
	
}
