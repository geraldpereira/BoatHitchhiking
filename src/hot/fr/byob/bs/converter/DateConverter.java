package fr.byob.bs.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("dateConverter")
@BypassInterceptors
@Converter
public class DateConverter implements javax.faces.convert.Converter {

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		try {
			Date date = dateFormat.parse(value);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		Date date = (Date) value;
		if (date == null) {
			return null;
		}
		return dateFormat.format(date);
	}
}