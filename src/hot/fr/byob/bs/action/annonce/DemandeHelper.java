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
import fr.byob.bs.model.annonce.Demande;

@Startup
@Name("demandeHelper")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class DemandeHelper extends AnnonceHelper<Demande> {

	@Override
	public String getURL(Demande instance, Locale locale) {
		if (FR.equals(locale.getLanguage())) {
			return "/Demande/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		} else {
			return "/Request/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		}
	}

	@Override
	public String getURL(Demande instance) {
			return "/Demande/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
	}

	@Override
	public Integer getNote(Demande instance) {
		int note = 0;
		Calendar auj = Calendar.getInstance();
		if (instance.getDateDebut() != null) {
			note += 5;
		}
		if (instance.getDateFin() != null) {
			note += 5;
		}
		if (instance.getLieuDepart() != null) {
			note += 15;
		}
		if (instance.getLieuArrivee() != null) {
			note += 15;
		}
		if (instance.getDescription() != null) { // sur 40 points
			int length = instance.getDescription().length();
			note += 5.3 * Math.log(length + 1);
		}
		if (instance.getTypesNav() != null) {
			note += 10;
		}
		if (instance.getTypeCoque() != null) {
			note += 10;
		}
		if (instance.getUtilisateur() != null) {
			note = note + (instance.getUtilisateur().getNote() / 2);
		}
		note = (note * 10) / 15;
		
		if (instance.getDateFin() != null) {
			Calendar dateFin = Calendar.getInstance();
			dateFin.setTime(instance.getDateFin());
			if (auj.after(dateFin)) {
				note = 0;
			}
		}
		if (instance.getDateDebut() != null) {
			Calendar dateDebut = Calendar.getInstance();
			dateDebut.setTime(instance.getDateDebut());
			if (auj.after(dateDebut)) {
				note = note - note * 60 / 100;
			}
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
	public List<String> getIndications(Demande instance) {
		Calendar auj = Calendar.getInstance();

		List<String> indications = new ArrayList<String>();
		if (instance.getDateDebut() == null) {
			indications.add(messages.getString("note.addDateDeb"));
		} else {
			Calendar dateDebut = Calendar.getInstance();
			dateDebut.setTime(instance.getDateDebut());
			if (auj.after(dateDebut)) {
				indications.add(messages.getString("note.passedDateDeb"));
			}
		}
		if (instance.getDateFin() == null) {
			indications.add(messages.getString("note.addDateFin"));
		} else {
			Calendar dateFin = Calendar.getInstance();
			dateFin.setTime(instance.getDateFin());
			if (auj.after(dateFin)) {
				indications.add(messages.getString("note.passedDateFin"));
			}
		}
		if (instance.getLieuDepart() == null) {
			indications.add(messages.getString("note.addLieuDeb"));
		}
		if (instance.getLieuArrivee() == null) {
			indications.add(messages.getString("note.addLieuFin"));
		}
		if (instance.getDescription() == null || (5.3 * Math.log(instance.getDescription().length() + 1)) < 20 ) { // sur 40 points
			indications.add(messages.getString("note.addDesc"));
		}
		if (instance.getTypesNav() == null || instance.getTypesNav().isEmpty()) {
			indications.add(messages.getString("note.addTypeNav"));
		}
		if (instance.getTypeCoque() == null) {
			indications.add(messages.getString("note.addTypeCoque"));
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
