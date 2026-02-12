package utils;

import dao.AccountsManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class GetUserId {
	
	public static long getUserId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = null;
		long userId = 0;

		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if ("cookie".equals(cookie.getName())) {
		            cookieValue = cookie.getValue();
		            break;
		        }
		    }
		}
		try {
			userId = AccountsManager.getUserId(cookieValue);
			System.out.print(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userId;
	}

}
