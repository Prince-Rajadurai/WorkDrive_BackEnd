package servlets;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ResourceManager;

/**
 * Servlet implementation class BreadCrumbServlet
 */
public class BreadCrumbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public BreadCrumbServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String folderIdParam = request.getParameter("folderId");
		JSONArray breadCrumb = new JSONArray();
		try {
			if (folderIdParam != null && !folderIdParam.equals("null")) {
				long currentId = Long.parseLong(folderIdParam);
				while (true) {
					JSONObject folder = ResourceManager.getResourceBasicInfo(currentId);
					if (folder == null) break;
					breadCrumb.put(folder);
					if (!folder.has("parentId") || folder.isNull("parentId")) break;
					currentId = folder.getLong("parentId");
				}
				JSONArray reversed = new JSONArray();
				for (int i = breadCrumb.length() - 1; i >= 0; i--) {
					reversed.put(breadCrumb.get(i));
				}
				breadCrumb = reversed;
			}
			response.setContentType("application/json");
			response.getWriter().write(breadCrumb.toString());
		} catch (Exception e) {
			response.sendError(500, "Failed to load bread crumb");
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