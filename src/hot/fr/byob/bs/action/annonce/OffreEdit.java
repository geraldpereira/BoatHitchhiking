package fr.byob.bs.action.annonce;

import static fr.byob.bs.Constantes.PERSISTED;
import static fr.byob.bs.Constantes.REMOVED;
import static fr.byob.bs.Constantes.UPDATED;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Roles;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.security.AuthorizationException;
import org.jboss.seam.security.Identity;

import fr.byob.bs.BSEntityEdit;
import fr.byob.bs.BSException;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Pays;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Role;

@Name("offreEdit")
@Scope(ScopeType.CONVERSATION)
@Roles( { @org.jboss.seam.annotations.Role(name = "offreRemove", scope = ScopeType.EVENT) })
@MeasureCalls
public class OffreEdit extends BSEntityEdit<Offre> {
	private static final long serialVersionUID = 1L;

	@In(create = true)
	private AnnonceHome annonceHome;
	@In(create = true)
	private HashMap<Long, Pays> paysMap;
	@In(create = true)
	EscaleManager escaleManager;

	@In(create = true)
	public Bateau dummyBoatInstance;
	
	@In
	private Events events;
	
	@In(required = false, create = true)
	private OffreHelper offreHelper;
	
	@In(required = false, create = false)
	private OffreView offreView;

	private List<Bateau> bateaux;
	
	public List<Bateau> getBateaux() {
		return this.bateaux;
	}

	public void setOffreNumAnnonce(Long id) {
		setId(id);
	}

	public Long getOffreNumAnnonce() {
		return (Long) getId();
	}

	public void wire() {
		if (ConversationUtils.beginConversation("/annonce/OffreEdit.xhtml")) {
			Long numAnnonce = getOffreNumAnnonce();
			if (offreView != null && offreView.getId() != null && (offreView.getId().equals(numAnnonce))) {
				setOffreNumAnnonce(offreView.getOffreNumAnnonce());
				setInstance(offreView.getInstance());
				setSession(offreView.getSession());
			}
			super.getInstance();
			if (!super.isManaged()) {
				if (utilisateurCourant != null) {
					// Initialiser les pays à celui de l'utilisateur
					Pays paysUtilisateur = paysMap.get(utilisateurCourant.getCoordonnees().getLieu().getPays().getIdPays());
					getInstance().getLieuDepart().setPays(paysUtilisateur);
					getInstance().getLieuArrivee().setPays(paysUtilisateur);

					// Initialiser la monnaie a celle du pays de l'utilisateurs
					getInstance().getContribFin().setMonnaie(paysUtilisateur.getMonnaie());
				}
			}

			annonceHome.setInstance(getInstance());
			annonceHome.getLieuDepart().wire(true, getInstance().getLieuDepart().clone());
			annonceHome.getLieuArrivee().wire(true, getInstance().getLieuArrivee().clone());

			// On fait ca ici pour éviter de remettre les escales a zero si
			// l'utilisateur ajoute un bateau en passant par la page d'édition
			// d'offre
			escaleManager.wire(this);
		}
	}

	public void initBateaux() {
		// Charger les bateaux de l'utilisateur
		String pseudonyme = null;
		if (getInstance().getUtilisateur() != null) {
			pseudonyme = getInstance().getUtilisateur().getPseudonyme();
		} else {
			pseudonyme = utilisateurCourant.getPseudonyme();
		}
		bateaux = getSession().getNamedQuery("bateau.findByUser").setParameter("pseudo", pseudonyme).list();
		bateaux.add(0, dummyBoatInstance);
	}

	public Boolean validerBateau() {
		if (getInstance().getBateau() == null || getInstance().getBateau().getIdBateau() == 1L) {
			return false;
		}
		return true;
	}

	@Override
	public void validerAuthorisation() {
		// On valide pour l'édition que l'utilisateur connecté est bien
		// propriétaire du bateau.
		if (getInstance().getUtilisateur() != null && utilisateurCourant != null && !getInstance().getUtilisateur().getPseudonyme().equals(utilisateurCourant.getPseudonyme())
				&& !Identity.instance().hasRole(Role.ADMIN)) {
			ResourceBundle messages = SeamResourceBundle.getBundle();
			throw new AuthorizationException(messages.getString("annonce.propriorequis"));
		}
	}

	@Override
	@Transactional
	public String persist() {
		info("Atempt to persist offer {0} for user {1}", getInstance(), utilisateurCourant);
		
		try {
			// Verification du pseudo
			if (!annonceHome.validerDates(true)) {
				return "dates_incorrectes";
			}

			if (!validerBateau()) {
				return "bateau_fail";
			}

			// Maj des escales
			getInstance().setEscales(escaleManager.updateEscales());

			// Maj du lieu
			getInstance().setLieuDepart(annonceHome.getLieuDepart().getLieu());
			getInstance().setLieuArrivee(annonceHome.getLieuArrivee().getLieu());

			getInstance().setDateMaj(new Date());
			getInstance().setUtilisateur(utilisateurCourant);
			getInstance().setNote(offreHelper.getNote(getInstance()));
			// Enregistrement de l'utilisateur
			String retour = super.persist();
			if (!PERSISTED.equals(retour)) {
				throw new BSException("error.offre.create");
			}

			// Add it to the home list
			events.raiseTransactionSuccessEvent("offreAdded", getInstance());

			info("Persisted offer {0} for user {1}", getInstance(), utilisateurCourant);
			
			return PERSISTED;
		} catch (BSException bse) {
			error("Error while persisting offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while persisting offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.offre.create");
		}
	}

	@Override
	@Transactional
	public String update() {
		info("Atempt to update offer {0} for user {1}", getInstance(), utilisateurCourant);
		
		try {
			// Verification du pseudo
			if (!annonceHome.validerDateDebut() || !annonceHome.validerDateFin()) {
				return "dates_incorrectes";
			}

			if (!validerBateau()) {
				return "bateau_fail";
			}

			// On vire les escales supprimées de la base de donnée
			escaleManager.removeEscales();
			// On met a jour les autre
			getInstance().setEscales(escaleManager.updateEscales());

			// Maj du lieu
			getInstance().setLieuDepart(annonceHome.getLieuDepart().getLieu());
			getInstance().setLieuArrivee(annonceHome.getLieuArrivee().getLieu());

			getInstance().setDateMaj(new Date());
			
			getInstance().setNote(offreHelper.getNote(getInstance()));

			// Enregistrement de l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.offre.update");
			}

			// annonceList.updateOffre(getInstance());
			events.raiseTransactionSuccessEvent("offreUpdated", getInstance());

			info("Updated offer {0} for user {1}", getInstance(), utilisateurCourant);
			
			return UPDATED;
		} catch (BSException bse) {
			error("Error while updating offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while updating offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.offre.update");
		}
	}

	@Override
	@Transactional
	public String remove() {
		info("Atempt to remove offer {0} for user {1}", getInstance(), utilisateurCourant);
		
		validerAuthorisation();

		Offre ref = getInstance();

		String retour = super.remove();
		if (!REMOVED.equals(retour)) {
			throw new BSException("error.offre.remove");
		}

		events.raiseTransactionSuccessEvent("offreRemoved", getInstance());
		
		info("Removed offer {0} for user {1}", ref, utilisateurCourant);
		
		return REMOVED;
	}
	
	@Transactional
	public String updateNote(Offre offre) {
		setInstance(offre);
		setOffreNumAnnonce(offre.getNumAnnonce());
		wire();
		if (utilisateurCourant != null)
			info("Atempt to update Note offer {0} for user {1}", getInstance(), utilisateurCourant);
		else
			info("Atempt to update Note offer {0} for user {1}", getInstance(), "batch");

		try {
			offre.setNote(offreHelper.getNote(getInstance()));

			// Enregistrement de l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.demande.update");
			}

			events.raiseTransactionSuccessEvent("offreUpdated", getInstance());

			if (utilisateurCourant != null)
				info("Updated offer {0} for user {1}", getInstance(), utilisateurCourant);
			else
				info("Updated offer {0} for user {1}", getInstance(), "batch");
			
			return UPDATED;
		} catch (BSException bse) {
			error("Error while updating Note offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while updating Note offer {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.demande.update");
		}
	}
	
	public String activate() {
		getInstance().setValide(true);

		// Enregistrement de la offre
		String retour = super.update();
		if (!UPDATED.equals(retour)) {
			throw new BSException("error.offre.update");
		}

		events.raiseTransactionSuccessEvent("offreAdded", getInstance());

		return UPDATED;
	}

	public String getSelectedTab() {
		if (!validerBateau()) {
			return "Bateau";
		}
		return "Escales";
	}

	public void setSelectedTab(String selectedTab) {
		// Does nothing
	}
}
