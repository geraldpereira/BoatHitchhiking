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

import fr.byob.bs.model.Pays;

@Startup
@Name("paysFactory")
@Scope(ScopeType.APPLICATION)
public class PaysFactory {

	public final static long ID_FRANCE = 76;
	public final static long ID_USA = 71;
	public final static long ID_ENGLAND = 177;

	// @Logger
	// private Log log;

	@In
	private Session hibernateSession;

	@Out(required = false)
	private HashMap<Long, Pays> paysMap;

	@Out(required = false)
	private List<Pays> pays;

	@Factory(value = "pays")
	public void getListePays() {
		pays = hibernateSession.getNamedQuery("pays.findAllOrderByLibelle").list();
	}

	@Factory(value = "paysMap")
	public void getMapPays() {
		getListePays();
		paysMap = new HashMap<Long, Pays>(pays.size());
		for (Pays curPays : pays) {
			paysMap.put(curPays.getIdPays(), curPays);
		}
	}

}
