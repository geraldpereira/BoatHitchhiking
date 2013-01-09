package fr.byob.bs.action.bateau;

import static fr.byob.bs.Constantes.CANCELLED;
import static fr.byob.bs.Constantes.PERSISTED;
import static fr.byob.bs.Constantes.REMOVED;
import static fr.byob.bs.Constantes.UPDATED;

import java.util.Date;
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
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Role;

@Name("bateauEdit")
@Scope(ScopeType.CONVERSATION)
@Roles( { @org.jboss.seam.annotations.Role(name = "bateauRemove", scope = ScopeType.EVENT) })
@MeasureCalls
public class BateauEdit extends BSEntityEdit<Bateau> {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	private EquipementManager equipementManager;
	
	@In(create = true)
	private BateauPhotoManager bateauPhotoManager;

	@In(create = true)
	private Bateau dummyBoatInstance;
	
	@In(required = false, create = false)
	private BateauView bateauView;

	@In
	private Events events;
	
	@In(required = false, create = true)
	private BateauHelper bateauHelper;

	public void setBateauIdBateau(Long id) {
		setId(id);
	}

	public Long getBateauIdBateau() {
		return (Long) getId();
	}

	public void wire() {
		if (ConversationUtils.beginConversation("/bateau/BateauEdit.xhtml")) {
			Long idBateau = getBateauIdBateau();
			if (bateauView != null && bateauView.getId() != null && (bateauView.getId().equals(idBateau))) {
				setBateauIdBateau(bateauView.getBateauIdBateau());
				setInstance(bateauView.getInstance());
				setSession(bateauView.getSession());
			}
			getInstance();
			bateauPhotoManager.wire();
			equipementManager.wire(this);
		}
	}

	@Override
	public void validerAuthorisation() {
		// On valide pour l'édition que l'utilisateur connecté est bien
		// propriétaire du bateau.
		if (getInstance().getUtilisateur() != null && utilisateurCourant != null && !getInstance().getUtilisateur().getPseudonyme().equals(utilisateurCourant.getPseudonyme())
				&& !Identity.instance().hasRole(Role.ADMIN)) {
			ResourceBundle messages = SeamResourceBundle.getBundle();
			throw new AuthorizationException(messages.getString("bateau.propriorequis"));
		}
	}


	@Override
	@Transactional
	public String remove() {
		
		info("Atempt to remove boat {0} for user {1}", getInstance(), utilisateurCourant);
		
		validerAuthorisation();
		
		Bateau ref = getInstance();
		
		// Rechercher les offre avec ce bateau
		List<Offre> offres = getSession().getNamedQuery("offre.findByBateau").setParameter("bateau", getInstance()).list();
	
		// Les parcourir
		for (Offre offre : offres) {
			// Mettre à jour l'offre courante avec le dummyBoat
			offre.setBateau(dummyBoatInstance);
			getSession().update(offre);
		}
		
		String retour = super.remove();
		if (!REMOVED.equals(retour)) {
			throw new BSException("error.bateau.remove");
		}

		bateauPhotoManager.removePhotos();
		
		events.raiseTransactionSuccessEvent("bateauRemoved", getInstance());
		
		info("Removed boat {0} for user {1}", ref, utilisateurCourant);
		
		return REMOVED;
	}


	@Override
	@Transactional
	public String persist() {

		info("Atempt to persist boat {0} for user {1}", getInstance(), utilisateurCourant);
		
		try {

			getInstance().setUtilisateur(utilisateurCourant);

			getInstance().setDateMaj(new Date());
			
			getInstance().setNote(bateauHelper.getNote(getInstance()));
			
			// Enregistrement du bateau
			String retour = super.persist();
			if (!PERSISTED.equals(retour)) {
				throw new BSException("error.bateau.create");
			}
			
			// Enregistrement des photo
			bateauPhotoManager.persistPhotos();
			
			// Enregistrement des équipements
			equipementManager.updateEquipements();
			events.raiseTransactionSuccessEvent("bateauAdded", getInstance());
			
			info("Persisted boat {0} for user {1}", getInstance(), utilisateurCourant);
			
			return PERSISTED;
		} catch (BSException bse) {
			// En cas d'erreur on supprime les fichiers de photo
			error("Error while persisting boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			// En cas d'erreur on supprime les fichiers de photo
			error("Error while persisting boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.bateau.create", e);
		}
	}

	@Override
	@Transactional
	public String update() {
		info("Atempt to update boat {0} for user {1}", getInstance(), utilisateurCourant);
		
		try {
			getInstance().setDateMaj(new Date());
			
			// Enregistrement des équipements
			equipementManager.updateEquipements();
			
			// Maj de la photo
			bateauPhotoManager.updatePhotos();
			
			getInstance().setNote(bateauHelper.getNote(getInstance()));
			
			// On met à jour l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.bateau.update");
			}
		
			events.raiseTransactionSuccessEvent("bateauUpdated", getInstance());

			info("Updated boat {0} for user {1}", getInstance(), utilisateurCourant);
			
			return UPDATED;
		} catch (BSException bse) {
			// bateauPhotoManager.supprimerFichiersTemporaires();
			error("Error while updating boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			// bateauPhotoManager.supprimerFichiersTemporaires();
			error("Error while updating boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.bateau.update", e);
		}
	}

	/**
	 * Lors de l'annulation d'une modification bateau , on reset tout !!
	 * 
	 * @return
	 */
	@Transactional
	public String cancel() {
		bateauPhotoManager.reset();
		return CANCELLED;
	}

	@Transactional
	public String updateNote(Bateau bateau) {
		setInstance(bateau);
		setBateauIdBateau(bateau.getIdBateau());
		wire();
		if (utilisateurCourant != null)
			info("Atempt to update Note boat {0} for user {1}", getInstance(), utilisateurCourant);
		else
			info("Atempt to update Note boat {0} for user {1}", getInstance(), "batch");
		try {
			bateau.setNote(bateauHelper.getNote(bateau));

			// On met à jour l'utilisateur
			String retour = super.update();
			if (!UPDATED.equals(retour)) {
				throw new BSException("error.bateau.update");
			}

			events.raiseTransactionSuccessEvent("bateauUpdated", bateau);

			if (utilisateurCourant != null)
				info("Updated boat {0} for user {1}", getInstance(), utilisateurCourant);
			else
				info("Updated boat {0} for user {1}", getInstance(), "batch");

			return UPDATED;
		} catch (BSException bse) {
			// bateauPhotoManager.supprimerFichiersTemporaires();
			error("Error while updating Note boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, bse.getMessage());
			throw bse;
		} catch (Exception e) {
			// bateauPhotoManager.supprimerFichiersTemporaires();
			error("Error while updating Note boat {0} for user {1} : {2} ", getInstance(), utilisateurCourant, e.getMessage());
			throw new BSException("error.bateau.update", e);
		}
	}

	public String activate() {
		getInstance().setValide(true);

		String retour = super.update();
		if (!UPDATED.equals(retour)) {
			throw new BSException("error.bateau.update");
		}

		events.raiseTransactionSuccessEvent("bateauAdded", getInstance());
		return UPDATED;
	}


}
