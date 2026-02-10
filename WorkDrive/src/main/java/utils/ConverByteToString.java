package utils;

import java.security.MessageDigest;

public class ConverByteToString {

	public static String convertByteToString(MessageDigest md) {
		
		StringBuilder sb = new StringBuilder();
		
		for(byte digit:md.digest()) {
			sb.append(String.format("%02x", digit));
		}
		
		return sb.toString();
		
	}
	
}
