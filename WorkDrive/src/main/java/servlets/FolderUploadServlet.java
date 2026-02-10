package servlets;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class FolderUploadServlet
 */
@MultipartConfig
public class FolderUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public FolderUploadServlet() {
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
		// TODO Auto-generated method stub
		String parentId = request.getParameter("parentId");
		
		System.out.println(parentId);
		Collection<Part> parts = request.getParts();

		for (Part part : parts) {
		    if (part.getName().equals("files")) {
		        String fileName = part.getSubmittedFileName();
		        long size = part.getSize();

		        System.out.println("File name: " + fileName);
		        System.out.println("Size: " + size);

		        // InputStream is = part.getInputStream();
		        // save file logic here
		    }
		}

	}

}
