package fr.byob.bs.action.utilisateur;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import fr.byob.bs.BSEntityView;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Role;
import fr.byob.bs.model.utilisateur.Utilisateur;

/**
 * Crud pour l'utilisateur et tout ce qui va avec (photo, langues, compétences
 * et expériences)
 * 
 * @author GPEREIRA
 * 
 */
@Name("utilisateurView")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class UtilisateurView extends BSEntityView<Utilisateur> {

	private static final long serialVersionUID = 1L;

	@In(required = false)
	private UtilisateurList utilisateurList;


	/**
	 * Initialise ce home
	 */
	public void wire() {
		if (ConversationUtils.beginConversation("/utilisateur/Utilisateur.xhtml")) {
			getInstance();
		}
	}

	@Override
	public boolean estProprietaire() {
		return super.estProprietaire() && (getInstance().getPseudonyme().equals(utilisateurCourant.getPseudonyme()) || Identity.instance().hasRole(Role.ADMIN));
	}

	/**
	 * If the row number is given in page params, we try to take the instance
	 * from the offre bean.
	 * 
	 * WARNING : DO NOT USE RowNumber page parameter to edit an instance.
	 * 
	 * @param rowNumber
	 */
	public void setRowNumber(Integer rowNumber) {
		if (utilisateurList != null) {
			if (rowNumber >= 0 && rowNumber < utilisateurList.getResultList().size()) {
				Utilisateur utilisateur = utilisateurList.getResultList().get(rowNumber);
				if (utilisateur.getPseudonyme().equals(getUtilisateurPseudonyme())) {
					setUtilisateurPseudonyme(utilisateur.getPseudonyme());
					setInstance(utilisateur);
				}
			}
		}
	}

	public Integer getRowNumber() {
		return null;
	}

	/* Accesseurs */
	public void setUtilisateurPseudonyme(String id) {
		setId(id);
	}

	public String getUtilisateurPseudonyme() {
		return (String) getId();
	}


}
