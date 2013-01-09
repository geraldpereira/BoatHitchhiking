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

import fr.byob.bs.model.experience.TypeXp;

@Startup
@Name("typeXPFactory")
@Scope(ScopeType.APPLICATION)
public class TypeXPFactory {

	@Logger
	private Log log;
	
	@Out(required = false)
	public List<TypeXp> typeXps;
	
	@Factory("typeXps")
	public void getListeTypeXP(){
		log.info("Factoring type xp list");
		typeXps = Arrays.asList( TypeXp.values()); 
	}
}
