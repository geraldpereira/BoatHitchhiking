package fr.byob.bs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import fr.byob.bs.debug.MeasureCalls;

@MeasureCalls
public class BSUtils {

	public static Date getAgeAsDate(int age) {
		Calendar cBirthday = new GregorianCalendar();
		cBirthday.setTime(new Date());
		int year = cBirthday.get(Calendar.YEAR);
		year -= (age + 1);
		cBirthday.set(Calendar.YEAR, year);
		return cBirthday.getTime();
	}

	public static Date getAgeAsDate(String value) {
		if (value == null) {
			return null;
		}
		Integer age = null;
		try {
			age = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
		return getAgeAsDate(age);
	}

	public static String getDateAsAge(Object value) {
		if (value == null) {
			return null;
		}

		Date birthday = (Date) value;

		Calendar cBirthday = new GregorianCalendar();
		Calendar cToday = new GregorianCalendar();
		cBirthday.setTime(birthday);
		cToday.setTime(new Date());

		int yearDiff = cToday.get(Calendar.YEAR) - cBirthday.get(Calendar.YEAR);
		cBirthday.set(Calendar.YEAR, cToday.get(Calendar.YEAR));
		if (cBirthday.before(cToday)) {
			return "" + yearDiff; // Birthday already celebrated this year
		} else {
			return "" + Math.max(0, yearDiff - 1); // Need a max to avoid -1 for
			// baby
		} 
	}
	
	public static String normalize(String string) {
		if (string == null){
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (equivalent.containsKey(c)) {
				Character character = equivalent.get(c);
				if (character != null) {
					stringBuilder.append(character);
				}
			} else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	private static Map<Character, Character> equivalent;

	static {
		equivalent = new HashMap<Character, Character>();

		equivalent.put('À', 'A');
		equivalent.put('Á', 'A');
		equivalent.put('Â', 'A');
		equivalent.put('Ã', 'A');
		equivalent.put('Ä', 'A');
		equivalent.put('Å', 'A');
		equivalent.put('Ç', 'C');
		equivalent.put('È', 'E');
		equivalent.put('É', 'E');
		equivalent.put('Ê', 'E');
		equivalent.put('Ë', 'E');
		equivalent.put('Ì', 'I');
		equivalent.put('Í', 'I');
		equivalent.put('Î', 'I');
		equivalent.put('Ï', 'I');
		equivalent.put('Ò', 'O');
		equivalent.put('Ó', 'O');
		equivalent.put('Ô', 'O');
		equivalent.put('Õ', 'O');
		equivalent.put('Ö', 'O');
		equivalent.put('Ù', 'U');
		equivalent.put('Ú', 'U');
		equivalent.put('Û', 'U');
		equivalent.put('Ü', 'U');
		equivalent.put('Ý', 'Y');
		equivalent.put('à', 'a');
		equivalent.put('á', 'a');
		equivalent.put('â', 'a');
		equivalent.put('ã', 'a');
		equivalent.put('ä', 'a');
		equivalent.put('å', 'a');
		equivalent.put('ç', 'c');
		equivalent.put('è', 'e');
		equivalent.put('é', 'e');
		equivalent.put('ê', 'e');
		equivalent.put('ë', 'e');
		equivalent.put('ì', 'i');
		equivalent.put('í', 'i');
		equivalent.put('î', 'i');
		equivalent.put('ï', 'i');
		equivalent.put('ð', 'o');
		equivalent.put('ò', 'o');
		equivalent.put('ó', 'o');
		equivalent.put('ô', 'o');
		equivalent.put('õ', 'o');
		equivalent.put('ö', 'o');
		equivalent.put('ù', 'u');
		equivalent.put('ú', 'u');
		equivalent.put('û', 'u');
		equivalent.put('ü', 'u');
		equivalent.put('ý', 'y');
		equivalent.put('ÿ', 'y');
		
		equivalent.put(' ', '-');
		equivalent.put('#', null);
		equivalent.put('$', null);
		equivalent.put('%', null);
		equivalent.put('&', null);
		equivalent.put('\'', '-');
		equivalent.put('/', null);
		equivalent.put(':', null);
		equivalent.put(';', null);
		equivalent.put('<', null);
		equivalent.put('>', null);
		equivalent.put('=', null);
		equivalent.put('?', null);
		equivalent.put('[', null);
		equivalent.put(']', null);
		equivalent.put('{', null);
		equivalent.put('}', null);
		equivalent.put('\\', null);
		equivalent.put('^', null);
		equivalent.put('|', null);
		equivalent.put('~', null);
		equivalent.put('@', null);
	}

}
