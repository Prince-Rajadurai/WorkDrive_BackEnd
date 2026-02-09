package servlets;



import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import utils.RequestHandler;
import dao.AccountsManager;
import dao.ResourceManager;

/**
* Servlet implementation class ShowFiles
*/

public class ResourceRenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
	
	public ResourceRenderServlet() {
		super();
		// TODO Auto-generated constructor stub
    }
	

	/**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		try {
			Long parentId;
			long userId = 0;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("cookie".equals(cookie.getName())) {
						userId = AccountsManager.getUserId(cookie.getValue());
						break;
					}
				}
			}
			String parentIdParam = request.getParameter("parentId");
			if (parentIdParam != null && !parentIdParam.equals("null") && !parentIdParam.isEmpty()) {
				parentId = Long.parseLong(parentIdParam);
			} else {
				parentId = ResourceManager.getMyFolderId(userId);
			}
			String folderCursorParam = request.getParameter("folderCursor");
			String fileCursorParam = request.getParameter("fileCursor");
			String limitParam = request.getParameter("limit");
			long folderCursor = folderCursorParam != null ? Long.parseLong(folderCursorParam) : 0;
			long fileCursor = fileCursorParam != null ? Long.parseLong(fileCursorParam) : 0;
			int limit = limitParam != null ? Integer.parseInt(limitParam) : 20;
			ArrayList<JSONObject> folders = ResourceManager.getResource(parentId, userId, folderCursor, limit);
			ArrayList<JSONObject> files = ResourceManager.getAllFiles(parentId, fileCursor, limit);
			ArrayList<JSONObject> resources = new ArrayList<>();
			long lastFolderCursor = folderCursor;
			long lastFileCursor = fileCursor;
			for (JSONObject folder : folders) {
				folder.put("type", "FOLDER");
				folder.put("id", String.valueOf(folder.getLong("resourceId")));
				resources.add(folder);
				lastFolderCursor = folder.getLong("resourceId");
			}
			for (JSONObject file : files) {
				file.put("type", "FILE");
				file.put("id", file.getLong("id"));
				resources.add(file);
				lastFileCursor = file.getLong("id");
			}
			JSONObject responseJson = new JSONObject();
			responseJson.put("resources", resources);
			responseJson.put("parentId", parentId);
			responseJson.put("nextFolderCursor", lastFolderCursor);
			responseJson.put("nextFileCursor", lastFileCursor);
			System.out.println(resources);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			System.out.println(files);
			System.out.println(parentId);
//            response.getWriter().write(RequestHandler.sendResponse(200, "Resources rendered successfully", resources , String.valueOf(parentId)));
			response.getWriter().write(RequestHandler.sendResponse(200, "Resources rendered successfully", responseJson, String.valueOf(parentId)));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "Failed to render resource"));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	*      response)
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}