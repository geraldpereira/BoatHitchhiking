package fr.byob.bs.model.competence;

// Generated 27 févr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

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

import fr.byob.bs.model.utilisateur.Utilisateur;

@Entity
@Table(name = "COMP_UTILISATEUR")
public class CompetenceUtilisateur implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "competence", column = @Column(name = "COMPETENCE", nullable = false)),
			@AttributeOverride(name = "utilisateur", column = @Column(name = "UTILISATEUR", nullable = false)) })
	@NotNull
	private CompetenceUtilisateurId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPETENCE", nullable = false, insertable = false, updatable = false)
	@NotNull
	private Competence competence;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTILISATEUR", nullable = false, insertable = false, updatable = false)
	@NotNull
	private Utilisateur utilisateur;
	
	@Column(name = "NIVEAU", nullable = false)
	@NotNull
	@Min(0)
	@Max(5)
	private Integer niveau;

	public CompetenceUtilisateur() {
		this.niveau = 0;
	}

	public CompetenceUtilisateur(CompetenceUtilisateurId id,
			Competence competence, Utilisateur utilisateur, Integer niveau) {
		this.id = id;
		this.competence = competence;
		this.utilisateur = utilisateur;
		this.niveau = niveau;
	}

	
	public CompetenceUtilisateurId getId() {
		return this.id;
	}

	public void setId(CompetenceUtilisateurId id) {
		this.id = id;
	}

	
	public Competence getCompetence() {
		return this.competence;
	}

	public void setCompetence(Competence competence) {
		this.competence = competence;
	}

	
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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
		CompetenceUtilisateur other = (CompetenceUtilisateur) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
