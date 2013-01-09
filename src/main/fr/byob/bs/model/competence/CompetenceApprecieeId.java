package fr.byob.bs.model.competence;
// Generated 27 f�vr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.NotNull;

/**
 * CompsAutresApprecieesId generated by hbm2java
 */
@Embeddable
public class CompetenceApprecieeId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long appreciation;
	private long competence;

	public CompetenceApprecieeId() {
	}

	public CompetenceApprecieeId(long appreciation, long competence) {
		this.appreciation = appreciation;
		this.competence = competence;
	}

	@Column(name = "APPRECIATION", nullable = false)
	@NotNull
	public long getAppreciation() {
		return this.appreciation;
	}

	public void setAppreciation(long appreciation) {
		this.appreciation = appreciation;
	}

	@Column(name = "COMPETENCE", nullable = false)
	@NotNull
	public long getCompetence() {
		return this.competence;
	}

	public void setCompetence(long competence) {
		this.competence = competence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (appreciation ^ (appreciation >>> 32));
		result = prime * result + (int) (competence ^ (competence >>> 32));
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
		CompetenceApprecieeId other = (CompetenceApprecieeId) obj;
		if (appreciation != other.appreciation)
			return false;
		if (competence != other.competence)
			return false;
		return true;
	}

}