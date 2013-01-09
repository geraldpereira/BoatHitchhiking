package fr.byob.bs.action.bateau;

import fr.byob.bs.model.bateau.Equipement;

public class ArboEquipement {
	
	private Equipement equipement;
	private Integer nombre;
	
	public ArboEquipement(Equipement equipement, Integer nombre) {
		super();
		this.equipement = equipement;
		this.nombre = nombre;
	}

	public Equipement getEquipement() {
		return equipement;
	}

	public void setEquipement(Equipement equipement) {
		this.equipement = equipement;
	}

	public Integer getNombre() {
		return nombre;
	}

	public void setNombre(Integer nombre) {
		this.nombre = nombre;
	}
	
	
}
