package fr.byob.bs.action.utilisateur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.AbstractBSObjectHelper;
import fr.byob.bs.DateFormat;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Startup
@Name("utilisateurHelper")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class UtilisateurHelper extends AbstractBSObjectHelper<Utilisateur> {
	

	@Override
	public String getCaption(Utilisateur instance, Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append(messages.getString("global.de_" + locale.getLanguage().toLowerCase())).append(' ');
		appendLieu(sb, instance.getCoordonnees().getLieu(), locale);
		return sb.toString();
	}

	@Override
	public String getDescription(Utilisateur instance, Locale locale) {
		
		StringBuilder sb = new StringBuilder();

		if (instance.getDescription() != null) {
			sb.append(instance.getDescription()).append(" - ");
		}
		String global = messages.getString("global.maj_" + locale.getLanguage().toLowerCase());
		String date = DateFormat.getDateFormattedShort(locale, instance.getDateInscription());
		sb.append(global + " ").append(date);
		return sb.toString();
	}

	@Override
	public String getURL(Utilisateur instance) {
			return "/Utilisateur/" + instance.getObjectKey();
	}

	@Override
	public String getURL(Utilisateur instance, Locale locale) {
		if (FR.equals(locale.getLanguage())) {
			return "/Utilisateur/" + instance.getObjectKey();
		} else {
			return "/User/" + instance.getObjectKey();
		}
	}

	@Override
	public Integer getNote(Utilisateur instance) {
		int note = 0;
		if (instance.getPhotoUtilisateur() != null) {
			note += 20;
		}
		if (instance.getCompetences() != null && !instance.getCompetences().isEmpty()) {
			note += 8;
		}
		if (instance.getDescription() != null) { // sur 30 points
			int length = instance.getDescription().length();
			note += 3.8 * Math.log(length + 1);
		}
		if (instance.getXps() != null && !instance.getXps().isEmpty()) {
			note += 8;
		}
		if (instance.getLangues() != null && !instance.getLangues().isEmpty()) {
			note += 8;
		}
		if (instance.getDateNaissance() != null) {
			note += 8;
		}
		if (instance.getSexe() != null) {
			note += 8;
		}
		if (instance.getXpGenerale() != null) {
			note += 10;
		}

		Calendar inscription = Calendar.getInstance();
		inscription.setTime(instance.getDateInscription());

		int mois = getMoisAvecAuj(inscription);
		if (mois <= 1) {
			note = note - note * 10 / 100;
		} else if (mois == 2) {
			note = note - note * 20 / 100;
		} else if (mois > 2) {
			note = note - note * 30 / 100;
		}
		if (note > 100) {
			note = 100;
		}
		return note;
	}
	
	@Override
	public List<String> getIndications(Utilisateur instance) {
		List<String> indications = new ArrayList<String>();
			if (instance.isActif()) {
				if (instance.getPhotoUtilisateur() == null) {
					indications.add(messages.getString("note.addPhoto"));
				}
			if (instance.getCompetences() == null || instance.getCompetences().isEmpty()) {
					indications.add(messages.getString("note.addCompetences"));
				}
				if (instance.getDescription() == null || (5.3 * Math.log(instance.getDescription().length() + 1)) < 15 ) { // sur 30 points
				indications.add(messages.getString("note.addDescription"));
				}
			if (instance.getXps() == null || instance.getXps().isEmpty()) {
					indications.add(messages.getString("note.addXp"));
				}
			if (instance.getLangues() == null || instance.getLangues().isEmpty()) {
					indications.add(messages.getString("note.addLangue"));
				}
				if (instance.getDateNaissance() == null) {
					indications.add(messages.getString("note.addDateNaissance"));
				}
				if (instance.getSexe() == null) {
					indications.add(messages.getString("note.addSexe"));
				}
				if (instance.getXpGenerale() == null) {
					indications.add(messages.getString("note.addXPG"));
				}

			}
			if (indications.size() > 4) {
			return indications.subList(0, 4);
		}
		return indications;
	}
}
