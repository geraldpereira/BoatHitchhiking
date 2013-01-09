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

import fr.byob.bs.model.annonce.TypePaiement;

@Startup
@Name("typePaiementFactory")
@Scope(ScopeType.APPLICATION)
public class TypePaiementFactory {

	@Logger
	private Log log;
	
	@Out(required = false)
	public List<TypePaiement> typePaiements;
	
	@Factory("typePaiements")
	public void getListePostes(){
		log.info("Factoring types pePaiement list");
		typePaiements = Arrays.asList(TypePaiement.values()); 
	}
	
}
