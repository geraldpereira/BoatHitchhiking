package fr.byob.bs.model.bateau;

// Generated 27 f�vr. 2009 10:34:58 by Hibernate Tools 3.2.2.GA

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;

/**
 * EquipGeneral generated by hbm2java
 */
@Entity
@Table(name = "EQUIP_GENERAL")
public class EquipGeneral implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idEquipGeneralGenerator")
	@GenericGenerator(name = "idEquipGeneralGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "id_equip_general_sequence") })
	@Column(name = "ID_EQUIP_GENERAL", unique = true, nullable = false)
	private Long idEquipGeneral;
	
	@Column(name = "ANNEXE")
	private Boolean annexe;
	
	@Column(name = "NB_MOTEURS")
	@Min(0)
	private Integer nbMoteurs;
	
	@Column(name = "PUISSANCE")
	@Min(0)
	private Integer puissance;
	
	@Column(name = "CONTENANCE_GAZOIL")
	@Min(0)
	private Integer contenanceGazoil;
	
	@Column(name = "NB_CABINES")
	@Min(0)
	@Max(100)
	private Integer nbCabines;
	
	@Column(name = "NB_DOUCHES")
	@Min(0)
	@Max(100)
	private Integer nbDouches;
	
	@Column(name = "NB_WC")
	@Min(0)
	@Max(100)
	private Integer nbWC;
	
	@Column(name = "CONTENANCE_EAU")
	@Min(0)
	private Integer contenanceEau;
	
	@Column(name = "AUTRES", length = 1000)
	@Length(max = 1000)
	private String autres;

	@MapKey(name = "id")
	@OneToMany(cascade = { CascadeType.ALL, CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "equipGeneral")
	private Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> equipGeneralHasEquipements = new HashMap<EquipGeneralHasEquipementId, EquipGeneralHasEquipement>(
			0);
	

	public EquipGeneral() {
	}

	public EquipGeneral(Boolean annexe, Integer nbMoteurs, Integer puissance,
			Integer contenanceGazoil, Integer nbCabines, Integer nbDouches,
			Integer nbWC, Integer contenanceEau, String autres,
			Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> equipGeneralHasEquipements) {
		this.annexe = annexe;
		this.nbMoteurs = nbMoteurs;
		this.puissance = puissance;
		this.contenanceGazoil = contenanceGazoil;
		this.nbCabines = nbCabines;
		this.nbDouches = nbDouches;
		this.nbWC = nbWC;
		this.contenanceEau = contenanceEau;
		this.autres = autres;
		this.equipGeneralHasEquipements = equipGeneralHasEquipements;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idEquipGeneral == null) ? 0 : idEquipGeneral.hashCode());
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
		EquipGeneral other = (EquipGeneral) obj;
		if (idEquipGeneral == null) {
			if (other.idEquipGeneral != null)
				return false;
		} else if (!idEquipGeneral.equals(other.idEquipGeneral))
			return false;
		return true;
	}

	
	public Long getIdEquipGeneral() {
		return this.idEquipGeneral;
	}

	public void setIdEquipGeneral(Long idEquipGeneral) {
		this.idEquipGeneral = idEquipGeneral;
	}

	public Boolean getAnnexe() {
		return this.annexe;
	}

	public void setAnnexe(Boolean annexe) {
		this.annexe = annexe;
	}

	public Integer getNbMoteurs() {
		return this.nbMoteurs;
	}

	public void setNbMoteurs(Integer nbMoteurs) {
		this.nbMoteurs = nbMoteurs;
	}

	public Integer getPuissance() {
		return this.puissance;
	}

	public void setPuissance(Integer puissance) {
		this.puissance = puissance;
	}

	public Integer getContenanceGazoil() {
		return this.contenanceGazoil;
	}

	public void setContenanceGazoil(Integer contenance) {
		this.contenanceGazoil = contenance;
	}

	public Integer getNbCabines() {
		return this.nbCabines;
	}

	public void setNbCabines(Integer nbCabines) {
		this.nbCabines = nbCabines;
	}

	public Integer getNbDouches() {
		return this.nbDouches;
	}

	public void setNbDouches(Integer nbDouches) {
		this.nbDouches = nbDouches;
	}

	public Integer getNbWC() {
		return this.nbWC;
	}

	public void setNbWC(Integer nbWC) {
		this.nbWC = nbWC;
	}

	public Integer getContenanceEau() {
		return this.contenanceEau;
	}

	public void setContenanceEau(Integer contenanceEau) {
		this.contenanceEau = contenanceEau;
	}

	public String getAutres() {
		return this.autres;
	}

	public void setAutres(String autres) {
		this.autres = autres;
	}

	public Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> getEquipGeneralHasEquipements() {
		return this.equipGeneralHasEquipements;
	}

	public void setEquipGeneralHasEquipements(
			Map<EquipGeneralHasEquipementId, EquipGeneralHasEquipement> equipGeneralHasEquipements) {
		this.equipGeneralHasEquipements = equipGeneralHasEquipements;
	}
/*
	public Bateau getBateau() {
		return this.bateau;
	}

	public void setBateau(Bateau bateau) {
		this.bateau = bateau;
	}*/
}