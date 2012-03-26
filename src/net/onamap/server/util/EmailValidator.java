package net.onamap.server.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {
	public static boolean isValidEmailAddress(String email) {
		if (email == null)
			return false;
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			if (!hasNameAndDomain(email)) {
				result = false;
			}
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private static boolean hasNameAndDomain(String aEmailAddress) {
		String[] tokens = aEmailAddress.split("@");
		return tokens.length == 2 && !isNullOrEmpty(tokens[0])
				&& !isNullOrEmpty(tokens[1]);
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}

}
