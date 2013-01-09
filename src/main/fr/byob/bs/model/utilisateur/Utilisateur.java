package fr.byob.bs.model.utilisateur;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.AssertTrue;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

import fr.byob.bs.action.utilisateur.messagerie.ContactManager;
import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.competence.CompetenceUtilisateur;
import fr.byob.bs.model.competence.CompetenceUtilisateurId;
import fr.byob.bs.model.experience.Xp;
import fr.byob.bs.model.experience.XpGenerale;
import fr.byob.bs.validator.LanguesUtilisateur;

@NamedQueries( {
		@NamedQuery(name = "user.updateLangueByPseudo", query = "update Utilisateur utilisateur set utilisateur.languePreferee = :locale where lower(utilisateur.pseudonyme) like :pseudo"),
		@NamedQuery(name = "user.findByPseudo", query = "select utilisateur from Utilisateur utilisateur where lower(utilisateur.pseudonyme) like :pseudo and utilisateur.pseudonyme <> :pseudoDummy"),
		@NamedQuery(name = "user.findByMail", query = "select utilisateur from Utilisateur utilisateur where lower(utilisateur.mail) like :mail and utilisateur.pseudonyme <> :pseudoDummy"),
		@NamedQuery(name = "user.findAll", query = "select utilisateur from Utilisateur utilisateur"),
		@NamedQuery(name = "user.connectByPseudo", query = "select distinct u from Utilisateur u left join fetch u.roles join fetch u.coordonnees.lieu residence join fetch residence.pays where lower(u.pseudonyme) = :pseudonyme and u.pseudonyme <> :pseudoDummy"),
		@NamedQuery(name = "user.connectByMail", query = "select distinct u from Utilisateur u left join fetch u.roles join fetch u.coordonnees.lieu residence join fetch residence.pays where lower(u.mail) like :mail and u.pseudonyme <> :pseudoDummy") })
@Entity
@Table(name = "UTILISATEUR")
public class Utilisateur implements java.io.Serializable, BSEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PSEUDONYME", unique = true, nullable = false)
	@NotEmpty
	@Pattern(regex = "[a-zA-Z][a-zA-Z0-9]*", message = "#{messages['validator.lettrelettresEtChiffres']}")
	@Length(min = 3, max = 25)
	// @Unique(entity="Utilisateur", field="pseudonyme")
	private String pseudonyme;

	@Column(name = "MAIL", nullable = false, length = 128)
	@NotEmpty
	@Email
	@Length(max = 128)
	private String mail;

	@Column(name = "MAIL_AUTH", nullable = false)
	private boolean mailAuth;
	
	@Transient
	@AssertTrue(message="#{messages['validator.conditions']}")
	private transient Boolean cndUtilisation;

	@Column(name = "MOT_DE_PASSE", nullable = false, length = 128)
	@NotEmpty
	@Length(max = 128)
	private String motDePasse;

	@Column(name = "ACTIF", nullable = true)
	@Length(max = 10)
	private String actif;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_INSCRIPTION", nullable = false)
	@NotNull
	private Date dateInscription;

	@Column(name = "LANGUE_PREF", nullable = false, length = 2)
	@NotNull
	@Length(max = 2)
	private String languePreferee;

	@Column(name = "MOYENNE_NOTES", nullable = false)
	@NotNull
	private BigDecimal moyenneNotes;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_NAISSANCE", nullable = true, length = 8)
	private Date dateNaissance;

	@Column(name = "DESCRIPTION")
	@Length(max = 3000)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "XP_GENERALE", nullable = true)
	private XpGenerale xpGenerale;
	
	@Column(name = "NOTE", nullable = true)
	@Min(0)
	private Integer note;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "PHOTO", nullable = true)
	private PhotoUtilisateur photoUtilisateur;

	@Enumerated(EnumType.STRING)
	@Column(name = "SEXE", nullable = true)
	private Sexe sexe;

	@Embedded
	private Coordonnees coordonnees;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sujet")
	private List<Appreciation> appreciationsSujet = new ArrayList<Appreciation>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "auteur")
	private List<Appreciation> appreciationsAuteur = new ArrayList<Appreciation>(0);

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ROLE_UTILISATEUR", joinColumns = @JoinColumn(name = "utilisateur", referencedColumnName = "pseudonyme"), inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id_role"))
	private Set<Role> roles = new HashSet<Role>();

	@MapKey(name = "id")
	@OneToMany(cascade = { CascadeType.ALL, CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private Map<CompetenceUtilisateurId, CompetenceUtilisateur> competences = new HashMap<CompetenceUtilisateurId, CompetenceUtilisateur>(
			0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private List<Xp> xps = new ArrayList<Xp>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private List<Demande> demandes = new ArrayList<Demande>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private List<Offre> offres = new ArrayList<Offre>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	private List<Bateau> bateaux = new ArrayList<Bateau>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "utilisateur")
	@LanguesUtilisateur
	private List<LangueUtilisateur> langues = new ArrayList<LangueUtilisateur>(
			0);

	public Utilisateur() {
		this.coordonnees = new Coordonnees();
	}

	@Override
	public String toString() {
		return this.getObjectType() + " [" + this.getObjectKey() + "] " + this.getObjectLabel();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pseudonyme == null) ? 0 : pseudonyme.hashCode());
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
		Utilisateur other = (Utilisateur) obj;
		if (pseudonyme == null) {
			if (other.pseudonyme != null)
				return false;
		} else if (!pseudonyme.equals(other.pseudonyme))
			return false;
		return true;
	}


	/**
	 * Et non pas isActif sinon JSF fait chier et ne veux pas lire le champ !
	 * 
	 * @return
	 */
	public String getActif() {
		return this.actif;
	}

	public void setActif(String actif) {
		this.actif = actif;
	}

	public boolean isActif() {
		return this.actif == null || this.actif.length() == 0;
	}

	public void setPseudonyme(String pseudonyme) {
		this.pseudonyme = pseudonyme;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	// public String getMailConfirm() {
	// if (mailConfirm == null)
	// return null;
	// if (mail == null){
	// return mailConfirm;
	// }
	// if (!mail.equals(mailConfirm)){
	// return null;
	// }
	// return mailConfirm;
	// }

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public String getLanguePreferee() {
		return languePreferee;
	}

	public void setLanguePreferee(String languePreferee) {
		this.languePreferee = languePreferee;
	}

	public BigDecimal getMoyenneNotes() {
		return moyenneNotes;
	}

	public void setMoyenneNotes(BigDecimal moyenneNotes) {
		this.moyenneNotes = moyenneNotes;
	}

	public Date getDateNaissance() {
		return this.dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	// public Integer getAge() {
	// return age;
	// }
	//
	// public void setAge(Integer age) {
	// this.age = age;
	// }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public XpGenerale getXpGenerale() {
		return xpGenerale;
	}

	public void setXpGenerale(XpGenerale xpGenerale) {
		this.xpGenerale = xpGenerale;
	}

	public PhotoUtilisateur getPhotoUtilisateur() {
		return photoUtilisateur;
	}

	public void setPhotoUtilisateur(PhotoUtilisateur photo) {
		this.photoUtilisateur = photo;
	}

	public Sexe getSexe() {
		return sexe;
	}

	public void setSexe(Sexe sexe) {
		this.sexe = sexe;
	}

	public Coordonnees getCoordonnees() {
		return coordonnees;
	}

	public void setCoordonnees(Coordonnees coordonnees) {
		this.coordonnees = coordonnees;
	}

	public List<Appreciation> getAppreciationsSujet() {
		return appreciationsSujet;
	}

	public void setAppreciationsSujet(List<Appreciation> appreciationsSujet) {
		this.appreciationsSujet = appreciationsSujet;
	}

	public List<Appreciation> getAppreciationsAuteur() {
		return appreciationsAuteur;
	}

	public void setAppreciationsAuteur(List<Appreciation> appreciationsAuteur) {
		this.appreciationsAuteur = appreciationsAuteur;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Map<CompetenceUtilisateurId, CompetenceUtilisateur> getCompetences() {
		return competences;
	}

	public void setCompetences(
			Map<CompetenceUtilisateurId, CompetenceUtilisateur> competences) {
		this.competences = competences;
	}

	public List<Xp> getXps() {
		return xps;
	}

	public void setXps(List<Xp> xps) {
		this.xps = xps;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}

	public List<Offre> getOffres() {
		return offres;
	}

	public void setOffres(List<Offre> offres) {
		this.offres = offres;
	}

	public List<Bateau> getBateaux() {
		return bateaux;
	}

	public void setBateaux(List<Bateau> bateaux) {
		this.bateaux = bateaux;
	}

	public List<LangueUtilisateur> getLangues() {
		return langues;
	}

	public void setLangues(List<LangueUtilisateur> langues) {
		this.langues = langues;
	}

	public String getPseudonyme() {
		return pseudonyme;
	}

	public Boolean getCndUtilisation() {
		return cndUtilisation;
	}

	public void setCndUtilisation(Boolean cndUtilisation) {
		this.cndUtilisation = cndUtilisation;
	}
	
	public String getObjectType() {
		return ContactManager.OBJECT_TYPE.UTILISATEUR.name();
	}

	public String getObjectKey() {
		return getPseudonyme();
	}

	public String getObjectLabel() {
		return getPseudonyme();
	}
	
	public Integer getNote() {
		return this.note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}
	

	public boolean isMailAuth() {
		return this.mailAuth;
	}

	public void setMailAuth(boolean mailAuth) {
		this.mailAuth = mailAuth;
	}

	public void updateOtherUtilisateur(Utilisateur other) {
		other.actif = this.actif;
		other.appreciationsAuteur = this.appreciationsAuteur;
		other.appreciationsSujet = this.appreciationsSujet;
		other.bateaux = this.bateaux;
		other.competences = this.competences;
		other.coordonnees = this.coordonnees;
		other.dateInscription = this.dateInscription;
		other.dateNaissance = this.dateNaissance;
		other.demandes = this.demandes;
		other.description = this.description;
		other.languePreferee = this.languePreferee;
		other.langues = this.langues;
		other.mail = this.mail;
		other.motDePasse = this.motDePasse;
		other.moyenneNotes = this.moyenneNotes;
		other.offres = this.offres;
		other.photoUtilisateur = this.photoUtilisateur;
		other.pseudonyme = this.pseudonyme;
		other.roles = this.roles;
		other.sexe = this.sexe;
		other.xpGenerale = this.xpGenerale;
		other.xps = this.xps;
	}
}
