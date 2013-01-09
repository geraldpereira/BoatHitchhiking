package fr.byob.bs.action.bateau;

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
import fr.byob.bs.model.bateau.Bateau;

@Startup
@Name("bateauHelper")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class BateauHelper extends AbstractBSObjectHelper<Bateau> {

	@Override
	public String getCaption(Bateau instance, Locale locale) {
		return instance.getMarque();
	}

	@Override
	public String getDescription(Bateau instance, Locale locale) {

		String localeStr = locale.getLanguage().toLowerCase();
		StringBuilder sb = new StringBuilder();
		if (instance.getTypeCoque() != null){
			String coque = "typeCoque." + instance.getTypeCoque() + "_" + localeStr;
			sb.append(messages.getString(coque));
		} else {
			sb.append(messages.getString("bateau" + "_" + localeStr));			
		}
		
		if (instance.getGreement() != null) {
			String greement = "greement." + instance.getGreement() + "_" + localeStr;
			sb.append(" (").append(messages.getString(greement)).append(")");
		}
		
		if (instance.getTaille() != null) {
			sb.append(' ').append(messages.getString("global.of" + "_" + localeStr)).append(' ').append(instance.getTaille()).append(' ').append(
					messages.getString("bateau.taille.type" + "_" + localeStr));
		}
		
		sb.append(' ').append(messages.getString("bateau.construit" + "_" + localeStr)).append(' ').append(instance.getAnnee()).append('.');

		if (instance.getInfo() != null) {
			sb.append(' ').append(instance.getInfo());
		}
				
		sb.append(" - ").append(messages.getString("global.maj" + "_" + localeStr)).append(' ').append(DateFormat.getDateFormattedShort(locale, instance.getDateMaj()));
		
		return sb.toString();
	}

	@Override
	public String getURL(Bateau instance, Locale locale) {
		if (FR.equals(locale.getLanguage())) {
			return "/Bateau/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		}else{
			return "/Boat/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
		}
	}

	@Override
	public String getURL(Bateau instance) {
			return "/Bateau/" + instance.getObjectKey() + "-" + escapeURL(instance.getObjectLabel());
	}

	@Override
	public Integer getNote(Bateau instance) {
		int note = 0;
		if (instance.getPhotosBateau() != null) {
			note += instance.getPhotosBateau().size() * 10;
		}
		if (instance.getGreement() != null) {
			note += 10;
		}
		if (instance.getTypeCoque() != null) {
			note += 10;
		}
		if (instance.getVoiles() != null && !instance.getVoiles().isEmpty()) {
			note += 10;
		}
		if (instance.getInfo() != null) { // sur 20 points
			int length = instance.getInfo().length();
			note += 2.65 * Math.log(length + 1);
		}
		if (instance.getEquipGeneral() != null) {
			if (instance.getEquipGeneral().getAnnexe() != null && instance.getEquipGeneral().getAnnexe() == true) {
				note++;
			}
			if (instance.getEquipGeneral().getAutres() != null && !instance.getEquipGeneral().getAutres().isEmpty()) {
				note++;
			}
			if (instance.getEquipGeneral().getContenanceEau() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getContenanceGazoil() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getNbCabines() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getNbDouches() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getNbMoteurs() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getNbWC() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getPuissance() != null) {
				note++;
			}
			if (instance.getEquipGeneral().getEquipGeneralHasEquipements() != null && !instance.getEquipGeneral().getEquipGeneralHasEquipements().isEmpty()) {
				note += 10;
			}
			note++;
		}
		

		Calendar inscription = Calendar.getInstance();
		inscription.setTime(instance.getDateMaj());

		int mois = getMoisAvecAuj(inscription);
		if (mois == 1) {
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
	public List<String> getIndications(Bateau instance) {
		List<String> indications = new ArrayList<String>();
		if (instance.getPhotosBateau() == null || instance.getPhotosBateau().isEmpty()) {
			indications.add(messages.getString("note.addPhoto"));
		}
		if (instance.getGreement() == null) {
			indications.add(messages.getString("note.addGreement"));
		}
		if (instance.getTypeCoque() == null) {
			indications.add(messages.getString("note.addTypeCoque"));
		}
		if (instance.getVoiles() == null && instance.getVoiles().isEmpty()) {
			indications.add(messages.getString("note.addVoiles"));
		}
		if (instance.getInfo() == null   || (5.3 * Math.log(instance.getInfo().length() + 1)) < 10 ) { // sur 20 points
			indications.add(messages.getString("note.addDesc"));
		}
		if (instance.getEquipGeneral() == null) {
			indications.add(messages.getString("note.addEquip"));
		}
		if (indications.size() > 4) {
			return indications.subList(0, 4);
		}
		return indications;
	}
}
