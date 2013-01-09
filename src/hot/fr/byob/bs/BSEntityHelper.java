package fr.byob.bs;

import java.util.List;
import java.util.Locale;

import fr.byob.bs.model.BSEntity;

public interface BSEntityHelper<T extends BSEntity> {

	public final static String FR = "fr";
	public final static String EN = "en";

	public String getDescription(T instance, Locale locale);

	public String getTitle(T instance);

	public String getCaption(T instance, Locale locale);

	public String getURL(T instance, Locale locale);

	public String getURL(T instance);

	public Integer getNote(T instance);

	public List<String> getIndications(T instance);

}
