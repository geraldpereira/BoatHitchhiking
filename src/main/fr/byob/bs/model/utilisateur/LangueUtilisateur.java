package fr.byob.bs.model.utilisateur;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.Langue;

@Entity
@Table(name = "LANGUE_UTILISATEUR")
public class LangueUtilisateur implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "langue", column = @Column(name = "LANGUE", nullable = false)),
			@AttributeOverride(name = "utilisateur", column = @Column(name = "UTILISATEUR", nullable = false)) })
	@NotNull
	private LangueUtilisateurId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUE", nullable = false, insertable = false, updatable = false)
	@NotNull
	private Langue langue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTILISATEUR", nullable = false, insertable = false, updatable = false)
	@NotNull
	private Utilisateur utilisateur;

	@Column(name = "NIVEAU", nullable = false)
	@Min(1)
	@Max(5)
	@NotNull
	private Integer niveau;

	public LangueUtilisateur() {
//		this.niveau = 1;
		id = new LangueUtilisateurId();
	}

	public LangueUtilisateur(LangueUtilisateurId id, Langue langue,
			Utilisateur utilisateur, Integer niveau) {
		this.id = id;
		this.langue = langue;
		this.utilisateur = utilisateur;
		this.niveau = niveau;
	}

	public LangueUtilisateurId getId() {
		return this.id;
	}

	public void setId(LangueUtilisateurId id) {
		this.id = id;
	}

	public Langue getLangue() {
		return this.langue;
	}

	public void setLangue(Langue langue) {
		this.langue = langue;
		id.setLangue(langue.getIdlangue());
	}

	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
		id.setUtilisateur(utilisateur.getPseudonyme());
	}

	public Integer getNiveau() {
		return this.niveau;
	}

	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LangueUtilisateur other = (LangueUtilisateur) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
