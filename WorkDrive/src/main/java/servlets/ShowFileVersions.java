package servlets;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class ShowFileVersions
 */
//@WebServlet("/ShowFileVersions")
public class ShowFileVersions extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ShowFileVersions() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileid = request.getParameter("fileId");
		long fileId = Long.parseLong(fileid);
		long dfsId = ResourceManager.findDfsId(fileId);
		long StorageSize = ResourceManager.getFileSize(fileId);
		long userId = GetUserId.getUserId(request);
		
		String filePath = ResourceManager.getFilePath(fileId);
		
		String timeZone = ResourceManager.getUserTimeZone(userId);
		ArrayList<JSONObject> fileVersions = ResourceManager.getAllVersions(dfsId , timeZone);
		
		response.getWriter().write(RequestHandler.sendResponse(200,FileOperations.converFileSizeToString(StorageSize),FileOperations.getFileSize(filePath),fileVersions));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
