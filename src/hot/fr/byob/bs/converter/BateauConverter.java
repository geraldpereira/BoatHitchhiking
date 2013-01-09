package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;

import fr.byob.bs.action.annonce.OffreEdit;
import fr.byob.bs.model.bateau.Bateau;

@Name("bateauConverter")
@BypassInterceptors
@Converter
public class BateauConverter implements javax.faces.convert.Converter {

	
	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		OffreEdit offreEdit = (OffreEdit) Contexts.getConversationContext().get("offreEdit");
		for (Bateau bateau : offreEdit.getBateaux()) {
			if (bateau.getIdBateau().equals(Long.parseLong(value))) {
				return bateau;
			}
		}
		return null;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		if (value == null) {
			return null;
		}
		return ((Bateau) value).getIdBateau().toString();
	}
}
