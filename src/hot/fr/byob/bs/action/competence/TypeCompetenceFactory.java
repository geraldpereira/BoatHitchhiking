package fr.byob.bs.action.competence;

import java.util.List;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.competence.TypeCompetence;

@Startup
@Name("typeCompetenceFactory")
@Scope(ScopeType.APPLICATION)
public class TypeCompetenceFactory {

	@In
	private Session hibernateSession;

	@Out(required = false)
	public List<TypeCompetence> typesCompetences;

	@Factory(value="typesCompetences")
	public void getTypesCompetencesListe() {
		typesCompetences = hibernateSession.getNamedQuery("typeCompetenceByCompetence").list();
	}
}
