package com.cvte.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.csvreader.CsvWriter;
import com.cvte.cons.Constant;
import com.cvte.cons.Version;
import com.cvte.ui.WebUI;

/** 
* @author: jan 
* @date: 2018年6月4日 上午10:56:32 
*/
public class ExportUtil {

	public static void exportData(String id) {
		//创建导出数据文件夹
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String rootPath = System.getProperty("user.dir").replace('\\', '/');
		String date = format.format(new Date());
		String exportPath = rootPath + "/export-" + Version.VersionId + "/" + date + "/"
				+ Constant.User;
		String filePath = exportPath + "/file";
		//String dataPath = exportPath + "/data";
		//File file2 = new File(dataPath);
		File file1 = new File(filePath);
		if(!file1.exists()){
			file1.mkdirs();
		}
//		if(!file2.exists()) {
//			file2.mkdirs();
//		}
		
		exportFile(exportPath, id);
		DialogUtil.showAlert(WebUI.stageOld, "导出成功");
	}

	private static void exportFile(String exportPath, String id) {
		String rootPath = System.getProperty("user.dir").replace('\\', '/');
		
		String filePath = exportPath + "/file";
		//String dataPath = exportPath + "/data";
		String oldFilePath = rootPath + "/" + Constant.User + "/file/dir";
		String orignPath = "";
		List<String[]> list = ReadCSV.readCSV(oldFilePath);
		//保存指定任务(dir)
		for(String[] s : list) {
			if(s[0].equals(id)) {
				copySoleFile(s, filePath);
				orignPath = s[2];
				//copySoleData(s, dataPath);
				writeCSV(filePath, s);
				break;
			}
		}
		
		//保存指定任务(imgAllLabel)
		File srcFile = new File(rootPath + "/" + Constant.User + "/file/imgAllLabel");
		File destFile = new File(filePath + "/imgAllLabel");
		if(!destFile.exists()) {
			try {
				destFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		copyImgAllLabel(orignPath, rootPath + "/" + Constant.User + "/file/imgAllLabel", filePath + "/imgAllLabel");
//		try {
//			FileUtils.copyFile(srcFile, destFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

	private static void copyImgAllLabel(String orignPath, String allpath, String imgAllPath) {
		List<String[]> s = ReadCSV.readCSV(allpath);
		List<String[]> list = new ArrayList<String[]>();
		for(String[] st : s) {
			if(st[8].equals(orignPath)) {
				list.add(st);
			}
		}
		writeAllImage(list, imgAllPath);
	}

	private static void writeAllImage(List<String[]> list, String imgAllPath) {
		// 定义一个CSV路径  
	    try {
	    	CsvWriter csvWriter = new CsvWriter(imgAllPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	    	String[] csvHeaders = { "id", "user", "dir","img_name","mark_time","zoom_ratio",
	        		"canvas_data", "label_data", "orign_dir", "img_sole_path"};
	        csvWriter.writeRecord(csvHeaders);   
	        // 写内容  
	        for(String[] s : list) {
	        	String[] csvContent = s;
		        
		        csvWriter.writeRecord(csvContent);
	        }
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}

	private static void copySoleData(String[] s, String dataPath) {
		String dir = s[2];
		String[] tmp = dir.split(":");
		String path = tmp[0].replace('/', '.') + tmp[1].replace('/', '.');
		String rootPath = System.getProperty("user.dir").replace('\\', '/');
		String solePath = rootPath + "/" + s[1].trim() + "/data/" + path;
		File f = new File(dataPath + "/" + path);
		if(!f.exists()) {
			f.mkdirs();
		}
		File[] files = new File(solePath).listFiles();
		for(File file : files) {
			File srcFile = new File(solePath + "/" + file.getName());
			File destFile = new File(dataPath + "/" + path + "/" + file.getName());
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void copySoleFile(String[] s, String filePath) {
		String dir = s[2];
		String[] tmp = dir.split(":");
		String path = tmp[0].replace('/', '.') + tmp[1].replace('/', '.');
		String rootPath = System.getProperty("user.dir").replace('\\', '/');
		String solePath = rootPath + "/" + s[1].trim() + "/file/" + path;
		File f = new File(filePath + "/" + path);
		if(!f.exists()) {
			f.mkdirs();
		}
		File[] files = new File(solePath).listFiles();
		for(File file : files) {
			File srcFile = new File(solePath + "/" + file.getName());
			File destFile = new File(filePath + "/" + path + "/" + file.getName());
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void writeCSV(String filePath, String[] s) {
		File file = new File(filePath + "/dir");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 定义一个CSV路径  
	    try {
	    	CsvWriter csvWriter = new CsvWriter(filePath + "/dir",',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","markedId","total","addTime",
	        		"imgAllName", "deleted"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        String[] csvContent = s;
	        
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
}
