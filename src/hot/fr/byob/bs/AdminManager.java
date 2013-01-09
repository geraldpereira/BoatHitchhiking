package fr.byob.bs;

import static fr.byob.bs.Constantes.UPDATED;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import fr.byob.bs.action.annonce.DemandeEdit;
import fr.byob.bs.action.annonce.DemandeList;
import fr.byob.bs.action.annonce.OffreEdit;
import fr.byob.bs.action.annonce.OffreList;
import fr.byob.bs.action.bateau.BateauEdit;
import fr.byob.bs.action.bateau.BateauList;
import fr.byob.bs.action.utilisateur.UtilisateurEdit;
import fr.byob.bs.action.utilisateur.UtilisateurList;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("adminManager")
@Scope(ScopeType.CONVERSATION)
public class AdminManager {
	
	@In
	private Session hibernateSession;
	
	@In(create = true)
	protected UtilisateurList utilisateurList;
	
	@In(create = true)
	private UtilisateurEdit utilisateurEdit;

	@In(create = true)
	private OffreList offreList;

	@In(create = true)
	private DemandeList demandeList;
	
	@In(create = true)
	private OffreEdit offreEdit;

	@In(create = true)
	private DemandeEdit demandeEdit;
	
	@In(create = true)
	private BateauList bateauList;
	
	@In(create = true)
	private BateauEdit bateauEdit;

	public String updateAllUtilisateursNotes() {
		for (Utilisateur utilisateur : utilisateurList.getAllUtilisateurs()) {
			utilisateurEdit.updateNote(utilisateur);
		}
		return UPDATED;
	}
	public String updateAllDemandesNotes() {
		for (Demande demande : demandeList.getAllDemandes()) {
			demandeEdit.updateNote(demande);
		}
		return UPDATED;
	}
	public String updateAllOffresNotes() {
		for (Offre offre : offreList.getAllOffres()) {
			offreEdit.updateNote(offre);
		}
		return UPDATED;
	}
	
	public String updateAllBateauxNotes() {
		for (Bateau bateau : bateauList.getAllBateaux()) {
			bateauEdit.updateNote(bateau);
		}
		return UPDATED;
	}
	
	public void testException() {
		throw new BSException("error.bateau.create");
	}
}
