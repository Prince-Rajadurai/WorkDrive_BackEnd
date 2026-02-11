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
 * Servlet implementation class MoveFileServlet
 */
//@WebServlet("/MoveFileServlet")
public class MoveFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MoveFileServlet() {
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
		
		JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
		
		String filename = requestObject.getString("filename");
		
		
		String oldFolderid = requestObject.getString("oldFolderId");
		
		String newFolderid = requestObject.getString("newFolderId");
		
		long oldFolderId = Long.parseLong(oldFolderid);
		
		long newFolderId = Long.parseLong(newFolderid);
		
		long fileId = ResourceManager.findFileId(oldFolderId, filename);
		
		
		
		boolean moveFileResult = ResourceManager.fileMove(fileId, oldFolderId, newFolderId, filename);
		
		if(moveFileResult) {
			response.getWriter().write(RequestHandler.sendResponse(200, "File moved successfully"));
		}
		else {
			response.getWriter().write(RequestHandler.sendResponse(400, "File moved failed"));
		}
		
	}

}
