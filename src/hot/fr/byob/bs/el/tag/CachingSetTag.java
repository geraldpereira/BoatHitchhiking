package fr.byob.bs.el.tag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

/**
 * Simple tag for setting the CachingSet component, a simple usage would be
 * 
 * <pre>
 *  &lt;app:cachingSet var="studentSearchResults" value="#{studentManager.searchResults}"&gt;
 *      Size: #{studentSearchResults.size}
 *      &lt;s:div rendered="#{studentSearchResults.size  ge 1 and selectedStudent == null}" style="padding: 10px;text-align:left;"&gt;
 *      ...
 *      &lt;/s:div&gt;
 *      &lt;s:div rendered="#{studentSearchResults.size  eq 0}" style="padding: 10px;text-align:left;"&gt;
 *      ...
 *      &lt;/s:div&gt;
 *  &lt;/app:cachingSet&gt;
 *  
 *  In this example even if studentSearchResults is not marked with DataModel annotation it would still be evaluated only once for rendering
 * </pre>
 * 
 * @author Adam Warski (adam at warski dot org)
 * @author apurba
 * 
 */
public class CachingSetTag extends UIComponentELTag {

	private static String COMPONENT_TYPE = UICachingSet.class.getName();

	// setters and getters for the include only
	private boolean includeOnly = false;

	public void setIncludeOnly(boolean includeOnly) {
		this.includeOnly = includeOnly;
	}

	// setters for the variable name
	private String var;

	public void setVar(String var) {
		this.var = var;
	}

	// setters for the usual ui tag property value
	private ValueExpression value;

	public void setValue(ValueExpression value) {
		this.value = value;
	}

	@Override
	public void release() {
		super.release();
		var = null;
		value = null;
	}

	@Override
	protected void setProperties(UIComponent component) {
		// the super class method should be called
		super.setProperties(component);
		UICachingSet castedComponent = (UICachingSet) component;
		castedComponent.setVar(var);
		castedComponent.setValueExpression("value", value);
		castedComponent.setIncludeOnly(includeOnly);
	}

    @Override
	public String getComponentType() {
		return COMPONENT_TYPE;
	}

	@Override
	public String getRendererType() {
		return null;
	}

}