package servlets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.ResultSet;

import org.json.JSONObject;

import constants.Queries;
import dao.AccountsManager;
import databasemanager.QueryHandler;
import hashing.AESEncryption;
import hashing.PasswordHashing;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class ProfileEditServlet
 */
public class ProfileEditServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProfileEditServlet() {
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
		try {

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

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

				userId = AccountsManager.getUserId(cookieValue);

				response.getWriter().write(RequestHandler.sendResponse(200, "User details successfully received",
						AccountsManager.getUserDetails(userId)));

			} else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Login first"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

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

				userId = AccountsManager.getUserId(cookieValue);

				JSONObject req = new JSONObject(RequestHandler.getRequestObjectString(request));
				// TODO Auto-generated method stub

				String name = req.optString("name", "").trim();
				String password = req.optString("password", "").trim();
				String confirmPassword = req.optString("confirmPassword", "").trim();
				String timeZone = req.optString("timeZone", "").trim();

				if (name.length() < 3 || !name.matches("^[A-Za-z ]+$")) {
					response.getWriter().write(RequestHandler.sendResponse(400, "Invalid name"));
					return;
				}

				String encryptedPassword = null;

				if (!password.isEmpty() || !confirmPassword.isEmpty()) {

					if (!password.equals(confirmPassword)) {
						response.getWriter().write(RequestHandler.sendResponse(400, "Passwords mismatch"));
						return;
					}

					if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")
							|| !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")
							|| password.contains(" ")) {

						response.getWriter().write(RequestHandler.sendResponse(400, "Weak password"));
						return;
					}

					encryptedPassword =new PasswordHashing().passwordHashing(password);
				}

				if (AccountsManager.updateUser(name, timeZone, encryptedPassword, userId)) {
					response.getWriter().write(RequestHandler.sendResponse(200, "Profile updated"));
				} else {
					response.getWriter().write(RequestHandler.sendResponse(500, "Profile update failed"));
				}

			} else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Login first"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
	}

}
