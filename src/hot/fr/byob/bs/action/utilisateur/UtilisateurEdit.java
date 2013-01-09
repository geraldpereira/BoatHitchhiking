package fr.byob.bs.action.utilisateur;

import static fr.byob.bs.Constantes.CANCELLED;
import static fr.byob.bs.Constantes.REMOVED;
import static fr.byob.bs.Constantes.UPDATED;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.AuthorizationException;
import org.jboss.seam.security.Identity;

import fr.byob.bs.Authenticator;
import fr.byob.bs.BSEntityEdit;
import fr.byob.bs.BSException;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.PasswordManager;
import fr.byob.bs.action.LieuHome;
import fr.byob.bs.action.experience.XpManager;
import fr.byob.bs.action.utilisateur.messagerie.EmailManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.utilisateur.Role;
import fr.byob.bs.model.utilisateur.Utilisateur;
import fr.byob.bs.model.utilisateur.messagerie.Conversation;

/**
 * Crud pour l'utilisateur et tout ce qui va avec (photo, langues, compétences
 * et expériences)
 * 
 * @author GPEREIRA
 * 
 */
@Name("utilisateurEdit")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class UtilisateurEdit extends BSEntityEdit<Utilisateur> {

	private static final long serialVersionUID = 1L;

	@In
	private FacesMessages facesMessages;
	@In
	private Locale locale;
	@In(create = true)
	private EmailManager emailManager;

	@In(value = "BSPasswordManager", create = true)
	private PasswordManager passwordManager;
	@In(create = true)
	private CompetenceUtilisateurManager competenceUtilisateurManager;
	@In(create = true)
	private LangueManager langueManager;
	@In(create = true)
	private UtilisateurPhotoManager utilisateurPhotoManager;
	@In(create = true)
	private LieuHome lieuHome;
	@In(create = true)
	private XpManager xpManager;

	@In(required = false)
	private Authenticator authenticator;

	private String ancienMDP;

	@In(required = false, create = true)
	private UtilisateurHelper utilisateurHelper;

	@In(required = false, create = false)
	private UtilisateurView utilisateurView;

	@In
	private Events events;

	/**
	 * Mot de passe, utilisé pour la fenêtre modale de validation de suppression
	 * d'un compte utilisateur
	 */
	@NotNull
	private String motDePasse;

	@Override
	@Transactional
	public String remove() {

		info("Atempt to remove user {0} by {1}", getInstance(), utilisateurCourant);

		if (!validatePasswordForRemove()) {
			return "error_password";
		}

		// Remove messages
		List<Conversation> conversationsInbox = getSession().getNamedQuery("conversation.findAll").setParameter("utilisateur", utilisateurCourant.getPseudonyme()).list();
		if (conversationsInbox != null) {
			for (Conversation conversation : conversationsInbox) {
				if (conversation != null) {
					getSession().delete(conversation);
				}
			}
		}

		// We must remove the offers before the user (so they do not reference
		// an unexisting boat when we remove them!)
		for (Offre offre : getInstance().getOffres()) {
			getSession().delete(offre);
		}

		String retour = super.remove();
		if (!REMOVED.equals(retour)) {
			throw new BSException("error.utilisateur.remove");
		}

		// Suppr les photo
		utilisateurPhotoManager.supprimerRepertoireUtilisateur();

		// On utilise l'emailManager pour envoyer une conformation de
		// suppression
		emailManager.envoiEmailSuppression(instance);
		// On deco l'utilisateur
		authenticator.logout();

		info("Remove user {0} by {1}", getInstance(), utilisateurCourant);

		return REMOVED;
	}

	@Override
	@Transactional
	public String update() {

		info("Atempt to update user {0} by {1}", getInstance(), utilisateurCourant);

		try {
			// On valide les langues
			if (!langueManager.validerLangues()) {
				return "erreur_langue";
			}
			// On met à jour la locale
			getInstance().setLanguePreferee(locale.toString());

			// On vire les languesUtilisateur supprimmées de la base de donnée
			langueManager.updateLangues();
			// Idem avec les xps

			// Maj des coméptences
			competenceUtilisateurManager.updateCompetences();
			// Maj de la photo
			utilisateurPhotoManager.updatePhoto();
			// On vire les expériences utilisateur supprimées de la base de
			// donnée
			xpManager.updateXps();

			// Maj du lieu
			getInstance().getCoordonnees().setLieu(lieuHome.getLieu());

			// Si le mot de passe à changé, on le hash et on le maj
			if (!ancienMDP.equals(getInstance().getMotDePasse())) {
				getInstance().setMotDePasse(passwordManager.hash(getInstance().getMotDePasse()));
			}
			getInstance().setNote(utilisateurHelper.getNote(getInstance()));

			// So the "must be equal to password won't be displayed"
			facesMessages.clear();

			// On met à jour l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.utilisateur.update");
			}

			info("Updated user {0} by {1}", getInstance(), utilisateurCourant);

			// Update current user (if not edited by some other admin !)
			if (getInstance().getPseudonyme().equals(utilisateurCourant.getPseudonyme())) {
				getInstance().updateOtherUtilisateur(utilisateurCourant);
			}

			return UPDATED;
		} catch (BSException bse) {
			// On met a jour l'utilisateur lui même
			utilisateurPhotoManager.supprimerFichierTemporaire();
			error("Error while updating user {0} by {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			utilisateurPhotoManager.supprimerFichierTemporaire();
			e.printStackTrace();
			error("Error while updating user {0} by {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.utilisateur.update");
		}
	}

	/**
	 * Lors de l'annulation d'une modification utilisateur, on reset tout !!
	 * 
	 * @return
	 */
	@Transactional
	public String cancel() {
		utilisateurPhotoManager.supprimerFichierTemporaire();
		return CANCELLED;
	}

	/**
	 * Initialise ce home
	 */
	// @Begin(nested = true)
	public void wire() {
		if (ConversationUtils.beginConversation("/utilisateur/UtilisateurEdit.xhtml")) {
			// Si j'ai un utilisateurView, recupérer son instance (si admin !)
			if (utilisateurView != null && Identity.instance().hasRole(Role.ADMIN)) {
				setInstance(utilisateurView.getInstance());
				setId(utilisateurView.getInstance().getPseudonyme());
			} else {
				// Sinon, récuperer l'utilisateur courant
				if (utilisateurCourant != null) {
					// setInstance(utilisateurCourant);
					setId(utilisateurCourant.getPseudonyme());
				}
			}

			// Le lieu est clonné (voir lieuHome pour explications)
			lieuHome.wire(true, getInstance().getCoordonnees().getLieu().clone());

			// On memorise le mot de passe
			ancienMDP = getInstance().getMotDePasse();
		}
	}

	@Override
	public void validerAuthorisation() {
		// On valide pour l'édition que l'utilisateur connecté est bien
		// propriétaire du bateau.
		if (utilisateurCourant != null && !utilisateurCourant.getPseudonyme().equals(getInstance().getPseudonyme()) && !Identity.instance().hasRole(Role.ADMIN)) {
			ResourceBundle messages = SeamResourceBundle.getBundle();
			throw new AuthorizationException(messages.getString("utilisateur.propriorequis"));
		}
	}

	public boolean validatePasswordForRemove() {
		String hash = null;
		if (this.motDePasse != null) {
			hash = passwordManager.hash("" + this.motDePasse);
		}

		if (hash != null && !hash.equals(getInstance().getMotDePasse())) {
			facesMessages.addToControlFromResourceBundle("motDePasseConfirm", "validator.mdpConfirm");
			return false;
		}
		return true;
	}

	/**
	 * Affecte le mot de passe de la fenetre modale de confirmation de
	 * suppression Vérifie aussi qu'il correspond au mdp utilisateur !
	 * 
	 * @param motDePasse
	 */
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public void sendActivationMail() {
		emailManager.envoiEmailActivation(getInstance());
	}

	/* Accesseurs */

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setUtilisateurPseudonyme(String id) {
		setId(id);
	}

	public String getUtilisateurPseudonyme() {
		return (String) getId();
	}

	public String updateNote(Utilisateur utilisateur) {
		setInstance(utilisateur);
		setId(utilisateur.getPseudonyme());
		wire();
		if (utilisateurCourant != null)
			info("Atempt to update Note user {0} by user {1}", getInstance(), utilisateurCourant);
		else
			info("Atempt to update Note user {0} by user {1}", getInstance(), "batch");
		try {
			utilisateur.setNote(utilisateurHelper.getNote(utilisateur));

			// Enregistrement de l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.utilisateur.update");
			}

			if (utilisateurCourant != null)
				info("Updated user {0} by user {1}", getInstance(), utilisateurCourant);
			else
				info("Updated user {0} by user {1}", getInstance(), "batch");

			return UPDATED;
		} catch (BSException bse) {
			// On met a jour l'utilisateur lui même
			utilisateurPhotoManager.supprimerFichierTemporaire();
			error("Error while updating user {0} by {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			utilisateurPhotoManager.supprimerFichierTemporaire();
			e.printStackTrace();
			error("Error while updating user {0} by {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.utilisateur.update");
		}
	}

	@Transactional
	public String activate() {
		getInstance().setActif(null);

		// Enregistrement de l'utilisateur
		String retour = super.update();
		if (!UPDATED.equals(retour)) {
			throw new BSException("error.utilisateur.update");
		}

		events.raiseTransactionSuccessEvent("utilisateurAdded", getInstance());

		return UPDATED;
	}


}
