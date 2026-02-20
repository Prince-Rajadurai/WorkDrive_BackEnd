package servlets;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.RedisHandler;
import utils.RequestHandler;

/**
 * Servlet implementation class UploadProgressServlet
 */
public class UploadProgressServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadProgressServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String uploadId = request.getParameter("uploadId");
			String progress = RedisHandler.getKey(uploadId);

			if (progress.equals("DONE")) {
				RedisHandler.expireKey(uploadId, 30);
				response.getWriter().write(RequestHandler.sendResponse(200, "Successfully Uploaded"));
			} else {
				response.getWriter().write(RequestHandler.sendResponse(200, "In-Progress ", progress));
			}
		} catch (Exception e) {
			response.getWriter().write(RequestHandler.sendResponse(200, "In-Progress ", "0"));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
