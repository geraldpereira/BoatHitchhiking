package fr.byob.bs;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import fr.byob.bs.action.utilisateur.messagerie.EmailManager;

@Name("error")
@Scope(ScopeType.EVENT)
public class BSErrorAction implements Serializable {

	@Logger
	private Log log;
	
	@In(value = "org.jboss.seam.handledException", required = false)
	private Exception e;

	@In(create = true, required = true)
	private EmailManager emailManager;

    public void sendMail() {
    	log.info("BSErrorAction send mail");
		emailManager.envoiEmailException(e, "exception");
		log.info("BSErrorAction send mail");
	}
}
