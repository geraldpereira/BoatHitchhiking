package fr.byob.bs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.NotNull;

@NamedQueries( { @NamedQuery(name = "propriete.findAll", query = "select propriete from Propriete propriete") })
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "PROPRIETE")
public class Propriete implements java.io.Serializable  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLE", unique = true, nullable = false)
	private String cle;
	
	@Column(name = "VALEUR", nullable = false)
	@NotNull
	private String valeur;
	
	public Propriete() {
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}
	
	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

}
