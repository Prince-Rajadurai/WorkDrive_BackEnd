package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import databasemanager.QueryHandler;
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
			fs.create(new Path("/"+folderId+"/"+fileName));
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
        return "File created sucessfully";
	}
	
//	File upload -> change
	public static String UploadFile(int chunkIndex, Part file, String folderId, String filename) {
	    
		try {
			InputStream in = file.getInputStream();
			Path hdfsPath = new Path("/"+folderId+"/"+filename);
			FSDataOutputStream out = null;
			if(!fs.exists(hdfsPath)) {
				out = fs.create(hdfsPath);
				IOUtils.copyBytes(in, out, conf);
			}
			else {
				out = fs.append(hdfsPath);
				IOUtils.copyBytes(in, out, conf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	    return "File uploaded sucessfully";
	}

	
//	File delete
	public static String DeleteFile(String folderId , String fileName) {
		
		Path file = new Path("/"+folderId+"/"+fileName); 
        try {
        	if(fs.exists(file))
        		fs.delete(file,false);
        	else {
        		System.out.println(false);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return "File deleted successfully";
	}
	
//	File download 
	public static String DownloadFile(String folderId , String fileName) {
		
		Path hdfsFile = new Path("/"+folderId+"/"+fileName);

        
        Path localPath = new Path("/home/prince-zstk430/Downloads");

        try {
			fs.copyToLocalFile(false, hdfsFile, localPath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

        return "File deleted successfully";
	}
	
//	File rename
	public static String renameFile(String folderId,String olderFileName , String newFileName) throws IOException {
		
		Path olderName = new Path("/"+folderId+"/"+olderFileName);
		Path newName = new Path("/"+folderId+"/"+newFileName);
		
		fs.rename(olderName, newName);
		
		return "File renamed sucessfully";
	}
// File copy operation
	public static void copyFile( String oldFolderId, String newFolderId , String filename) {
		
		Path sourcePath = new Path(oldFolderId + "/" + filename);

	    Path destinationPath = new Path(newFolderId+"/"+ filename);

	    try {
	        FileUtil.copy(fs, sourcePath, fs, destinationPath, false, fs.getConf());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
//	Folder size 
	public static String getFolderSize(String folderId) throws IOException {
		
		Path file = new Path("/"+folderId);
		FileStatus status = fs.getFileStatus(file);
		
		long fileSize = status.getLen();
		String size = fileSize+"B";
		
		if(fileSize>=1024) {
			fileSize = fileSize/1000;
			size = fileSize+"KB";
			if(fileSize>=1024) {
				fileSize = fileSize/1000;
				size = fileSize+"MB";
				if(fileSize>=1024) {
					fileSize = fileSize/1000;
					size = fileSize+"GB";
				}
			}
		}
		
		return size;
		
	}
	
//	File size
	public static String getFileSize(String filePath) throws IOException {
		
		Path file = new Path("/"+filePath);
		FileStatus status = fs.getFileStatus(file);
		
		long fileSize = status.getLen();
		String size = fileSize+"B";
		System.out.println(size);
		
		if(fileSize>=1024) {
			fileSize = fileSize/1000;
			size = fileSize+"KB";
			if(fileSize>=1024) {
				fileSize = fileSize/1000;
				size = fileSize+"MB";
				if(fileSize>=1024) {
					fileSize = fileSize/1000;
					size = fileSize+"GB";
				}
			}
		}
		
		return size;
		
	}
	
}

