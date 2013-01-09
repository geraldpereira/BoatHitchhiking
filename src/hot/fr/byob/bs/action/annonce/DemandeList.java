package fr.byob.bs.action.annonce;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Demande;

@Name("demandeList")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class DemandeList extends AnnonceList<Demande> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select demande from Demande demande " 
	// + "join fetch demande.contribFin.monnaie "
		+ "left join fetch demande.lieuDepart depart "
			+ "left join fetch depart.pays " + "left join fetch demande.lieuArrivee arrivee " + "left join fetch arrivee.pays ";
	
	private static final String[] RESTRICTIONS = {

	"demande.valide <> #{false}",

	"lower(demande.utilisateur) like concat(lower(#{demandeList.filters." + UTILISATEUR_COURANT + "}),'%')",
			"lower(demande.utilisateur) like concat(lower(#{demandeList.filters." + PSEUDONYME + "}),'%')", "lower(demande.titre) like concat(lower(#{demandeList.filters." + TITRE + "}),'%')",

			"demande.dateDebut >= #{demandeList.filters." + DATE_DEBUT_FROM + "}", "demande.dateDebut <= #{demandeList.filters." + DATE_DEBUT_TO + "}",
			"demande.lieuDepart.pays = #{demandeList.filters." + PAYS_DEPART + "}", "lower(demande.lieuDepart.ville) like concat('%',concat(lower(#{demandeList.filters." + VILLE_DEPART + "}),'%'))",

			"demande.dateFin >= #{demandeList.filters." + DATE_FIN_FROM + "}", "demande.dateFin <= #{demandeList.filters." + DATE_FIN_TO + "}",
			"demande.lieuArrivee.pays = #{demandeList.filters." + PAYS_ARRIVEE + "}",
			"lower(demande.lieuArrivee.ville) like concat('%',concat(lower(#{demandeList.filters." + VILLE_ARRIVEE + "}),'%'))",

			"demande.contribFin.typePaiement = #{demandeList.filters." + TYPE_PAIEMENT + "}", "demande.contribFin.montant <= cast(#{demandeList.filters." + MONTANT_MAX + "},int)",
			
			"demande.utilisateur.photoUtilisateur is not null and true = #{demandeList.filters." + AVEC_PHOTO + "}", "demande.utilisateur.sexe = #{demandeList.filters." + SEXE + "}",
			"demande.utilisateur.dateNaissance >= #{demandeList.dateNaissanceFrom}", "demande.utilisateur.dateNaissance <= #{demandeList.dateNaissanceTo}",
			"demande.utilisateur.xpGenerale in (#{demandeList.filters." + XP_GENERALE + "})",
	};

	public DemandeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		reinitFilters();

	}

	@In(create = true)
	private DemandeEdit demandeRemove;

	public void remove() {
		Demande demande = super.getResultList().get(super.getCurrentRow());
		demandeRemove.setDemandeNumAnnonce(demande.getNumAnnonce());
		demandeRemove.setInstance(demande);
		demandeRemove.remove();
	}

	public List<Demande> getAllDemandes() {
		return getSession().getNamedQuery("demande.findAll").list();
	}

	public List<Demande> getAllDemandes(final int maxNB) {
		return getSession().getNamedQuery("demande.findAll").setMaxResults(maxNB).list();
	}

	@Override
	protected void reinitFilters() {
		super.reinitFilters();
		setOrderColumn("demande.note");
		setOrderDirection("desc");
	}

	@Observer("demandeAdded")
	public void addDemande(Demande demande) {
		info("addDemande " + demande);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(demande.getUtilisateur().getPseudonyme())) {
			super.addToList(demande);
		}
	}

	@Observer("demandeUpdated")
	public void updateDemande(Demande demande) {
		info("updateDemande " + demande);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(demande.getUtilisateur().getPseudonyme())) {
			int index = super.getResultList().indexOf(demande);// Rely on equals
			if (index != -1) {
				super.getResultList().set(index, demande);
			}
		}
	}

	@Observer("demandeRemoved")
	public void removeDemande(Demande demande) {
		info("removeDemande " + demande);
		String pseudo = (String) getFilters().get(UTILISATEUR_COURANT);
		if (pseudo != null && pseudo.equals(demande.getUtilisateur().getPseudonyme())) {
			super.removeFromList(demande);
		}
	}
}
