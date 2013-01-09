package fr.byob.bs.action.experience;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.HibernateEntityHome;
import org.jboss.seam.log.Log;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.experience.Xp;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("xpManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class XpManager implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;
	
	@In
	private Session hibernateSession;

	private HibernateEntityHome<Utilisateur> utilisateurHome;

	private List<Xp> xpsASupprimer;

	private List<Xp> xpsUtilisateur;

	public void wire(HibernateEntityHome<Utilisateur> utilisateurHome) {
		this.utilisateurHome = utilisateurHome;
		xpsUtilisateur = new ArrayList<Xp>(utilisateurHome.getInstance().getXps());
		xpsASupprimer = new ArrayList<Xp>();
	}

	public void ajouterExperience() {
		Xp xp = new Xp();
		xp.setUtilisateur(utilisateurHome.getInstance());
		xpsUtilisateur.add(xp);
	}

	public void supprimerExperience(int index) {
		xpsASupprimer.add(xpsUtilisateur.remove(index));
	}
	
	public List<Xp> getXpsUtilisateur() {
		return xpsUtilisateur;
	}

	public void setXpsUtilisateur(List<Xp> xpsUtilisateur) {
		this.xpsUtilisateur = xpsUtilisateur;
	}

	public void updateXps() {
		log.info("Updating {0} xps ({1} to remove)", xpsUtilisateur.size(), xpsASupprimer.size());

		for (Xp xp : xpsASupprimer) {
			hibernateSession.delete(xp);
		}
		utilisateurHome.getInstance().setXps(xpsUtilisateur);
	}
}
