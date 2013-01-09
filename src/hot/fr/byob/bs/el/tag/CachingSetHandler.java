package fr.byob.bs.el.tag;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.apache.log4j.Logger;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;

/**
 * The caching setter keeps an el value in scope via encodeBegin and encodeEnd
 * method, unfortunately with includes the el is evaluated before encoding even
 * begins. For example if I use something like
 * 
 * <pre>
 *     &lt;app:cachingSet var=&quot;currentContentPage&quot; value=&quot;#{someBean.contentPage}&quot;&gt;
 *                   &lt;a:include viewId=&quot;#{currentContentPage}&quot; /&gt;
 *    &lt;/app:cachingSete&gt;
 * </pre>
 * 
 * then it would not work as currentContentPage would be evaluated by the
 * IncludeHandlerbefore cachingSet methods have even been called, so to get
 * around this we are getting into the Handler Business.
 * 
 * @author apurba
 */
public class CachingSetHandler extends ComponentHandler {

	private static final Logger LOG = Logger.getLogger(CachingSetHandler.class);

	public CachingSetHandler(ComponentConfig config) {
		super(config);
	}

	@Override
	protected void applyNextHandler(FaceletContext ctx, UIComponent component) throws IOException, FacesException, ELException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("applying next handler for " + component);
		}
		UICachingSet setter = getCastedComponent(component);
		if (setter.isIncludeOnly()) {
			setter.addVariableValue(ctx.getFacesContext(), ctx.getFacesContext().getELContext());
		}
		super.applyNextHandler(ctx, component);
		if (setter.isIncludeOnly()) {
			setter.removeVariableValue(ctx.getFacesContext());
		}
	}

    private UICachingSet getCastedComponent(UIComponent component) {
		if (component instanceof UICachingSet) {
			return (UICachingSet) component;
		} else {
			throw new IllegalStateException("Planned to be used only for caching setter component");
		}
	}
}