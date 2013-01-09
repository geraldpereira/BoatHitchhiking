package fr.byob.bs.action.utilisateur.messagerie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Roles;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import fr.byob.bs.BSException;
import fr.byob.bs.Constantes;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;
import fr.byob.bs.model.utilisateur.messagerie.Conversation;
import fr.byob.bs.model.utilisateur.messagerie.Message;

@Name("inbox")
@Scope(ScopeType.CONVERSATION)
@Roles( { @Role(name = "sent", scope = ScopeType.CONVERSATION) })
@MeasureCalls
public class ConversationListHandler {

	@In(create = true)
	private ContactEmailManager contactEmailManager;
	
	@In
	private Session hibernateSession;

	@In(create = false, required = false)
	private Utilisateur utilisateurCourant;

	private List<Conversation> conversations;
	private int indexCourant = -1;
	private List<Message> messagesCourant;

	private Message reponse;
	
	private final static ConversationComparator conversationComparator = new ConversationComparator();
	
	private static final class ConversationComparator implements Comparator<Conversation> {
		@Override
		public int compare(Conversation o1, Conversation o2) {
			return o2.getDernierMessage().getDateEnvoi().compareTo(o1.getDernierMessage().getDateEnvoi());
		}
	}
	
	public ConversationListHandler() {
	}

	public void wire(List<Conversation> conversations) {
		this.conversations = conversations;
		Collections.sort(this.conversations, conversationComparator);
		messagesCourant = new ArrayList<Message>();
	}

	public List<Conversation> getConversations() {
		return this.conversations;
	}

	public boolean isIndexCourant(int index) {
		return indexCourant == index;
	}

	public int getIndexCourant() {
		return indexCourant;
	}

	public Conversation getConversationCourante() {
		return conversations.get(indexCourant);
	}

	public void setIndexCourant(int index) {
		this.indexCourant = index;
		messagesCourant.clear();
		if (index != -1) {
			messagesCourant.addAll(getConversationCourante().getMessages());
		}
	}

	public void resetIndexCourant() {
		Query query = hibernateSession.getNamedQuery("message.updateLu");
		query.setParameter("conversation", getConversationCourante().getIdConversation());
		query.setParameter("destinataire", utilisateurCourant.getPseudonyme());
		query.executeUpdate();
		
		for (Message message : getMessagesCourant()) {
			if (isRecu(message)) {
				message.setLu(true);
			}
		}
		
		// User may have answer to the conversation so the order can change
		Collections.sort(this.conversations, conversationComparator);
		
		setIndexCourant(-1);
	}

	public void supprimerConversation() {
		Conversation conv = getConversationCourante();
		if (conv.getSupprimePar() != null) {
			// Suppr en base
			hibernateSession.delete(conv);
		} else {

			conv.setSupprimePar(utilisateurCourant);
			// update en BD;
			hibernateSession.update(conv);
		}
		// Flush
		hibernateSession.flush();
		conversations.remove(indexCourant);
		setIndexCourant(-1);
	}

	@Transactional
	public String repondreConversation() {
		Message reponse = getReponse();

		if (reponse.getTexte() == null || reponse.getTexte().isEmpty() || reponse.getTexte().length() < 10 || reponse.getTexte().length() > 3000) {
			return Constantes.ERREUR_AJOUT;
		}
		
		Conversation conv = getConversationCourante();
		Utilisateur destinataire = getAutre(conv.getDernierMessage());
		try {
			hibernateSession.refresh(conv);

			reponse.setConversation(conv);
			reponse.setDateEnvoi(new Date());
			reponse.setDestinataire(destinataire);
			conv.setDernierMessage(reponse);
			conv.getMessages().add(reponse);
			conv.setNombreMessages(conv.getMessages().size());
			conv.setSupprimePar(null);

			hibernateSession.update(conv);
			hibernateSession.flush();
		} catch (Exception e) {
			throw new BSException("error.contact.envoiMessage", e);
		}
		resetReponse();
		
		// If there was a refresh we get the last up to date messages
		messagesCourant.clear();
		messagesCourant.addAll(getConversationCourante().getMessages());
		
		Email mail = new Email();
		mail.setCopy(false);
		mail.setToMail(destinataire.getMail());
		mail.setToPseudo(destinataire.getPseudonyme());
		mail.setLocale(destinataire.getLanguePreferee());

		contactEmailManager.sendContactMailForMP(mail, utilisateurCourant);
		
		return Constantes.SENT;
	}

	public List<Message> getMessagesCourant() {
		return this.messagesCourant;
	}

	public boolean isRecu(Message message) {
		return message.getDestinataire().equals(utilisateurCourant);
	}
	
	
	public boolean isLu(Message message) {
		// Si on visialise la liste
		if (indexCourant == -1) {
			return message.isLu() || !isRecu(message);
		} else { // visu d'une conv
			// Lu si envoyé ou lu
			return (message.isLu() || !isRecu(message)) && !getConversationCourante().getDernierMessage().equals(message);
		}
	}

	public String getAutrePseudonyme(Message message) {
		return getAutre(message).getPseudonyme();
	}

	public Utilisateur getAutre(Message message) {
		if (message.getDestinataire().equals(utilisateurCourant)) {
			return message.getEmetteur();
		} else {
			return message.getDestinataire();
		}
	}

	public Message getReponse() {
		if (reponse == null) {
			reponse = new Message();
			reponse.setEmetteur(utilisateurCourant);
			reponse.setLu(false);
		}
		return reponse;
	}
	
	private void resetReponse() {
		this.reponse = null;
	}
}
