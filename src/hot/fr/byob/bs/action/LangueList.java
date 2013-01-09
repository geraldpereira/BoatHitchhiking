package fr.byob.bs.action;

import fr.byob.bs.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.framework.HibernateEntityQuery;

import java.util.Arrays;

@Name("langueList")
public class LangueList extends HibernateEntityQuery<Langue> {

	private static final String EJBQL = "select langue from Langue langue";

	private static final String[] RESTRICTIONS = {"lower(langue.libelle) like concat(lower(#{langueList.langue.libelle}),'%')",};

	private Langue langue = new Langue();

	public LangueList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Langue getLangue() {
		return langue;
	}
}
