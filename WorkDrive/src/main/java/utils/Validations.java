package utils;

import constants.RegexChecker;

public class Validations {
	
	public static boolean lengthValidation(String fullName,int length) {
		return fullName.trim().length() > 3;
	}
	
	public static boolean isValidEmail(String email) {
		return email.matches(RegexChecker.EMAIL_CHECK);
	}
	
	public static boolean passwordValidation(String password) {
		return !lengthValidation(password,8) || !password.matches(RegexChecker.UPPERCASE_CONTAINS)
				|| !password.matches(RegexChecker.LOWERCASE_CONTAINS)
				|| !password.matches(RegexChecker.SPECIAL_CHARACTER_CONTAINS);
	}

}
