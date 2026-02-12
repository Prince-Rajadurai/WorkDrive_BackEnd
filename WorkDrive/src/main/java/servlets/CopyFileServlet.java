package servlets;

import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.CheckDuplicateFile;
import utils.FileOperations;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class CopyFileServlet
 */
//@WebServlet("/CopyFileServlet")
public class CopyFileServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CopyFileServlet() {
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
		
		long userId = GetUserId.getUserId(request);
		
		long fileId = ResourceManager.findFileIdUsingFilename(oldFolderId, filename);
		
		boolean fileCopyResult = ResourceManager.fileCopy(oldFolderId, newFolderId, filename , userId , fileId);
		
		if(fileCopyResult) {
			
			response.getWriter().write(RequestHandler.sendResponse(200, "File copied successfully"));
			
		}
		else {
			response.getWriter().write(RequestHandler.sendResponse(200, "File copied failed"));
		}
		
		
		
	}

}
