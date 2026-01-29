package servlets;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.FileOperations;
import utils.RequestHandler;

/**
 * Servlet implementation class DownloadFileServlet
 */
public class DownloadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public DownloadFileServlet() {
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
		JSONObject responseObject = new JSONObject();
				
		String path = requestObject.getString("path");
		String fileName = requestObject.getString("filename");
		String result = FileOperations.DownloadFile(path, fileName);
		
		if(result.equals("File deleted successfully")) {
			
			responseObject.put("Status Code", 200);
			responseObject.put("message", "File downloaded sucessfully");
			
		}
		else {
			responseObject.put("Status Code", 400);
			responseObject.put("message", "File download failed");
		}
		
		response.getWriter().write(responseObject.toString());
	}

}
