package fr.byob.bs.action.utilisateur.messagerie;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;

import fr.byob.bs.BSEntityList;
import fr.byob.bs.BSEntityView;
import fr.byob.bs.BSException;
import fr.byob.bs.Constantes;
import fr.byob.bs.ConversationUtils;
import fr.byob.bs.action.ProprieteFactory;
import fr.byob.bs.action.annonce.DemandeList;
import fr.byob.bs.action.annonce.DemandeView;
import fr.byob.bs.action.annonce.OffreList;
import fr.byob.bs.action.annonce.OffreView;
import fr.byob.bs.action.bateau.BateauList;
import fr.byob.bs.action.bateau.BateauView;
import fr.byob.bs.action.utilisateur.UtilisateurList;
import fr.byob.bs.action.utilisateur.UtilisateurView;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.utilisateur.Utilisateur;
import fr.byob.bs.model.utilisateur.messagerie.Conversation;
import fr.byob.bs.model.utilisateur.messagerie.Message;

@Name("contactManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class ContactManager {

	@In
	private Session hibernateSession;

	@In(create = true)
	private ContactEmailManager contactEmailManager;

	@In
	private ProprieteFactory proprieteFactory;

	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;

	@In(create = false, required = false)
	protected UtilisateurList utilisateurList;

	@In(create = false, required = false)
	protected OffreList offreList;

	@In(create = false, required = false)
	protected DemandeList demandeList;

	@In(create = false, required = false)
	protected BateauList bateauList;

	@In(create = false, required = false)
	protected OffreView offreView;

	@In(create = false, required = false)
	protected DemandeView demandeView;

	@In(create = false, required = false)
	protected BateauView bateauView;

	@In(create = false, required = false)
	protected UtilisateurView utilisateurView;

	@In
	ResourceBundle resourceBundle;

	@In
	private FacesMessages facesMessages;

	@In
	Locale locale;

	private final Email mail = new Email();

	private Utilisateur destinataire;

	private String objectType;
	// For lists only
	private Integer objectRow;

	public enum OBJECT_TYPE {
		OFFRE {
			@Override
			public String getTextKey() {
				return "contact.offre.text";
			}

			@Override
			public String getTitleKey() {
				return "contact.offre.title";
			}

			@Override
			public BSEntityView<? extends BSEntity> getHome(ContactManager manager) {
				return manager.offreView;
			}

			@Override
			public BSEntityList<? extends BSEntity> getList(ContactManager manager) {
				return manager.offreList;
			}
		},
		DEMANDE {
			@Override
			public String getTextKey() {
				return "contact.demande.text";
			}

			@Override
			public String getTitleKey() {
				return "contact.demande.title";
			}

			@Override
			public BSEntityView<? extends BSEntity> getHome(ContactManager manager) {
				return manager.demandeView;
			}

			@Override
			public BSEntityList<? extends BSEntity> getList(ContactManager manager) {
				return manager.demandeList;
			}
		},
		BATEAU {
			@Override
			public String getTextKey() {
				return "contact.bateau.text";
			}

			@Override
			public String getTitleKey() {
				return "contact.bateau.title";
			}

			@Override
			public BSEntityView<? extends BSEntity> getHome(ContactManager manager) {
				return manager.bateauView;
			}

			@Override
			public BSEntityList<? extends BSEntity> getList(ContactManager manager) {
				return manager.bateauList;
			}
		},
		UTILISATEUR {
			@Override
			public String getTextKey() {
				return "contact.utilisateur.text";
			}

			@Override
			public String getTitleKey() {
				return "contact.utilisateur.title";
			}

			@Override
			public BSEntityView<? extends BSEntity> getHome(ContactManager manager) {
				return manager.utilisateurView;
			}

			@Override
			public BSEntityList<? extends BSEntity> getList(ContactManager manager) {
				return manager.utilisateurList;
			}
		};

		public String getObjectLabel(ContactManager manager, BSEntityView<? extends BSEntity> home, BSEntityList<? extends BSEntity> list) {
			if (home != null && manager.getObjectRow() == null) {
				return home.getInstance().getObjectLabel();
			} else if (list != null && manager.getObjectRow() != null) {
				return list.getResultList().get(manager.getObjectRow()).getObjectLabel();
			}
			return null;
		}

		public String getObjectKey(ContactManager manager, BSEntityView<? extends BSEntity> home, BSEntityList<? extends BSEntity> list) {
			if (home != null && manager.getObjectRow() == null) {
				return home.getInstance().getObjectKey();
			} else if (list != null && manager.getObjectRow() != null) {
				return list.getResultList().get(manager.getObjectRow()).getObjectKey();
			}
			return null;
		}

		public abstract String getTitleKey();

		public abstract String getTextKey();

		public abstract BSEntityView<? extends BSEntity> getHome(ContactManager manager);

		public abstract BSEntityList<? extends BSEntity> getList(ContactManager manager);
	}

	public String send() {
		// Anti bug !
		antiBugSeamTextAndRichEditor();

		if (this.isMP()) {
			return sendMP();
		} else {
			return sendMail();
		}
	}

	public void antiBugSeamTextAndRichEditor() {
		if (!Constantes.localeFR.getLanguage().equals(mail.getLocale())) {
			return;
		}

		String path = proprieteFactory.getBasePath(mail.getLocale());
		
		if (objectType == null) {
			return;
		}
		OBJECT_TYPE type = OBJECT_TYPE.valueOf(objectType);
		if (type == null) {
			return;
		}
		// Init object label and object key
		BSEntityView<? extends BSEntity> view = type.getHome(this);
		BSEntityList<? extends BSEntity> list = type.getList(this);

		String objectLabel = type.getObjectLabel(this, view, list);
		String objectKey = type.getObjectKey(this, view, list);

		if (objectLabel == null || objectKey == null) {
			return;
		}

		String text = mail.getText();
		text = text.replaceAll("\\[" + mail.getFromPseudo() + "=>/\\]", "[" + mail.getFromPseudo() + "=>" + path + "Utilisateur/" + mail.getFromPseudo() + "]");
		text = text.replaceAll("\\[profil=>/\\]", "[profil=>" + path + "Utilisateur/" + objectKey + "]");

		text = text.replaceAll("\\[offre d'embarquement=>/\\]", "[offre d'embarquement=>" + path + "Offre/" + objectKey + "]");
		text = text.replaceAll("\\[demande d'embarquement=>/\\]", "[demande d'embarquement=>" + path + "Demande/" + objectKey + "]");
		text = text.replaceAll("\\[bateau=>/\\]", "[bateau=>" + path + "Bateau/" + objectKey + "]");

		mail.setText(text);
	}

	@Transactional
	public String sendMP() {
		try {
			// Créer une conversation
			Conversation conv = new Conversation();
			conv.setSupprimePar(null);
			conv.setTitre(mail.getTitle());

			// Créer un message
			Message message = new Message();
			message.setConversation(conv);
			message.setEmetteur(utilisateurCourant);
			message.setDestinataire(destinataire);
			message.setLu(false);
			message.setTexte(mail.getText());
			message.setDateEnvoi(new Date());

			conv.setDernierMessage(message);
			conv.getMessages().add(message);
			conv.setNombreMessages(conv.getMessages().size());

			// Enregistrer ces deux la
			hibernateSession.persist(conv);
			hibernateSession.flush();
		} catch (Exception e) {
			throw new BSException("error.contact.envoiMessage", e);
		}
		// Si pas d'erreur, envoyer un email de copie
		contactEmailManager.sendContactMailForMP(mail, utilisateurCourant);
		facesMessages.addFromResourceBundle("Contact_mp");
		return Constantes.SENT;
	}

	public String sendMail() {
		contactEmailManager.sendContactMail(mail, utilisateurCourant);
		facesMessages.addFromResourceBundle("Contact_mail");
		return Constantes.SENT;
	}

	public void init() {
		if (ConversationUtils.beginConversation("/messagerie/Contact.xhtml")) {
			// init from
			if (utilisateurCourant == null) {
				throw new BSException("error.contact");
			}
			mail.setFromMail(utilisateurCourant.getMail());
			mail.setFromPseudo(utilisateurCourant.getPseudonyme());

			Utilisateur destinataire = (Utilisateur) hibernateSession.load(Utilisateur.class, mail.getToPseudo());

			mail.setToMail(destinataire.getMail());
			mail.setLocale(destinataire.getLanguePreferee());
			this.destinataire = destinataire;

			String path = proprieteFactory.getBasePath(mail.getLocale());

			if (objectType == null) {
				initToDefault(path);
				return;
			}

			OBJECT_TYPE type = OBJECT_TYPE.valueOf(objectType);
			if (type == null) {
				initToDefault(path);
				return;
			}
			// Init object label and object key
			BSEntityView<? extends BSEntity> view = type.getHome(this);
			BSEntityList<? extends BSEntity> list = type.getList(this);

			String objectLabel = type.getObjectLabel(this, view, list);
			String objectKey = type.getObjectKey(this, view, list);

			if (objectLabel == null || objectKey == null) {
				initToDefault(path);
				return;
				// throw new BSException("error.contact");
			}

			// Init title and text
			String title = resourceBundle.getString(type.getTitleKey() + "_" + mail.getLocale()).replace("objectLabel", objectLabel);
			String text = resourceBundle.getString(type.getTextKey() + "_" + mail.getLocale()).replace("objectKey", objectKey).replace("objectLabel", objectLabel).replace("fromPseudo",
					mail.getFromPseudo()).replace("path", path);
			mail.setTitle(title);
			mail.setText(text);
		}
	}

	private void initToDefault(final String path) {
		// le mail ne porte sur aucun objet en particulier
		// Init title and text
		String title = resourceBundle.getString("contact.title_" + mail.getLocale());
		String text = resourceBundle.getString("contact.text_" + mail.getLocale()).replace("fromPseudo", mail.getFromPseudo()).replace("path", path);
		mail.setTitle(title);
		mail.setText(text);
	}

	public Email getMail() {
		return this.mail;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Integer getObjectRow() {
		return this.objectRow;
	}

	public void setObjectRow(Integer objectRow) {
		this.objectRow = objectRow;
	}

	public boolean isMP() {
		return !destinataire.isMailAuth();
	}

}
