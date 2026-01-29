package filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
import utils.RequestHandler;

import java.io.IOException;
import java.sql.ResultSet;

import org.json.JSONObject;

import constants.Queries;
import databasemanager.QueryHandler;
import hashing.AESEncryption;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * Servlet Filter implementation class SessionCheckFilter
 */
public class SessionCheckFilter extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public SessionCheckFilter() {
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
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		
		Cookie[] cookie = req.getCookies();
		String encrtyptMail = "";
		if (cookie != null) {
	        for (Cookie c : cookie) {
	            if ("cookie".equals(c.getName())) {  
	            	encrtyptMail = c.getValue();
	                break;
	            }
	        }
	    }
		
		String responseString = null;
		
		try {
			String validMail = new AESEncryption().decrypt(encrtyptMail);
			ResultSet result = QueryHandler.executeQuerry(Queries.CHECK_DUPLICATE_USER, new Object[] {validMail});
			if(result.next()) {
				responseString = RequestHandler.sendResponse(200, "Session exsist");
			}
			else {
				responseString = RequestHandler.sendResponse(400, "Session empty");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.getWriter().write(responseString);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
