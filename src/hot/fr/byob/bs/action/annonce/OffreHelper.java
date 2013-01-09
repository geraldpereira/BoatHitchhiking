package fr.byob.bs.action.annonce;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.annonce.Offre;

@Startup
@Name("offreHelper")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class OffreHelper extends AnnonceHelper<Offre> {


	@Override
	public String getURL(Offre instance, Locale locale) {
		if (FR.equals(locale.getLanguage())) {
			return "/Offre/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		} else {
			return "/Offer/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		}
	}

	@Override
	public String getURL(Offre instance) {
			return "/Offre/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
	}

	@Override
	public Integer getNote(Offre instance) {
		int note = 0;
		Calendar auj = Calendar.getInstance();
		if (instance.getDescription() != null) { // sur 40 points
			int length = instance.getDescription().length();
			note += 5.3 * Math.log(length + 1);
		}
		if (instance.getTypesNav() != null) {
			note += 15;
		}
		if (instance.getCompetences() != null) { // sur 20 points
			int length = instance.getCompetences().length();
			note += 3.25 * Math.log(length + 1);
		}
		if (instance.getPostesRecherches() != null && !instance.getPostesRecherches().isEmpty()) {
			note += 15;
		}
		if (instance.getEscales() != null && !instance.getEscales().isEmpty()) {
			note += 10;
		}
		if (instance.getBateau() != null) {
			note = note + (instance.getBateau().getNote() / 2);
		}
		if (instance.getUtilisateur() != null) {
			note = note + (instance.getUtilisateur().getNote() / 2);
		}
		note = note / 2;

		Calendar dateFin = Calendar.getInstance();
		dateFin.setTime(instance.getDateFin());
		Calendar dateDebut = Calendar.getInstance();
		dateDebut.setTime(instance.getDateDebut());
		if( auj.after(dateFin)){
			note = 0;
		}
		if( auj.after(dateDebut)){
			note = note - note * 60 / 100;
		}

		Calendar maj = Calendar.getInstance();
		maj.setTime(instance.getDateMaj());
		int mois = getMoisAvecAuj(maj);
		if (mois != 0) {
			note = note - note * mois * 10 / 100;
		}
		if (note > 100) {
			note = 100;
		}
		if (note < 0) {
			note = 0;
		}
		return note;
	}

	
	@Override
	public List<String> getIndications(Offre instance) {
		List<String> indications = new ArrayList<String>();
		Calendar auj = Calendar.getInstance();

		if (instance.getDateDebut() != null) {
			Calendar dateDebut = Calendar.getInstance();
			dateDebut.setTime(instance.getDateDebut());
			if (auj.after(dateDebut)) {
				indications.add(messages.getString("note.passedDateDeb"));
			}
		}

		if (instance.getDateFin() != null) {
			Calendar dateFin = Calendar.getInstance();
			dateFin.setTime(instance.getDateFin());
			if (auj.after(dateFin)) {
				indications.add(messages.getString("note.passedDateFin"));
			}
		}

		if (instance.getDescription() == null  || (5.3 * Math.log(instance.getDescription().length() + 1)) < 20 ) { // sur 40 points
			indications.add(messages.getString("note.addDesc"));
		}
		if (instance.getTypesNav() == null || instance.getTypesNav().isEmpty() ) {
			indications.add(messages.getString("note.addTypeNav"));
		}
		if (instance.getCompetences() == null || (3.25 * Math.log(instance.getCompetences().length() + 1)) < 10) { // sur
																													// 20
																													// points
			indications.add(messages.getString("note.addComp"));
		}
		if (instance.getPostesRecherches() == null || instance.getPostesRecherches().isEmpty()) {
			indications.add(messages.getString("note.addPoste"));
		}
		if (instance.getEscales() == null || instance.getEscales().isEmpty()) {
			indications.add(messages.getString("note.addEscales"));
		}
		if (instance.getBateau() != null && instance.getBateau().getNote() < 50) {
			indications.add(messages.getString("note.addBateau"));
		}
		if (instance.getUtilisateur() != null && instance.getUtilisateur().getNote() < 50) {
			indications.add(messages.getString("note.addUser"));
		}
		if (indications.size() > 4) {
			return indications.subList(0, 4);
		}
		return indications;
	}
}
