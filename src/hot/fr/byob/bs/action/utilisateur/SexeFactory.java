package fr.byob.bs.action.utilisateur;

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

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Sexe;


/**
 * Factory de la liste des sexes
 * 
 * @author GPEREIRA
 */
@Startup
@Name("sexeFactory")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class SexeFactory {

	@Logger
	private Log log;
	
	@Out(required = false)
	public List<Sexe> sexes;
	
	@Factory("sexes")
	public void getListeSexes(){
		log.info("Factoring sexe list");
		sexes = Arrays.asList(Sexe.values());
	}
	
}
