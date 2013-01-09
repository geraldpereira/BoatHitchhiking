package fr.byob.bs.action.annonce;

import java.util.ArrayList;
import java.util.Date;

import fr.byob.bs.BSEntityList;
import fr.byob.bs.BSUtils;
import fr.byob.bs.action.LieuSelectionManager;
import fr.byob.bs.model.Pays;
import fr.byob.bs.model.annonce.Annonce;
import fr.byob.bs.model.experience.XpGenerale;

public abstract class AnnonceList<T extends Annonce> extends BSEntityList<T> {

	private static final long serialVersionUID = 1L;
	
	protected final static String UTILISATEUR_COURANT = "utilisateurCourant";

	protected final static String PSEUDONYME = "pseudonyme";
	protected final static String TITRE = "titre";

	protected final static String DATE_DEBUT_FROM = "dateDebutFrom";
	protected final static String DATE_DEBUT_TO = "dateDebutTo";
	protected final static String PAYS_DEPART = "paysDepart";
	protected final static String VILLE_DEPART = "villeDepart";

	protected final static String DATE_FIN_FROM = "dateFinFrom";
	protected final static String DATE_FIN_TO = "dateFinTo";
	protected final static String PAYS_ARRIVEE = "paysArrivee";
	protected final static String VILLE_ARRIVEE = "villeArrivee";

	protected final static String TYPE_PAIEMENT = "typePaiement";
	protected final static String MONTANT_MAX = "montantMax";

	// critères utilisateur
	protected final static String AVEC_PHOTO = "avecPhoto";
	protected final static String SEXE = "sexe";
	protected final static String AGE_FROM = "ageFrom";
	protected final static String AGE_TO = "ageTo";
	protected final static String XP_GENERALE = "xpGenerale";

	protected final LieuSelectionManager lieuDepartSM = new LieuSelectionManager(getSession(), false) {

		@Override
		public Pays getPays() {
			return (Pays) getFilters().get(PAYS_DEPART);
		}

		@Override
		public String getVille() {
			return (String) getFilters().get(VILLE_DEPART);
		}

		@Override
		public void setPays(Pays pays) {
			getFilters().put(PAYS_DEPART, pays);
		}

		@Override
		public void setVille(String ville) {
			getFilters().put(VILLE_DEPART, ville);
		}
	};

	protected final LieuSelectionManager lieuArriveeSM = new LieuSelectionManager(getSession(), false) {

		@Override
		public Pays getPays() {
			return (Pays) getFilters().get(PAYS_ARRIVEE);
		}

		@Override
		public String getVille() {
			return (String) getFilters().get(VILLE_ARRIVEE);
		}

		@Override
		public void setPays(Pays pays) {
			getFilters().put(PAYS_ARRIVEE, pays);
		}

		@Override
		public void setVille(String ville) {
			getFilters().put(VILLE_ARRIVEE, ville);
		}
	};

	public LieuSelectionManager getLieuDepartSM() {
		return this.lieuDepartSM;
	}

	public LieuSelectionManager getLieuArriveeSM() {
		return this.lieuArriveeSM;
	}

	public Date getDateNaissanceFrom() {
		return BSUtils.getAgeAsDate((String) getFilters().get(AGE_TO));
	}

	public Date getDateNaissanceTo() {
		return BSUtils.getAgeAsDate((String) getFilters().get(AGE_FROM));
	}

	@Override
	protected void reinitFilters() {
		lieuArriveeSM.reset();
		lieuDepartSM.reset();

		super.getFilters().put(XP_GENERALE, new ArrayList<XpGenerale>());
	}

	public void setInstanceUtilisateurConnecte() {
		if (utilisateurCourant != null) {
			super.getFilters().put(UTILISATEUR_COURANT, utilisateurCourant.getPseudonyme());
			// super.getFilters().remove(DATE_DEBUT_FROM);
			super.doFilterResultList();
		}
	}
}
