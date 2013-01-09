package fr.byob.bs.action.annonce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.HibernateEntityHome;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.LieuHome;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Pays;
import fr.byob.bs.model.annonce.Escale;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("escaleManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class EscaleManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;
	// @In
	// private FacesMessages facesMessages;

	@Logger
	private Log log;
	@In
	private Session hibernateSession;

	private HibernateEntityHome<Offre> offreHome;
	@In
	private Locale locale;
	@In(create = true)
	private List<Pays> pays;
	@In(create = true)
	private HashMap<Long, Pays> paysMap;
	
	List<Escale> escalesASupprimer;

	List<Escale> escalesOffre;
	List<LieuHome> escalesLieuHome;

	public List<Escale> getEscalesOffre() {
		return escalesOffre;
	}

	public void setEscalesOffre(List<Escale> escalesOffre) {
		this.escalesOffre = escalesOffre;
	}

	
	
	public List<LieuHome> getEscalesLieuHome() {
		return this.escalesLieuHome;
	}

	/**
	 * Supprime de la base les escales qui sont supprimées par l'utilisateur
	 */
	public void removeEscales() {
		for (Escale escale : escalesASupprimer) {
			hibernateSession.delete(escale);
		}
	}

	public void wire(HibernateEntityHome<Offre> offreHome) {
		this.offreHome = offreHome;
		escalesOffre = new ArrayList<Escale>(offreHome.getInstance().getEscales());
		escalesLieuHome = new ArrayList<LieuHome>(escalesOffre.size());
		for (Escale escale : escalesOffre) {
			addNewLieuHome(escale);
		}
		escalesASupprimer = new ArrayList<Escale>();
	}

	private void addNewLieuHome(Escale escale) {
		LieuHome currentLieuHome = new LieuHome();
		currentLieuHome.setSession(hibernateSession);
		currentLieuHome.setPays(pays);
		currentLieuHome.setPaysMap(paysMap);
		currentLieuHome.setLocale(locale);
		currentLieuHome.wire(true, escale.getLieu());
		escalesLieuHome.add(currentLieuHome);
	}
	
	public void ajouterEscale() {
		Escale escale = new Escale();

		// Initialiser les pays à celui de l'utilisateur
		Pays paysUtilisateur = paysMap.get(utilisateurCourant.getCoordonnees().getLieu().getPays().getIdPays());
		escale.getLieu().setPays(paysUtilisateur);
		// Initialiser l'offre
		escale.setOffre(offreHome.getInstance());
		escalesOffre.add(escale);
		addNewLieuHome(escale);
	}

	public void supprimerEscale(int index) {
		escalesASupprimer.add(escalesOffre.remove(index));
		escalesLieuHome.remove(index);
	}

	public SortedSet<Escale> updateEscales() {
		log.info("Updating {0} esclaes", escalesOffre.size());
		// parcourir les escales et mettre a jour leur lieu
		for (int i = 0; i < escalesOffre.size(); i++) {
			Escale escale = escalesOffre.get(i);
			LieuHome lieuHome = escalesLieuHome.get(i);
			escale.setLieu(lieuHome.getLieu());
		}
		return new TreeSet<Escale>(escalesOffre);
	}

	// public void validerDate(ActionEvent event) {
	// }

	// public Boolean validerDate(int index) {
	// Escale escale = escalesOffre.get(index);
	// Date dateEscale = escale.getDate();
	// Date debut = offreHome.getInstance().getDateDebut();
	// Date fin = offreHome.getInstance().getDateFin();
	//
	// if (dateEscale == null) {
	// return false;
	// }
	//
	// if (fin != null && dateEscale.compareTo(fin) > 0) {
	// facesMessages.addToControlFromResourceBundle("date",
	// "validator.dateAvant");
	// return false;
	// }
	//
	// if (debut != null && dateEscale.compareTo(debut) < 0) {
	// facesMessages.addToControlFromResourceBundle("date",
	// "validator.dateAvant");
	// return false;
	// }
	//	
	// return true;
	// }

}
