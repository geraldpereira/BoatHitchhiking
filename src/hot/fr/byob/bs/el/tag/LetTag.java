package fr.byob.bs.el.tag;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class LetTag extends UIComponentTag{
    private String var;
    private String value;

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
	public void release() {
        // the super class method should be called
        super.release();

        var = null;
        value = null;
    }

    @Override
	@SuppressWarnings({"unchecked"})
    protected void setProperties(UIComponent component) {
        // the super class method should be called
        super.setProperties(component);

        if (var != null) {
            component.getAttributes().put("var", var);
        }

        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = getFacesContext().getApplication().createValueBinding(value);
                component.setValueBinding("value", vb);
            } else {
                component.getAttributes().put("value", value);
            }
        }
    }

    @Override
	public String getComponentType() {
        return "let";
    }

    @Override
	public String getRendererType() {
        return null;
    }
}
