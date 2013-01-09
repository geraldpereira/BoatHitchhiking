package fr.byob.bs.model.annonce;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.Lieu;
import fr.byob.bs.model.TypeNav;
import fr.byob.bs.model.utilisateur.Appreciation;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Annonce implements java.io.Serializable, BSEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "numAnnonceGenerator")
	@GenericGenerator(name = "numAnnonceGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "num_annonce_sequence") })
	@Column(name = "NUM_ANNONCE", unique = true, nullable = false)
	private Long numAnnonce;

	@Embedded
	private ContribFin contribFin;
	
	@Column(name = "TITRE", nullable = false, length = 255)
	@NotNull
	@Length(max = 255)
	private String titre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIEU_ARRIVEE", nullable = true)
	// @NotNull
	private Lieu lieuArrivee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIEU_DEPART", nullable = true)
	// @NotNull
	private Lieu lieuDepart;

	@org.hibernate.annotations.CollectionOfElements
	@JoinTable(name = "TYPES_NAV_ANNONCE", joinColumns = @JoinColumn(name = "TYPE_NAV_ANNONCE_ID"))
	@Column(name = "TYPE_NAV", nullable = true)
	private List<TypeNav> typesNav = new ArrayList<TypeNav>(0);

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MAJ", nullable = false)
	@NotNull
	private Date dateMaj;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_DEBUT", length = 8, nullable = true)
	private Date dateDebut;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_FIN", length = 8, nullable = true)
	private Date dateFin;

	@Column(name = "DESCRIPTION", length = 2000)
	@Length(max = 2000)
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "annonce")
	private List<Appreciation> appreciations = new ArrayList<Appreciation>(0);

	@Column(name = "VALIDE", nullable = true)
	private Boolean valide;
	
	@Column(name = "NOTE", nullable = true)
	@Min(0)
	private Integer note;
	
	public Annonce() {
		this.contribFin = new ContribFin();
		this.lieuArrivee = new Lieu();
		this.lieuDepart = new Lieu();
		this.valide = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numAnnonce == null) ? 0 : numAnnonce.hashCode());
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
		Annonce other = (Annonce) obj;
		if (numAnnonce == null) {
			if (other.numAnnonce != null)
				return false;
		} else if (!numAnnonce.equals(other.numAnnonce))
			return false;
		return true;
	}

	public Long getNumAnnonce() {
		return this.numAnnonce;
	}

	public void setNumAnnonce(Long numAnnonce) {
		this.numAnnonce = numAnnonce;
	}

	public ContribFin getContribFin() {
		return this.contribFin;
	}

	public void setContribFin(ContribFin contribFin) {
		this.contribFin = contribFin;
	}

	public Lieu getLieuArrivee() {
		return this.lieuArrivee;
	}

	public void setLieuArrivee(Lieu lieuArrivee) {
		this.lieuArrivee = lieuArrivee;
	}

	public Lieu getLieuDepart() {
		return this.lieuDepart;
	}

	public void setLieuDepart(Lieu lieuDepart) {
		this.lieuDepart = lieuDepart;
	}

	public Date getDateMaj() {
		return this.dateMaj;
	}

	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}

	public Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return this.dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Appreciation> getAppreciations() {
		return this.appreciations;
	}

	public void setAppreciations(List<Appreciation> appreciations) {
		this.appreciations = appreciations;
	}

	public List<TypeNav> getTypesNav() {
		return typesNav;
	}

	public void setTypesNav(List<TypeNav> typesNav) {
		this.typesNav = typesNav;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Boolean getValide() {
		return this.valide;
	}

	public void setValide(Boolean valide) {
		this.valide = valide;
	}

	@Override
	public String toString() {
		return this.getObjectType() + " [" + this.getObjectKey() + "] " + this.getObjectLabel();
	}
	
	public String getObjectKey() {
		return "" + getNumAnnonce();
	}

	public String getObjectLabel() {
		return getTitre();
	}

	public Integer getNote() {
		return this.note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}
}
