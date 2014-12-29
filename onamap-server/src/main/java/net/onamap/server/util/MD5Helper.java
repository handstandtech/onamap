package net.onamap.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Computes an MD5 digest
 * 
 * @author Sam Edwards
 */
public class MD5Helper {

	/**
	 * Create an MD5 digest from the input string.
	 * 
	 * @param s
	 * @return
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String doDigest(String s) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(s.getBytes());
			byte[] messageDigest = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
