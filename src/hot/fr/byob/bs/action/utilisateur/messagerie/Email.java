package fr.byob.bs.action.utilisateur.messagerie;

import java.io.Serializable;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

public class Email implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Warning : email and conversation MUST have the same title length
	@NotEmpty
	@Length(min = 10, max = 300)
	private String title;

	// Warning : email and message MUST have the same text length
	@NotEmpty
	@Length(min = 10, max = 3000)
	private String text;
	
	private boolean copy;
	
	
	private String fromPseudo;
	private String fromMail;
	private String toMail;
	private String toPseudo;

	private String locale;


	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCopy() {
		return this.copy;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public String getFromPseudo() {
		return this.fromPseudo;
	}

	public void setFromPseudo(String fromPseudo) {
		this.fromPseudo = fromPseudo;
	}

	public String getFromMail() {
		return this.fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getToMail() {
		return this.toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public String getToPseudo() {
		return this.toPseudo;
	}

	public void setToPseudo(String toPseudo) {
		this.toPseudo = toPseudo;
	}

	
}
