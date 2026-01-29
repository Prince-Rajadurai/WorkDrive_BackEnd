package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.AccountsManager;
import dao.ResourceManager;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderGetServlet
 */
public class FolderGetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FolderGetServlet() {
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
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
			

			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));

			Long parentId = null;


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
			
			if (!requestObject.isNull("parentId")) {
				parentId = Long.parseLong(requestObject.getString("parentId"));
			}else {
				parentId = ResourceManager.getMyFolderId(userId);
			}

			ArrayList<JSONObject> resources = ResourceManager.getResource(parentId, userId);
			ArrayList<JSONObject> files = ResourceManager.getAllFiles(parentId);
			

			response.getWriter().write(RequestHandler.sendResponse(200, "Resource passed Successfully", resources));
		} catch (Exception e) {

			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));

		}

	}

}
