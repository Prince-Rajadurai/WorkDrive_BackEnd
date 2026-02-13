package servlets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;

import org.json.JSONObject;

import dao.AccountsManager;
import dao.ResourceManager;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class CopyResourceServlet
 */
public class CopyResourceServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CopyResourceServlet() {
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

			long parentId = requestObject.getLong("parentId");
			long resourceId = requestObject.getLong("resourceId");
			String resourceName = requestObject.getString("resourceName");

			long userId = 0;

			Cookie[] cookies = request.getCookies();
			String cookieValue = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("cookie".equals(cookie.getName())) {
						cookieValue = cookie.getValue();
						break;
					}
				}
				userId = AccountsManager.getUserId(cookieValue);
			}
			
			

			if (!ResourceManager.sameFolder(parentId, resourceId)) {

				String finalName = resourceName.replaceAll("\\(\\d+\\)$", "");
				int counter = 1;

				while (ResourceManager.existResourceName(userId, parentId, finalName)) {
					finalName = resourceName + "(" + counter + ")";
					counter++;
				}
				ResourceManager.copyFolder(parentId, resourceId, finalName, userId);

				response.getWriter().write(RequestHandler.sendResponse(200, "Folder moved as " + finalName));
			} else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Folder is placed in same place"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}

	}

}
