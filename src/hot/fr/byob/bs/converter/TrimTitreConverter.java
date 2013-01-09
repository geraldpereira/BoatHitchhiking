package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("trimTitreConverter")
@BypassInterceptors
@Converter
public class TrimTitreConverter implements javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String description = (String) arg2;
		if (description.length() > 20) {
			description = description.substring(0, 19) + " ...";
		}
		return description;
	}

}
