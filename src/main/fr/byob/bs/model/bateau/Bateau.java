package fr.byob.bs.model.bateau;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import fr.byob.bs.action.utilisateur.messagerie.ContactManager;
import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.utilisateur.Utilisateur;

@NamedQueries( { @NamedQuery(name = "bateau.findByUser", query = "select bateau from Bateau bateau where lower(bateau.utilisateur) like lower(:pseudo)"),
	@NamedQuery(name = "bateau.findAll", query = "select bateau from Bateau bateau"),
		@NamedQuery(name = "bateau.validateByUser", query = "update Bateau bateau set bateau.valide = true where lower(bateau.utilisateur) like lower(:pseudo)") })	
@Entity
@Table(name = "BATEAU")
public class Bateau implements java.io.Serializable, BSEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idBateauGenerator")
	@GenericGenerator(name = "idBateauGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "id_bateau_sequence") })
	@Column(name = "ID_BATEAU", unique = true, nullable = false)
	private Long idBateau;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTILISATEUR", nullable = false)
	@NotNull
	private Utilisateur utilisateur;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE_COQUE", nullable = true)
	private TypeCoque typeCoque;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,optional= false)
	@JoinColumn(name = "EQUIP_GENERAL", nullable = false)
	private EquipGeneral equipGeneral;

	@Column(name = "NOM", length = 30)
	@Length(max = 30)
	@NotNull
	private String nom;
	
	@Column(name = "MARQUE", length = 20)
	@Length(max = 20)
	@NotNull
	private String marque;

	@Column(name = "ANNEE", nullable = false)
	@NotNull
	@Min(1700)
	@Max(2100)
	private Integer annee;

	@Column(name = "TAILLE", nullable = false)
	@NotNull
	@Min(0)
	@Max(100)
	private Integer taille;
	
	@Column(name = "INFO", length = 2000)
	@Length(max = 2000)
	private String info;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bateau", cascade = { CascadeType.ALL })
	private List<PhotoBateau> photosBateau = new ArrayList<PhotoBateau>(0);
	
	@Enumerated(EnumType.STRING)
	@Column(name = "GREEMENT", nullable = true)
	private Greement greement;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bateau")
	private Set<Offre> offres = new HashSet<Offre>(0);
	
	@org.hibernate.annotations.CollectionOfElements
	@JoinTable(name = "VOILES_BATEAU", joinColumns = @JoinColumn(name = "BATEAU_ID"))
	// @org.hibernate.annotations.IndexColumn(name = "POSITION", base = 1)
	@Column(name = "VOILE_BATEAU", nullable = true)
	private List<Voile> voiles = new ArrayList<Voile>();

	@Column(name = "VALIDE", nullable = true)
	private Boolean valide;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MAJ", nullable = false)
	@NotNull
	private Date dateMaj;
	
	@Column(name = "NOTE", nullable = true)
	@Min(0)
	private Integer note;
	
	public Bateau() {
		this.annee = 2000;
		this.equipGeneral = new EquipGeneral();
		// this.equipGeneral.setBateau(this);
		this.valide = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idBateau == null) ? 0 : idBateau.hashCode());
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
		Bateau other = (Bateau) obj;
		if (idBateau == null) {
			if (other.idBateau != null)
				return false;
		} else if (!idBateau.equals(other.idBateau))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getObjectType() + " [" + this.getObjectKey() + "] " + this.getObjectLabel();
	}

	public Long getIdBateau() {
		return this.idBateau;
	}

	public void setIdBateau(Long idBateau) {
		this.idBateau = idBateau;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	

	public TypeCoque getTypeCoque() {
		return this.typeCoque;
	}

	public void setTypeCoque(TypeCoque typeCoque) {
		this.typeCoque = typeCoque;
	}
		
	public EquipGeneral getEquipGeneral() {
		return this.equipGeneral;
	}

	public void setEquipGeneral(EquipGeneral equipGeneral) {
		this.equipGeneral = equipGeneral;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMarque() {
		return this.marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public Integer getAnnee() {
		return this.annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Integer getTaille() {
		return this.taille;
	}

	public void setTaille(Integer taille) {
		this.taille = taille;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<PhotoBateau> getPhotosBateau() {
		return this.photosBateau;
	}

	public void setPhotosBateau(List<PhotoBateau> photos) {
		this.photosBateau = photos;
	}
	
	public Greement getGreement() {
		return this.greement;
	}

	public void setGreement(Greement greement) {
		this.greement = greement;
	}
	
	public Set<Offre> getOffres() {
		return this.offres;
	}

	public void setOffres(Set<Offre> offres) {
		this.offres = offres;
	}

	public List<Voile> getVoiles() {
		return voiles;
	}

	public void setVoiles(List<Voile> voiles) {
		this.voiles = voiles;
	}

	public Boolean getValide() {
		return this.valide;
	}

	public void setValide(Boolean valide) {
		this.valide = valide;
	}	
	
	public String getObjectType() {
		return ContactManager.OBJECT_TYPE.BATEAU.name();
	}

	public String getObjectKey() {
		return "" + getIdBateau();
	}

	public String getObjectLabel() {
		return getNom();
	}

	public Date getDateMaj() {
		return this.dateMaj;
	}

	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}

	public Integer getNote() {
		return this.note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}
}
