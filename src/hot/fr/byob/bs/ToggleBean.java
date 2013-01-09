package fr.byob.bs;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Roles;
import org.jboss.seam.annotations.Scope;
@Name("toggleBean")
@Scope(ScopeType.CONVERSATION)
@Roles( { 
@Role(name = "utilisateurListRecherche", scope = ScopeType.CONVERSATION),
@Role(name = "utilisateurListCivilite", scope = ScopeType.CONVERSATION), 
@Role(name = "utilisateurListXP", scope = ScopeType.CONVERSATION),
@Role(name = "bateauListRecherche", scope = ScopeType.CONVERSATION),
@Role(name = "bateauListModele", scope = ScopeType.CONVERSATION),
@Role(name = "bateauListEquipement", scope = ScopeType.CONVERSATION),
@Role(name = "demandeListRecherche", scope = ScopeType.CONVERSATION),
@Role(name = "demandeListUtilisateur", scope = ScopeType.CONVERSATION),
@Role(name = "offreListRecherche", scope = ScopeType.CONVERSATION),
@Role(name = "offreListUtilisateur", scope = ScopeType.CONVERSATION),
@Role(name = "offreListBateau", scope = ScopeType.CONVERSATION),
@Role(name = "utilisateurAvance", scope = ScopeType.CONVERSATION),
@Role(name = "annonceAvance", scope = ScopeType.CONVERSATION),
@Role(name = "bateauAvance", scope = ScopeType.CONVERSATION)
 })

public class ToggleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean toggleOuvert = true;
	
	public ToggleBean() {
	}
	
	public ToggleBean(boolean toggleOuvert) {
		this.toggleOuvert = toggleOuvert;
	}

	public boolean isToggleOuvert() {
		return toggleOuvert;
	}

	public void setToggleOuvert(boolean toggleOuvert) {
		this.toggleOuvert = toggleOuvert;
	}

}
