package fr.byob.bs.model.competence;

// Generated 27 févr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.NotNull;

@Embeddable
public class CompetenceUtilisateurId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long competence;
	private String utilisateur;

	public CompetenceUtilisateurId() {
	}

	public CompetenceUtilisateurId(long competence, String utilisateur) {
		this.competence = competence;
		this.utilisateur = utilisateur;
	}

	@Column(name = "COMPETENCE", nullable = false)
	@NotNull
	public long getCompetence() {
		return this.competence;
	}

	public void setCompetence(long competence) {
		this.competence = competence;
	}

	@Column(name = "UTILISATEUR", nullable = false)
	@NotNull
	public String getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (competence ^ (competence >>> 32));
		if (utilisateur != null) {
			result = prime * result + utilisateur.hashCode();
		}
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
		CompetenceUtilisateurId other = (CompetenceUtilisateurId) obj;
		if (competence != other.competence)
			return false;
		if (!utilisateur.equals(other.utilisateur))
			return false;
		return true;
	}

}
