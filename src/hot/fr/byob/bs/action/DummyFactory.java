package fr.byob.bs.action;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.bateau.Bateau;

@Startup
@Name("dummyFactory")
@Scope(ScopeType.APPLICATION)
public class DummyFactory {

	@In
	private Session hibernateSession;
	
	@Out(required = false)
	public String dummyUser;

	@Factory(value = "dummyUser")
	public void getDummyUser() {
		dummyUser = "DummyUser";
	}
	
	@Out(required = false)
	public long dummyBoat;

	@Factory(value = "dummyBoat")
	public void getDummyBoat() {
		dummyBoat = 1L;
	}
	
	@Out(required = false)
	public Bateau dummyBoatInstance;

	@Factory(value = "dummyBoatInstance")
	public void getDummyBoatInstance() {
		dummyBoatInstance = (Bateau) hibernateSession.get(Bateau.class, Long.valueOf(1L));
	}

	@Out(required = false)
	public long dummyPlace;

	@Factory(value = "dummyPlace")
	public void getDummyPlace() {
		dummyPlace = 1L;
	}
}
