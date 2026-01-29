package servlets;

import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderUpdateServlet
 */
public class FolderUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public FolderUpdateServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
			String newResourceName = requestObject.getString("newName");
			long resourceId=Long.parseLong(requestObject.getString("resourceId"));
			
			if(ResourceManager.updateResource(newResourceName,resourceId)) {
				response.getWriter().write(RequestHandler.sendResponse(200, "Resource updated successfully"));
			}else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Failed to Update resource (Something wrong with Server)"));
			}
			
		}catch(Exception e) {
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
			
		}
	}

}
