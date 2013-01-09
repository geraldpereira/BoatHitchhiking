package fr.byob.bs.model.annonce;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import fr.byob.bs.action.utilisateur.messagerie.ContactManager;
import fr.byob.bs.model.TypePoste;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Utilisateur;

@NamedQueries( {
		@NamedQuery(name = "offre.findAll", query = "select offre from Offre offre join fetch offre.lieuDepart join fetch offre.lieuArrivee "
				+ "join fetch offre.utilisateur u left join fetch u.roles left join u.langues left join fetch u.photoUtilisateur join fetch u.coordonnees.lieu residence join fetch residence.pays "
				/*
				 * + "left join fetch offre.bateau " +
				 * "left join fetch offre.bateau.photosBateau "
				 */+ "where offre.valide <> false order by offre.note desc"),
		@NamedQuery(name = "offre.findByBateau", query = "select offre from Offre offre where offre.bateau = :bateau"),
		@NamedQuery(name = "offre.validateByUser", query = "update Offre offre set offre.valide = true where lower(offre.utilisateur) like lower(:pseudo)") })
@Entity
@Table(name = "OFFRE")
public class Offre extends Annonce {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BATEAU", nullable = false)
	@NotNull
	private Bateau bateau;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTILISATEUR", nullable = false)
	@NotNull
	private Utilisateur utilisateur;
	
	@Column(name = "NB_PERSONNES", nullable = false)
	@NotNull
	@Min(1)
	@Max(99)
	private Integer nbPersonnes;
	
	@Column(name = "COMPETENCES", length = 500)
	@Length(max = 500)
	private String competences;
	
    @org.hibernate.annotations.CollectionOfElements
	@JoinTable(name = "POSTES_RECHERCHES", joinColumns = @JoinColumn(name = "OFFRE_ID"))
	@Column(name = "POSTE_RECHERCHE", nullable = false)
	private List<TypePoste> postesRecherches = new ArrayList<TypePoste>(
			0);
	
    @Sort(type = SortType.COMPARATOR, comparator = EscaleComparator.class)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "offre")
	private SortedSet<Escale> escales = new TreeSet<Escale>();

	public Offre() {
		nbPersonnes = 1;
	}

	public Bateau getBateau() {
		return this.bateau;
	}

	public void setBateau(Bateau bateau) {
		this.bateau = bateau;
	}
	
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Integer getNbPersonnes() {
		return this.nbPersonnes;
	}

	public void setNbPersonnes(Integer nbPersonnes) {
		this.nbPersonnes = nbPersonnes;
	}

	public String getCompetences() {
		return this.competences;
	}

	public void setCompetences(String competences) {
		this.competences = competences;
	}

	public List<TypePoste> getPostesRecherches() {
		return this.postesRecherches;
	}

	public void setPostesRecherches(List<TypePoste> postesRecherches) {
		this.postesRecherches = postesRecherches;
	}
	
	public SortedSet<Escale> getEscales() {
		return this.escales;
	}

	public void setEscales(SortedSet<Escale> escales) {
		this.escales = escales;
	}

	public String getObjectType() {
		return ContactManager.OBJECT_TYPE.OFFRE.name();
	}
	public boolean isRenderableOnMap() {
		for (Escale escale : escales) {
			if (!escale.getLieu().isRenderableOnMap()) {
				return false;
			}
		}
		return getLieuDepart().isRenderableOnMap() && getLieuArrivee().isRenderableOnMap();
	}
}
