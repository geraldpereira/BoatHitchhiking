package fr.byob.bs;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.seam.core.SeamResourceBundle;

import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.Lieu;

public abstract class AbstractBSObjectHelper<T extends BSEntity> implements BSEntityHelper<T>{

	protected final static ResourceBundle messages = SeamResourceBundle.getBundle();
	
	@Override
	public String getTitle(T instance) {
		return instance.getObjectLabel();
	}
	
	protected void appendLieu(StringBuilder sb, Lieu lieu, Locale locale) {
		if (lieu != null && lieu.getPays() != null) {
			String idPays = "pays." + lieu.getPays().getIdPays() + "_" + locale.getLanguage().toLowerCase();
			String pays = messages.getString(idPays);
			if (lieu.getVille() != null && !lieu.getVille().isEmpty()) {
				sb.append(lieu.getVille()).append(" (").append(pays).append(')');
			} else {
				sb.append(pays);
			}
		}
	}
	protected int getMoisAvecAuj(Calendar date) {
		Calendar auj = Calendar.getInstance();
		return (int) (auj.getTimeInMillis() - date.getTimeInMillis()) / 1000 / 60 / 60 / 24;
	}

	public static String escapeURL(String url) {
		String escaped = BSUtils.normalize(url);
		return escaped;
	}
	public abstract List<String> getIndications(T instance);
}
