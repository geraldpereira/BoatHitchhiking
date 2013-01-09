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

import fr.byob.bs.model.TypeNav;

@Startup
@Name("typeNavFactory")
@Scope(ScopeType.APPLICATION)
public class TypeNavFactory {

	@Logger
	private Log log;
	
	@Out(required = false)
	public List<TypeNav> typeNavs;
	
	@Factory("typeNavs")
	public void getListTypeNav(){
		log.info("Factoring types nav list");
		typeNavs = Arrays.asList(TypeNav.values());
	}
	
}
