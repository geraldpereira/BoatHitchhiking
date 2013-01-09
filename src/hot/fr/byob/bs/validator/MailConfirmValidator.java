package fr.byob.bs.validator;

import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import fr.byob.bs.action.utilisateur.SEnregistrerManager;

public class MailConfirmValidator implements Validator<MailConfirm> {

	@Override
	public void initialize(MailConfirm mailConfirm) {
	}

	@Override
	public boolean isValid(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof String) {
			return isValid((String) o);
		}
		return isValid(o.toString());
	}

	protected boolean isValid(String mailConfirm) {
		SEnregistrerManager manager = (SEnregistrerManager) Component.getInstance("senregistrerManager");
		String mail = manager.getUtilisateur().getMail();
		if (mail == null ){
			return true;
		}
		if (mailConfirm == null){
			return false;
		}
		
		return mailConfirm.equals(mail);
	}
}
