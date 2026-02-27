package servlets;

import java.io.IOException;
import org.json.JSONObject;

import constants.RegexChecker;
import dao.AccountsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class LoginServlet
 */
//@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));

			String email = requestObject.getString("email").toLowerCase();
			String password = requestObject.getString("password");

			if (email.isEmpty()) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Email is required"));
				return;
			}
			if (!email.matches(RegexChecker.EMAIL_CHECK)) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Invalid email format"));
				return;
			}
			if (password.isEmpty()) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Password is required"));
				return;
			}
			if (password.length() < 8) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Password must be at least 8 characters"));
				return;
			}
			
			boolean isValidUser = false;
			
			try {
				System.out.println("Hello");
				isValidUser = AccountsManager.validateUser(email, password);
				System.out.println(isValidUser);

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
			}
			
			if (!isValidUser) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Invalid email or password"));
				return;
			}
			
			

			response.addCookie(RequestHandler.setCookie(email));
			response.getWriter().write(RequestHandler.sendResponse(200, "Login successful"));

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
	}

}
