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
 * Servlet implementation class TrashFile
 */
//@WebServlet("/TrashFile")
public class TrashFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TrashFile() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
		String type = requestObject.getString("type");
		
		if(type.equals("FILE")) {
			String id = requestObject.getString("resourceId");
			String folderid = requestObject.getString("folderId");
			
			long folderId = Long.parseLong(folderid);
			long fileId = Long.parseLong(id);
			
			
			boolean trashRes = ResourceManager.updateFileStatus(fileId,"inactive");
			
			if(trashRes) {
				response.getWriter().write(RequestHandler.sendResponse(200, "File trashed"));
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File trashed failed"));
			}
		}
		else {
			String folderid = requestObject.getString("resourceId");
			long resourceId = Long.parseLong(folderid);
			
			boolean trashRes =  ResourceManager.updateFolderStatus(resourceId);
			if(trashRes) {
				response.getWriter().write(RequestHandler.sendResponse(200, "Folder trashed"));
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "Folder trashed failed"));
			}
		}
	}

}
