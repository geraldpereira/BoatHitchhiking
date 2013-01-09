package fr.byob.bs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;

import fr.byob.bs.Constantes;
import fr.byob.bs.el.func.SeamFunc;
import fr.byob.bs.model.Propriete;

@Startup
@Name("proprieteFactory")
@Scope(ScopeType.APPLICATION)
public class ProprieteFactory {

	@Logger
	private Log log;

	@In
	private Session hibernateSession;

	@Out
	public HashMap<String, String> proprietes;

	@Factory(autoCreate = true, value = "proprietes")
	public HashMap<String, String> getProprietes() {
		log.info("Factoring props list");
		List<Propriete> proprieteList = hibernateSession.getNamedQuery("propriete.findAll").list();
		proprietes = new HashMap<String, String>(proprieteList.size());
		for (Propriete curProp : proprieteList) {
			proprietes.put(curProp.getCle(), curProp.getValeur());
		}
		return proprietes;
	}

	public String getBasePath() {
		if (proprietes == null || proprietes.get(Constantes.BASE_PATH_FR) == null) {
			getProprietes();
		}
		String url = SeamFunc.getUrl();
		// log.info(url);
		// log.info(proprietes);
		// log.info(proprietes.get(Constantes.BASE_PATH_FR));
		if (url != null && url.startsWith(proprietes.get(Constantes.BASE_PATH_FR))) {
			return proprietes.get(Constantes.BASE_PATH_FR);
		} else {
			return proprietes.get(Constantes.BASE_PATH_EN);
		}
	}

	public String getBasePath(Locale locale) {
		if (locale == null) {
			return getBasePath("fr");
		}
		return getBasePath(locale.getLanguage().toString());
	}

	public String getBasePath(String locale) {
		if (proprietes == null) {
			getProprietes();
		}
		if ("fr".equals(locale)) {
			return proprietes.get(Constantes.BASE_PATH_FR);
		} else {
			return proprietes.get(Constantes.BASE_PATH_EN);
		}
	}
	
	public String getGMapsKey() {
		if (proprietes == null) {
			getProprietes();
		}
		return proprietes.get("gmap_key_fr");
	}
	public String getRessource() {
		if (proprietes == null) {
			getProprietes();
		}
		return proprietes.get(Constantes.RES_URL);
	}

	public String getRessourcePath() {
		if (proprietes == null) {
			getProprietes();
		}
		return proprietes.get(Constantes.RES_LOCAL_PATH);
	}

	public String getRssUrlOffre(Locale locale) {
		return getRssUrlOffre(locale.getLanguage().toString());
	}

	public String getRssUrlOffre(String locale) {
		if (proprietes == null) {
			getProprietes();
		}
		if ("fr".equals(locale)) {
			return proprietes.get(Constantes.RSS_URL_OFFRE_FR);
		} else {
			return proprietes.get(Constantes.RSS_URL_OFFRE_EN);
		}
	}

	public String getRssUrlDemande(Locale locale) {
		return getRssUrlDemande(locale.getLanguage().toString());
	}

	public String getRssUrlDemande(String locale) {
		if (proprietes == null) {
			getProprietes();
		}
		if ("fr".equals(locale)) {
			return proprietes.get(Constantes.RSS_URL_DEMANDE_FR);
		} else {
			return proprietes.get(Constantes.RSS_URL_DEMANDE_EN);
		}
	}

	public String getRssUrlUtilisateur(Locale locale) {
		return getRssUrlUtilisateur(locale.getLanguage().toString());
	}

	public String getRssUrlUtilisateur(String locale) {
		if (proprietes == null) {
			getProprietes();
		}
		if ("fr".equals(locale)) {
			return proprietes.get(Constantes.RSS_URL_UTILISATEUR_FR);
		} else {
			return proprietes.get(Constantes.RSS_URL_UTILISATEUR_EN);
		}
	}

	public String getRssUrlTout(Locale locale) {
		return getRssUrlTout(locale.getLanguage().toString());
	}

	public String getRssUrlTout(String locale) {
		if (proprietes == null) {
			getProprietes();
		}
		if ("fr".equals(locale)) {
			return proprietes.get(Constantes.RSS_URL_TOUT_FR);
		} else {
			return proprietes.get(Constantes.RSS_URL_TOUT_EN);
		}
	}
}
