package fr.byob.bs.action.utilisateur.messagerie;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("contactEmailManager")
@MeasureCalls
public class ContactEmailManager implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private transient Renderer renderer;
	

	private Email mail;
	
	@Asynchronous
	public void sendContactMail(Email mail, Utilisateur utilisateurCourant) {
		log.info("Contact mail sent from {0} to {1}", mail.getFromPseudo(), mail.getToPseudo());
		
		this.mail = mail;
		renderer.render("/email/ContactEmail.xhtml");
		if (mail.isCopy()) {
			if ("fr".equals(utilisateurCourant.getLanguePreferee())) {
				renderer.render("/email/ContactEmailCopy_fr.xhtml");
			} else {
				renderer.render("/email/ContactEmailCopy_en.xhtml");
			}
		}
	}

	@Asynchronous
	public void sendContactMailForMP(Email mail, Utilisateur utilisateurCourant) {
		log.info("Contact mail for MP sent from {0} to {1}", mail.getFromPseudo(), mail.getToPseudo());

		this.mail = mail;
		
		if ("fr".equals(mail.getLocale())) {
			renderer.render("/email/ContactMPSent_fr.xhtml");
		} else {
			renderer.render("/email/ContactMPSent_en.xhtml");
		}
		if (mail.isCopy()) {
			if ("fr".equals(utilisateurCourant.getLanguePreferee())) {
				renderer.render("/email/ContactEmailCopy_fr.xhtml");
			} else {
				renderer.render("/email/ContactEmailCopy_en.xhtml");
			}
		}
	}
	

	public Email getMail() {
		return this.mail;
	}

	public void setMail(Email mail) {
		this.mail = mail;
	}
}
