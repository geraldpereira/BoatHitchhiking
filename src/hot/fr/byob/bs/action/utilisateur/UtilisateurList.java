package fr.byob.bs.action.utilisateur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import fr.byob.bs.BSEntityList;
import fr.byob.bs.BSUtils;
import fr.byob.bs.action.LieuSelectionManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Pays;
import fr.byob.bs.model.experience.XpGenerale;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("utilisateurList")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class UtilisateurList extends BSEntityList<Utilisateur> {

	private static final long serialVersionUID = 1L;
	
	private final static String PSEUDONYME = "pseudonyme";
	private final static String NOM = "nom";
	private final static String PRENOM = "prenom";
	private final static String INSCRIT_FROM = "inscritFrom";
	private final static String INSCRIT_TO = "inscritTo";
	private final static String ACTIF = "actif";
	private final static String AVEC_PHOTO = "avecPhoto";
	private final static String AVEC_XP = "avecXP";
	private final static String AVEC_COMPETENCE = "avecCompetence";
	private final static String SEXE = "sexe";
	private final static String AGE_FROM = "ageFrom";
	private final static String AGE_TO = "ageTo";
	private final static String XP_GENERALE = "xpGenerale";
	private final static String PAYS = "pays";
	private final static String VILLE = "ville";

	// @In(create = true)
	// private String dummyUser;
	
	private static final String EJBQL = "select utilisateur from Utilisateur utilisateur join fetch utilisateur.coordonnees.lieu residence join fetch residence.pays";

	private static final String[] RESTRICTIONS = {
			"lower(utilisateur.pseudonyme) like concat(lower(#{utilisateurList.filters." + PSEUDONYME + "}),'%')",
			"lower(utilisateur.coordonnees.prenom) like concat(lower(#{utilisateurList.filters." + NOM + "}),'%')",
			"lower(utilisateur.coordonnees.nom) like concat(lower(#{utilisateurList.filters." + PRENOM + "}),'%')", 
			"utilisateur.actif is null and true = #{utilisateurList.filters." + ACTIF + "}",
			"utilisateur.photoUtilisateur is not null and true = #{utilisateurList.filters." + AVEC_PHOTO + "}",
			
			"utilisateur.dateInscription >= #{utilisateurList.filters." + INSCRIT_FROM + "}",
			"utilisateur.dateInscription <= #{utilisateurList.filters." + INSCRIT_TO + "}", 
			"utilisateur.sexe = #{utilisateurList.filters." + SEXE + "}", 
			"utilisateur.dateNaissance >= #{utilisateurList.dateNaissanceFrom}", 
			"utilisateur.dateNaissance <= #{utilisateurList.dateNaissanceTo}",			
			"utilisateur.xpGenerale in (#{utilisateurList.filters." + XP_GENERALE + "})",
			"utilisateur.xps is not empty and true = #{utilisateurList.filters." + AVEC_XP + "}",
			"utilisateur.competences is not empty and true = #{utilisateurList.filters." + AVEC_COMPETENCE + "}",
			
			 "utilisateur.coordonnees.lieu.pays = #{utilisateurList.filters." + PAYS + "}",
			"lower(utilisateur.coordonnees.lieu.ville) like concat('%',concat(lower(#{utilisateurList.filters." + VILLE + "}),'%'))",
		
			"utilisateur.pseudonyme <> #{dummyUser}",
	};

	public Date getDateNaissanceFrom() {
		return BSUtils.getAgeAsDate((String) getFilters().get(AGE_TO));
	}

	public Date getDateNaissanceTo() {
		return BSUtils.getAgeAsDate((String) getFilters().get(AGE_FROM));
	}
	
	private final LieuSelectionManager lieuSelectionManager = new LieuSelectionManager(
			getSession(), false) {
		
		@Override
		public Pays getPays() {
			return (Pays) getFilters().get(PAYS);
		}

		@Override
		public String getVille() {
			return (String) getFilters().get(VILLE);
		}

		@Override
		public void setPays(Pays pays) {
			getFilters().put(PAYS, pays);
		}

		@Override
		public void setVille(String ville) {
			getFilters().put(VILLE, ville);
		}
	};
	
	public UtilisateurList() {
		setEjbql(EJBQL); 
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		reinitFilters();
	}

	@Override
	public void reinitFilters() {
		lieuSelectionManager.reset();
		super.getFilters().put(XP_GENERALE, new ArrayList<XpGenerale>());
		setOrderColumn("utilisateur.note");
		setOrderDirection("desc");
	}
	
	
	/*  Accesseurs */
	
	public LieuSelectionManager getLieuSelectionManager() {
		return this.lieuSelectionManager;
	}
	
	public List<Utilisateur> getAllUtilisateurs() {
		return getSession().getNamedQuery("user.findAll").list();
	}
}
