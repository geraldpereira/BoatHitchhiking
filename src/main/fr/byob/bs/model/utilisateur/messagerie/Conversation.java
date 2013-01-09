package fr.byob.bs.model.utilisateur.messagerie;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.utilisateur.Utilisateur;


// Selection des conversations dans la boite de reception
// - conversation avec un nombre de message > à 1 et 
// 		* emmetteur = user
//		* OU destinataire = user
// - conversation avec un nombre de message = 1 et
// 		* destinataire = user

// Selection des conversations dans les messages envoyés (sans réponse)
// - conversation avec un nombre de message = 1 et  
//		* emetteur = user

@NamedQueries( {
		@NamedQuery(name = "conversation.findInbox", query = "select conversation from Conversation conversation where (conversation.supprimePar is null or conversation.supprimePar.pseudonyme <> :utilisateur) and (conversation.nombreMessages > 1 and (conversation.dernierMessage.emetteur.pseudonyme = :utilisateur or conversation.dernierMessage.destinataire.pseudonyme = :utilisateur)) or (conversation.nombreMessages = 1 and conversation.dernierMessage.destinataire.pseudonyme = :utilisateur)"),
		@NamedQuery(name = "conversation.findSent", query = "select conversation from Conversation conversation where (conversation.supprimePar is null or conversation.supprimePar.pseudonyme <> :utilisateur) and conversation.nombreMessages = 1 and conversation.dernierMessage.emetteur.pseudonyme = :utilisateur"),
		@NamedQuery(name = "conversation.findAll", query = "select conversation from Conversation conversation where conversation.dernierMessage.emetteur.pseudonyme = :utilisateur or conversation.dernierMessage.destinataire.pseudonyme = :utilisateur") })
	
@Entity
@Table(name = "CONVERSATION")
public class Conversation implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idConversationGenerator")
	@GenericGenerator(name = "idConversationGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "id_conversation_sequence") })
	@Column(name = "ID_CONVERSATION", unique = true, nullable = false)
	private Long idConversation;

	@Sort(type = SortType.COMPARATOR, comparator = MessageComparator.class)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "conversation")
	private SortedSet<Message> messages = new TreeSet<Message>();
	
	@Column(name = "NOMBRE_MSG", nullable = false)
	@NotNull
	private Integer nombreMessages;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DERNIER_MSG", nullable = true)
	private Message dernierMessage;
		
	// Utilisateur ayant supprimé cette conversation
	// Si les deux utilisateurs suppriment la conversation, elle est virée de la
	// base de donnée
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUPPRIME_PAR", nullable = true)
	private Utilisateur supprimePar;
	
	
	// Warning : email and conversation MUST have the same title length
	@Column(name = "TITRE", nullable = false)
	@NotNull
	@Length(min = 10, max = 300)
	private String titre;
	
	public Conversation() {
	}
	
	public Long getIdConversation() {
		return this.idConversation;
	}

	public void setIdConversation(Long idConversation) {
		this.idConversation = idConversation;
	}

	public Integer getNombreMessages() {
		return this.nombreMessages;
	}

	public void setNombreMessages(Integer nombreMessages) {
		this.nombreMessages = nombreMessages;
	}

	public Message getDernierMessage() {
		return this.dernierMessage;
	}

	public void setDernierMessage(Message dernierMessage) {
		this.dernierMessage = dernierMessage;
	}

	public SortedSet<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(SortedSet<Message> messages) {
		this.messages = messages;
	}

	public Utilisateur getSupprimePar() {
		return this.supprimePar;
	}

	public void setSupprimePar(Utilisateur supprimePar) {
		this.supprimePar = supprimePar;
	}
	
	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}
