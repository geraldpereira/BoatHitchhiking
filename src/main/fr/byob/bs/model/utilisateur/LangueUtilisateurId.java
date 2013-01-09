package fr.byob.bs.model.utilisateur;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.NotNull;

@Embeddable
public class LangueUtilisateurId implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long langue;
	private String utilisateur;

	public LangueUtilisateurId() {
	}

	public LangueUtilisateurId(long langue, String utilisateur) {
		this.langue = langue;
		this.utilisateur = utilisateur;
	}

	@Column(name = "LANGUE", nullable = false)
	@NotNull
	public long getLangue() {
		return this.langue;
	}

	public void setLangue(long langue) {
		this.langue = langue;
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
		result = prime * result + (int) (langue ^ (langue >>> 32));
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
		LangueUtilisateurId other = (LangueUtilisateurId) obj;
		if (langue != other.langue)
			return false;
		if (!utilisateur.equals(other.utilisateur))
			return false;
		return true;
	}
}
