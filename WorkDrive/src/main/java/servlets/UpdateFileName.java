package servlets;

import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.FileRename;
import utils.RequestHandler;

/**
 * Servlet implementation class UpdateFileName
 */
//@WebServlet("/UpdateFileName")
public class UpdateFileName extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UpdateFileName() {
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
		
		String newFileName = requestObject.getString("newFileName");
		String folderid = requestObject.getString("folderId");
		long folderId = Long.parseLong(folderid);
		String olderFileName = requestObject.getString("olderFileName");
		
		long fileId = ResourceManager.findFileId(folderId, olderFileName);
		
//		String res =  FileOperations.renameFile( folderid , olderFileName, FileRename.getFileName(newFileName)+"."+FileRename.getFileExtension(olderFileName));
			
		boolean result = ResourceManager.renameFile(FileRename.getFileName(newFileName)+"."+FileRename.getFileExtension(olderFileName), fileId , folderId);
		if(result) {
			response.getWriter().write(RequestHandler.sendResponse(200, "File renamed"));
			}
			
		else {
			response.getWriter().write(RequestHandler.sendResponse(400, "File renamed failed"));
		}
		
	}

}
