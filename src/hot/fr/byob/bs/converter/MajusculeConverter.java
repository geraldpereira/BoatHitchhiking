package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("majusculeConverter")
@BypassInterceptors
@Converter
public class MajusculeConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		if (value == null){
			return null;			
		}
		value = value.trim();
		if (value.length() == 0){
			return value;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(value.charAt(0)));
		sb.append(value.substring(1, value.length()));
		return sb.toString();
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		
		return (String)value;
	}
}