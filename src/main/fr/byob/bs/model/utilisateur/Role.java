package fr.byob.bs.model.utilisateur;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.Length;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "ROLE")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String ADMIN = "admin";
	
	@Id
	@Column(name = "ID_ROLE", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NOM", unique= true, nullable = false, length = 20)
	@Length(max = 20)
	private String nom;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
