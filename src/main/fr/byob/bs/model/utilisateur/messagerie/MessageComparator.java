package fr.byob.bs.model.utilisateur.messagerie;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {

	@Override
	public int compare(Message m1, Message m2) {
		return m1.compareTo(m2);
	}

}
