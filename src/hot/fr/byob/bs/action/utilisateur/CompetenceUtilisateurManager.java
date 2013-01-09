package fr.byob.bs.action.utilisateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.HibernateEntityHome;
import org.jboss.seam.log.Log;

import fr.byob.bs.BSException;
import fr.byob.bs.action.competence.ArboCompUtilisateur;
import fr.byob.bs.action.competence.ArboTypeCompetence;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.competence.Competence;
import fr.byob.bs.model.competence.CompetenceUtilisateur;
import fr.byob.bs.model.competence.CompetenceUtilisateurId;
import fr.byob.bs.model.competence.TypeCompetence;
import fr.byob.bs.model.utilisateur.Utilisateur;

/**
 * Gére la liste de compétences utilisateur.
 * Un utilisateur peut avoir un niveau dans chaque competence enregistrée en base. Les compétences sont classées par type, dans une arborescence. 
 *  
 * @author GPEREIRA
 *
 */

@Name("competenceUtilisateurManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class CompetenceUtilisateurManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Logger
	private transient Log log;
	
	@In
	private Session hibernateSession;

	private HibernateEntityHome<Utilisateur> utilisateurHome;

	/**
	 * Valeur récupérée depuis la Factory competences.TypeCompetenceFactory
	 */
	@In(create = true)
	public List<TypeCompetence> typesCompetences; 

	/**
	 * Liste des compétences utilisateur
	 * On utilise des ArboTypeCompetence pour l'affichage. Ces objets sont plus pratiques que les beans CompetenceUtilisateur pour l'affichage.
	 *  
	 */
	List<ArboTypeCompetence> typesCompetencesUtilisateur;


	/**
	 * Enregistre les ajouts, modifications et suppressions de competences utilisateur.
	 * Une competence de niveau 0 est supprimée. 
	 * 
	 * La suppression de l'ensemble des competences d'un utilisateur (quand ce dernier est supprimé) est gérée directement par hibernate
	 */
	@Transactional
	public void updateCompetences() {
		final Utilisateur utilisateur = utilisateurHome.getInstance();
		final Map<CompetenceUtilisateurId, CompetenceUtilisateur> competencesUtilisateur = utilisateur
				.getCompetences();
		final String pseudo = utilisateur.getPseudonyme();

		try {
			// Parcours de l'arborescence de compétences
			for (ArboTypeCompetence arboTypeCompetence : typesCompetencesUtilisateur) {
				for (ArboCompUtilisateur arboCompetence : arboTypeCompetence
						.getCompetences()) {

					final CompetenceUtilisateurId compId = new CompetenceUtilisateurId(
							arboCompetence.getCompetence().getIdCompetence(),
							pseudo);
					final CompetenceUtilisateur comp = competencesUtilisateur
							.get(compId);
					
					if (0 == arboCompetence.getNiveau()) { // Competence a supprimer
						if (comp == null) {
							// L'utilisateur n'avait pas cette compétence, et ne l'a toujours, on passe à la suivante
							continue;
						} else {
							// La compétence était enregistrée en base, comme
							// elle est maintenant a 0, on la supprime
							// session.
							competencesUtilisateur.remove(compId);
							hibernateSession.delete(comp);
						}
					} else { // Competence a mettre à jour
						if (comp == null) {
							// L'utilisateur n'avait pas cette competence, on l'ajoute dans sa liste
							competencesUtilisateur.put(compId,
									new CompetenceUtilisateur(compId,
											arboCompetence.getCompetence(),
											utilisateur, arboCompetence
													.getNiveau()));
						} else {
							comp.setNiveau(arboCompetence.getNiveau());
						}
					}
				}
			}
			// hibernateSession.flush();

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BSException(
					"error.competences");
		}
	}

	/**
	 * Connecte ce manager avec le utilisateurHome.
	 * 
	 * Concrètement on initialise ici notre arborescence de competences en fonction des competences de l'utilisateur.
	 */
	public void wire(HibernateEntityHome<Utilisateur> utilisateurHome) {
		this.utilisateurHome = utilisateurHome;
		typesCompetencesUtilisateur = new ArrayList<ArboTypeCompetence>();
		final Utilisateur utilisateur = utilisateurHome.getInstance();

		// Les compétences utilisateur 
		final Map<CompetenceUtilisateurId, CompetenceUtilisateur> competencesUtilisateur = utilisateur
				.getCompetences();

		// On parcours les types de competences
		for (TypeCompetence typeCompetence : typesCompetences) {

			ArboTypeCompetence arboTypeCompetence = new ArboTypeCompetence(
					typeCompetence.getIdTypeCompetence());

			// On parcours les compétences
			for (Competence competence : typeCompetence.getCompetences()) {

				ArboCompUtilisateur curArboCompetenceUtilisateur;
				final CompetenceUtilisateurId compId = new CompetenceUtilisateurId(
						competence.getIdCompetence(), utilisateur
								.getPseudonyme());

				// On initialise notre arborescence
				if (competencesUtilisateur.containsKey(compId)) {
					curArboCompetenceUtilisateur = new ArboCompUtilisateur(
							competence,
							competencesUtilisateur.get(compId).getNiveau());
				} else {
					curArboCompetenceUtilisateur = new ArboCompUtilisateur(
							competence, 0);
				}

				arboTypeCompetence.addCompetence(curArboCompetenceUtilisateur);

			}
			typesCompetencesUtilisateur.add(arboTypeCompetence);
		}
	}

	/* ********************* Accesseurs ********************* */

	public List<ArboTypeCompetence> getTypesCompetencesUtilisateur() {
		return typesCompetencesUtilisateur;
	}
}
