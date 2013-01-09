package fr.byob.bs.model.utilisateur.messagerie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import fr.byob.bs.model.utilisateur.Utilisateur;

@NamedQueries( { @NamedQuery(name = "message.updateLu", query = "update Message message set message.lu = true where message.destinataire.pseudonyme = :destinataire and message.conversation.idConversation = :conversation") })

@Entity
@Table(name = "MESSAGE")
public class Message implements java.io.Serializable, Comparable<Message> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idMessageGenerator")
	@GenericGenerator(name = "idMessageGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "sequence_name", value = "id_message_sequence") })
	@Column(name = "ID_MESSAGE", unique = true, nullable = false)
	private Long idMessage;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONVERSATION", nullable = false)
	@NotNull
	private Conversation conversation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMETTEUR", nullable = false)
	@NotNull
	private Utilisateur emetteur;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESTINATAIRE", nullable = false)
	@NotNull
	private Utilisateur destinataire;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_ENVOI", nullable = false)
	@NotNull
	private Date dateEnvoi;
	
	// Warning : email and message MUST have the same text length
	@Column(name = "TEXTE")
	@Length(min = 10, max = 3000)
	@NotNull
	private String texte;

	// Lu par le destinataire ?
	@Column(name = "LU", nullable = false)
	@NotNull	
	private boolean lu;
	
	public Message() {
		lu = false;
	}
	
	public Long getIdMessage() {
		return this.idMessage;
	}

	public void setIdMessage(Long idMessage) {
		this.idMessage = idMessage;
	}

	public Utilisateur getEmetteur() {
		return this.emetteur;
	}

	public void setEmetteur(Utilisateur emetteur) {
		this.emetteur = emetteur;
	}

	public Utilisateur getDestinataire() {
		return this.destinataire;
	}

	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}

	public Date getDateEnvoi() {
		return this.dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public String getTexte() {
		return this.texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Conversation getConversation() {
		return this.conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public boolean isLu() {
		return this.lu;
	}

	public void setLu(boolean lu) {
		this.lu = lu;
	}

	public int compareTo(Message m2) {
		return getDateEnvoi().compareTo(m2.getDateEnvoi());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.idMessage == null) ? 0 : this.idMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (this.idMessage == null) {
			if (other.idMessage != null)
				return false;
		} else if (!this.idMessage.equals(other.idMessage))
			return false;
		return true;
	}
	
	
}
