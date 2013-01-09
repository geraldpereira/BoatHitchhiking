package fr.byob.bs.el.func;

import com.sun.facelets.tag.TagLibrary;
import com.sun.facelets.tag.AbstractTagLibrary;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FnLibrary extends AbstractTagLibrary implements TagLibrary {

	public final static String Namespace = "http://www.bateaustop.fr/SeamFunc";

	public static final FnLibrary INSTANCE = new FnLibrary();

	public FnLibrary() {
		super(Namespace);
		try {
			Method[] methods = SeamFunc.class.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (Modifier.isStatic(methods[i].getModifiers())) {
					this.addFunction(methods[i].getName(), methods[i]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

