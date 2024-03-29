package fr.byob.bs.model.competence;
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
 * CompAutre generated by hbm2java
 */
@NamedQueries( { @NamedQuery(name = "typeCompetenceByCompetence", query = "select distinct typeCompetence from TypeCompetence typeCompetence join fetch typeCompetence.competences") })
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "TYPE_COMPETENCE")
public class TypeCompetence implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private Long idTypeCompetence;
	private String libelle;
	private Set<Competence> competences = new HashSet<Competence>(0);

	public TypeCompetence() {
	}

	public TypeCompetence(String libelle) {
		this.libelle = libelle;
	}
	public TypeCompetence(String libelle, Set<Competence> competences) {
		this.libelle = libelle;
		this.competences = competences;
	}
	
	@Override
	public TypeCompetence clone(){
		TypeCompetence clone = new TypeCompetence();
		clone.setIdTypeCompetence(getIdTypeCompetence().longValue());
		clone.setLibelle(""+getLibelle());		
		return clone;
	}

	@Id
	@Column(name = "ID_TYPE_COMPETENCE", unique = true, nullable = false)
	public Long getIdTypeCompetence() {
		return idTypeCompetence;
	}

	public void setIdTypeCompetence(Long idTypeCompetence) {
		this.idTypeCompetence = idTypeCompetence;
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeCompetence")
	public Set<Competence> getCompetences() {
		return competences;
	}

	public void setCompetences(Set<Competence> competences) {
		this.competences = competences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTypeCompetence == null) ? 0 : idTypeCompetence.hashCode());
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
		TypeCompetence other = (TypeCompetence) obj;
		if (idTypeCompetence == null) {
			if (other.idTypeCompetence != null)
				return false;
		} else if (!idTypeCompetence.equals(other.idTypeCompetence))
			return false;
		return true;
	}
	
}
