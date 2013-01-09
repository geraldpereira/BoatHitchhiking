package fr.byob.bs;

import java.util.Date;
import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.debug.MeasureCalls;

@Startup
@Name("dateFormat")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class DateFormat {

	public static String getDateFormatShort(Locale locale) {
		if ("en".equals(locale.toString())) {
			return "MM/dd/yyyy";
		} else if ("fr".equals(locale.toString())) {
			return "dd/MM/yyyy";
		} else {
			return "MM/dd/yyyy";
		}
	}
	
	public static String getDateFormattedShort(Locale locale, Date date) {
		java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, locale);
		if (date != null) {
			return df.format(date);
		}
		return null;
	}
}
