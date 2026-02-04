package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Types;

import org.json.JSONObject;

import dao.AccountsManager;
import dao.ResourceManager;
import hashing.AESEncryption;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderAddServlet
 */
public class FolderAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FolderAddServlet() {
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

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
			String resourceName = requestObject.getString("resourceName");

			
			long parentId = Long.parseLong(requestObject.getString("parentId"));

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
			}
			
			if (!ResourceManager.existResourceName(userId, parentId, resourceName)) {
				JSONObject resource = ResourceManager.addResource(resourceName, parentId, userId);

				if (resource != null) {
					response.getWriter().write(RequestHandler.sendResponse(200, "Resource Added successfully", resource));
				} else {
					response.getWriter().write(
							RequestHandler.sendResponse(400, "Failed to add resource (Something wrong with Server)"));
				}
			} else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Folder Name already exist"));

			}

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
	}

}
