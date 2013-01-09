package fr.byob.bs.action.bateau;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.bateau.Greement;

@Startup
@Name("greementFactory")
@Scope(ScopeType.APPLICATION)
public class GreementFactory {

	@Out(required = false)
	public List<Greement> greements;

	@Factory("greements")
	public void getListeGreements() {
		greements = Arrays.asList(Greement.values());
	}
	
}
