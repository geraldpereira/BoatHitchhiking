package fr.byob.bs.action;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Roles;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.byob.bs.BSException;
import fr.byob.bs.model.Lieu;
import fr.byob.bs.model.Pays;

/**
 * 
 * Lors de l'ajout d'un lieu, si un identique existe déjà, un nouveau n'est PAS
 * créé. De même si l'on modifie un lieu, il n'est pas directement modifié mais
 * un nouveau est créé.
 * 
 * @author Kojiro
 * 
 */
@Name("lieuHome")
@Scope(ScopeType.CONVERSATION)
@Roles( { @Role(name = "lieuDepart", scope = ScopeType.CONVERSATION), @Role(name = "lieuArrivee", scope = ScopeType.CONVERSATION) })
public class LieuHome /* extends BSEntityHome<Lieu> */implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;

	@In
	private Session hibernateSession;

	@In
	private Locale locale;
	
	@In(create = true)
	private List<Pays> pays;
	
	@In(create = true)
	private HashMap<Long, Pays> paysMap;

	private Lieu instance;

	@In(required = true, create = true)
	public long dummyPlace;
	
	public Session getSession() {
		return this.hibernateSession;
	}

	public void setSession(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}

	public Lieu getInstance() {
		return this.instance;
	}

	public void setInstance(Lieu instance) {
		this.instance = instance;
	}

	private LieuSelectionManager lieuSelectionManager; 
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setPaysMap(HashMap<Long, Pays> paysMap) {
		this.paysMap = paysMap;
	}

	public void setPays(List<Pays> pays) {
		this.pays = pays;
	}

	public void wire(boolean required, Lieu lieu) {
		setInstance(lieu);
		wire(required);
	}

	public void wire(boolean required) {
		// .setRequired(required);
		if (getInstance() == null) {
			setInstance(new Lieu());
		}

		if (getInstance().getPays() == null && required) {
			// Initialiser le pays en fonction de la locale : france ou
			// angleterre (ou au premier de la liste sinon)
			if ("en".equals(locale.toString())) {
				getInstance().setPays(paysMap.get(PaysFactory.ID_USA));
			} else if ("fr".equals(locale.toString())) {
				getInstance().setPays(paysMap.get(PaysFactory.ID_FRANCE));
			} else {
				getInstance().setPays(pays.get(0));
			}
		}
		
		lieuSelectionManager = new LieuSelectionManager(getSession(), required, getInstance().getPays(), getInstance().getVille()) {

					@Override
			public Pays getPays() {
				return getInstance().getPays();
			}

			@Override
			public String getVille() {
				return getInstance().getVille();
			}

			@Override
			public void setPays(Pays pays) {
				getInstance().setPays(pays);
			}

					@Override
			public void setVille(String ville) {
				getInstance().setVille(ville);
			}
		
		};
	}

	

	/**
	 * Retourne le lieu, un nouveau si besoin, un déjà existant en base sinon
	 * 
	 * @return
	 */
	public Lieu getLieu() {
		Lieu lieu = null;
		try {
			
			if (getInstance().getPays() == null && (getInstance().getVille() == null || getInstance().getVille().isEmpty())) {
				// Pas de lieu sélectionné
				return null;
			} else if (getInstance().getVille() == null) {
				lieu = (Lieu) getSession().getNamedQuery("lieu.findByPays").setParameter("pays", getInstance().getPays()).setParameter("placeDummy", dummyPlace).uniqueResult();
			} else {
				lieu = (Lieu) getSession().getNamedQuery("lieu.findByPaysAndVille").setParameter("ville", getInstance().getVille()).setParameter("pays", getInstance().getPays()).setParameter(
						"placeDummy", dummyPlace).uniqueResult();
			}
			if (lieu == null) {
				// Si le lieu n'est pas connu, une nouvelle instance est
				// retournée
				lieu = new Lieu();
				lieu.setPays(getInstance().getPays());
				lieu.setVille(getInstance().getVille());
				
				try {
					geocoder(lieu);
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("Erreur geocoding {0} {1}", ex, lieu.getPays(), lieu.getVille());
				}
				
				setInstance(lieu);

				// Let hibernate handle the Place persistance
				getSession().persist(getInstance());
				getSession().flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BSException("error.lieu.create", e);
		}
		return lieu;
	}

	/*  Accesseurs */

	public LieuSelectionManager getLieuSelectionManager() {
		return lieuSelectionManager;
	}
	
	public void geocoder(Lieu lieu) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {

		String GEOCODE_REQUEST_PREFIX = "http://maps.google.com/maps/api/geocode/xml";

		String inputQuery = lieu.getVille() + " " + lieu.getPays().getLibelle();
		String urlString = null;

		urlString = GEOCODE_REQUEST_PREFIX + "?address=" + URLEncoder.encode(inputQuery, "UTF-8") + "&sensor=false";

		// Convert the string to a URL so we can parse it
		URL url = new URL(urlString);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		Document geocoderResultDocument = null;
		try {
			// open the connection and get results as InputSource.
			conn.connect();
			InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

			// read result and parse into XML Document
			geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
		} finally {
			conn.disconnect();
		}

		NodeList list = geocoderResultDocument.getElementsByTagName("location");
		for (int index = 0; index < list.getLength(); index++) {
			Node node = list.item(index);
			if (node instanceof Element) {
				Element e = ((Element) node);
				NodeList latList = e.getElementsByTagName("lat");
				float latitude = Float.valueOf(((Element) latList.item(0)).getTextContent());
				NodeList lngList = e.getElementsByTagName("lng");
				float longitude = Float.valueOf(((Element) lngList.item(0)).getTextContent());
				lieu.setLatitude(latitude);
				lieu.setLongitude(longitude);
			}
		}
	}
}
