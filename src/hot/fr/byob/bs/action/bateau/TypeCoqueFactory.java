package fr.byob.bs.action.bateau;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.bateau.TypeCoque;

@Startup
@Name("typeCoqueFactory")
@Scope(ScopeType.APPLICATION)
public class TypeCoqueFactory {

	// @Logger
	// private Log log;
	
	@Out(required = false)
	public List<TypeCoque> typeCoques;
	
	@Factory("typeCoques")
	public void getListeTypeCoques(){
		typeCoques = Arrays.asList( TypeCoque.values()); 
	}
	
}
