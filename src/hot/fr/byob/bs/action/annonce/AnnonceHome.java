package fr.byob.bs.action.annonce;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import fr.byob.bs.action.LieuHome;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Annonce;

@Name("annonceHome")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class AnnonceHome implements Serializable {
	private static final long serialVersionUID = 1L;

	@In
	private FacesMessages facesMessages;

	@In(create = true)
	protected LieuHome lieuDepart;

	@In(create = true)
	protected LieuHome lieuArrivee;
	
	private Annonce instance;
	
	public AnnonceHome() {
	}
	
	public Boolean validerDateDebut() {
		Date debut = getInstance().getDateDebut();
		Date fin = getInstance().getDateFin();
		if (fin == null || debut == null) {
			return true;
		}

		if (debut.compareTo(fin) > 0) {
			facesMessages.addToControlFromResourceBundle("dateDebut", "validator.dateAvant");
			return false;
		}
		return true;
	}

	public Boolean validerDateFin() {
		Date debut = getInstance().getDateDebut();
		Date fin = getInstance().getDateFin();
		if (fin == null || debut == null) {
			return true;
		}

		if (debut.compareTo(fin) > 0) {
			facesMessages.addToControlFromResourceBundle("dateFin", "validator.dateApres");
			return false;
		}
		return true;
	}

	public Boolean validerDates(boolean required) {
		Date debut = getInstance().getDateDebut();
		Date fin = getInstance().getDateFin();
		if (required) {
			if (debut == null || fin == null) {
				return false;
			}
			if (debut.compareTo(fin) > 0) {
				return false;
			}
		} else {
			if (debut == null || fin == null) {
				return true;
			}
			if (debut.compareTo(fin) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public Annonce getInstance() {
		return instance;
	}

	public void setInstance(Annonce instance) {
		this.instance = instance;
	}
	
	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}
	
	public LieuHome getLieuDepart() {
		return this.lieuDepart;
	}

	public LieuHome getLieuArrivee() {
		return this.lieuArrivee;
	}

	public void clear() {
		setInstance(null);
		lieuDepart.setInstance(null);
		lieuArrivee.setInstance(null);
	}
}

