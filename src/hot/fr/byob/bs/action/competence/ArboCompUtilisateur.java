package fr.byob.bs.action.competence;

import fr.byob.bs.model.competence.Competence;

public class ArboCompUtilisateur {
	
	private Competence competence; 
	private int niveau;
	
	public ArboCompUtilisateur(Competence competence,int niveau) {
		super();
		this.competence = competence;
		this.niveau = niveau;
	}

	public Competence getCompetence() {
		return competence;
	}
	public int getNiveau() {
		return niveau;
	}
	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}
	
	
}
