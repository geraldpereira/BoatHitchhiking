package fr.byob.bs.validator;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.validator.Validator;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

@Name("uniqueValidator")
public class UniqueValidator implements Validator<Unique> {


	private Unique unique;

	@Override
	public void initialize(Unique unique) {
		this.unique = unique;
	}

	@Override
	public boolean isValid(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof String) {
			return isValid((String) o);
		}
		return isValid(o.toString());
	}

	protected boolean isValid(String s) {
		Session session = (Session) Component.getInstance("hibernateSession");
		return session
				.createQuery(
						"select entity from "+unique.entity()+" entity where entity."+unique.field()+" like :pseudo").setParameter("pseudo", s).list().isEmpty();
	}

}
