package fr.byob.bs.model.bateau;
// Generated 27 f�vr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Equipement generated by hbm2java
 */

@NamedQueries( { @NamedQuery(name = "typeEquipementByEquipement", query = "select distinct typeEquipement from TypeEquipement typeEquipement join fetch typeEquipement.equipements") })
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "TYPE_EQUIPEMENT")
public class TypeEquipement implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long idTypeEquipement;
	private String libelle;
	private Set<Equipement> equipements = new HashSet<Equipement>(
			0);

	public TypeEquipement() {
	}

	public TypeEquipement(String libelle) {
		this.libelle = libelle;
	}
	public TypeEquipement(String libelle,
			Set<Equipement> equipements) {
		this.libelle = libelle;
		this.equipements = equipements;
	}

	@Id
	@Column(name = "ID_TYPE_EQUIPEMENT", unique = true, nullable = false)
	public Long getIdTypeEquipement() {
		return idTypeEquipement;
	}

	public void setIdTypeEquipement(Long idTypeEquipement) {
		this.idTypeEquipement = idTypeEquipement;
	}

	@Column(name = "LIBELLE", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getLibelle() {
		return this.libelle;
	}
	
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeEquipement")
	public Set<Equipement> getEquipements() {
		return equipements;
	}

	public void setEquipements(Set<Equipement> equipements) {
		this.equipements = equipements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTypeEquipement == null) ? 0 : idTypeEquipement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeEquipement other = (TypeEquipement) obj;
		if (idTypeEquipement == null) {
			if (other.idTypeEquipement != null)
				return false;
		} else if (!idTypeEquipement.equals(other.idTypeEquipement))
			return false;
		return true;
	}

	
}
