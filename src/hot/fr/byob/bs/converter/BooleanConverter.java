package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("bsBooleanConverter")
@BypassInterceptors
@Converter
public class BooleanConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		if ("true".equals(value)) {
			return Boolean.TRUE;
		}
		return null;			
		
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		Boolean bool = (Boolean) value;
		if (bool) {
			return "true";
		}
		return null;
	}
}