package fr.byob.bs.converter;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.model.Pays;

@Name("paysConverter")
@BypassInterceptors
@Converter
public class PaysConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		HashMap<Long, Pays> pays = (HashMap<Long, Pays>) Component
				.getInstance("paysMap");
		Pays p = null;
		try {
			p = pays.get(Long.parseLong(value));
		} catch (NumberFormatException e) {
		}
		return p;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		if (value == null) {
			return null;
		}
		Pays p = (Pays) value;
		return p.getIdPays().toString();
	}
}
