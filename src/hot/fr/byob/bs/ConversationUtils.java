package fr.byob.bs;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.ConversationEntries;
import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.core.Manager;

import fr.byob.bs.debug.MeasureCalls;

@Name("conversationUtils")
@Scope(ScopeType.PAGE)
@MeasureCalls
public class ConversationUtils {

	@In
	private List<ConversationEntry> conversationStack;

	public boolean isCurrentConversation(ConversationEntry entry) {
		if (Conversation.instance() != null && entry != null) {
			return Conversation.instance().getId().equals(entry.getId());
		} else {
			return false;
		}
	}

	public void select(ConversationEntry entry) {
		String entryId = entry.getId();
		entry.select();
		// Conversation.instance().killAllOthers();

		// Destroy all remaining conversations
		boolean destroy = false;
		for (ConversationEntry curEntry : conversationStack) {
			if (destroy) {
				// Attention a verifier les fuites mémoire
				curEntry.end();
				curEntry.setRemoveAfterRedirect(true);
				ConversationEntries.instance().removeConversationEntry(curEntry.getId());
			} else if (curEntry.getId().equals(entryId)) {
				destroy = true;
			}
		}

	}

	public String getPreviousPage(String from) {
		Conversation conversation = Conversation.instance();
		String page = null;
		if (conversation != null && conversation.isNested()) {
			page = ConversationEntries.instance().getConversationEntry(conversation.getParentId()).getViewId();
		} else if (from != null && !from.isEmpty()) {
			page = "/" + from + ".xhtml";
		} else {
			page = "/home.xhtml";
		}
		return page;
	}

	public static boolean beginConversation(String viewId) {
		ConversationEntry currentConversationEntry = Manager.instance().getCurrentConversationEntry();
		if (currentConversationEntry != null && viewId.equals(currentConversationEntry.getViewId())) {
			// Join
			Conversation.instance().begin(true, false);
			return false;
		} else {
			// Nested
			Conversation.instance().begin(false, true);
			return true;
		}
	}

	public List<ConversationEntry> getConversationStack() {
		return this.conversationStack;
	}
}
