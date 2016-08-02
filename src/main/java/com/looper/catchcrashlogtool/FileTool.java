package com.looper.catchcrashlogtool;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileTool {
	
	private static String PATH;
	public static String ZipPath;
	
	public static String CreateNewFileDir() {
		String path = null;
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)) {
			String SdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			if(!TextUtils.isEmpty(SdPath)) {
				File newFileDir = new File(SdPath+"/CrashInfoLog");
				if(!newFileDir.exists()) {
					newFileDir.mkdir();
				}
				path = newFileDir.getAbsolutePath();
			}
		}
		return path;
	}
	
	
	public static String CreateNewFile(String FilePath,String filename){
		String path = null;
		if(!TextUtils.isEmpty(filename)) {
			File newFile = new File(FilePath,filename+".txt");
			if(!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			path = newFile.getAbsolutePath();
			PATH = path;
		}
		return path;
	}


	public  static void  ZipFile() {
		String filename = String.valueOf(System.currentTimeMillis());
		File file = new File(Cclt.crashinfo);
		File zipFile = new File(Cclt.crashinfo + filename + ".zip");
		InputStream input = null;
		try {
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.setComment(file.getName());
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					input = new FileInputStream(files[i]);
					zipOut.putNextEntry(new ZipEntry(files[i].getName()));
					int temp = 0;
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
			}
			zipOut.close();
			ZipPath = zipFile.getAbsolutePath();
			if (file.exists()) {
				if (file.isDirectory()) {
					for (File f:file.listFiles()) {
						if (!f.getAbsolutePath().contains("zip")) {
							f.delete();
						}
					}
				}
			}
		}catch (Exception e) {
		}
	}


	
	
	public static void Data2File(String data) {
	     try {
	    	    FileWriter fw = new FileWriter(PATH,true);
	            fw.write(data);
	            fw.flush();
	            fw.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
