package fr.byob.bs.action.bateau;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import fr.byob.bs.BSEntityList;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.bateau.Bateau;

@Name("bateauList")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class BateauList extends BSEntityList<Bateau> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select bateau from Bateau bateau";

	private final static String UTILISATEUR_COURANT = "utilisateurCourant";

	private final static String PSEUDONYME = "pseudonyme";
	private final static String NOM = "nom";
	private final static String AVEC_PHOTO = "avecPhoto";

	private final static String MARQUE = "marque";
	private final static String ANNEE_FROM = "anneeFrom";
	private final static String ANNEE_TO = "anneeTo";
	private final static String TAILLE_FROM = "tailleFrom";
	private final static String TAILLE_TO = "tailleTo";

	private final static String TYPE_COQUE = "typeCoque";
	private final static String GREEMENT = "greement";
	
	private final static String NB_CABINES = "nbCabine";
	private final static String NB_SDB = "nbSDB";
	private final static String NB_WC = "nbWC";
	private final static String AVEC_ANNEXE = "avecAnnexe";
	private final static String AVEC_EQUIPEMENT = "avecEquipement";
	
	private static final String[] RESTRICTIONS = {
			// objet valide
			"bateau.valide <> #{false}", 
			// Filtre dans la page de recherche
			"lower(bateau.utilisateur) like concat(lower(#{bateauList.filters." + PSEUDONYME + "}),'%')",
			// Utilisateur connecté
			"lower(bateau.utilisateur) like concat(lower(#{bateauList.filters." + UTILISATEUR_COURANT + "}),'%')", "lower(bateau.nom) like concat(lower(#{bateauList.filters." + NOM + "}),'%')",
			"bateau.photosBateau is not empty and true = #{bateauList.filters." + AVEC_PHOTO + "}",
			
			
			"lower(bateau.marque) like concat(lower(#{bateauList.filters." + MARQUE + "}),'%')", "bateau.annee >= cast(#{bateauList.filters." + ANNEE_FROM + "},int)",
			"bateau.annee <= cast(#{bateauList.filters." + ANNEE_TO + "},int)", "bateau.taille >= cast(#{bateauList.filters." + TAILLE_FROM + "},int)",
			"bateau.taille <= cast(#{bateauList.filters." + TAILLE_TO + "},int)", "bateau.typeCoque = #{bateauList.filters." + TYPE_COQUE + "}",
			"bateau.greement = #{bateauList.filters." + GREEMENT + "}", "bateau.utilisateur.pseudonyme <> #{dummyUser}",
			
			"bateau.equipGeneral.nbCabines >= cast(#{bateauList.filters." + NB_CABINES + "},int)",
			"bateau.equipGeneral.nbDouches >= cast(#{bateauList.filters." + NB_SDB + "},int)",
			"bateau.equipGeneral.nbWC >= cast(#{bateauList.filters." + NB_WC + "},int)",
			"bateau.equipGeneral.annexe = #{bateauList.filters." + AVEC_ANNEXE + "}",
			"bateau.equipGeneral.equipGeneralHasEquipements is not empty and true = #{bateauList.filters." + AVEC_EQUIPEMENT + "}",
	};

	public BateauList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		reinitFilters();
	}

	public void setInstanceUtilisateurConnecte() {
		if (utilisateurCourant != null) {
			super.getFilters().put(UTILISATEUR_COURANT, utilisateurCourant.getPseudonyme());
			super.doFilterResultList();
		}
	}

	@Override
	protected void reinitFilters() {
		setOrderColumn("bateau.note");
		setOrderDirection("desc");
	}

	@In(create = true)
	private BateauEdit bateauRemove;

	public void remove() {
		Bateau bateau = super.getResultList().get(super.getCurrentRow());
		bateauRemove.setBateauIdBateau(bateau.getIdBateau());
		bateauRemove.setInstance(bateau);
		bateauRemove.remove();
	}
	
	public List<Bateau> getAllBateaux() {
		return getSession().getNamedQuery("bateau.findAll").list();
	}

	@Observer("bateauAdded")
	public void addBateau(Bateau bateau) {
		info("addBateau " + bateau);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(bateau.getUtilisateur().getPseudonyme())) {
			super.addToList(bateau);
		}
	}

	@Observer("bateauUpdated")
	public void updateBateau(Bateau bateau) {
		info("updateBateau " + bateau);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(bateau.getUtilisateur().getPseudonyme())) {
			int index = super.getResultList().indexOf(bateau);// Rely on equals
			if (index != -1) {
				super.getResultList().set(index, bateau);
			}
		}
	}

	@Observer("bateauRemoved")
	public void removeBateau(Bateau bateau) {
		info("removeBateau " + bateau);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(bateau.getUtilisateur().getPseudonyme())) {
			super.removeFromList(bateau);
		}
	}
}
