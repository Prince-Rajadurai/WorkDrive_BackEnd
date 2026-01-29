package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.ResourceManager;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderDeleteServlet
 */
public class FolderDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FolderDeleteServlet() {
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
			long resourceId = Long.parseLong(requestObject.getString("resourceId"));
			if (ResourceManager.deleteResource(resourceId)) {
				response.getWriter().write(RequestHandler.sendResponse(200, "Resource deleted successfully"));
			} else {
				response.getWriter().write(
						RequestHandler.sendResponse(400, "Failed to remove resource (Something wrong with Server)"));
			}

		} catch (Exception e) {
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));

		}
	}


}
