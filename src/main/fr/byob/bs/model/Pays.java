package fr.byob.bs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@NamedQueries( { @NamedQuery(name = "pays.findAll", query = "select pays from Pays pays join fetch pays.langue order by pays.langue.libelle"),
		@NamedQuery(name = "pays.findAllOrderByLibelle", query = "select pays from Pays pays order by pays.libelle") })
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PAYS")
public class Pays implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_PAYS", unique = true, nullable = false)
	@NotNull
	private Long idPays;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Monnaie monnaie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUE", nullable = false)
	@NotNull
	private Langue langue;

	@Column(name = "INDICATIF_TEL", nullable = false, length = 3)
	@NotNull
	@Length(max = 3)
	private String indicatifTel;

	@Column(name = "LIBELLE", nullable = false, length = 40)
	@NotNull
	@Length(max = 40)
	private String libelle;

	public Pays() {
	}

	public Pays(Long idPays, Monnaie monnaie, Langue langue,
			String indicatifTel, String libelle) {
		this.idPays = idPays;
		this.monnaie = monnaie;
		this.langue = langue;
		this.indicatifTel = indicatifTel;
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPays == null) ? 0 : idPays.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Pays other = (Pays) obj;
		if (idPays == null) {
			if (other.idPays != null) {
				return false;
			}
		} else if (!idPays.equals(other.idPays)) {
			return false;
		}
		return true;
	}

//	@Override
//	public String toString() {
//		return "#{messages['pays'+this.getIdPays()]}";
////		return  this.libelle + "A int!";
//	}

	public Long getIdPays() {
		return this.idPays;
	}

	public void setIdPays(Long idPays) {
		this.idPays = idPays;
	}

	public Monnaie getMonnaie() {
		return this.monnaie;
	}

	public void setMonnaie(Monnaie monnaie) {
		this.monnaie = monnaie;
	}

	public Langue getLangue() {
		return this.langue;
	}

	public void setLangue(Langue langue) {
		this.langue = langue;
	}

	public String getIndicatifTel() {
		return this.indicatifTel;
	}

	public void setIndicatifTel(String indicatifTel) {
		this.indicatifTel = indicatifTel;
	}

	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
