package servlets;

import java.io.IOException;
import java.sql.SQLException;

import dao.ResourceManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.GetUserId;
import utils.RequestHandler;

/**
 * Servlet implementation class RestoreServlet
 */
//@WebServlet("/RestoreServlet")
public class RestoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RestoreServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileid = request.getParameter("fileId");
		String folderid = request.getParameter("folderId");
		String replace = request.getParameter("replace");
		String type = request.getParameter("type");
		
		
		boolean restoreToRoot = Boolean.parseBoolean(replace);
		long folderId = Long.parseLong(folderid);
		long fileId = Long.parseLong(fileid);
		
		boolean checkFile = ResourceManager.checkResource(fileId , "inactive");
		boolean checkFolder = ResourceManager.checkResource(folderId , "active");
		
		if(restoreToRoot) {
			long userId = GetUserId.getUserId(request);
			try {
				long rootId = ResourceManager.getMyFolderId(userId);
				boolean updateParent = ResourceManager.updateParent(fileId,rootId, userId);
				
				if(updateParent) {
					
					boolean res = false;
					
					if(type.equals("FILE")) {
						res = ResourceManager.updateFileStatus(fileId, "active");
					}
					else {
						res = ResourceManager.updateChild(fileId);
						ResourceManager.updateFolder(fileId);
					}
					if(res) {
						response.getWriter().write(RequestHandler.sendResponse(200, "File Restored"));
					}
					else {
						response.getWriter().write(RequestHandler.sendResponse(400, "File Restored failed"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		else if(checkFile&&checkFolder) {
			
			boolean res = false;
			
			if(type.equals("FILE")) {
				res = ResourceManager.updateFileStatus(fileId, "active");
			}
			else {
				res = ResourceManager.updateChild(fileId);
				ResourceManager.updateFolder(fileId);
			}
			
			if(res) {
				response.getWriter().write(RequestHandler.sendResponse(200, "File Restored"));
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File Restored failed"));
			}
		} 
		else {
			response.getWriter().write(RequestHandler.sendResponse(300, "File Restored failed"));
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
