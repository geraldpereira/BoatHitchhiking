package fr.byob.bs.action.bateau;

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
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.bateau.EquipGeneral;
import fr.byob.bs.model.bateau.EquipGeneralHasEquipement;
import fr.byob.bs.model.bateau.EquipGeneralHasEquipementId;
import fr.byob.bs.model.bateau.Equipement;
import fr.byob.bs.model.bateau.TypeEquipement;

@Name("equipementManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class EquipementManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;
	
	@In
	private Session hibernateSession;

	private HibernateEntityHome<Bateau> bateauHome;

	/**
	 * Valeur récupérée depuis la Factory bateau.TypeEquipementFactory
	 */
	@In(create = true)
	public List<TypeEquipement> typesEquipement;

	/**
	 * Liste des équipements On utilise des ArboTypeEquipement pour l'affichage.
	 * Ces objets sont plus pratiques que les beans TypeEquipement pour
	 * l'affichage.
	 * 
	 */
	private List<ArboTypeEquipement> typesEquipementArbo;

	
	/**
	 * Enregistre les ajouts, modifications et suppressions d'équipement. Un
	 * equipement de nombre 0 est supprimée.
	 * 
	 * La suppression de l'ensemble des equipements d'un equipGeneral (quand ce
	 * dernier est supprimé) est gérée directement par hibernate
	 */
	@Transactional
	@Deprecated
	public void addEquipements() {

		final EquipGeneral equipGeneral = bateauHome.getInstance()
				.getEquipGeneral();
		final Long idEquipGeneral = equipGeneral.getIdEquipGeneral();

		try {
			// Parcours de l'arborescence de compétences
			for (ArboTypeEquipement arboTypeEquipement : typesEquipementArbo) {
				for (ArboEquipement arboEquipement : arboTypeEquipement
						.getEquipements()) {

					final EquipGeneralHasEquipementId equipId = new EquipGeneralHasEquipementId(
							idEquipGeneral, arboEquipement.getEquipement()
									.getIdEquipement());

					if (arboEquipement.getNombre() != null
							&& 0 != arboEquipement.getNombre()) {
						// Le bateau n'avait pas cet equipement, on
						// l'ajoute dans sa liste
						EquipGeneralHasEquipement equipGenhasEquip = new EquipGeneralHasEquipement(
								equipId, equipGeneral, arboEquipement
										.getEquipement(), arboEquipement
										.getNombre());
						hibernateSession.persist(equipGenhasEquip);
					}
				}
			}
			hibernateSession.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BSException(
					"error.equipement");
		}
	}
	
	/**
	 * Enregistre les ajouts, modifications et suppressions d'équipement. Un
	 * equipement de nombre 0 est supprimée.
	 * 
	 * La suppression de l'ensemble des equipements d'un equipGeneral (quand ce
	 * dernier est supprimé) est gérée directement par hibernate
	 */
	@Transactional
	public void updateEquipements() {

		log.info("Updating boat equipments");
		
		final EquipGeneral equipGeneral = bateauHome.getInstance()
				.getEquipGeneral();
		final Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> equipements = equipGeneral
				.getEquipGeneralHasEquipements();
		final Long idEquipGeneral = equipGeneral.getIdEquipGeneral();

		try {
			// Parcours de l'arborescence de compétences
			for (ArboTypeEquipement arboTypeEquipement : typesEquipementArbo) {
				for (ArboEquipement arboEquipement : arboTypeEquipement
						.getEquipements()) {
					
					final EquipGeneralHasEquipementId equipId = new EquipGeneralHasEquipementId(
							idEquipGeneral, arboEquipement.getEquipement()
									.getIdEquipement());
					final EquipGeneralHasEquipement equip = equipements
							.get(equipId);

					if (arboEquipement.getNombre() == null
							|| 0 == arboEquipement.getNombre()) { // Equipement
																	// a
															// supprimer
						if (equip == null) {
							// Le bateau n'avait pas cet equipement, et ne
							// l'a toujours pas, on passe à la suivante
							continue;
						} else {
							// L'equip était enregistrée en base, comme
							// elle est maintenant a 0, on la supprime
							// session.
							equipements.remove(equipId);
							hibernateSession.delete(equip);
						}
					} else { // Equipement a mettre à jour
						if (equip == null) {
							// Le bateau n'avait pas cet equipement, on
							// l'ajoute dans sa liste
							equipements.put(equipId,
									new EquipGeneralHasEquipement(equipId,
											equipGeneral, arboEquipement
													.getEquipement(),
											arboEquipement
													.getNombre()));
						} else {
							equip.setNombre(arboEquipement.getNombre());
						}
					}
				}
			}
			

		} catch (Exception e) {
			log.error("Error while updating boat equipments : {0}", e.getMessage());
			throw new BSException(
					"error.equipement");
		}
	}

	/**
	 * Connecte ce manager avec le bateauHome.
	 * 
	 * Concrètement on initialise ici notre arborescence d'équipements en
	 * fonction des competences de l'utilisateur.
	 */
	public void wire(HibernateEntityHome<Bateau> bateauHome) {
		this.bateauHome = bateauHome;
		typesEquipementArbo = new ArrayList<ArboTypeEquipement>();
		final EquipGeneral equipGeneral = bateauHome.getInstance()
				.getEquipGeneral();

		// Les compétences utilisateur
		final Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> equipements = equipGeneral
				.getEquipGeneralHasEquipements();

		// On parcours les types de competences
		for (TypeEquipement typeEquipement : typesEquipement) {

			ArboTypeEquipement arboTypeEquipement = new ArboTypeEquipement(
					typeEquipement.getIdTypeEquipement());

			// On parcours les compétences
			for (Equipement equipement : typeEquipement.getEquipements()) {

				ArboEquipement curArboEquipement;
				final EquipGeneralHasEquipementId equipId = new EquipGeneralHasEquipementId(
						equipGeneral.getIdEquipGeneral() != null ? equipGeneral
								.getIdEquipGeneral() : -1, equipement 
								.getIdEquipement());
				// On initialise notre arborescence
				if (equipements.containsKey(equipId)) {
					curArboEquipement = new ArboEquipement(equipement,
							equipements.get(equipId).getNombre());
				} else {
					curArboEquipement = new ArboEquipement(equipement, 0);
				}

				arboTypeEquipement.addEquipement(curArboEquipement);

			}
			typesEquipementArbo.add(arboTypeEquipement);
		}
	}

	/*  Accesseurs */

	public List<ArboTypeEquipement> getTypesEquipementArbo() {
		return typesEquipementArbo;
	}
}
