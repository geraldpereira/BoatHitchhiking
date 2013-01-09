package fr.byob.bs.action.annonce;

import java.util.Date;
import java.util.Locale;

import fr.byob.bs.AbstractBSObjectHelper;
import fr.byob.bs.DateFormat;
import fr.byob.bs.model.Lieu;
import fr.byob.bs.model.annonce.Annonce;

public abstract class AnnonceHelper<T extends Annonce> extends AbstractBSObjectHelper<T> {

	public String getJSTitle(T instance) {
		return escapeURL(super.getTitle(instance));
	}

	@Override
	public String getCaption(T instance, Locale locale) {
		String localeStr = locale.getLanguage().toLowerCase();
		
		StringBuilder sb = new StringBuilder();
		// Depart
		Lieu lieuDepart = instance.getLieuDepart();
		Date dateDebut = instance.getDateDebut();
		if (lieuDepart != null) {
			sb.append(messages.getString("global.de" + "_" + localeStr)).append(' ');
			appendLieu(sb, lieuDepart,locale);
		}
		if (dateDebut != null) {
			if (lieuDepart == null) {
				sb.append(messages.getString("annonce.depart" + "_" + localeStr)).append(' ');
			} else {
				sb.append(' ');
			}
			sb.append(messages.getString("global.le" + "_" + localeStr)).append(' ');
			sb.append(DateFormat.getDateFormattedShort(locale, dateDebut) + " ");
		}
		
		// Arrivée
		Lieu lieuArrivee = instance.getLieuArrivee();
		Date dateFin = instance.getDateFin();
		if (lieuArrivee != null) {
			sb.append(messages.getString("global.a" + "_" + localeStr)).append(' ');
			appendLieu(sb, lieuArrivee,locale);
		}
		if (dateFin != null) {
			if (lieuArrivee == null) {
				sb.append(messages.getString("annonce.arrivee" + "_" + localeStr)).append(' ');
			} else {
				sb.append(' ');
			}
			sb.append(messages.getString("global.le" + "_" + localeStr)).append(' ');
			sb.append(DateFormat.getDateFormattedShort(locale, dateFin));
		}
		return sb.toString();
	}

	@Override
	public String getDescription(T instance, Locale locale) {
		StringBuilder sb = new StringBuilder();
		
		if (instance.getDescription() != null) {
			sb.append(instance.getDescription()).append(" - ");
		}
		sb.append(" - ").append(messages.getString("global.maj" + "_" + locale.getLanguage().toLowerCase())).append(' ').append(DateFormat.getDateFormattedShort(locale, instance.getDateMaj()));
		return sb.toString();
	}

}
