package fr.byob.bs.action.experience;

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

import fr.byob.bs.model.experience.XpGenerale;

@Startup
@Name("xpGeneraleFactory")
@Scope(ScopeType.APPLICATION)
public class XPGeneraleFactory {

	@Logger
	private Log log;
	
	@Out(required=false)
	public List<XpGenerale> experiencesGenerales;
	
	@Factory("experiencesGenerales")
	public void getListeXpGenerales(){
		log.info("Factoring xp generales list");
		experiencesGenerales = Arrays.asList( XpGenerale.values()); 
	}
	
}
