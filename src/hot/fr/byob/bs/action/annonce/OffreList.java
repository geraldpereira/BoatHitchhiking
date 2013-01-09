package fr.byob.bs.action.annonce;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Offre;

@Name("offreList")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class OffreList extends AnnonceList<Offre> {

	private static final long serialVersionUID = 1L;

	// Bateau
	private final static String NOM_BATEAU = "nomBateau";
	private final static String AVEC_PHOTO_B = "avecPhotoB";
	private final static String MARQUE = "marque";
	private final static String TYPE_COQUE = "typeCoque";
	private final static String GREEMENT = "greement";
	
	private static final String EJBQL = "select offre from Offre offre " 
	// + "join fetch offre.contribFin.monnaie "
		+ "join fetch offre.lieuDepart depart " + "join fetch depart.pays "
		+ "join fetch offre.lieuArrivee arrivee " + "join fetch arrivee.pays ";
	
	private static final String[] RESTRICTIONS = {

	"offre.valide <> #{false}",

	"lower(offre.bateau.nom) like concat(lower(#{offreList.filters." + NOM_BATEAU + "}),'%')",

	"lower(offre.utilisateur) like concat(lower(#{offreList.filters." + UTILISATEUR_COURANT + "}),'%')",
			"lower(offre.utilisateur) like concat(lower(#{offreList.filters." + PSEUDONYME + "}),'%')", "lower(offre.titre) like concat(lower(#{offreList.filters." + TITRE + "}),'%')",

			"offre.dateDebut >= #{offreList.filters." + DATE_DEBUT_FROM + "}", "offre.dateDebut <= #{offreList.filters." + DATE_DEBUT_TO + "}",
			"offre.lieuDepart.pays = #{offreList.filters." + PAYS_DEPART + "}", "lower(offre.lieuDepart.ville) like concat('%',concat(lower(#{offreList.filters." + VILLE_DEPART + "}),'%'))",

			"offre.dateFin >= #{offreList.filters." + DATE_FIN_FROM + "}", "offre.dateFin <= #{offreList.filters." + DATE_FIN_TO + "}",
			"offre.lieuArrivee.pays = #{offreList.filters." + PAYS_ARRIVEE + "}", "lower(offre.lieuArrivee.ville) like concat('%',concat(lower(#{offreList.filters." + VILLE_ARRIVEE + "}),'%'))",

			"offre.contribFin.typePaiement = #{offreList.filters." + TYPE_PAIEMENT + "}", "offre.contribFin.montant <= cast(#{offreList.filters." + MONTANT_MAX + "},int)",
			
			"offre.utilisateur.photoUtilisateur is not null and true = #{offreList.filters." + AVEC_PHOTO + "}", "offre.utilisateur.sexe = #{offreList.filters." + SEXE + "}",
			"offre.utilisateur.dateNaissance >= #{offreList.dateNaissanceFrom}", "offre.utilisateur.dateNaissance <= #{offreList.dateNaissanceTo}",
			"offre.utilisateur.xpGenerale in (#{offreList.filters." + XP_GENERALE + "})",
			
			"offre.bateau.photosBateau is not empty and true = #{offreList.filters." + AVEC_PHOTO_B + "}", "lower(offre.bateau.marque) like concat(lower(#{offreList.filters." + MARQUE + "}),'%')",
			"offre.bateau.typeCoque = #{offreList.filters." + TYPE_COQUE + "}", "offre.bateau.greement = #{offreList.filters." + GREEMENT + "}"
	};

	public OffreList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		reinitFilters();
	}

	@In(create = true)
	private OffreEdit offreRemove;

	public void remove() {
		Offre offre = super.getResultList().get(super.getCurrentRow());
		offreRemove.setOffreNumAnnonce(offre.getNumAnnonce());
		offreRemove.setInstance(offre);
		offreRemove.remove();
	}

	public List<Offre> getAllOffres() {
		return getSession().getNamedQuery("offre.findAll").list();
	}

	public List<Offre> getAllOffres(final int maxNB) {
		return getSession().getNamedQuery("offre.findAll").setMaxResults(maxNB).list();
	}

	@Override
	protected void reinitFilters() {
		super.reinitFilters();
		setOrderColumn("offre.note");
		setOrderDirection("desc");
	}

	@Observer("offreAdded")
	public void addOffre(Offre offre) {
		info("addOffre " + offre);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(offre.getUtilisateur().getPseudonyme())) {
			super.addToList(offre);
		}
	}

	@Observer("offreUpdated")
	public void updateOffre(Offre offre) {
		info("updateOffre " + offre);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(offre.getUtilisateur().getPseudonyme())) {
			int index = super.getResultList().indexOf(offre);// Rely on equals
			if (index != -1) {
				super.getResultList().set(index, offre);
			}
		}
	}

	@Observer("offreRemoved")
	public void removeOffre(Offre offre) {
		info("removeOffre " + offre);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(offre.getUtilisateur().getPseudonyme())) {
			super.removeFromList(offre);
		}
	}
}
