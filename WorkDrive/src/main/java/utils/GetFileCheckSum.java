package utils;

import java.io.InputStream;
import java.security.MessageDigest;

import jakarta.servlet.http.Part;

public class GetFileCheckSum {
	
	public static MessageDigest getFileCheckSumValue(Part file) {
		MessageDigest md = null;
		try {
			InputStream in = file.getInputStream();
			md = MessageDigest.getInstance("SHA-256");
			byte[] buffer = new byte[16384];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				md.update(buffer , 0 , bytesRead);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return md;
		
	}

}
