package fr.byob.bs.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import fr.byob.bs.model.experience.XpGenerale;

@Name("xpGeneraleConverter")
@BypassInterceptors
@Converter
public class XpGeneraleConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		
		final ArrayList<XpGenerale> xpsGen = new ArrayList<XpGenerale>();
		if (value == null || value.trim().length() == 0) {
			return xpsGen;
		}
		
		String[] xpsStr = value.split("-");
		for (String xpString : xpsStr) {
			xpsGen.add(XpGenerale.valueOf(xpString));
		}
		return xpsGen;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp, Object value) {
		
		List<XpGenerale> xpsGen = (List<XpGenerale>) value;

		if (xpsGen.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();

		for (XpGenerale xpGenerale : xpsGen) {
			sb.append(xpGenerale.name()).append("-");
		}

		sb.setLength(sb.length() - 1);

		return sb.toString();
	}
	
	

}