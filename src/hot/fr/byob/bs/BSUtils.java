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

		equivalent.put('�', 'A');
		equivalent.put('�', 'A');
		equivalent.put('�', 'A');
		equivalent.put('�', 'A');
		equivalent.put('�', 'A');
		equivalent.put('�', 'A');
		equivalent.put('�', 'C');
		equivalent.put('�', 'E');
		equivalent.put('�', 'E');
		equivalent.put('�', 'E');
		equivalent.put('�', 'E');
		equivalent.put('�', 'I');
		equivalent.put('�', 'I');
		equivalent.put('�', 'I');
		equivalent.put('�', 'I');
		equivalent.put('�', 'O');
		equivalent.put('�', 'O');
		equivalent.put('�', 'O');
		equivalent.put('�', 'O');
		equivalent.put('�', 'O');
		equivalent.put('�', 'U');
		equivalent.put('�', 'U');
		equivalent.put('�', 'U');
		equivalent.put('�', 'U');
		equivalent.put('�', 'Y');
		equivalent.put('�', 'a');
		equivalent.put('�', 'a');
		equivalent.put('�', 'a');
		equivalent.put('�', 'a');
		equivalent.put('�', 'a');
		equivalent.put('�', 'a');
		equivalent.put('�', 'c');
		equivalent.put('�', 'e');
		equivalent.put('�', 'e');
		equivalent.put('�', 'e');
		equivalent.put('�', 'e');
		equivalent.put('�', 'i');
		equivalent.put('�', 'i');
		equivalent.put('�', 'i');
		equivalent.put('�', 'i');
		equivalent.put('�', 'o');
		equivalent.put('�', 'o');
		equivalent.put('�', 'o');
		equivalent.put('�', 'o');
		equivalent.put('�', 'o');
		equivalent.put('�', 'o');
		equivalent.put('�', 'u');
		equivalent.put('�', 'u');
		equivalent.put('�', 'u');
		equivalent.put('�', 'u');
		equivalent.put('�', 'y');
		equivalent.put('�', 'y');
		
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
