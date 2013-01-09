package fr.byob.bs.converter;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.model.Langue;

@Name("langueConverter")
@BypassInterceptors
@Converter
public class LangueConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		HashMap<Long, Langue> langues = (HashMap<Long, Langue>) Component
				.getInstance("languesMap");
		Langue o = langues.get(Long.parseLong(value));
		return o;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		if (value == null) {
			return null;
		}
		String s = ((Langue) value).getIdlangue().toString();
		return s;
	}
}
