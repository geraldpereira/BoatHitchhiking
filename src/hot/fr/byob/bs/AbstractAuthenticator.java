package fr.byob.bs;

import java.io.Serializable;

import org.hibernate.Session;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import fr.byob.bs.model.utilisateur.Role;
import fr.byob.bs.model.utilisateur.Utilisateur;

public abstract class AbstractAuthenticator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Logger
	protected transient Log log;

	@In
	protected Identity identity;
	@In
	protected Credentials credentials;
	@In
	protected FacesMessages facesMessages;
	@In
	protected Session hibernateSession;

	// @Out(required = false, scope = ScopeType.SESSION)
	// protected Utilisateur utilisateurCourant;
	@In(create = true)
	protected String dummyUser;
	@In
	protected LocaleSelector localeSelector;
	
	protected void doConnectUsuer(Utilisateur utilisateur) {
		identity.addRole("membre");
		if (utilisateur.getRoles() != null) {
			for (Role role : utilisateur.getRoles()) {
				identity.addRole(role.getNom());
			}
		}

		log.info("User connected : {0}", utilisateur);

		// On affecte l'utilisateur courant a la session (pour pouvoir le
		// retrouver par la suite)
		Contexts.getSessionContext().set("utilisateurCourant", utilisateur);
		
		// Un @Observer écoute la modification de la locale. Il est donc
		// activé dès l'appel au select()
		// Cependant, il semble que l'outjection n'affecte l'utilisateur
		// courant qu'a la sortie de notre méthode d'authentification.
		// Ainsi il sera null lors de l'appel systématique a la méthode
		// de notre observeur, ce qui nous arange, pour éviter
		// d'enregistrer la langue préférée à chaque connexion.
		localeSelector.setLocaleString(utilisateur.getLanguePreferee());
		localeSelector.select();
	}
	
	public boolean isAdmin() {
		
		if (Contexts.getSessionContext().get("utilisateurCourant") == null) {
			return false;
		}
		if (Identity.instance().hasRole(Role.ADMIN)) {
			return true;
		}

		return false;
	}
	
	

}
