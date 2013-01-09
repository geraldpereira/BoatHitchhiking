package fr.byob.bs.action.annonce;

import static fr.byob.bs.Constantes.PERSISTED;
import static fr.byob.bs.Constantes.REMOVED;
import static fr.byob.bs.Constantes.UPDATED;

import java.util.Date;
import java.util.HashMap;
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
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.utilisateur.Role;

@Name("demandeEdit")
@Scope(ScopeType.CONVERSATION)
@Roles( { @org.jboss.seam.annotations.Role(name = "demandeRemove", scope = ScopeType.EVENT) })
@MeasureCalls
public class DemandeEdit extends BSEntityEdit<Demande> {
	private static final long serialVersionUID = 1L;

	@In(create = true)
	private AnnonceHome annonceHome;
	@In(create = true)
	private HashMap<Long, Pays> paysMap;
	@In
	private Events events;
	@In(required = false, create = true)
	private DemandeHelper demandeHelper;

	@In(required = false, create = false)
	private DemandeView demandeView;

	public void setDemandeNumAnnonce(Long id) {
		setId(id);
	}

	public Long getDemandeNumAnnonce() {
		return (Long) getId();
	}

	public void wire() {
		if (ConversationUtils.beginConversation("/annonce/DemandeEdit.xhtml")) {
			Long numAnnonce = getDemandeNumAnnonce();
			if (demandeView != null && demandeView.getId() != null && (demandeView.getId().equals(numAnnonce))) {
				setDemandeNumAnnonce(demandeView.getDemandeNumAnnonce());
				setInstance(demandeView.getInstance());
				setSession(demandeView.getSession());
			}
			super.getInstance();
			if (!super.isManaged()) {
				// Initialiser les pays à celui de l'utilisateur
				if (utilisateurCourant != null) {
					Pays paysUtilisateur = paysMap.get(utilisateurCourant.getCoordonnees().getLieu().getPays().getIdPays());
					// getInstance().getLieuDepart().setPays(paysUtilisateur);
					// getInstance().getLieuArrivee().setPays(paysUtilisateur);

					// Initialiser la monnaie a celle du pays de l'utilisateurs
					getInstance().getContribFin().setMonnaie(paysUtilisateur.getMonnaie());
				}
			}
			annonceHome.setInstance(getInstance());
			annonceHome.getLieuDepart().wire(false, getInstance().getLieuDepart() != null ? getInstance().getLieuDepart().clone() : null);
			annonceHome.getLieuArrivee().wire(false, getInstance().getLieuArrivee() != null ? getInstance().getLieuArrivee().clone() : null);
		}
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
		info("Atempt to persist request {0} for user {1}", getInstance(), utilisateurCourant);
		try {
			// Verification du pseudo
			if (!annonceHome.validerDates(false)) {
				return "dates_incorrectes";
			}
			// Maj du lieu
			getInstance().setLieuDepart(annonceHome.getLieuDepart().getLieu());
			getInstance().setLieuArrivee(annonceHome.getLieuArrivee().getLieu());

			getInstance().setDateMaj(new Date());
			getInstance().setUtilisateur(utilisateurCourant);
			getInstance().setNote(demandeHelper.getNote(getInstance()));
			// Enregistrement de la demande
			String retour = super.persist();
			if (!PERSISTED.equals(retour)) {
				throw new BSException("error.demande.create");
			}

			// Add it to the home list
			events.raiseTransactionSuccessEvent("demandeAdded", getInstance());

			info("Persisted request {0} for user {1}", getInstance(), utilisateurCourant);
			
			return PERSISTED;
		} catch (BSException bse) {
			error("Error while persisting request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while persisting request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.demande.create");
		}
	}

	@Override
	@Transactional
	public String update() {
		info("Atempt to update request {0} for user {1}", getInstance(), utilisateurCourant);
			
		try {
			// Verification du pseudo
			if (!annonceHome.validerDateDebut() || !annonceHome.validerDateFin()) {
				return "dates_incorrectes";
			}

			// Maj du lieu
			getInstance().setLieuDepart(annonceHome.getLieuDepart().getLieu());
			getInstance().setLieuArrivee(annonceHome.getLieuArrivee().getLieu());

			getInstance().setDateMaj(new Date());
			
			getInstance().setNote(demandeHelper.getNote(getInstance()));

			// Enregistrement de l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.demande.update");
			}

			// annonceList.updateDemande(getInstance());
			events.raiseTransactionSuccessEvent("demandeUpdated", getInstance());
			
			info("Updated request {0} for user {1}", getInstance(), utilisateurCourant);
			
			return UPDATED;
		} catch (BSException bse) {
			error("Error while updating request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while updating request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.demande.update");
		}
	}

	@Override
	@Transactional
	public String remove() {
		info("Atempt to remove request {0} for user {1}", getInstance(), utilisateurCourant);

		validerAuthorisation();

		Demande ref = getInstance();

		String retour = super.remove();
		if (!REMOVED.equals(retour)) {
			throw new BSException("error.demande.remove");
		}

		events.raiseTransactionSuccessEvent("demandeRemoved", ref);

		info("Removed request {0} for user {1}", ref, utilisateurCourant);

		return REMOVED;
	}

	@Transactional
	public String updateNote(Demande demande) {
		setInstance(demande);
		setDemandeNumAnnonce(demande.getNumAnnonce());
		wire();
		if (utilisateurCourant != null)
			info("Atempt to update Note request {0} for user {1}", getInstance(), utilisateurCourant);
		else
			info("Atempt to update Note request {0} for user {1}", getInstance(), "batch");

		try {
			demande.setNote(demandeHelper.getNote(getInstance()));

			// Enregistrement de la demande
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.demande.update");
			}

			events.raiseTransactionSuccessEvent("demandeUpdated", getInstance());

			if (utilisateurCourant != null)
				info("Updated request {0} for user {1}", getInstance(), utilisateurCourant);
			else
				info("Updated request {0} for user {1}", getInstance(), "batch");

			return UPDATED;
		} catch (BSException bse) {
			error("Error while updating Note request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			error("Error while updating Note request {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.demande.update");
		}
	}

	public String activate() {
		getInstance().setValide(true);

		// Enregistrement de la demande
		String retour = super.update();
		if (!UPDATED.equals(retour)) {
			throw new BSException("error.demande.update");
		}

		events.raiseTransactionSuccessEvent("demandeAdded", getInstance());

		return UPDATED;
	}

}
