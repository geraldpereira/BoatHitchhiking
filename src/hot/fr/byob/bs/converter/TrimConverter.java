package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("trimConverter")
@BypassInterceptors
@Converter
public class TrimConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		if (value == null){
			return null;			
		}
		value = value.trim();
		if (value.length() == 0){
			return null;
		}
		return value;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		
		return (String)value;
	}
	
	public static String trimString(String str, int size) {
		if (str.length() > size) {
			str = str.substring(0, size - 1) + " ...";
		}
		return str;
	}
}