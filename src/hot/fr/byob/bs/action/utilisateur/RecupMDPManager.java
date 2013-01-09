package fr.byob.bs.action.utilisateur;

import java.io.Serializable;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import fr.byob.bs.Constantes;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.PasswordManager;
import fr.byob.bs.action.utilisateur.messagerie.EmailManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;

/**
 * Manager utilis� pour la recup�ration de son mot de passe par un utilisateur. 
 *  
 * @author GPEREIRA
 *
 */

@Startup
@Name("recupMDPManager")
@Scope(ScopeType.SESSION)
@MeasureCalls
public class RecupMDPManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Logger
	private Log log;
	@In
	private Session hibernateSession;
	@In
	private FacesMessages facesMessages;
	@In(create = true)
	private EmailManager emailManager;
	
	@In(create = true)
	private String dummyUser;
	
	@In(value = "BSPasswordManager", create = true)
	private PasswordManager passwordManager;

	@In
	private Events events;


	/* ********************* Pour la fen�tre modale de recup du mdp  ********************* */
	/**
	 * Champ de saisie du pseudonyme ou de l'adresse mail de l'utilisateur d�sirant r�cup�rer son mdp.
	 */
	private String recupMDP = null;
	/**
	 * D�finit si la fen�tre modale de r�cup�ration du mot de passe doit �tre affich�e
	 */
	private boolean recupMDPModalPanelAffiche = true;
	/**
	 * Utilisateur dont on va r�cup�rer le mdp
	 */
	private Utilisateur utilisateur;
	
	/**
	 * Valide le champ de r�cup�ration de mot de passe :
	 * Doit �tre un pseudonyme ou un email connu.
	 * L'idicateur recupMDPModalPanelAffiche est mis � true, pour que la fen�tre modale reste affich�e au rechargemetn de page.
	 * 
	 * @return vrais si valide, false sinon (un message d'erreur est alros affich�)
	 */
	public boolean validerRecupMDP() {
		log.info("RecupMDPManager.validerRecupMDP() " + recupMDP);
		recupMDPModalPanelAffiche = true;
		if (recupMDP == null || recupMDP.length() == 0) {
			facesMessages.addToControlFromResourceBundle("recupMDP",
					"validator.mailOuPseudo");
			log.info("RecupMDPManager.validerRecupMDP() returned false : empty string");
			return false;
		} else if (recupMDP.contains("@")) {
			// c'est le mail utilisateur
			utilisateur = (Utilisateur) hibernateSession
					.getNamedQuery(
					"user.findByMail").setParameter("mail", recupMDP.toLowerCase()).setParameter("pseudoDummy", dummyUser)
					.uniqueResult();
			
			if (utilisateur == null) {
				facesMessages.addToControlFromResourceBundle("recupMDP",
						"validator.mailInconnu");
				log.info("RecupMDPManager.validerRecupMDP() returned false : unknown user");
				return false;
			}
		} else {
			// c'est son pseudonyme
			utilisateur = (Utilisateur) hibernateSession
			.getNamedQuery("user.findByPseudo").setParameter("pseudo", recupMDP.toLowerCase()).setParameter("pseudoDummy", dummyUser)
					.uniqueResult();
			if (utilisateur == null) {
				facesMessages.addToControlFromResourceBundle("recupMDP", "validator.pseudoInconnu");
				
				log.info("RecupMDPManager.validerRecupMDP() return false : unknown mail");
				return false;
			}
		}
		log.info("RecupMDPManager.validerRecupMDP() returned true");
		return true;

	}

	/**
	 * Place l'indicateur d'affichage de la fen�tre modale
	 * recupMDPModalPanelAffiche � false (pour la masquer) Envoi un email de
	 * redirection vers la page de saisie d'un nouveau mot de passe. Cet envoi
	 * de mail permet de s'assurer que l'utilisateur ayant demand� � r�cup�rer
	 * son mot de passe est bien le propri�taire du comtpe utilisateur.
	 */
	public void recupererMDP() {
		if (validerRecupMDP()) {
			log.info("RecupMDPManager.recupererMDP()");
			recupMDPModalPanelAffiche = false;
			emailManager.envoiEmailRecupMDPAsync(utilisateur);
			facesMessages.addFromResourceBundle("MotDePasse_email");
		}
	}
	

	/* ********************* Pour la fen�tre de saisie du nouveau mdp ********************* */	
	/**
	 * Param�tre pass� � la page de saisie du nouveau mot de passe : le pseudonyme utilisateur
	 */
	private String utilisateurPseudonyme;
	/**
	 * Param�tre pass� � la page de saisie du nouveau mot de passe : la hash du mot de passe concat�n� avec pseudonyme utilisateur
	 * Ce hash nous assure que l'url http d'acc�s � la page de peut �tre forg�e par un hacker. 
	 */
	private String hash;
	/**
	 * Champ de saisie du mot de passe sur la page de saisie d'un nouveau mdp
	 */
	private String nouveauMDP;
	/**
	 * Champ de saisie de la confirmation de mot de passe sur la page de saisie d'un nouveau mdp
	 */
	private String nouveauMDPConfirm;

	/**
	 * Enregistre si les param�tres pass�s a la pages sont corrects
	 */
	private Boolean parametresOk = null;

	/**
	 * Valide les param�tres pass�s � la page de r�cup�ration du mot de passe
	 * Le hash doit correspondre � au pseudonyme utilisateur.
	 * Le champ parametresOK est mis � jour lors du premier appel � cette m�thode puis utilis� pour �viter de trop nombreux acc�s en base de donn�e
	 * @return vrais si les param�tres sont OK
	 */
	public boolean validerParametres() {
		if (parametresOk != null) {
			// Pour �viter d'envoyer moult requ�tes en base a chaque demande
			return parametresOk;
		}

		// On v�rifie que l'utilisateur est connu (et au passage on
		// l'enregistre)
		this.utilisateur = (Utilisateur) hibernateSession
		.getNamedQuery("user.findByPseudo").setParameter("pseudo", utilisateurPseudonyme.toLowerCase()).setParameter("pseudoDummy", dummyUser)
				.uniqueResult();

		if (utilisateur == null) {
			parametresOk = false;
			return false;
		}
		
		// On v�rifie que le hash est bon 
		String hash = emailManager.getHashMail(utilisateur);
		if (!hash.equals(this.hash)) {
			parametresOk = false;
			return false;
		}
		// Rastafari !
		parametresOk = true;
		return true;
	}

	/**
	 * Valide la saisie du mot de passe et de la confirmation de mot de passe.
	 * Ils doivent �tre identiques et non vides.	 * 
	 * @return vrais si les mdp sont valides, sinon affiche un message d'erreur.
	 */
	public boolean validerMDP() {
		if (nouveauMDP == null || nouveauMDPConfirm == null) {
			facesMessages.addToControlFromResourceBundle("nouveauMDPConfirm", "javax.faces.component.UIInput.REQUIRED");
			return false;
		}
		if (!nouveauMDP.equals(nouveauMDPConfirm)) {

			facesMessages.addToControlFromResourceBundle("nouveauMDPConfirm", "validator.mdpConfirm");
			return false;
		}
		return true;
	}

	/**
	 * Met � jour le mot de passe utilisateur.
	 * Un message g�n�ral est aussi envoy�.
	 * @return home si c'est bon (on redirige donc vers la page d'accueil), une autre valeur sinon
	 */
	@Transactional
	public String updateMDP() {
		log.info("RecupMDPManager.updateMDP() {0}", nouveauMDP);
		if (validerMDP()) {
			
			utilisateur.setMotDePasse(passwordManager.hash(nouveauMDP));
			hibernateSession.update(utilisateur);
			hibernateSession.flush();
			
			facesMessages.addFromResourceBundle("MotDePasse_updated");

			return Constantes.UPDATED;
		}
		return null;
	}

	/* ********************* Accesseurs ********************* */
	
	
	public String getUtilisateurPseudonyme() {
		return utilisateurPseudonyme;
	}

	public void setUtilisateurPseudonyme(String utilisateurPseudonyme) {
		this.utilisateurPseudonyme = utilisateurPseudonyme;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNouveauMDP() {
		return nouveauMDP;
	}

	public void setNouveauMDP(String nouveauMDP) {
		this.nouveauMDP = nouveauMDP;
	}

	public String getNouveauMDPConfirm() {
		return nouveauMDPConfirm;
	}

	public void setNouveauMDPConfirm(String nouveauMDPConfirm) {
		this.nouveauMDPConfirm = nouveauMDPConfirm;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getRecupMDP() {
		return recupMDP;
	}

	public void setRecupMDP(String recupMDP) {
		this.recupMDP = recupMDP;
	}

	public boolean isRecupMDPModalPanelAffiche() {
		return recupMDPModalPanelAffiche;
	}

	public void setRecupMDPModalPanelAffiche(boolean recupMDPModalPanelAffiche) {
		this.recupMDPModalPanelAffiche = recupMDPModalPanelAffiche;
	}
	
	public void wire() {
		ConversationUtils.beginConversation("/utilisateur/compte/ModificationMDP.xhtml");
	}
}
