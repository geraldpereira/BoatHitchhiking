package fr.byob.bs;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Scope(ScopeType.APPLICATION)
@Name("rssManager")
@Install(value = false)
public class RssManager {

	

	private String adresseMSNRss;
	private String adresseGoogleRss;
	private String adresseYahooRss;
	private String adresseNetvibes;

	private String imgXMLRss;
	private String imgMSNRss;
	private String imgGoogleRss;
	private String imgYahooRss;
	private String imgNetvibesRss;

	public String getAdresseMSNRss() {
		return this.adresseMSNRss;
	}

	public String getAdresseGoogleRss() {
		return this.adresseGoogleRss;
	}

	public String getAdresseYahooRss() {
		return this.adresseYahooRss;
	}

	public String getAdresseNetvibes() {
		return this.adresseNetvibes;
	}

	public String getImgXMLRss() {
		return this.imgXMLRss;
	}

	public String getImgMSNRss() {
		return this.imgMSNRss;
	}

	public String getImgGoogleRss() {
		return this.imgGoogleRss;
	}

	public String getImgYahooRss() {
		return this.imgYahooRss;
	}

	public String getImgNetvibesRss() {
		return this.imgNetvibesRss;
	}

	
	
}
