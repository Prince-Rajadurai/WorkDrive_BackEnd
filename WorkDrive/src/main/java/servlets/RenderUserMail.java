package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;
import hashing.AESEncryption;
import jakarta.servlet.http.Cookie;

/**
 * Servlet implementation class RenderUserMail
 */
public class RenderUserMail extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RenderUserMail() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		String cookieValue = null;
		String userMail = null;
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if ("cookie".equals(cookie.getName())) {
		            cookieValue = cookie.getValue();
		            break;
		        }
		    }
		}
		
		try {
			userMail = new AESEncryption().decrypt(cookieValue);
			response.getWriter().write(RequestHandler.sendResponse(200, userMail));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
