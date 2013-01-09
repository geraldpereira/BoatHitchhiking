package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("trimMessageConverter")
@BypassInterceptors
@Converter
public class TrimMessageConverter implements javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String description = (String) arg2;
		description = description.replaceAll("<[^>]*>", "");
		description = description.replaceAll("[*]", "");
		return TrimConverter.trimString(description, 50);
	}

}
