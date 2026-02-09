package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import hashing.AESEncryption;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class RequestHandler {

	public static String getRequestObjectString(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
	public static String getRequestObjectString(ServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
	
	public static String sendResponse(int statusCode,String message) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode,String message,ArrayList<JSONObject> resource, String parentId) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resource", resource);
		responseObject.put("folderId", parentId);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode,String message,JSONObject resource, String parentId) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resource", resource);
		responseObject.put("folderId", parentId);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode,String message,ArrayList<JSONObject> resources) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resources", resources);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode,String message,JSONObject resource) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resource", resource);
		return responseObject.toString();
	}
	
	
	public static Cookie setCookie(String email) throws Exception {
		String encryptMail = new AESEncryption().encrypt(email);
		Cookie cookie = new Cookie("cookie", encryptMail);

		cookie.setMaxAge(60 * 60 * 24);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		return cookie;
	}

}
