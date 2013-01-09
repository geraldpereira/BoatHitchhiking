package fr.byob.bs.model.utilisateur;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

import fr.byob.bs.model.Lieu;

@Embeddable
public class Coordonnees implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIEU", nullable = false)
	@NotNull
	private Lieu lieu;
	
	@Column(name = "ADRESSE", nullable = true, length = 300)
	@Length(max = 300)
	private String adresse;
	
	@Column(name = "NOM", nullable = false, length = 30)
	@NotEmpty
	@Length(max = 30)
	@Pattern(regex="\\p{L}*([- ]\\p{L}*)*",message = "#{messages['validator.lettreEtMoins']}")
	private String nom;
	
	@Column(name = "PRENOM", nullable = false, length = 20)
	@NotEmpty
	@Length(max = 20)
	@Pattern(regex="\\p{L}*([- ]\\p{L}*)*",message = "#{messages['validator.lettreEtMoins']}")
	private String prenom;
	
	@Column(name = "TEL", length = 15, nullable = false)
	@Pattern(regex="[+]{0,1}[0-9\\s]*", message = "#{messages['validator.tel']}")
	@Length(max = 15)
	@NotEmpty
	private String tel;
	
	@Column(name = "TEL_BIS", length = 15)
	@Pattern(regex = "[+]{0,1}[0-9\\s]*", message = "#{messages['validator.tel']}")
	@Length(max = 15)
	private String telBis;
	
	@Column(name = "SITE", length = 255)
	@Length(max = 255)
	@Pattern(regex="http[s]?://\\S*", message = "#{messages['validator.url']}")
	private String site;

	public Coordonnees() {
		this.site = "http://";
		this.lieu = new Lieu();
	}

	public Coordonnees(Lieu lieu, String nom, String prenom) {
		this.lieu = lieu;
		this.nom = nom;
		this.prenom = prenom;
	}
	public Coordonnees(Lieu lieu, String nom, String prenom, String tel,
			String site) {
		this.lieu = lieu;
		this.nom = nom;
		this.prenom = prenom;
		this.tel = tel;
		this.site = site;
	}

	public Lieu getLieu() {
		return this.lieu;
	}

	public void setLieu(Lieu lieu) {
		this.lieu = lieu;
	}


	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTelBis() {
		return this.telBis;
	}

	public void setTelBis(String telBis) {
		this.telBis = telBis;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	
	
}
