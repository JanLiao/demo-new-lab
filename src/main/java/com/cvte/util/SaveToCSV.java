package com.cvte.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.cvte.cons.Constant;
import com.cvte.ui.Browser;

/** 
* @author: jan 
* @date: 2018年5月11日 上午1:15:12 
*/
public class SaveToCSV {

	public static void saveDir(String date, Integer dirNum, int total, String dir, String imgAllName) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(rootPath);
		File file = new File(rootPath + "/" + Constant.User + "/file/");
		if(!file.exists()) {
			file.mkdirs();
		}
		String csvPath = rootPath + "/" + Constant.User + "/file/dir";
		File f = new File(csvPath);
		if(!f.exists()) {
			try {
				f.createNewFile();
				writeCSV(date, dirNum, total, dir, imgAllName, csvPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			appendCSV(date, dirNum, total, dir, imgAllName, csvPath);
		}
		
	}
	
	public static void writeCSV(String date, int id, int total, String dir, String imgAllName, String csvPath) {  
	    // 定义一个CSV路径  
	    try {
	    	CsvWriter csvWriter = new CsvWriter(csvPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","markedId","total","addTime",
	        		"imgAllName", "deleted"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        String[] csvContent = {"" + id, Constant.User, dir, "0", "" + total, date, imgAllName, "0"};
	        
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	}
	
	public static void appendCSV(String date, int id, int total, String dir, String imgAllName, String csvPath) {  
	    // 定义一个CSV路径  
	    //String csvFilePath = "E://StemQ.csv";  
	    try {  
	    	 BufferedWriter out = new BufferedWriter(new 

	    	           OutputStreamWriter(new FileOutputStream(csvPath,true),"UTF-8"),1024);
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	        CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        //String[] csvHeaders = { "编号", "姓名", "年龄" };  
	        //csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        //String
	        String[] csvContent = {"" + id, Constant.User, dir, "0", "" + total, date, imgAllName, "0"};
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经append写入--------");  
	    } catch (IOException e) {
	        e.printStackTrace();
	    }  
	}
	
	public static void main(String[] args) {
		SaveToCSV.saveDir("", 2, 3, "", "");
	}

	public static void updateCSV(List<String[]> list) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(rootPath);
		File file = new File(rootPath + "/" + Constant.User + "/file/");
		if(!file.exists()) {
			file.mkdirs();
		}
		String csvPath = rootPath + "/" + Constant.User + "/file/dir";
		File f = new File(csvPath);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    try {  
	    	CsvWriter csvWriter = new CsvWriter(csvPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","markedId","total","addTime",
	        		"imgAllName", "deleted"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        for(String[] s : list) {
	        	csvWriter.writeRecord(s);
	        }
	        
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	public static void updateImgLabelDataNew(String imgData) {
		//修改标注只append就OK
		writeImgSole(imgData);
		
		if("1".equals(Constant.MarkType)) {
			Constant.ShuffleFlag += 1;
		}
		else if("2".equals(Constant.MarkType)) {
			Constant.MarkedOrderFlag += 1;
		}
	}
	
	public static void savaImgLabelDataNew(String imgData) {
		JSONObject obj = JSON.parseObject(imgData);
		//System.out.println(obj.getString("image_name"));
		//System.out.println(obj.getString("label_data"));
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(rootPath);
		File file = new File(rootPath + "/" + Constant.User + "/file/");
		if(!file.exists()) {
			file.mkdirs();
		}
		String csvPath = rootPath + "/" + Constant.User + "/file/imgAllLabel";
		File f = new File(csvPath);
		if(!f.exists()) {
			try {
				f.createNewFile();
				writeCSVNew(imgData, csvPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			appendCSVNew(imgData, csvPath);
		}
		
		writeImgSole(imgData);
		if(Constant.writeFlag == 1) {
			System.out.println("写入失败，无需更新");
			//更新dir.csv文件  已标注id
			//updateDirCSVNewOne(obj.getString("dir"), obj.getString("image_name"));
		}else {
			//更新dir.csv文件  已标注id
			updateDirCSVNew(obj.getString("dir"), obj.getString("image_name"));
		}
	}

	public static void saveImgLabelData(String imgData) {
		JSONObject obj = JSON.parseObject(imgData);
		//System.out.println(obj.getString("image_name"));
		//System.out.println(obj.getString("label_data"));
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(rootPath);
		File file = new File(rootPath + "/" + Constant.User + "/file/");
		if(!file.exists()) {
			file.mkdirs();
		}
		String csvPath = rootPath + "/" + Constant.User + "/file/imgAllLabel";
		File f = new File(csvPath);
		if(!f.exists()) {
			try {
				f.createNewFile();
				writeCSV(imgData, csvPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			appendCSV(imgData, csvPath);
		}
		System.out.println("开始====");
		writeImgSole(imgData);
		System.out.println("开始更新id");
		//更新dir.csv文件  已标注id
		updateDirCSV(obj.getString("dir"));
	}
	
	public static void writeImgSole(String imgData) {
		JSONObject obj = JSON.parseObject(imgData);
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
		String imgName = obj.getString("image_name");
		String imgSolePath = rootPath + "/" + Constant.User + 
				"/file/" + dataDir + "/" + imgName.split("[.]")[0] + "";
		File file1 = new File(rootPath + "/" + Constant.User + 
				"/file/" + dataDir);
		if(!file1.exists()) {
			file1.mkdirs();
		}
		
		System.out.println("开始sole");
		File file2 = new File(imgSolePath);
		if(!file2.exists()) {
			try {
				file2.createNewFile();
				writeSole(imgData, imgSolePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			appendSole(imgData, imgSolePath);
		}
	}
	
	private static void appendSole(String imgData, String csvPath) {
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    //String csvFilePath = "E://StemQ.csv";  
	    try {  
	    	 BufferedWriter out = new BufferedWriter(new 

	    	           OutputStreamWriter(new FileOutputStream(csvPath,true),"UTF-8"),1024);
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	        CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        //String[] csvHeaders = { "编号", "姓名", "年龄" };  
	        //csvWriter.writeRecord(csvHeaders);
	        // 写内容  
	        //String
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"", Constant.User, rootPath + "/" + Constant.User + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir, csvPath};
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------Sole Img CSV文件已经append写入--------");  
	    } catch (IOException e) {
	    	Constant.CSVFlag = 1;
	        e.printStackTrace();
	    }  
	}

	private static void writeSole(String imgData, String csvPath) {
		System.out.println("进入写sole 文件");
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    try {  
	    	CsvWriter csvWriter = new CsvWriter(csvPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","img_name","mark_time","zoom_ratio",
	        		"canvas_data", "label_data", "orign_dir", "save_path"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"1", Constant.User, rootPath + "/" + Constant.User + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir, csvPath};
	        
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------Sole Img CSV文件已经write写入--------");  
	    } catch (IOException e) {
	    	Constant.writeFlag = 1;
	        e.printStackTrace();  
	    }  
	}
	
	private static void updateDirCSVNewOne(String dir, String imgName) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/" + Constant.User + "/file/dir";
		List<String[]> list = ReadCSV.readCSV(csvPath);
		int markId = Constant.MarkFlag.get(dir) + 1;
		for(int i = 0; i < list.size(); i++) {
			if(dir.equals(list.get(i)[2])) {
				list.get(i)[3] = "" + markId;
			}
		}
		if(Constant.CSVFlag == 0) {
			updateCSV(list);
			Constant.ImgId += 1;
			if(markId >= numDir(dir)) {
				Constant.MarkFlag.put(dir, numDir(dir));
			}else {
				Constant.MarkFlag.put(dir, markId);
			}
			Constant.MarkFlag.put(dir, markId);
			if("1".equals(Constant.MarkType)) {
				Constant.ShuffleFlag += 1;
			}
			else if("2".equals(Constant.MarkType)) {
				Constant.MarkedOrderFlag += 1;
			}
			else if("3".equals(Constant.MarkType)) {
				Constant.OrderFlag += 1;
			}
			Constant.ImgAll.put(dir + "," + imgName, 1);
		}else if(Constant.CSVFlag == 1) {
			System.out.println("打开失败");
		}
		
	}
	
	private static void updateDirCSVNew(String dir, String imgName) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/" + Constant.User + "/file/dir";
		List<String[]> list = ReadCSV.readCSV(csvPath);
		int markId = Constant.MarkFlag.get(dir) + 1;
		if(markId >= Constant.ImgMap.get(dir).size()) {
			markId = Constant.ImgMap.get(dir).size();
		}
		for(int i = 0; i < list.size(); i++) {
			if(dir.equals(list.get(i)[2])) {
				list.get(i)[3] = "" + markId;
			}
		}
		if(Constant.CSVFlag == 0) {
			updateCSV(list);
			Constant.ImgId += 1;
			if(markId >= numDir(dir)) {
				Constant.MarkFlag.put(dir, numDir(dir));
			}else {
				Constant.MarkFlag.put(dir, markId);
			}
			Constant.MarkFlag.put(dir, markId);
			if("1".equals(Constant.MarkType)) {
				Constant.ShuffleFlag += 1;
			}
			else if("2".equals(Constant.MarkType)) {
				Constant.MarkedOrderFlag += 1;
			}
			else if("3".equals(Constant.MarkType)) {
				Constant.OrderFlag += 1;
			}
			Constant.ImgAll.put(dir + "," + imgName, 1);
		}else if(Constant.CSVFlag == 1) {
			System.out.println("打开失败");
		}
		
	}
	
	public static int numDir(String dir) {
		int num = 0;
		File[] files = new File(dir).listFiles();
		for(File file : files) {
			if(!file.isDirectory()) {
				String[] str = file.getName().split("[.]");
				if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
						|| "jpeg".equals(str[1].toLowerCase())) {
					num++;
				}
			}
		}
		return num;
	}

	//更新dir.csv文件  已标注id
	private static void updateDirCSV(String dir) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/" + Constant.User + "/file/dir";
		List<String[]> list = ReadCSV.readCSV(csvPath);
		int markId = Constant.MarkFlag.get(dir) + 1;
		for(int i = 0; i < list.size(); i++) {
			if(dir.equals(list.get(i)[2])) {
				list.get(i)[3] = "" + markId;
			}
		}
		if(Constant.CSVFlag == 0) {
			updateCSV(list);
			Constant.MarkFlag.put(dir, markId);
		}else if(Constant.CSVFlag == 1) {
			System.out.println("打开失败");
		}
		
	}
	
	private static void writeCSVNew(String imgData, String csvPath) {
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    try {  
	    	CsvWriter csvWriter = new CsvWriter(csvPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","img_name","mark_time","zoom_ratio",
	        		"canvas_data", "label_data", "orign_dir", "img_sole_path"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			String imgSolePath = rootPath + "/" + Constant.User + 
					"/file/" + dataDir + "/" + imgName.split("[.]")[0] + "";
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"" + Constant.ImgId, Constant.User, rootPath + "/" + Constant.User + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir, imgSolePath};
	        
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {
	    	Constant.CSVFlag = 1;
	    	Constant.writeFlag = 1;
	        e.printStackTrace();  
	    }  
	}

	private static void writeCSV(String imgData, String csvPath) {
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    try {  
	    	CsvWriter csvWriter = new CsvWriter(csvPath,',',Charset.forName("UTF-8"));
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	       // CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        String[] csvHeaders = { "id", "user", "dir","img_name","mark_time","zoom_ratio",
	        		"canvas_data", "label_data", "orign_dir"};
	        csvWriter.writeRecord(csvHeaders);  
	        // 写内容  
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"" + Constant.ImgId, Constant.User, rootPath + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir};
	        
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经write写入--------");  
	    } catch (IOException e) {
	    	Constant.CSVFlag = 1;
	    	Constant.writeFlag = 1;
	        e.printStackTrace();  
	    }  
	}
	
	private static void appendCSVNew(String imgData, String csvPath) {
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    //String csvFilePath = "E://StemQ.csv";  
	    try {  
	    	 BufferedWriter out = new BufferedWriter(new 

	    	           OutputStreamWriter(new FileOutputStream(csvPath,true),"UTF-8"),1024);
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	        CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        //String[] csvHeaders = { "编号", "姓名", "年龄" };  
	        //csvWriter.writeRecord(csvHeaders);
	        // 写内容  
	        //String
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			String imgSolePath = rootPath + "/" + Constant.User + 
					"/file/" + dataDir + "/" + imgName.split("[.]")[0] + "";
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"" + Constant.ImgId, Constant.User, rootPath + "/" + Constant.User + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir, imgSolePath};
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经append写入--------");  
	    } catch (IOException e) {
	    	Constant.CSVFlag = 1;
	    	Constant.writeFlag = 1;
	        e.printStackTrace();
	    }  
	}

	private static void appendCSV(String imgData, String csvPath) {
		JSONObject obj = JSON.parseObject(imgData);
		// 定义一个CSV路径  
	    //String csvFilePath = "E://StemQ.csv";  
	    try {  
	    	 BufferedWriter out = new BufferedWriter(new 

	    	           OutputStreamWriter(new FileOutputStream(csvPath,true),"UTF-8"),1024);
	        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
	        CsvWriter csvWriter = new CsvWriter(out, ',');  
	        // 写表头  
	        //String[] csvHeaders = { "编号", "姓名", "年龄" };  
	        //csvWriter.writeRecord(csvHeaders);
	        // 写内容  
	        //String
	        String rootPath = System.getProperty("user.dir").replace("\\", "/");
	        String tmpdir = obj.getString("dir").split(":")[0] + "/" + obj.getString("dir").split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String dir = obj.getString("dir");
			String zoomRatio = obj.getString("zoom_ratio");
			String canvasData = obj.getString("canvas_data");
			String imgName = obj.getString("image_name");
			obj.remove("user");
			obj.remove("dir");
			obj.remove("image_name");
			String jsonStr = obj.toJSONString();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String[] csvContent = {"" + Constant.ImgId, Constant.User, rootPath + "/data/" + dataDir, 
	        		imgName, format.format(new Date()),
	        		zoomRatio, canvasData,
	        		jsonStr, dir};
	        csvWriter.writeRecord(csvContent);
	        csvWriter.close();  
	        System.out.println("--------CSV文件已经append写入--------");  
	    } catch (IOException e) {
	    	Constant.CSVFlag = 1;
	    	Constant.writeFlag = 1;
	        e.printStackTrace();
	    }  
	}
	
}
