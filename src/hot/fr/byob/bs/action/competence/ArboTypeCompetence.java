package fr.byob.bs.action.competence;

import java.util.ArrayList;
import java.util.List;

public class ArboTypeCompetence {
				 
	private Long idTypeCompetence;
	private List<ArboCompUtilisateur> competences;
	
	public ArboTypeCompetence(Long idTypeCompetence) {
		super();
		this.idTypeCompetence = idTypeCompetence;
		this.competences = new ArrayList<ArboCompUtilisateur>();
	}
	
	public Long getIdTypeCompetence() {
		return idTypeCompetence;
	}
	

	public List<ArboCompUtilisateur> getCompetences() {
		return competences;
	}
	
	public void setCompetences(List<ArboCompUtilisateur> competences) {
		this.competences = competences;
	}
	
	public void addCompetence(ArboCompUtilisateur competence) {
		this.competences.add(competence);
	}
	
	
	
}
