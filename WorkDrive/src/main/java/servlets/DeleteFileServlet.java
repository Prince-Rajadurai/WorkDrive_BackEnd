package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.RequestHandler;

/**
 * Servlet implementation class DeleteFileServlet
 */
public class DeleteFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public DeleteFileServlet() {
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
		String fileName = requestObject.getString("filename");
		String folderid = requestObject.getString("folderId");
		long folderId = Long.parseLong(folderid);
		
		long fileId = ResourceManager.findFileIdUsingFilename(folderId, fileName);
		String path = ResourceManager.getFilePath(fileId);
		
		int pathCount = ResourceManager.checkExistPaths(path);
		
		if(pathCount==1) {
			String result = FileOperations.DeleteFile(path);
		}
			
		boolean res = ResourceManager.deleteFile(fileId);
		if(res) {
			response.getWriter().write(RequestHandler.sendResponse(200, "file deleted"));
		}
		else {
			response.getWriter().write(RequestHandler.sendResponse(400, "file deleted failed"));
		}
		
		
	}

}
