package fr.byob.bs.model.annonce;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.Monnaie;

@Embeddable
public class ContribFin implements java.io.Serializable {

	private static final long serialVersionUID = -6710221375912520571L;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Monnaie monnaie;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE_PAIEMENT", nullable = false)
	private TypePaiement typePaiement;
	
	@Column(name = "MONTANT", nullable = false)
	@NotNull
	@Min(0)
	@Max(999999)
	private Integer montant;
	

	public ContribFin() {
	}

	public ContribFin(Monnaie monnaie, TypePaiement typePaiement) {
		this.monnaie = monnaie;
		this.typePaiement = typePaiement;
	}
	
	
	public Monnaie getMonnaie() {
		return this.monnaie;
	}

	public void setMonnaie(Monnaie monnaie) {
		this.monnaie = monnaie;
	}
	
	public TypePaiement getTypePaiement() {
		return this.typePaiement;
	}

	public void setTypePaiement(TypePaiement typePaiement) {
		this.typePaiement = typePaiement;
	}

	public Integer getMontant() {
		return this.montant;
	}

	public void setMontant(Integer montant) {
		this.montant = montant;
	}

}
