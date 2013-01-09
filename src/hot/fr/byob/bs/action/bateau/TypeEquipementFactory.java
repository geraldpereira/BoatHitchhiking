package fr.byob.bs.action.bateau;

import java.util.List;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.bateau.TypeEquipement;

@Startup
@Name("typeEquipementFactory")
@Scope(ScopeType.APPLICATION)
public class TypeEquipementFactory {

	@In
	private Session hibernateSession;

	@Out(required = false)
	public List<TypeEquipement> typesEquipement;

	@Factory(value = "typesEquipement")
	public void getTypesEquipementListe() {
		typesEquipement = hibernateSession
				.getNamedQuery("typeEquipementByEquipement")
				.list();
	}
	
}



	