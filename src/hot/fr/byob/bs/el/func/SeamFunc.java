package fr.byob.bs.el.func;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class SeamFunc {

	public static Integer[] newIncIntegerTabLength(int length) {
		Integer[] tab = new Integer[length];
		for (int i = 0; i < length; i++) {
			tab[i] = Integer.valueOf(i + 1);
		}
		return tab;
	}

	public static Integer[] newIncIntegerTab(int minVal, int maxVal) {
		Integer[] tab = new Integer[maxVal - minVal + 1];
		for (int i = 0, j = minVal; i < tab.length; i++, j++) {
			tab[i] = Integer.valueOf(j);
		}
		return tab;
	}

	public static String concat(String str1, String str2) {
		return str1 + str2;
	}

	public static String getXXX(Object o) {
		if (o == null) {
			return null;
		}
		
		if (o instanceof String) {
			String str = (String) o;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == ' ') {
					sb.append(' ');
				} else {
					sb.append('x');
				}
				
			}

			return sb.toString();
		} else {
			return "XXX";
		}
	}

	private final static String TEMPLATE = "/layout/template.xhtml";
	
	public static String getTemplate() {
		return TEMPLATE;
	}

	public static String getUrl() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
	} 
	
	public static int getNotePar5(Integer note) {
		return (int) Math.round(new Double(note) / 20);
	}
}
