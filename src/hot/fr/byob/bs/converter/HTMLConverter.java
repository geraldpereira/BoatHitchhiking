package fr.byob.bs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("HTMLConverter")
@BypassInterceptors
@Converter
public class HTMLConverter implements javax.faces.convert.Converter {

	@Transactional
	@Override
	public Object getAsObject(FacesContext context, UIComponent cmp,
			String value) {
		if (value == null){
			return null;			
		}
		value = value.trim();
		if (value.length() == 0){
			return null;
		}
		return value;
	}

	@Transactional
	@Override
	public String getAsString(FacesContext context, UIComponent cmp,
			Object value) {
		
		String str = (String) value;
		str = StringEscapeUtils.escapeHtml(str);
		str = str.replace("\r\n", "<br/>");
		str = str.replace("\n\r", "<br/>");
		str = str.replace("\r", "<br/>");
		str = str.replace("\n", "<br/>");
				
		return str;
	}
}