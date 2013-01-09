package fr.byob.bs.action.annonce;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import fr.byob.bs.BSEntityView;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.utilisateur.Role;

@Name("demandeView")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class DemandeView extends BSEntityView<Demande> {
	private static final long serialVersionUID = 1L;

	@In(required = false, create = false)
	private DemandeList demandeList;
	@In(create = true)
	private AnnonceHome annonceHome;

	@Override
	public boolean estProprietaire() {
		return super.estProprietaire() && (getInstance().getUtilisateur().getPseudonyme().equals(utilisateurCourant.getPseudonyme()) || Identity.instance().hasRole(Role.ADMIN));
	}

	public void setDemandeNumAnnonce(Long id) {
		setId(id);
	}

	public Long getDemandeNumAnnonce() {
		return (Long) getId();
	}

	public void wire() {
		if (ConversationUtils.beginConversation("/annonce/Demande.xhtml")) {
			if (demandeList != null) {
				String numAnnonce = "" + getDemandeNumAnnonce();
				Demande demande = demandeList.get(numAnnonce);
				if (demande != null) {
					setInstance(demande);
				}
			}
			super.getInstance();
			annonceHome.setInstance(getInstance());
			annonceHome.getLieuDepart().wire(false, getInstance().getLieuDepart() != null ? getInstance().getLieuDepart().clone() : null);
			annonceHome.getLieuArrivee().wire(false, getInstance().getLieuArrivee() != null ? getInstance().getLieuArrivee().clone() : null);
		}
	}


}
