package servlets;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class ShowTrash
 */
//@WebServlet("/ShowTrash")
public class ShowTrash extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ShowTrash() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<JSONObject> resources = null;
		try {
			long userId = GetUserId.getUserId(request);
			
			String timezone = ResourceManager.getUserTimeZone(userId);
			
			long allItems = ResourceManager.getTrashItems(userId);
			long files = ResourceManager.getParticularResource(userId, "FILE");
			long folders = ResourceManager.getParticularResource(userId, "FOLDER");
			long size = ResourceManager.getTrashSize(userId);
			
			resources = ResourceManager.showTrashItems(userId , timezone);
			
			response.getWriter().write(RequestHandler.sendResponse(200,"Trash Data fetched",allItems,files,folders,size,resources));
		} 
		catch (Exception e) {
			e.printStackTrace();
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
