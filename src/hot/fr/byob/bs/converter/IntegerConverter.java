package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("integerConverter")
@BypassInterceptors
@Converter
public class IntegerConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		try {
			Integer integer = Integer.parseInt(value);
			return integer;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		Integer integer = (Integer) value;
		if (integer == null) {
			return null;
		}
		return integer.toString();
	}
}