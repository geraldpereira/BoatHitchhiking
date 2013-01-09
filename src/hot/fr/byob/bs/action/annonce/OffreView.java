package fr.byob.bs.action.annonce;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import fr.byob.bs.BSEntityView;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Role;

@Name("offreView")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class OffreView extends BSEntityView<Offre> {
	private static final long serialVersionUID = 1L;

	@In(required = false, create = false)
	private OffreList offreList;
	@In(create = true)
	private AnnonceHome annonceHome;
	@In(create = true)
	EscaleManager escaleManager;

	@In(create = true)
	public Bateau dummyBoatInstance;
	
	@Override
	public boolean estProprietaire() {
		return super.estProprietaire() && (getInstance().getUtilisateur().getPseudonyme().equals(utilisateurCourant.getPseudonyme()) || Identity.instance().hasRole(Role.ADMIN));
	}
	
	public void setOffreNumAnnonce(Long id) {
		setId(id);
	}

	public Long getOffreNumAnnonce() {
		return (Long) getId();
	}



	public void wire() {
		if (ConversationUtils.beginConversation("/annonce/Offre.xhtml")) {
			if (offreList != null) {
				String numAnnonce = "" + getOffreNumAnnonce();
				Offre offre = offreList.get(numAnnonce);
				if (offre != null) {
					setInstance(offre);
				}
			}
			super.getInstance();
			annonceHome.setInstance(getInstance());
			annonceHome.getLieuDepart().wire(true, getInstance().getLieuDepart().clone());
			annonceHome.getLieuArrivee().wire(true, getInstance().getLieuArrivee().clone());
		}
	}


}
