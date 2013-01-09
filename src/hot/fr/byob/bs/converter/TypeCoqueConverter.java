package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.model.bateau.TypeCoque;

@Name("typeCoqueConverter")
@BypassInterceptors
@Converter
public class TypeCoqueConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		if (value == null || value.trim().length() == 0) {
			return null;			
		}
		return TypeCoque.valueOf(value);
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		
		return ((TypeCoque) value).name();
	}
}