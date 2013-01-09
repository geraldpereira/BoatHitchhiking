package fr.byob.bs.el.tag;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class UILet extends UIComponentBase {
    @Override
	public String getFamily() {
        return "pl.net.mamut.jsf.Let";
    }

    @Override
	@SuppressWarnings({"unchecked"})
    public void encodeBegin(FacesContext context) throws IOException {
        String var = (String) getAttributes().get("var");
        Object value = getAttributes().get("value");

        context.getExternalContext().getRequestMap().put(var, value);
    }

    @Override
	public void encodeEnd(FacesContext context) throws IOException {
        String var = (String) getAttributes().get("var");

        context.getExternalContext().getRequestMap().remove(var);
    }
}
