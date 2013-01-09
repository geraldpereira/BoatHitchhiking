package fr.byob.bs.action.bateau;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import fr.byob.bs.BSEntityView;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Role;

@Name("bateauView")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class BateauView extends BSEntityView<Bateau> {

	private static final long serialVersionUID = 1L;

	@In(required = false)
	private BateauList bateauList;

	@In(create = true)
	private EquipementManager equipementManager;
	
	public void wire() {
		if (ConversationUtils.beginConversation("/bateau/Bateau.xhtml")) {
			if (bateauList != null) {
				String idBateau = "" + getBateauIdBateau();
				Bateau bateau = bateauList.get(idBateau);
				if (bateau != null) {
					setInstance(bateau);
				}
			}
			getInstance();
			equipementManager.wire(this);
		}
	}

	@Override
	public boolean estProprietaire() {
		return super.estProprietaire() && (getInstance().getUtilisateur().getPseudonyme().equals(utilisateurCourant.getPseudonyme()) || Identity.instance().hasRole(Role.ADMIN));
	}

	/* Accesseurs */

	public void setBateauIdBateau(Long id) {
		setId(id);
	}

	public Long getBateauIdBateau() {
		return (Long) getId();
	}
	
}
