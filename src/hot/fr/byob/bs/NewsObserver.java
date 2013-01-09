package fr.byob.bs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.annonce.DemandeList;
import fr.byob.bs.action.annonce.OffreList;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Annonce;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;

@Startup
@Name("newsObserver")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class NewsObserver {

	protected final static ResourceBundle messages = SeamResourceBundle.getBundle();

	public final static int NB = 5;

	@Logger
	private transient Log log;

	private final AnnonceCompare comparator = new AnnonceCompare();

	@Out(scope = ScopeType.APPLICATION, required = false)
	private List<Annonce> annonces;

	private int minNoteDemande = 0;

	private int minNoteOffre = 0;

	@In(create = true)
	private OffreList offreList;

	@In(create = true)
	private DemandeList demandeList;

	@Out(scope = ScopeType.APPLICATION, required = false)
	List<Offre> offres;

	@Out(scope = ScopeType.APPLICATION, required = false)
	List<Demande> demandes;

	@Factory(autoCreate = true, value = "offres")
	public synchronized void getOffres() {
		offres = offreList.getAllOffres(NB);
		minNoteOffre = getMinNote(offres);
	}

	@Factory(autoCreate = true, value = "demandes")
	public synchronized void getDemandes() {
		demandes = demandeList.getAllDemandes(NB);
		minNoteDemande = getMinNote(demandes);
	}

	private synchronized int getMinNote(List<? extends Annonce> liste) {
		int minNote = 0;
		if (liste != null && !liste.isEmpty()) {
			minNote = liste.get(liste.size() - 1).getNote();
		}
		return minNote;
	}
	
	private synchronized void removeFromList(List<? extends Annonce> liste, Annonce annonce) {
		int indexToRemove = -1;
		for (int i = 0; i < liste.size(); i++) {
			Annonce curAnnonce = liste.get(i);
			if (curAnnonce.getNumAnnonce().equals(annonce.getNumAnnonce())) {
				indexToRemove = i;
			}
		}
		if (indexToRemove != -1) {
			liste.remove(indexToRemove);
			annonces = null;
		}
	}

	private synchronized <T extends Annonce> void addToList(List<T> liste, T annonce) {
		annonce.getLieuArrivee().getPays();
		annonce.getLieuDepart().getPays();

		liste.add(0, annonce);
		Collections.sort(liste, comparator);
		if (liste.size() > NB) {
			if (liste.size() - NB == 1) {
				// Only one to remove
				T t = liste.remove(NB);
				if (!t.equals(annonce)) {
					annonces = null;
				}
			} else {
				liste = new ArrayList<T>(liste.subList(0, NB));
			}
		}
	}

	private synchronized <T extends Annonce> boolean updateInList(List<T> liste, T annonce) {
		int indexToUpdate = -1;
		for (int i = 0; i < liste.size(); i++) {
			T curAnnonce = liste.get(i);
			if (curAnnonce.getNumAnnonce().equals(annonce.getNumAnnonce())) {
				indexToUpdate = i;
			}
		}
		if (indexToUpdate != -1) {
			liste.remove(indexToUpdate);
			liste.add(indexToUpdate, annonce);
			annonces = null;
			return true;
		}
		return false;
	}

	@Factory(autoCreate = true, value = "annonces")
	public synchronized void getAnnonces() {
		if (offres == null) {
			getOffres();
		}
		if (demandes == null) {
			getDemandes();
		}

		if (annonces == null) {
			annonces = new ArrayList<Annonce>();
		}
		annonces.addAll(offres);
		annonces.addAll(demandes);
		Collections.sort(annonces, comparator);
	}

	@Observer("offreAdded")
	public synchronized void addOffre(Offre offre) {
		log.info("NewsObserver : offreAdded");

		if (offre.getNote() < minNoteOffre && offres.size() == NB) {
			return;
		}

		if (offres == null) {
			getOffres();
		}

		offre.getUtilisateur().getLangues();

		// Ajoute l'offre
		addToList(offres, offre);

		minNoteOffre = getMinNote(offres);
	}

	@Observer("demandeAdded")
	public synchronized void addDemande(Demande demande) {
		log.info("NewsObserver : demandeAdded");

		if (demande.getNote() < minNoteDemande && demandes.size() == NB) {
			return;
		}

		if (demandes == null) {
			getDemandes();
		}

		demande.getUtilisateur().getLangues();

		// Ajoute la demande
		addToList(demandes, demande);

		minNoteDemande = getMinNote(demandes);
	}

	@Observer("offreRemoved")
	public synchronized void removeOffre(Offre offre) {
		log.info("NewsObserver : offreRemoved");

		if (offres == null) {
			return;
		}

		removeFromList(offres, offre);

		minNoteOffre = getMinNote(offres);
	}

	@Observer("demandeRemoved")
	public synchronized void removeDemande(Demande demande) {
		log.info("NewsObserver : demandeREmoved");

		if (demandes == null) {
			return;
		}

		removeFromList(demandes, demande);

		minNoteDemande = getMinNote(demandes);
	}

	@Observer("offreUpdated")
	public synchronized void updateOffre(Offre offre) {
		log.info("NewsObserver : offreUpdate");

		if (offres == null) {
			getOffres();
		}

		if (!updateInList(offres, offre)) {
			if (offre.getNote() >= minNoteOffre) {
				addToList(offres, offre);
			}
		}

		minNoteOffre = getMinNote(offres);
	}


	@Observer("demandeUpdated")
	public synchronized void updateDemande(Demande demande) {
		log.info("NewsObserver : demandeUpdated");

		if (demandes == null) {
			getDemandes();
		}

		if (!updateInList(demandes, demande)) {
			if (demande.getNote() >= minNoteDemande) {
				addToList(demandes, demande);
			}
		}

		minNoteDemande = getMinNote(demandes);
	}

	public synchronized void reset() {
		if (offres != null) {
			offres.clear();
		}
		offres = null;
		if (demandes != null) {
			demandes.clear();
		}
		demandes = null;
		if (annonces != null) {
			annonces.clear();
		}
		annonces = null;
		getAnnonces();
	}


	private static class AnnonceCompare implements Comparator<Annonce> {

		@Override
		public int compare(Annonce a1, Annonce a2) {
			if (a2 == null && a1 == null) {
				return 0;
			}
			if (a2 == null) {
				return -1;
			}
			if (a1 == null) {
				return -1;
			}
			if (a1.getNote() < a2.getNote()) {
				return 1;
			}
			if (a1.getNote() > a2.getNote()) {
				return -1;
			}
			return 0;
		}

	}

}
