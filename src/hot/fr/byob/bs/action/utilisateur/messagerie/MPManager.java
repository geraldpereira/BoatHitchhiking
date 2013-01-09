package fr.byob.bs.action.utilisateur.messagerie;

import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.security.AuthorizationException;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;
import fr.byob.bs.model.utilisateur.messagerie.Conversation;

@Name("mpManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class MPManager {

	@In
	private Session hibernateSession;
	
	@In(create = true) 
	private ConversationListHandler inbox;

	@In(create = true)
	private ConversationListHandler sent;
	
	@In(create = false, required = false)
	private Utilisateur utilisateurCourant;

	@Begin(join = true)
	public void wire() {
		if (utilisateurCourant == null) {
			ResourceBundle messages = SeamResourceBundle.getBundle();
			throw new AuthorizationException(messages.getString("mp.utilisateurRequis"));
		}
		List<Conversation> conversationsInbox = hibernateSession.getNamedQuery("conversation.findInbox").setParameter("utilisateur", utilisateurCourant.getPseudonyme()).list();
		List<Conversation> conversationsSent = hibernateSession.getNamedQuery("conversation.findSent").setParameter("utilisateur", utilisateurCourant.getPseudonyme()).list();

		inbox.wire(conversationsInbox);
		sent.wire(conversationsSent);
	}

	public ConversationListHandler getInbox() {
		return this.inbox;
	}

	public ConversationListHandler getSent() {
		return this.sent;
	}
}
