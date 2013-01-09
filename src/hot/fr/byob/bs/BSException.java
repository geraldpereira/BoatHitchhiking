package fr.byob.bs;

import java.util.ResourceBundle;

import javax.ejb.ApplicationException;

import org.jboss.seam.core.SeamResourceBundle;

import fr.byob.mail.Emailer;

@ApplicationException(rollback = true)
public class BSException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle messages = SeamResourceBundle.getBundle();
	
	public BSException (String msg){
		super(messages.getString(msg));
		sendMailToAdmin(messages.getString(msg), null);
	}
	
	public BSException (String msg, Throwable t){
		super(messages.getString(msg), t);		
		sendMailToAdmin(messages.getString(msg), t);
	}

	public BSException(Throwable t) {
		super(t);
		sendMailToAdmin(t.getMessage(), t);
	}

	private void sendMailToAdmin(String message, Throwable t) {
		if (message == null) {
			message = t.toString();
		}
		StringBuffer err = new StringBuffer();
		if (t != null) {
			err.append(t.toString()).append("<br/>");
			err.append(t.getLocalizedMessage()).append("<br/>");
			err.append(t.getMessage()).append("<br/>");
			for (StackTraceElement elem : t.getStackTrace()) {
				err.append(elem.getFileName()).append(":").append(elem.getLineNumber()).append(":").append(elem.getClassName()).append(":").append(elem.getMethodName()).append("<br/>");
			}
		} else {
			err.append(message);
		}
		Emailer emailer = new Emailer();
		// emailer.sendMessage("smtp.gmail.com", "587", "true",
		// "boathitchhiking@gmail.com", "xxxx1710",
		// "boathitchhiking@gmail.com", "kojiro.sazaki@gmail.com", message,
		// err.toString(), true);
		emailer.sendMessage("smtp.gmail.com", "587", "true", "boathitchhiking@gmail.com", "xxxx1710", "boathitchhiking@gmail.com", "edeltil@gmail.com", message, err.toString(), true);
	}
}
