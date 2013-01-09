package fr.byob.bs.action;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.Langue;
import fr.byob.bs.model.Pays;

@Startup
@Name("langueFactory")
@Scope(ScopeType.APPLICATION)
public class LangueFactory {

	// @Logger
	// private Log log;

	@In
	private Session hibernateSession;

	@Out(required = false)
	private HashMap<Long, Langue> languesMap;

	@Out(required = false)
	private HashMap<Pays, Langue> paysLanguesMap;

	@Out(required = false)
	private List<Langue> langues;

	@Factory(value = "langues")
	public void getListeLangues() {
		langues = hibernateSession.getNamedQuery("langue.findByLibelle").list();
	}

	@Factory(value = "languesMap")
	public void getMapLangues() {
		 List<Langue> langues = hibernateSession.getNamedQuery("langue.findByLibelle").list();
		languesMap = new HashMap<Long, Langue>(langues.size());
		for (Langue curLangue : langues) {
			languesMap.put(curLangue.getIdlangue(), curLangue);
		}
	}
	
	@Factory(value = "paysLanguesMap")
	public void getPaysLanguesMap() {
		List<Pays> pays = hibernateSession.getNamedQuery("pays.findAll").list();
		paysLanguesMap = new HashMap<Pays, Langue>(pays.size());
		for (Pays curPays : pays) {
			paysLanguesMap.put(curPays, curPays.getLangue());
		}
	}
}
