package fr.byob.bs;

import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;

import fr.byob.bs.action.utilisateur.messagerie.EmailManager;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("authenticator")
@Scope(ScopeType.SESSION)
public class Authenticator extends AbstractAuthenticator {
	private static final long serialVersionUID = 1L;

	@In(value = "BSPasswordManager", create = true)
	private PasswordManager passwordManager;	
	
	@In(create = true)
	private EmailManager emailManager;

	@Transactional
	public boolean authenticate() {
		
		log.info("Authenticating {0}", identity.getCredentials().getUsername());
		try {
			Utilisateur utilisateur = null;

			String username = identity.getCredentials().getUsername().toLowerCase();

			if (username.contains("@")) {
				// c'est le mail utilisateur
				// c'est son pseudonyme
				utilisateur = (Utilisateur) hibernateSession.getNamedQuery("user.connectByMail").setParameter("mail", username).setParameter("pseudoDummy", dummyUser).uniqueResult();
			} else {
				// c'est son pseudonyme
				utilisateur = (Utilisateur) hibernateSession.getNamedQuery("user.connectByPseudo").setParameter("pseudonyme", username).setParameter("pseudoDummy", dummyUser).uniqueResult();
			}

			if (utilisateur == null) {
				facesMessages.addFromResourceBundle("Utilisateur_inconnu");
				return false;
			}

			if (!validatePassword(identity.getCredentials().getPassword(), utilisateur)) {
				facesMessages.addFromResourceBundle("MotDePasse_incorrect");
				return false;
			}

			if (!utilisateur.isActif()) {
				// Envoi de l'email d'activation
				emailManager.envoiEmailActivation(utilisateur);

				facesMessages.addFromResourceBundle("Utilisateur_inactif");
				return false;
			}

			doConnectUsuer(utilisateur);
			
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public boolean validatePassword(String password, Utilisateur utilisateur) {
		return passwordManager.hash(password).equals(utilisateur.getMotDePasse());
	}

	public String login() {
		// WARNING : Do not do a identity.login(); directly !
		String retour = identity.login();
		return retour;
	}
	
	public void logout() {
		Utilisateur utilisateurCourant = (Utilisateur) Contexts.getSessionContext().get("utilisateurCourant");
		log.info("User logged out: {0}", utilisateurCourant);
		identity.unAuthenticate();
		utilisateurCourant = null;
		Contexts.getSessionContext().remove("utilisateurCourant");
	}

}
