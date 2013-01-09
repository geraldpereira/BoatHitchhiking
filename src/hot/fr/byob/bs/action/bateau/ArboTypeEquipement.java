package fr.byob.bs.action.bateau;

import java.util.ArrayList;
import java.util.List;

public class ArboTypeEquipement {
	private final Long idTypeEquipement;
	private List<ArboEquipement> equipements;

	public ArboTypeEquipement(Long idTypeEquipement) {
		super();
		this.idTypeEquipement = idTypeEquipement;
		this.equipements = new ArrayList<ArboEquipement>();
	}

	
	public Long getIdTypeEquipement() {
		return idTypeEquipement;
	}

	public List<ArboEquipement> getEquipements() {
		return equipements;
	}

	public void setEquipements(List<ArboEquipement> equipements) {
		this.equipements = equipements;
	}

	public void addEquipement(ArboEquipement equipement) {
		this.equipements.add(equipement);
	}
}
