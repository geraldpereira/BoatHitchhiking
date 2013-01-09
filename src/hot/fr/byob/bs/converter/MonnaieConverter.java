package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.model.Monnaie;

@Name("monnaieConverter")
@BypassInterceptors
@Converter
public class MonnaieConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp, String value) {
		try {
			Monnaie monnaie = Monnaie.get(Long.parseLong(value));
			return monnaie;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp, Object value) {
		if (value == null) {
			return null;
		}
		String retour = ((Monnaie) value).getIdMonnaie().toString();
		return retour;
	}
	

}
