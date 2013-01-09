package fr.byob.bs.action;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

import fr.byob.bs.model.Lieu;
import fr.byob.bs.model.Pays;

public abstract class LieuSelectionManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Session hibernateSession;

	private boolean required;

	public LieuSelectionManager(Session hibernateSession, boolean required,
			Pays pays, String ville) {
		this(hibernateSession, required);
		setPays(pays);
		setVille(ville);
	}
	
	public LieuSelectionManager(Session hibernateSession, boolean required) {
		this(hibernateSession);
		this.required = required;
	}

	public LieuSelectionManager(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}

	/**
	 * Réinitialise la ville lors du changement de pays
	 * 
	 * @param id
	 */
	public void resetVille(/* String idForm, String id */) {
		setVille("");
		// resetVille(idForm + ":villeField" + id + ":ville" + id);
	}

	// public void resetVille(String villeId) {
	// UIViewRoot uiViewRoot = javax.faces.context.FacesContext
	// .getCurrentInstance().getViewRoot();
	// HtmlInputText inputText = null;
	// inputText = (HtmlInputText) uiViewRoot.findComponent(villeId);
	// inputText.setSubmittedValue("");
	// setVille("");
	// }

	/**
	 * Pour la complétion automatique de la ville en fonction du pays choisi
	 * 
	 * @param o
	 *            Ce qu'a commencé a saisir l'utilisateur
	 * @return
	 */
	public List<Lieu> autoCompleteVille(Object o) {
		// 1 = dummyPlace !!
		String sql = "select lieu from Lieu lieu where (lower(lieu.ville) like concat('% ',lower(:villeACompleter),'%') or lower(lieu.ville) like concat(lower(:villeACompleter),'%')) and lieu.idLieu <> 1";
		if (getPays() != null) {
			sql += " and lieu.pays = :pays ";
		}

		Query query = hibernateSession.createQuery(sql);

		query.setParameter("villeACompleter", o.toString());
		if (getPays() != null) {
			query.setParameter("pays", getPays());
		}
		List<Lieu> lieux = query.list();
		return lieux;
	}

	public abstract Pays getPays();

	public abstract void setPays(Pays pays);

	@NotNull
	@Length(max = 50)
	@Pattern(regex = "\\p{L}*([- ]\\p{L}*)*", message = "#{messages['validator.lettreEtMoins']}")
	public abstract String getVille();

	public abstract void setVille(String ville);

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void reset() {
		setPays(null);
		setVille(null);
	}

}
