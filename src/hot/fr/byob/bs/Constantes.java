package fr.byob.bs;

import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Startup
@Name("Constantes")
@Scope(ScopeType.APPLICATION)
public class Constantes {
	public final static Locale localeFR = new Locale("fr", "FR");
	public final static Locale localeEN = new Locale("en", "GB");

	public final static String PROP_TMP_PHOTO_LOCAL_PATH = "tmp_photo_local_path";
	public final static String PROP_TMP_PHOTO_URL = "tmp_photo_url";

	public final static String PROP_UTILISATEUR_PHOTO_LOCAL_PATH = "utilisateur_photo_local_path";
	public final static String PROP_UTILISATEUR_PHOTO_URL = "utilisateur_photo_url";

	public final static String PROP_BATEAUX_PHOTO_URL = "bateaux_photo_url";
	public final static String PROP_BATEAUX_PHOTO_LOCAL_PATH = "bateaux_photo_local_path";

	public final static String BASE_PATH_FR = "base_path_fr";
	public final static String BASE_PATH_EN = "base_path_en";
	public final static String RES_URL = "res_url";
	public final static String RES_LOCAL_PATH = "res_url_path";

	public final static String RSS_URL_OFFRE_FR = "url_rss_offre_fr";
	public final static String RSS_URL_DEMANDE_FR = "url_rss_demande_fr";
	public final static String RSS_URL_UTILISATEUR_FR = "url_rss_utilisateur_fr";
	public final static String RSS_URL_TOUT_FR = "url_rss_tout_fr";
	public final static String RSS_URL_OFFRE_EN = "url_rss_offre_en";
	public static final String RSS_URL_DEMANDE_EN = "url_rss_demande_en";
	public final static String RSS_URL_UTILISATEUR_EN = "url_rss_utilisateur_en";
	public static final String RSS_URL_TOUT_EN = "url_rss_tout_en";

	public final static String RSS_EXT_URL_OFFRE_FR = "url_ext_rss_offre_fr";
	public final static String RSS_EXT_URL_DEMANDE_FR = "url_ext_rss_demande_fr";
	public final static String RSS_EXT_URL_UTILISATEUR_FR = "url_ext_rss_utilisateur_fr";
	public final static String RSS_EXT_URL_TOUT_FR = "url_ext_rss_tout_fr";
	public final static String RSS_EXT_URL_OFFRE_EN = "url_ext_rss_offre_en";
	public static final String RSS_EXT_URL_DEMANDE_EN = "url_ext_rss_demande_en";
	public final static String RSS_EXT_URL_UTILISATEUR_EN = "url_ext_rss_utilisateur_en";
	public static final String RSS_EXT_URL_TOUT_EN = "url_ext_rss_tout_en";
	
	public final static String PERSISTED = "persisted";
	public final static String CANCELLED = "cancelled";
	public final static String UPDATED = "updated";
	public final static String REMOVED = "removed";
	public static final String SENT = "sent";
	public final static String HOME = "home";
	public final static String ERREUR_MODIFICATION = "erreurModification";
	public final static String ERREUR_AJOUT = "erreurAjout";
	public final static String ERREUR_SUPPRESION = "erreurSuppression";
	
	

}
