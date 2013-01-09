package fr.byob.bs.action.utilisateur.messagerie;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

import fr.byob.bs.PasswordManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;

/**
 * Les méthodes asynchrones doivent être dans une classe séparée de celle qui
 * les appelle pour être effectivement asynchrones.
 * 
 * D'où l'existance de cette classe qui contient l'ensemble des méthodes d'envoi
 * d'email asynchrones.
 * 
 * @author Kojiro
 * 
 */
@Name("emailManager")
@MeasureCalls
public class EmailManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private transient Renderer renderer;

	@In(value = "BSPasswordManager", create = true)
	private PasswordManager passwordManager;

	private Utilisateur utilisateur;

	private String messageException;

	public String getMessageException() {
		return this.messageException;
	}

	public void setMessageException(String messageException) {
		this.messageException = messageException;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	@Asynchronous
	public void envoiEmailRecupMDPAsync(Utilisateur utilisateur) {
		log.info("Sending password recovery mail to {0}", utilisateur);
		this.utilisateur = utilisateur;
		renderer.render("/email/RecupMDPEmail_"
				+ utilisateur.getLanguePreferee() + ".xhtml");
	}

	public String getHashMail() {
		return getHashMail(utilisateur);
	}

	private final static String TA_MERE = "ta mère le hacker";
	
	public String getHashMail(Utilisateur utilisateur) {
		String hash = passwordManager.hash(utilisateur.getPseudonyme() + TA_MERE + utilisateur.getMotDePasse());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hash.length(); i++) {
			char cur = hash.charAt(i);
			if (cur != '+' && cur != ' ' && cur != '=' && cur != '/') {
				sb.append(cur);
			}
		}

		return sb.toString();
	}

	@Asynchronous
	public void envoiEmailSuppression(Utilisateur instance) {
		log.info("Sending account removal mail to {0}", utilisateur);
		// Pour conserver la reference vers l'instance dans cette nouvelle
		// conversation asynchrone !
		utilisateur = instance;
		renderer.render("/email/SuppressionUtilisateurEmail_"
				+ utilisateur.getLanguePreferee() + ".xhtml");
	}

	@Asynchronous
	public void envoiEmailActivation(Utilisateur instance) {
		log.info("Sending account activation mail to {0} {1}", instance, instance.getMail());
		// Pour conserver la reference vers l'instance dans cette nouvelle
		// conversation asynchrone !
		utilisateur = instance;
		renderer.render("/email/ActivationEmail_"
				+ utilisateur.getLanguePreferee() + ".xhtml");
	}

	@Asynchronous
	public void envoiEmailException(Throwable throwable, String message) {
		StringBuffer err = new StringBuffer();
		if (throwable != null) {
			err.append(throwable.toString()).append("\n");
			err.append(throwable.getLocalizedMessage()).append("\n");
			err.append(throwable.getMessage()).append("\n");
			for (StackTraceElement elem : throwable.getStackTrace()) {
				err.append(elem.getFileName()).append(":").append(elem.getLineNumber()).append(":").append(elem.getClassName()).append(":").append(elem.getMethodName()).append("\n");
			}
		}
		messageException = err.toString();
		renderer.render("/email/ExceptionEmail_fr.xhtml");
	}
	
}
