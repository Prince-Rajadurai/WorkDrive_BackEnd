package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import hashing.AESEncryption;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

	public static String sendResponse(int statusCode, String message) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		return responseObject.toString();
	}

	public static String sendResponse(int statusCode, String message, ArrayList<JSONObject> resource, String parentId,
			long nextCursor) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resources", resource);
		responseObject.put("folderId", parentId);
		responseObject.put("nextCursor", nextCursor);
		return responseObject.toString();
	}

	public static String sendResponse(int statusCode, String message, ArrayList<JSONObject> resource, String parentId,
			JSONObject cursors) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resources", resource);
		responseObject.put("folderId", parentId);
		responseObject.put("cursors", cursors);
		return responseObject.toString();
	}

	public static String sendResponse(int statusCode, String message, ArrayList<JSONObject> resources) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resources", resources);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode, String message, long items , long files , long folders , long size , ArrayList<JSONObject> resources) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resources", resources);
		responseObject.put("totalItems", items);
		responseObject.put("files", files);
		responseObject.put("folders", folders);
		responseObject.put("size", FileOperations.converFileSizeToString(size));
		return responseObject.toString();
	}

	public static String sendResponse(int statusCode, String message, JSONObject resource) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("message", message);
		responseObject.put("resource", resource);
		return responseObject.toString();
	}

	public static String sendResponse(int statusCode, String storage, String size, ArrayList<JSONObject> versions) {
		JSONObject responseObject = new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("storage", storage);
		responseObject.put("size", size);
		responseObject.put("versions", versions);
		return responseObject.toString();
	}
	
	public static String sendResponse(int statusCode , String total_size , String compress_size , long total_files , long deduplicate_files , float storage_precentage , float deduplicate_files_precentage , float deduplicate_size_percentage) {
		JSONObject responseObject=new JSONObject();
		responseObject.put("StatusCode", statusCode);
		responseObject.put("compress_size", total_size);
		responseObject.put("total_size", compress_size);
		responseObject.put("total_files", total_files);
		responseObject.put("deduplicate_files", deduplicate_files);
		responseObject.put("storage_precentage", Math.floor(storage_precentage));
		responseObject.put("deduplicate_files_precentage", 100-Math.floor(deduplicate_files_precentage));
		responseObject.put("deduplicate_size_percentage", Math.floor(deduplicate_size_percentage));
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

	public static boolean deleteCookie(HttpServletResponse response) {
			Cookie cookie = new Cookie("cookie", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			return true;

	}

}
