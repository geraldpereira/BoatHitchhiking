package fr.byob.bs.model.utilisateur;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.annonce.Annonce;
import fr.byob.bs.model.competence.CompetenceAppreciee;

@Entity
@Table(name = "APPRECIATION")
public class Appreciation implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idAppreciationGenerator")
	@GenericGenerator(name = "idAppreciationGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "id_appreciation_sequence") })
	@Column(name = "ID_APPRECIATION", unique = true, nullable = false)
	private Long idAppreciation;
	
	@Column(name = "COMMENTAIRE", nullable = false)
	@NotNull
	@Length(max = 2000)
	private String commentaire;
	
	@Column(name = "NOTE_GENERALE", nullable = false)
	@NotNull
	private Integer noteGenerale;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJET", nullable = false)
	@NotNull
	private Utilisateur sujet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTEUR", nullable = true)
	private Utilisateur auteur;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ANNONCE", nullable = true)
	private Annonce annonce;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appreciation")
	private List<CompetenceAppreciee> competences = new ArrayList<CompetenceAppreciee>(
			0);

	public Appreciation() {
	}

	public Appreciation(Utilisateur sujet, String commentaire,
			Integer noteGenerale) {
		this.sujet = sujet;
		this.commentaire = commentaire;
		this.noteGenerale = noteGenerale;
	}
	public Appreciation(Utilisateur sujet, String commentaire,
			Integer noteGenerale, Utilisateur auteur,
			Annonce annonce,
			List<CompetenceAppreciee> competences) {
		this.sujet = sujet;
		this.commentaire = commentaire;
		this.noteGenerale = noteGenerale;
		this.auteur = auteur;
		this.annonce = annonce;
		this.competences = competences;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idAppreciation == null) ? 0 : idAppreciation.hashCode());
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
		Appreciation other = (Appreciation) obj;
		if (idAppreciation == null) {
			if (other.idAppreciation != null)
				return false;
		} else if (!idAppreciation.equals(other.idAppreciation))
			return false;
		return true;
	}

	public Long getIdAppreciation() {
		return this.idAppreciation;
	}

	public void setIdAppreciation(Long idAppreciation) {
		this.idAppreciation = idAppreciation;
	}
	
	public Utilisateur getSujet() {
		return this.sujet;
	}

	public void setSujet(Utilisateur sujet) {
		this.sujet = sujet;
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public int getNoteGenerale() {
		return this.noteGenerale;
	}

	public void setNoteGenerale(int noteGenerale) {
		this.noteGenerale = noteGenerale;
	}

	public Utilisateur getAuteur() {
		return this.auteur;
	}

	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}

	public Annonce getAnnonce() {
		return this.annonce;
	}

	public void setAnnonce(Annonce annonce) {
		this.annonce = annonce;
	}
	
	public List<CompetenceAppreciee> getCompetences() {
		return this.competences;
	}

	public void setCompetences(
			List<CompetenceAppreciee> competences) {
		this.competences = competences;
	}

}
