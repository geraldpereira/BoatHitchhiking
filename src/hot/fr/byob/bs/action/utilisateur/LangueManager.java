package fr.byob.bs.action.utilisateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.LieuHome;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Langue;
import fr.byob.bs.model.Pays;
import fr.byob.bs.model.utilisateur.LangueUtilisateur;

/**
 * Manager pour les langues utilisateur.
 * 
 * Un utilisateur peut avoir de 0 à n langue. Pour chacune il à un niveau entre 1 et 5.
 * 
 * @author GPEREIRA
 *
 */
@Name("langueManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class LangueManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private Session hibernateSession;
	
	@In(create = true)
	private HashMap<Pays, Langue> paysLanguesMap;

	@In(required = true)
	UtilisateurEdit utilisateurEdit;
	@In(create = true)
	LieuHome lieuHome;
	
	/**
	 * Chaine utiliser pour l'affichage du résultat de la validation des langues
	 * (le facesMessage.add() ne fonctionne pas ici ! :@
	 */
	private String validerLangueResultat;
	
	/**
	 * Langues à supprimer
	 */
	private List<LangueUtilisateur> languesASupprimer;

	/**
	 * Langues utilisateur (référence vers utilisateurHome.instance.langues) 
	 */
	private List<LangueUtilisateur> languesUtilisateur;

	/**
	 * Supprime de la base les langues qui sont supprimées par l'utilisateur
	 */
	public void updateLangues() {
		log.info("Updating {0} languages ({1} to remove)", languesUtilisateur.size(), languesASupprimer.size());
		
		for (LangueUtilisateur langue : languesASupprimer) {
			hibernateSession.delete(langue);
		}
		utilisateurEdit.getInstance().setLangues(languesUtilisateur);
	}

	/**
	 * Connecte ce manager avec UtilisateurHome : on initialise notre liste de langue a celle de l'utilisateur de UtilisateurHome
	 */
	public void wire() {
		languesUtilisateur = new ArrayList<LangueUtilisateur>(utilisateurEdit.getInstance().getLangues());
		languesASupprimer = new ArrayList<LangueUtilisateur>();
	}

	/**
	 * Créé et ajoute une langue à la liste
	 */
	public void ajouterLangue() {
		LangueUtilisateur langue = new LangueUtilisateur();
		langue.setUtilisateur(utilisateurEdit.getInstance());
		langue.setNiveau(5);		
		langue.setLangue(paysLanguesMap.get(lieuHome.getInstance().getPays()));
		languesUtilisateur.add(langue);
		validerLangues();
	}

	/**
	 * Supprime une langue de la liste 
	 * Les langues supprimées sont conservée dans la liste languesASupprimer pour une eventuelle suppression en base de donnée
	 * @param index position dans la liste de la langue à supprimer 
	 */
	public void supprimerLangue(int index) {
		languesASupprimer.add(languesUtilisateur.remove(index));
		validerLangues();
	}

	/**
	 * Valide la liste de langue.
	 * Chaque langue ne doit être présente qu'une seule fois.
	 * @return true si la liste est valide
	 */
	public boolean validerLangues() {
		if (languesUtilisateur == null || languesUtilisateur.isEmpty()) {
			return true;
		}
		Set<Langue> setLangues = new HashSet<Langue>(languesUtilisateur.size());
		// Parcours des langues utilisateur
		for (LangueUtilisateur curLangueUtilisateur : languesUtilisateur) {
			if (!setLangues.add(curLangueUtilisateur.getLangue())) {
				validerLangueResultat = "Erreur de langues !";
				System.out.println("LangueManager.validerLangues()");
				return false;
			}
		}
		validerLangueResultat = null;
		return true;
	}
	
	/* ********************* Accesseurs ********************* */

	public String getValiderLangueResultat() {
		return validerLangueResultat;
	}

	public void setValiderLangueResultat(String validerLangueResultat) {
		this.validerLangueResultat = validerLangueResultat;
	}
	
	public List<LangueUtilisateur> getLanguesUtilisateur() {
		return languesUtilisateur;
	}

	public void setLanguesUtilisateur(List<LangueUtilisateur> languesUtilisateur) {
		this.languesUtilisateur = languesUtilisateur;
	}

}
