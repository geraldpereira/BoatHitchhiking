package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.BSUtils;

@Name("ageConverter")
@BypassInterceptors
@Converter
public class AgeConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		return BSUtils.getAgeAsDate(value);
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp, Object value) {
		return BSUtils.getDateAsAge(value);
	}
}