package fr.byob.bs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.ProprieteFactory;
import fr.byob.bs.action.annonce.DemandeHelper;
import fr.byob.bs.action.annonce.OffreHelper;
import fr.byob.bs.action.bateau.BateauHelper;
import fr.byob.bs.action.utilisateur.UtilisateurHelper;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;
import fr.byob.bs.model.bateau.Bateau;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Startup
@Name("rssObserver")
@Scope(ScopeType.APPLICATION)
public class RssObserver {
	
	protected final static ResourceBundle messages = SeamResourceBundle.getBundle();
	
	@Logger
	private transient Log log;
	@In(required = false, create = true)
	OffreHelper offreHelper;

	@In(required = false, create = true)
	DemandeHelper demandeHelper;

	@In(required = false, create = true)
	UtilisateurHelper utilisateurHelper;

	@In(required = false, create = true)
	BateauHelper bateauHelper;

	@In
	private ProprieteFactory proprieteFactory;

	@Observer("offreAdded")
	synchronized public void recordOffre(Offre offre) {
		recordOffre(offre, Constantes.localeFR);
		recordOffre(offre, Constantes.localeEN);
	}

	private void recordOffre(Offre offre, Locale locale) {
		String resourcePath = proprieteFactory.getRessourcePath();
		String title = messages.getString("rss.nouvelleOffre_" + locale.getLanguage().toLowerCase()) + offreHelper.getTitle(offre);
		String url = proprieteFactory.getBasePath(locale) + offreHelper.getURL(offre, locale);
		String caption = offreHelper.getCaption(offre, locale);
		String description = offreHelper.getDescription(offre, locale);
		String texte = caption + "\n" + description;

		addFluxRss(resourcePath + proprieteFactory.getRssUrlOffre(locale), title, url, texte);
		addFluxRss(resourcePath + proprieteFactory.getRssUrlTout(locale), title, url, texte);
	}

	@Observer("demandeAdded")
	synchronized public void recordDemande(Demande demande) {
		recordDemande(demande, Constantes.localeFR);
		recordDemande(demande, Constantes.localeEN);
	}

	public void recordDemande(Demande demande, Locale locale) {
		String resourcePath = proprieteFactory.getRessourcePath();
		String title = messages.getString("rss.nouvelleDemande_" + locale.getLanguage().toLowerCase()) + demandeHelper.getTitle(demande);
		String url = proprieteFactory.getBasePath(locale) + demandeHelper.getURL(demande, locale);
		String caption = demandeHelper.getCaption(demande, locale);
		String description = demandeHelper.getDescription(demande, locale);
		String texte = caption + "\n" + description;

		addFluxRss(resourcePath + proprieteFactory.getRssUrlDemande(locale), title, url, texte);
		addFluxRss(resourcePath + proprieteFactory.getRssUrlTout(locale), title, url, texte);
	}

	@Observer("utilisateurAdded")
	synchronized public void recordUtilisateur(Utilisateur utilisateur) {
		recordUtilisateur(utilisateur, Constantes.localeFR);
		recordUtilisateur(utilisateur, Constantes.localeEN);
	}

	public void recordUtilisateur(Utilisateur utilisateur, Locale locale) {
		String resourcePath = proprieteFactory.getRessourcePath();
		String title = messages.getString("rss.nouvelUtilisateur_" + locale.getLanguage().toLowerCase()) + utilisateurHelper.getTitle(utilisateur);
		String url = proprieteFactory.getBasePath(locale) + utilisateurHelper.getURL(utilisateur, locale);
		String caption = utilisateurHelper.getCaption(utilisateur, locale);
		String description = utilisateurHelper.getDescription(utilisateur, locale);
		String texte = caption + "\n" + description;

		addFluxRss(resourcePath + proprieteFactory.getRssUrlUtilisateur(locale), title, url, texte);
		addFluxRss(resourcePath + proprieteFactory.getRssUrlTout(locale), title, url, texte);
	}

	@Observer("bateauAdded")
	synchronized public void recordBateau(Bateau bateau) {
		recordBateau(bateau, Constantes.localeFR);
		recordBateau(bateau, Constantes.localeEN);
	}

	public void recordBateau(Bateau bateau, Locale locale) {
		String resourcePath = proprieteFactory.getRessourcePath();
		String newB = "rss.nouveauBateau_" + locale.getLanguage().toLowerCase();
		String title = messages.getString(newB) + bateauHelper.getTitle(bateau);
		String url = proprieteFactory.getBasePath(locale) + bateauHelper.getURL(bateau, locale);
		String caption = bateauHelper.getCaption(bateau, locale);
		String description = bateauHelper.getDescription(bateau, locale);
		String texte = caption + "\n" + description;

		addFluxRss(resourcePath + proprieteFactory.getRssUrlTout(locale), title, url, texte);
	}

	private void addFluxRss(String urlFile, String title, String url, String description) {
		FileInputStream fin;
		String content = "";
		try {
			fin = new FileInputStream(urlFile);
			BufferedReader read = new BufferedReader(new InputStreamReader(fin));
			while (read.ready()) {
				String text = read.readLine();
				if (text != null && !text.contains("<atom:link")) {
					content += text + "\n";
				} else {
					SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", new Locale("en"));
					content += text + "\n";
					content += "<item>\n" + "<title>" + title + "</title>\n" + "<guid>" + url + "</guid>\n" + "<link>" + url + "</link>\n" + "<pubDate>" + format.format(new Date())
							+ " +0000</pubDate>\n" + "<description>" + description + "</description>\n" + "</item>\n";

				}
			}
			fin.close();
			FileOutputStream fout;
			fout = new FileOutputStream(urlFile);
			new PrintStream(fout).println(content);
			fout.close();
			log.info("Enregistrement du flux RSS : " + urlFile + " avec le titre : " + title);
		} catch (IOException e) {
			log.error("Unable to read from file " + urlFile);
			throw new BSException(e);
		}
	}

}
