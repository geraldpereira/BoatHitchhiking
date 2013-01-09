package fr.byob.bs.action.utilisateur;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.annonce.DemandeEdit;
import fr.byob.bs.action.annonce.OffreEdit;
import fr.byob.bs.action.bateau.BateauEdit;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Utilisateur;


/**
 * Gére l'activation d'un compte utilisateur. L'activation sert à s'assurer que l'utilisateur est bien le propriétaire de l'adresse mail enregistrée.
 * Un compte non actif ne peut être utilisé. 
 * 
 * L'envoi de l'email d'activation est effectué par UtilisateurHome lors de la sauvegarde d'un utilisateur.
 * Cet email contient un lien vers la page d'activation, gérée ici.
 * 
 * @author GPEREIRA
 *
 */
@Name("activationManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class ActivationManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Logger
	private Log log;
	@In
	private Session hibernateSession;
	@In(create = true)
	private String dummyUser;
	@In
	private Events events;

	/**
	 * Paramètre passé à la page d'activation : le pseudo de l'utilisateur a activer.
	 */
	private String utilisateurPseudonyme;
	
	/**
	 * Paramètre passé à la page d'activation : le code d'activation.
	 * On enregistre en base, pour chaque utilisateur fraichement créé, une chaine de caractères aléatoire : le code d'activation.
	 * Si cette chaine est vide l'utilisateur est considéré comem actif.
	 * 
	 */
	private String codeActivation;

	/**
	 * Pour éviter que des robots ne puissent facilement créer et activer des comptes utilisateur, on utilise cette protection supplémentarie.
	 * Cette méthode modifie l'utilisateur en base de donnée pour l'activer. 
	 * 
	 * @return le message indiquant si l'activation s'est bien déroulée ou si une erreur est survenue.
	 */
	@Transactional
	public String activerUtilisateur() {
		log.info("Activating user {0}", utilisateurPseudonyme);
		
		ResourceBundle messages = SeamResourceBundle.getBundle();
		try {
			
			if (utilisateurPseudonyme == null) {
				return messages.getString(
						"activation.utilisateurInconnu");
			}
			
			Utilisateur utilisateur = (Utilisateur) hibernateSession
					.getNamedQuery("user.findByPseudo").setParameter("pseudo", utilisateurPseudonyme.toLowerCase()).setParameter("pseudoDummy",
							dummyUser)
					.uniqueResult();

			if (utilisateur == null) {
				return messages.getString("activation.utilisateurInconnu");
			}

			if (codeActivation == null) {
				return messages.getString("activation.codeActivationVide");
			}

			if (!codeActivation.equals(utilisateur.getActif())) {
				return messages.getString("activation.codeActivationIncorrect");
			}
			
			activerUtilisateur(/* hibernateSession, */utilisateur);

			return messages.getString("activation.ok");

		} catch (Exception e) {
			log.error("Error while activating user {0} : {1} ", utilisateurPseudonyme, e.getMessage());
			return messages.getString("activation.erreur");
		}

	}
	
	@In(create = true)
	private UtilisateurEdit utilisateurEdit;

	@In(create = true)
	private BateauEdit bateauEdit;

	@In(create = true)
	private OffreEdit offreEdit;

	@In(create = true)
	private DemandeEdit demandeEdit;

	public void activerUtilisateur(Utilisateur utilisateur) {

		// Recharger l'utilisateur
		utilisateurEdit.setUtilisateurPseudonyme(utilisateur.getPseudonyme());
		utilisateurEdit.wire();
		utilisateurEdit.activate();

		List<Bateau> bateaux = utilisateurEdit.getInstance().getBateaux();
		for (Bateau bateau : bateaux) {
			// Choper tous les bateaux de l'utilisateur (complets)
			bateauEdit.setBateauIdBateau(bateau.getIdBateau());
			bateauEdit.wire();
			bateauEdit.activate();
		}

		List<Demande> demandes = utilisateurEdit.getInstance().getDemandes();
		for (Demande demande : demandes) {
			// Choper tous les bateaux de l'utilisateur (complets)
			demandeEdit.setDemandeNumAnnonce(demande.getNumAnnonce());
			demandeEdit.wire();
			demandeEdit.activate();
		}

		List<Offre> offres = utilisateurEdit.getInstance().getOffres();
		for (Offre offre : offres) {
			// Choper tous les bateaux de l'utilisateur (complets)
			offreEdit.setOffreNumAnnonce(offre.getNumAnnonce());
			offreEdit.wire();
			offreEdit.activate();
		}

		// hibernateSession.update(utilisateur);
		//		
		// Query queryBateau =
		// hibernateSession.getNamedQuery("bateau.validateByUser");
		// queryBateau.setParameter("pseudo", utilisateur.getPseudonyme());
		// queryBateau.executeUpdate();
		//
		// Query queryOffre =
		// hibernateSession.getNamedQuery("offre.validateByUser");
		// queryOffre.setParameter("pseudo", utilisateur.getPseudonyme());
		// queryOffre.executeUpdate();
		//
		// Query queryDemande =
		// hibernateSession.getNamedQuery("demande.validateByUser");
		// queryDemande.setParameter("pseudo", utilisateur.getPseudonyme());
		// queryDemande.executeUpdate();
		//		
		// hibernateSession.flush();
		//
		// events.raiseTransactionSuccessEvent("utilisateurAdded", utilisateur);
		//
		// if (utilisateur.getBateaux() != null &&
		// !utilisateur.getBateaux().isEmpty()) {
		// events.raiseTransactionSuccessEvent("bateauAdded",
		// utilisateur.getBateaux().get(0));
		// }
		//		
		// if (utilisateur.getDemandes() != null &&
		// !utilisateur.getDemandes().isEmpty()) {
		// events.raiseTransactionSuccessEvent("demandeAdded",
		// utilisateur.getDemandes().get(0));
		// }
		//
		// if (utilisateur.getOffres() != null &&
		// !utilisateur.getOffres().isEmpty()) {
		// events.raiseTransactionSuccessEvent("offreAdded",
		// utilisateur.getOffres().get(0));
		// }
	}
	
	/* ********************* Accesseurs ********************* */
	
	public String getUtilisateurPseudonyme() {
		return utilisateurPseudonyme;
	}

	public void setUtilisateurPseudonyme(String utilisateurPseudonyme) {
		this.utilisateurPseudonyme = utilisateurPseudonyme;
	}

	public String getCodeActivation() {
		return codeActivation;
	}

	public void setCodeActivation(String codeActivation) {
		this.codeActivation = codeActivation;
	}

}