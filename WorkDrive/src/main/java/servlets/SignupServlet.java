package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;
import utils.Validations;

import org.json.JSONObject;
import dao.AccountsManager;
import dao.ResourceManager;
import hashing.AESEncryption;

/**
 * Servlet implementation class SignUpServlet
 */

//@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {

//	Query query = (Query) getServletContext().getAttribute("query");
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SignupServlet() {
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

		try {
			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));

			String fullName = requestObject.getString("fullName");
			String email = requestObject.getString("email").toLowerCase();
			String password = requestObject.getString("password");
			String confirmPassword = requestObject.getString("confirmPassword");
			boolean termsAccepted = requestObject.getBoolean("terms");
			String timeZone=requestObject.getString("timezone");

			// input validations
			if (!Validations.lengthValidation(fullName, 3)) {
				response.getWriter()
						.write(RequestHandler.sendResponse(400, "Full Name must be at least 3 characters"));
				return;
			}

			if (!Validations.isValidEmail(email)) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Enter a valid e-mail address"));
				return;
			}

			if (Validations.passwordValidation(password)) {
				response.getWriter().write(
						RequestHandler.sendResponse(400, "Password must be strong (8 chars, upper, lower, special)"));
				return;
			}

			if (!password.equals(confirmPassword)) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Confirm Password does not match"));
				return;
			}

			if (!termsAccepted) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Accept Terms & Privacy Policy"));
				return;
			}

			// Email existing check
			if (AccountsManager.isDuplicateUser(email)) {
				response.getWriter().write(RequestHandler.sendResponse(400, "Email already registered"));
				return;
			}

			// inserting valid user
			if (AccountsManager.addUser(email,fullName,password,timeZone)) {
				ResourceManager.addResource("MYFOLDER", null, AccountsManager.getUserId(new AESEncryption().encrypt(email)));
				response.getWriter().write(RequestHandler.sendResponse(200, "Signup successful"));
			} else {
				response.getWriter()
						.write(RequestHandler.sendResponse(400, "Failed to add User (Something wrong with Server)"));
			}
            
            response.addCookie(RequestHandler.setCookie(email));
			
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
	}

}
