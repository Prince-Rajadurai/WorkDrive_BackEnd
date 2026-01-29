package filter;

import jakarta.servlet.http.HttpFilter;
import utils.FileOperations;
import utils.RequestHandler;

import java.io.IOException;

import org.json.JSONObject;

import dao.ResourceManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class DuplicateFileCheck
 */
@WebFilter("/creation/DuplicateFileCheck")
public class DuplicateFileCheck extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public DuplicateFileCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			
			JSONObject requestObject = new JSONObject(RequestHandler.getRequestObjectString(request));
					
			String path = requestObject.getString("path");
			String fileName = requestObject.getString("filename");
			long folderId = requestObject.getLong("folderId");
			boolean replaceFile = requestObject.getBoolean("change"); 
			
			if(ResourceManager.duplicateFile(folderId, fileName)&&replaceFile) {
				FileOperations.DeleteFile(path, fileName);
				ResourceManager.deleteFile(folderId, path);
				request.setAttribute("fileDetails", requestObject);
				chain.doFilter(request, response);
			}
			else if(!(ResourceManager.duplicateFile(folderId, fileName))) {
				request.setAttribute("fileDetails", requestObject);
				chain.doFilter(request, response);
			}
			else {
				response.getWriter().write(RequestHandler.sendResponse(400, "File already exist"));
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			response.getWriter().write(RequestHandler.sendResponse(500, "server error"));
		}
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
