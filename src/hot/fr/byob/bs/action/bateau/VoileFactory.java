package fr.byob.bs.action.bateau;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.bateau.Voile;

@Startup
@Name("voileFactory")
@Scope(ScopeType.APPLICATION)
public class VoileFactory {

	@Out(required = false)
	public List<Voile> voiles;

	@Factory("voiles")
	public void getListeTypeCoques() {
		voiles = Arrays.asList(Voile.values());
	}
}
