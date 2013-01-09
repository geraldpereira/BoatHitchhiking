package fr.byob.bs.model;

// Generated 27 f�vr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Langue generated by hbm2java
 */
@NamedQueries( { @NamedQuery(name = "langue.findByLibelle", query = "select langue from Langue langue order by langue.libelle") })
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "LANGUE")
public class Langue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_LANGUE", unique = true, nullable = false)
	@NotNull
	private Long idLangue;

	@Column(name = "LIBELLE", nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	private String libelle;

	public Langue() {
	}

	public Langue(Long idLangue, String libelle) {
		this.idLangue = idLangue;
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idLangue == null) ? 0 : idLangue.hashCode());
		return result;
	}


	@Override
	   public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Langue other = (Langue) obj;
		if (idLangue == null) {
			if (other.idLangue != null) {
				return false;
			}
		} else if (!toString().equals(other.toString())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ""+this.idLangue;
	}

	public Long getIdlangue() {
		return this.idLangue;
	}

	public void setIdlangue(Long idLangue) {
		this.idLangue = idLangue;
	}

	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
