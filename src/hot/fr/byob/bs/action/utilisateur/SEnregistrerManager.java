package fr.byob.bs.action.utilisateur;

import static fr.byob.bs.Constantes.PERSISTED;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import fr.byob.bs.BSException;
import fr.byob.bs.PasswordManager;
import fr.byob.bs.action.LieuHome;
import fr.byob.bs.action.annonce.AnnonceHome;
import fr.byob.bs.action.annonce.DemandeHelper;
import fr.byob.bs.action.annonce.OffreHelper;
import fr.byob.bs.action.bateau.BateauHelper;
import fr.byob.bs.action.utilisateur.messagerie.EmailManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Monnaie;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Utilisateur;
import fr.byob.bs.validator.MailConfirm;

/**
 * Crud pour l'utilisateur et tout ce qui va avec (photo, langues, compétences
 * et expériences)
 * 
 * @author GPEREIRA
 * 
 */
@Name("senregistrerManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class SEnregistrerManager {

	private static final long serialVersionUID = 1L;

	public final static String NONE = "None";
	public final static String OFFRE = "Offre";
	public final static String DEMANDE = "Demande";

	@Logger
	private transient Log log;
	@In
	private FacesMessages facesMessages;
	@In
	private Locale locale;
	@In(create = true)
	private EmailManager emailManager;

	@In
	private Session hibernateSession;

	@In(value = "BSPasswordManager", create = true)
	private PasswordManager passwordManager;
	@In(create = true)
	private LieuHome lieuHome;

	@In(create = true)
	private String dummyUser;

	private String mode;

	private Utilisateur utilisateur = new Utilisateur();

	private Demande demande;

	private Offre offre;

	private Bateau bateau;

	@In(create = true)
	private AnnonceHome annonceHome;

	@MailConfirm
	private transient String mailConfirm;
	
	@In(required = false, create = true)
	BateauHelper bateauHelper;
	
	@In(required = false, create = true)
	OffreHelper offreHelper;

	@In(required = false, create = true)
	DemandeHelper demandeHelper;

	@In(required = false, create = true)
	UtilisateurHelper utilisateurHelper;

	@End
	public String persist() {

		log.info("Atempt to register user {0}", utilisateur);

		try {

			// Verification du pseudo
			if (!validerPseudonyme()) {
				return "pseudo_pas_unique";
			}

			// Verification du mail
			if (!validerMail()) {
				return "mail_pas_unique";
			}

			// Initialisation des champs
			getUtilisateur().setMailAuth(false);
			getUtilisateur().setMoyenneNotes(new BigDecimal(-1));
			getUtilisateur().setDateInscription(new Date());
			getUtilisateur().setActif(passwordManager.getRandomString(10));
			// Initilisation de la locale
			getUtilisateur().setLanguePreferee(locale.toString());
			// Initialisation du lieu
			getUtilisateur().getCoordonnees().setLieu(lieuHome.getLieu());

			// On hash le mot de passe
			getUtilisateur().setMotDePasse(passwordManager.hash(utilisateur.getMotDePasse()));
			
			getUtilisateur().setNote(utilisateurHelper.getNote(getUtilisateur()));
			// Enregistrement de l'utilisateur
			hibernateSession.persist(utilisateur);

			if (DEMANDE.equals(mode)) {
				log.info("Atempt to persist request {0} for user {1}", demande, utilisateur);

				if (!annonceHome.validerDates(false)) {
					return "dates_incorrectes";
				}
				demande.setLieuDepart(annonceHome.getLieuDepart().getLieu());
				demande.setLieuArrivee(annonceHome.getLieuArrivee().getLieu());
				demande.setDateMaj(new Date());
				demande.setUtilisateur(utilisateur);
				demande.setValide(false);
				demande.setNote(demandeHelper.getNote(demande));
				hibernateSession.persist(demande);
			} else if (OFFRE.equals(mode)) {

				log.info("Atempt to persist boat {0} for user {1}", bateau, utilisateur);
				bateau.setValide(false);
				bateau.setDateMaj(new Date());
				bateau.setUtilisateur(utilisateur);
				bateau.setNote(bateauHelper.getNote(bateau));
				hibernateSession.persist(bateau);
				log.info("Atempt to persist offer {0} for user {1}", offre, utilisateur);

				if (!annonceHome.validerDates(true)) {
					return "dates_incorrectes";
				}
				offre.setValide(false);
				offre.setLieuDepart(annonceHome.getLieuDepart().getLieu());
				offre.setLieuArrivee(annonceHome.getLieuArrivee().getLieu());
				offre.setDateMaj(new Date());
				offre.setUtilisateur(utilisateur);
				offre.setBateau(bateau);
				offre.setNote(offreHelper.getNote(offre));
				hibernateSession.persist(offre);
			}

			hibernateSession.flush();

			// Envoi de l'email d'activation
			emailManager.envoiEmailActivation(getUtilisateur());

			// Pas de langue, et ca évite les lazyLoading exception
			// TODO faire de même pour le reste et lors des autres appels
			utilisateur.setLangues(Collections.EMPTY_LIST);

			log.info("Registered user {0}", utilisateur);

			return PERSISTED;
		} catch (BSException bse) {
			throw bse;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BSException("error.utilisateur.create");
		}
	}

	/**
	 * Valide que le pseudonyme n'est pas déjà utilisé
	 * 
	 * @return
	 */
	public Boolean validerPseudonyme() {
		if (getUtilisateur().getPseudonyme().toLowerCase().equals(dummyUser.toLowerCase())) {
			return false;
		}

		if (!hibernateSession.getNamedQuery("user.findByPseudo").setParameter("pseudoDummy", dummyUser).setParameter("pseudo", getUtilisateur().getPseudonyme().toLowerCase()).list().isEmpty()) {
			facesMessages.addToControlFromResourceBundle("pseudonyme", "validator.pseudoPri");
			return false;
		}
		return true;
	}

	/**
	 * Valide que le mail n'est pas déjà utilisé
	 * 
	 * @return
	 */
	public Boolean validerMail() {
		if (getUtilisateur().getMail().toLowerCase().equals(dummyUser.toLowerCase())) {
			return false;
		}

		if (!hibernateSession.getNamedQuery("user.findByMail").setParameter("pseudoDummy", dummyUser).setParameter("mail", getUtilisateur().getMail().toLowerCase()).list().isEmpty()) {
			facesMessages.addToControlFromResourceBundle("mail", "validator.mailPri");
			return false;
		}
		return true;
	}

	/**
	 * Initialise ce home
	 */
	@Begin
	public void wire() {
		setUtilisateur(new Utilisateur());

		// Le lieu est clonné (voir lieuHome pour explications)
		lieuHome.wire(true, getUtilisateur().getCoordonnees().getLieu().clone());
		mode = NONE;
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;

		Monnaie monnaie = null;
		// Initialiser la monnaie a celle du pays de l'utilisateurs
		if ("en".equals(locale.toString())) {
			monnaie = Monnaie.USD;
		} else { // if ("fr".equals(locale.toString())) {
			monnaie = Monnaie.EUR;
		}

		if (OFFRE.equals(mode)) {
			demande = null;
			bateau = new Bateau();
			offre = new Offre();
			if (monnaie != null) {
				// Initialiser la monnaie a celle du pays de l'utilisateurs
				offre.getContribFin().setMonnaie(monnaie);
			}
			annonceHome.setInstance(offre);
			annonceHome.getLieuDepart().wire(true, offre.getLieuDepart().clone());
			annonceHome.getLieuArrivee().wire(true, offre.getLieuArrivee().clone());
		} else if (DEMANDE.equals(mode)) {
			offre = null;
			bateau = null;
			demande = new Demande();
			if (monnaie != null) {
				// Initialiser la monnaie a celle du pays de l'utilisateurs
				demande.getContribFin().setMonnaie(monnaie);
			}
			annonceHome.setInstance(demande);
			annonceHome.getLieuDepart().wire(false, demande.getLieuDepart() != null ? demande.getLieuDepart().clone() : null);
			annonceHome.getLieuArrivee().wire(false, demande.getLieuArrivee() != null ? demande.getLieuArrivee().clone() : null);
		} else { // None
			offre = null;
			demande = null;
			bateau = null;
			annonceHome.clear();
		}
	}

	public Demande getDemande() {
		return this.demande;
	}

	public void setDemande(Demande demande) {
		this.demande = demande;
	}

	public Offre getOffre() {
		return this.offre;
	}

	public void setOffre(Offre offre) {
		this.offre = offre;
	}

	public Bateau getBateau() {
		return this.bateau;
	}

	public void setBateau(Bateau bateau) {
		this.bateau = bateau;
	}

	public String getMailConfirm() {
		return mailConfirm;
	}

	public void setMailConfirm(String mailConfirm) {
		this.mailConfirm = mailConfirm;
	}
}
