package servlets;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.RequestHandler;

/**
 * Servlet implementation class FolderUploadServlet
 */

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
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        
        
        System.out.println("hello");
        
        String parentId = request.getParameter("parentId");
        
        System.out.println(request.getParts());
        
        System.out.println("Received parentId: " + parentId);
        
        Collection<Part> parts = request.getParts();
        Iterator<Part> partIterator = parts.iterator();

        while (partIterator.hasNext()) {
            Part part = partIterator.next();
            
            // Only handle file parts (skip other parts like "parentId")
            if (part.getName().equals("files")) {
            	String relativePath = part.getSubmittedFileName();
                String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);

                // Print out the file info (filename and relative path)
                System.out.println("Received file: " + fileName);
                System.out.println("Relative path: " + relativePath);

                // Print out the content-disposition header to see the form data
                System.out.println("Content-Disposition: " + part.getHeader("Content-Disposition"));
            }
        }
        
        
	}
	

}
