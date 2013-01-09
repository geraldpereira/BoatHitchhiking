package fr.byob.bs;

import java.util.ResourceBundle;

import org.jboss.seam.annotations.In;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.framework.HibernateEntityHome;

import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.utilisateur.Utilisateur;

public abstract class BSEntityManager<T extends BSEntity> extends HibernateEntityHome<T> {

	private static final long serialVersionUID = 1L;

	protected final static ResourceBundle messages = SeamResourceBundle.getBundle();

	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;

	public T getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	private String urlDecorator;

	public String getUrlDecorator() {
		return this.urlDecorator;
	}

	public void setUrlDecorator(String urlDecorator) {
		this.urlDecorator = urlDecorator;
	}

}
