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

import fr.byob.bs.model.Monnaie;

@Startup
@Name("monnaieFactory")
@Scope(ScopeType.APPLICATION)
public class MonnaieFactory {

	public final static long ID_EURO = 5;
	public final static long ID_DOLLAR_USD = 61;
	
	
	@Logger
	private Log log;

	@Out(required = false)
	private List<Monnaie> monnaies;

	@Factory(value = "monnaies")
	public void getListeMonnaie() {
		monnaies = Arrays.asList(Monnaie.values());
	}

}
