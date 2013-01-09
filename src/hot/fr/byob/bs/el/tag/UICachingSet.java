package fr.byob.bs.el.tag;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Since c:set does not remember the evaluated el value, meaning it is evaluated
 * again and again, this component remedies this.
 * 
 * @author Adam Warski (adam at warski dot org) http://www.warski.org/blog/?p=10
 * @author apurba http://blog.imaginea.com/seam-performance-tips/
 */
public class UICachingSet extends UIComponentBase {

	private static String COMPONENT_FAMILY = UICachingSet.class.getName();
	private String var;
	private boolean includeOnly;

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		ELContext elContext = context.getELContext();
		addVariableValue(context, elContext);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.writeAttribute("style", "display:none", "style");
		writer.write("caching value for " + var);
		writer.endElement("div");
		writer.flush();
		removeVariableValue(context);
	}

	/**
	 * For handling restoreView calls we need to understand the decode process
	 * too. Important if you have used this variable in any rendered element.
	 */
	@Override
	public void processDecodes(FacesContext context) {
		ELContext elContext = context.getELContext();
		addVariableValue(context, elContext);
		super.processDecodes(context);
		removeVariableValue(context);
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = var;
		values[2] = includeOnly;
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		var = (String) values[1];
		includeOnly = (Boolean) values[2];
	}

	public String getVar() {
		return var;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " " + getVar();
	}

	public void setIncludeOnly(boolean includeOnly) {
		this.includeOnly = includeOnly;
	}

	public boolean isIncludeOnly() {
		return this.includeOnly;
	}

	/** package */
	void removeVariableValue(FacesContext context) {
		context.getExternalContext().getRequestMap().remove(var);
	}

    /** package */
	void addVariableValue(FacesContext context, ELContext elContext) {
		ValueExpression expr = getValueExpression("value");
		context.getExternalContext().getRequestMap().put(var, expr.getValue(elContext));
	}
}