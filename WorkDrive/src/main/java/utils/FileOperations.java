package utils;

import databasemanager.QueryHandler;

import java.awt.desktop.QuitHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import constants.ColumnNames;
import constants.Queries;
import dao.ResourceManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


public class FileOperations {

	static Configuration conf = new Configuration();
	static FileSystem fs = null;
	static {
		conf.addResource(new Path("/usr/local/hadoop/etc/hadoop/core-site.xml"));
		conf.addResource(new Path("/usr/local/hadoop/etc/hadoop/hdfs-site.xml"));
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	File creation -> change
	public static String CreateFile(String folderId, String fileName) {
		try {
			fs.create(new Path("/" + folderId + "/" + fileName));
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		return "File created sucessfully";
	}

//	File upload -> change
	public static String UploadFile(Part file, String folderId, String filename, String uploadId) {

		try {

			long uploadedBytes = 0;

			RedisHandler.setKey(uploadId, "0");

			InputStream in = file.getInputStream();
			Path hdfsPath = new Path("/" + folderId + "/" + filename);
			FSDataOutputStream out = fs.create(hdfsPath, true);
			ZstdOutputStream zOut = new ZstdOutputStream(out, 10);
//			GZIPOutputStream zOut = new GZIPOutputStream(out);

			byte[] buffer = new byte[16384];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) != -1) {
				zOut.write(buffer, 0, bytesRead);
				uploadedBytes += bytesRead;
				RedisHandler.setKey(uploadId, String.valueOf(uploadedBytes));
			}

			RedisHandler.setKey(uploadId, "DONE");

			zOut.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			return "File uploaded failed";
		}

		return "File upload successfully";
	}

//	File delete
	public static String DeleteFile(String filePath) {

		Path file = new Path(filePath);
		try {
			if (fs.exists(file))
				fs.delete(file, false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "File deleted successfully";
	}

//	File download 
	public static void DownloadFile(String filepath, String fileName, HttpServletResponse response) {

		Path path = new Path(filepath);

		FSDataInputStream hdfsIn = null;
		ZstdInputStream in = null;
		OutputStream out = null;
		

		try {
			hdfsIn = fs.open(path);
			in = new ZstdInputStream(hdfsIn);

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			out = response.getOutputStream();

			byte[] buffer = new byte[65536];
			int len;

			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("File download failed");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

//	File rename
//	public static String renameFile(String folderId, String olderFileName, String newFileName) throws IOException {
//
//		Path olderName = new Path("/" + folderId + "/" + olderFileName);
//		Path newName = new Path("/" + folderId + "/" + newFileName);
//
//		fs.rename(olderName, newName);
//
//		return "File renamed sucessfully";
//	}

// File copy operation
	public static boolean copyFile(String oldFolderId, String newFolderId, String filename) {

		Path sourcePath = new Path("/" + oldFolderId + "/" + filename);

		filename = CheckDuplicateFile.getFileName(Long.parseLong(newFolderId), filename);

		Path destinationPath = new Path("/" + newFolderId + "/" + filename);

		try {
			FileUtil.copy(fs, sourcePath, fs, destinationPath, false, fs.getConf());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

//	Folder size 
	public static String getFolderSize(long folderId) {

		String size;
		long fileSize = 0;
		Path file = null;
		FileStatus status = null;

		try {

			ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_FILES, new Object[] { folderId , "FILE" });
			String filePath = "";
			while (res.next()) {
				fileSize += res.getLong(ColumnNames.RESOURCE_ORIGINAL_SIZE);
			}

			double conversionVal = 1024.0;

	        double sizeVal = fileSize;
	        String[] units = {"B", "KB", "MB", "GB", "TB"};
	        int index = 0;

	        while (sizeVal >= 1024 && index < units.length - 1) {
	            sizeVal = sizeVal / 1024;
	            index++;
	        }

	        size = String.format("%.2f %s", sizeVal, units[index]);

	    } catch (Exception e) {
	        e.printStackTrace();
	        size = "0 B";
	    }

	    return size;
	}

//	File size
	public static String getFileSize(String filePath) throws IOException {

		Path file = new Path(filePath);
		FileStatus status = fs.getFileStatus(file);
		double conversionVal = 1024.0;

		    long fileSize = status.getLen();
		    if (fileSize <= 0) {
		        return "0 B";
		    }

		    final String[] units = {"B", "KB", "MB", "GB", "TB", "PB"};
		    double size = fileSize;
		    int unitIndex = 0;

		    while (size >= 1024 && unitIndex < units.length - 1) {
		        size /= 1024.0;
		        unitIndex++;
		    }

		    return String.format("%.2f %s", size, units[unitIndex]);

	}

//	Get file byte size
	public static long getSize(String filePath) {

		long fileSize = 0;

		try {
			Path file = new Path(filePath);
			FileStatus status = fs.getFileStatus(file);
			fileSize = status.getLen();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileSize;
	}

	public static String converFileSizeToString(long fileSize) {

	    if (fileSize <= 0) {
	        return "0 B";
	    }

	    final String[] units = {"B", "KB", "MB", "GB", "TB", "PB"};
	    double size = fileSize;
	    int unitIndex = 0;

	    while (size >= 1024 && unitIndex < units.length - 1) {
	        size /= 1024.0;
	        unitIndex++;
	    }

	    return String.format("%.2f %s", size, units[unitIndex]);
	}

//	public static boolean moveFile(String oldFolder, String newFolder, String filename , long fileId) throws IOException {
//
//		boolean copy = copyFile(oldFolder, newFolder, filename);
//		String filePath = ResourceManager.getFilePath(fileId);
//		String delete = DeleteFile(filePath);
//
//		return copy && delete.equals("File deleted successfully");
//	}

}