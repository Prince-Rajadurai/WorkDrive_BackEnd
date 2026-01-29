package servlets;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RequestHandler;

/**
 * Servlet implementation class ShowFiles
 */
//@WebServlet("/ShowFiles")
public class ShowFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ShowFiles() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
		String folderid = requestObject.getString("folderId");
		long folderId = Long.parseLong(folderid);
		
		ArrayList<JSONObject> files = ResourceManager.getAllFiles(folderId);
		
			response.getWriter().write(RequestHandler.sendResponse(200, "Total number of files",files));
		
		}
		
		catch(Exception e) {
			
			response.getWriter().write(RequestHandler.sendResponse(400, "Server error"));
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
