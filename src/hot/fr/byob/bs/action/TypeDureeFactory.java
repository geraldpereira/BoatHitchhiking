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

import fr.byob.bs.model.TypeDuree;

@Startup
@Name("typeDureeFactory")
@Scope(ScopeType.APPLICATION)
public class TypeDureeFactory {

	@Logger
	private Log log;
	

	@Out(required = false)
	public List<TypeDuree> typeDurees;
	
	@Factory("typeDurees")
	public void getListeTypeDurees(){
		log.info("Factoring type duree list");
		typeDurees = Arrays.asList( TypeDuree.values());
	}
}
