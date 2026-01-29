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
 * Servlet implementation class CreateFileServlet
 */
public class CreateFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CreateFileServlet() {
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
		
		JSONObject requestObject = (JSONObject) request.getAttribute("fileDetails");
		JSONObject responseObject = new JSONObject();
				
		String path = requestObject.getString("path");
		String fileName = requestObject.getString("filename");
		String folderid = requestObject.getString("folderId");
		long folderId = Long.parseLong(folderid);
		String result = FileOperations.CreateFile(path, fileName);
		
		if(result.equals("File created sucessfully")) {
				
			boolean res = ResourceManager.AddFile(folderId, path, fileName);
			response.getWriter().write(RequestHandler.sendResponse(200, "File added sucessfully"));
				
		}
		else {
			response.getWriter().write(RequestHandler.sendResponse(500, "Server error"));
		}
		
	}

}
