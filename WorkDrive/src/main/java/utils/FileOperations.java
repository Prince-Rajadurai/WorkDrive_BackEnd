package utils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import databasemanager.QueryHandler;

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
	public static String CreateFile(String folderId ,String folderPath , String fileName) {
		try {
			fs.create(new Path(folderId+"/"+folderPath+"/"+fileName)).close();
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
        return "File created sucessfully";
	}
	
//	File upload -> change
	public static String UploadFile(String folderId , String folderPath , String localPath , String filename) {
		try {
			
			Path localFile = new Path(localPath);
	        Path folder = new Path(folderId+"/"+folderPath);
	        if(!fs.exists(folder)) {
	        	fs.mkdirs(folder);
	        }
	        Path hdfsPath = new Path(folderPath+"/"+filename);
	        fs.copyFromLocalFile(false, true, localFile, hdfsPath);
	        
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		
		return "File uploaded sucessfully";
	}
	
//	File delete
	public static String DeleteFile(String folderPath , String fileName) {
		
		Path file = new Path(folderPath+"/"+fileName); 
        try {
        	if(fs.exists(file))
        		fs.delete(file,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return "File deleted successfully";
	}
	
//	File download 
	public static String DownloadFile(String folderPath , String fileName) {
		
		Path hdfsFile = new Path(folderPath+"/"+fileName);

        
        Path localPath = new Path("/home/prince-zstk430/Pictures");

        try {
			fs.copyToLocalFile(false, hdfsFile, localPath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

        return "File downloaded successfully";
	}
	
//	File rename
	public static String renameFile(String olderPath , String newPath) throws IOException {
		
		Path olderName = new Path(olderPath);
		Path newName = new Path(newPath);
		
		fs.rename(olderName, newName);
		
		return "File renamed sucessfully";
	}

	
}

