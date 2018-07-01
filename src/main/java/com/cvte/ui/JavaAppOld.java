package com.cvte.ui;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cvte.cons.Constant;
import com.cvte.entity.Dir;
import com.cvte.entity.Image;
import com.cvte.util.FileUtil;
import com.cvte.util.PropertyUtil;
import com.cvte.util.ReadCSV;
import com.cvte.util.SaveToCSV;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * @author: jan
 * @date: 2018年5月9日 下午2:14:58
 */
//JavaScript interface object
public class JavaAppOld {
	
	public static WebEngine webEngineNew;
	
	public void openCloseMune(String flag) {
		if(Integer.parseInt(flag) == 0) {
			Browser.browser.setContextMenuEnabled(false);
		}else {
			Browser.browser.setContextMenuEnabled(true);
		}
		
	}
	
	public void loadData() {
		System.out.println("主页加载数据");
		List<Dir> list = new ArrayList<Dir>();
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/csv/dir.csv";
		File f = new File(rootPath + "/csv");
		if(!f.exists()) {
			f.mkdirs();
		}
		File f2 = new File(filePath);
		if(f2.exists()) {
			List<String[]> data = ReadCSV.readCSV(filePath);
			for(String[] s : data) {
				if(Integer.parseInt(s[7]) == 0) {
					Dir dir = new Dir(s[0], s[1], s[2], s[3], s[4], s[5], s[7]);
					list.add(dir);
				}
			}
			String str = JSON.toJSONString(list);
			Browser.webEngine.executeScript("getDirData('"+ str + "')");
		}
	}

	// login登录    有时间加一个人工session
	public void login(String account, String password) {
		System.out.println("登录 = " + account + " , " + password);
		String md5 = "";
		try {
			md5 = getMD5Str(password.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PropertyUtil prop = new PropertyUtil();
		prop.loadProperty();
		if(account.trim().equals(PropertyUtil.Account) && md5.equals(PropertyUtil.Password)) {
			cleanConstant();  //该种方法不明智  但能解决代码处理出现的问题
			initConstant();
//			if(Constant.login == 0) {
//				initConstant();
//				Constant.login = 1;
//			}else {
//				System.out.println("已登录过,且未关闭UI");
//			}
			Browser.webEngine.load(Browser.class.getResource("../views/index.html").toExternalForm());
			//Browser.webEngine.executeScript("loginJava('0')");
		}else {
			//initConstant();
			Browser.webEngine.executeScript("loginJava('1')");
			//Browser.webEngine.executeScript("loginJava('0')");
		}
	}
	
	public void logout() {
		Browser.webEngine.load(Browser.class.getResource("../views/login.html").toExternalForm());
	}
	
	public void toIndex() {
		//Browser.webEngine.reload();new WebView().getEngine()
		Browser.webEngine.load(Browser.class.getResource("../views/index.html").toExternalForm());
		Browser.webEngine.reload();
	}
	
	public void labelImg() {
		Browser.webEngine.load(Browser.class.getResource("../views/label.html").toExternalForm());
	}
	
	public void cleanConstant() {
		Constant.CurrDir = "";
		Constant.CurrImg = "";
		Constant.DirList = new ArrayList<String>();
		Constant.DirName = new HashMap<String, Integer>();
		Constant.Flag = 0;
		Constant.login = 0;
		Constant.DirNum = 0;
		Constant.IdDir = new HashMap<Integer, String>();
		Constant.MarkFlag = new HashMap<String, Integer>();
		Constant.ImgMap = new HashMap<String, List<String>>();
		Constant.ImgId = 0;
	}

	public void initConstant() {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/csv/dir.csv";
		String filePath1 = rootPath + "/csv/imgAllLabel.csv";
		File f = new File(rootPath + "/csv");
		if(!f.exists()) {
			f.mkdirs();
		}
		File f1 = new File(filePath);
		if(f1.exists()) {
			List<String[]> list = ReadCSV.readCSV(filePath);
			if(list.size() != 0) {
				int num = 0;
				for(String[] s : list) {
					//System.out.println(s[0] + " = " + s[2]);
					if(Integer.parseInt(s[0]) > num) {
						num = Integer.parseInt(s[0]);
					}
					Constant.DirName.put(s[2], Integer.parseInt(s[7]));
					Constant.DirList.add(s[2]);
					String[] imgAll = s[6].split(",");
					List<String> img = new ArrayList<String>();
					for(String s1 : imgAll) {
						img.add(s1);
					}
					Constant.ImgMap.put(s[2], img);
					Constant.MarkFlag.put(s[2], Integer.parseInt(s[3]));
					Constant.IdDir.put(Integer.parseInt(s[0]), s[2]);
				}
				Constant.DirNum = num;
			}
		}
		
		File f2 = new File(filePath1);
		if(f2.exists()) {
			List<String[]> list1 = ReadCSV.readCSV(filePath1);
			int num = 0;
			for(String[] s : list1) {
				if(Integer.parseInt(s[0]) > num) {
					num = Integer.parseInt(s[0]);
				}
			}
			Constant.ImgId = num;
		}
	}

	// 关闭界面
	public void exit() {
		Platform.exit();
	}
	
	public static String getMD5Str(String str) throws Exception {  
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            return new BigInteger(1, md.digest()).toString(16);  
        } catch (Exception e) {  
            throw new Exception("MD5加密出现错误，"+e.toString());  
        }  
    }  
	
	public void openDirectory() {
		Stage fileStage = null;
	    DirectoryChooser folderChooser = new DirectoryChooser();
	    folderChooser.setTitle("Choose Folder");
	    File selectedFile = folderChooser.showDialog(fileStage);
	    //System.out.println("dir = " + selectedFile);
	    String dir = selectedFile.getAbsolutePath().replace('\\', '/');
	    System.out.println("dir-new = " + dir);
	    File[] files = new File(dir).listFiles();
	    int code = 0;  //文件夹listFile数量  亦判断文件夹存在jpg文件
	    int flag = 0;  //判断文件夹曾经被选择过
	    if(Constant.DirName.size() == 0) {
	    	Constant.DirName.put(dir, 0);
	    	System.out.println("dirr = " + dir);
		    System.out.println("name = " + files[0].getName());
		    code = checkDir(files);
	    }else {
	    	for(String s : Constant.DirName.keySet()) {  //判断文件夹是否已选择
	    		if(s.equals(dir)) {
	    			Constant.Flag = 1;
	    			if(Constant.DirName.get(dir) == 1) {
	    				flag = 1;
	    			    Constant.Flag = 0;
	    			}else {
	    				code = -1;
	    			}
	    			break;
	    		}
	    	}
			if (Constant.Flag == 0) {
				Constant.DirName.put(dir, 0);
				System.out.println("dirr = " + dir);
				System.out.println("name = " + files[0].getName());
				code = checkDir(files);
			}
	    }
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
	    if(code > 0) {
	    	Constant.Flag = 0;
	    	Constant.DirNum += 1;
	    	Constant.IdDir.put(Constant.DirNum, dir);
	    	
	    	//该处可用Set
	    	int fflag = 0;
	    	for(String s : Constant.DirList) {
	    		if(s.equals(dir)) {
	    			fflag = 1;
	    			break;
	    		}
	    	}
	    	if(fflag == 0) {
	    		Constant.DirList.add(dir);
	    	}
	    	
	    	if(flag == 0) {
	    		Constant.MarkFlag.put(dir, 0);
	    	}else if(flag == 1) {
	    		System.out.println("继续上次标记处开始标注");
	    	}else {
	    		Constant.MarkFlag.put(dir, 0);
	    	}
	    	addImageMap(dir);
	    	String imgAllName = "";
	    	for(int i = 0; i < files.length; i++) {
	    		if(!files[i].isDirectory()) {
	    			if("jpg".equals(files[i].getName().split("[.]")[1].toLowerCase()) ||
		    				"png".equals(files[i].getName().split("[.]")[1].toLowerCase()) ||
		    				"jpeg".equals(files[i].getName().split("[.]")[1].toLowerCase())) {
		    			if(i == files.length - 1) {
			    			imgAllName = imgAllName + files[i].getName();
			    		}else {
			    			imgAllName = imgAllName + files[i].getName() + ",";
			    		}
		    		}
	    		}
	    	}
	    	String date = format.format(new Date());
	    	if(flag == 0) {
	    		SaveToCSV.saveDir(date, Constant.DirNum, code, dir, imgAllName);
	    	}else if(flag == 1) {
	    		//将曾经被选择过文件夹deleted置0  重新使用
	    		updateCSV(dir, "0");
	    	}else {
	    		System.out.println("flag操作有误");
	    	}
	    	
	    	Browser.webEngine.executeScript("getDirectory("+ Constant.MarkFlag.get(dir) + ",'" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }else {
	    	Constant.Flag = 0;
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory("+ Constant.MarkFlag.get(dir) + ",'" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }
	}
	
	public void addImageMap(String dir) {
		File[] files = new File(dir).listFiles();
		List<String> list = new ArrayList<String>();
		for(File file : files) {
			if(!file.isDirectory()) {
				String[] str = file.getName().split("[.]");
				if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
						|| "jpeg".equals(str[1].toLowerCase())) {
					list.add(file.getName());
				}
			}
		}
		Constant.ImgMap.put(dir, list);
	}

	public int checkDir(File[] files) {
		int num = 0;
		for(File f : files) {
			if(!f.isDirectory()) {
				if(checkImg(f)) {
					num++;
				}
			}
		}
		return num;
	}

	public boolean checkImg(File f) {
		String[] str = f.getName().split("[.]");
		if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
				|| "jpeg".equals(str[1].toLowerCase())) {
			return true;
		}else {
			return false;
		}
	}
	
	public void delDirectory(String offset) {
		//System.out.println("dirNum = " + offset + "=" + Constant.DirList.get(Integer.parseInt(offset) - 1));
		
		String dir = Constant.IdDir.get(Integer.parseInt(offset));
		Constant.DirName.put(Constant.IdDir.get(Integer.parseInt(offset)), 1);
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		File file = new File(rootPath + "/csv");
		if(!file.exists()) {
			file.mkdirs();
			System.out.println("不存在该文件夹路径");
		}else {
			updateCSV(dir, "1");
		}
		
	}
	
	public void updateCSV(String dir, String deleted) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/csv/dir.csv";
		List<String[]> list = ReadCSV.readCSV(csvPath);
		for(int i = 0; i < list.size(); i++) {
			if(dir.equals(list.get(i)[2])) {
				list.get(i)[0] = "" + Constant.DirNum;
				list.get(i)[3] = "" + Constant.MarkFlag.get(dir);
				list.get(i)[7] = deleted;
			}
		}
		SaveToCSV.updateCSV(list);
	}
	
	public List<String[]> readCSVData(String csv){
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/csv/" + csv;
		List<String[]> list = ReadCSV.readCSV(csvPath);
		return list;
	}
	
	//标注图片
	public void markImage(String offset) {
		//开启弹出菜单
		Browser.browser.setContextMenuEnabled(true);
		//读CSV文件更新MarkFlag 防止出现不一致bug
		List<String[]> list = readCSVData("dir.csv");
		for(String[] s : list) {
			if(s[2].equals(Constant.IdDir.get(Integer.parseInt(offset)))) {
				Constant.MarkFlag.put(s[2], Integer.parseInt(s[3]));
				break;
			}
		}
		
		System.out.println("mark offset " + offset);
		String dir = Constant.IdDir.get(Integer.parseInt(offset));
		System.out.println("mark dir = " + dir);
		//int markFlag = Constant.MarkFlag.get(Integer.parseInt(offset) - 1);
		String tmpdir = dir.split(":")[0] + "." + dir.split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
//		String root = this.getClass().getResource("").getPath().replaceAll("%20", "");
//		String rootPath = root.substring(0, root.length() - 1);
//		String dataPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
		String dataPath = System.getProperty("user.dir").replace("\\", "/");
		String imgPath = dataPath + "/data/" + dataDir;
		System.out.println("imgPath=" + imgPath);
		File file = new File(imgPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		readMark(offset, imgPath);
	}
	
	//标注图片  未判断是否已到达文件夹长度
	public static void readMark(String offset, String imgPath) {
		String dir = "";
		if("".equals(offset)) {
			dir = Constant.CurrDir;
		}else {
			dir = Constant.IdDir.get(Integer.parseInt(offset));
		}
		
		Constant.CurrDir = dir;
		String tmpdir = dir.split(":")[0] + "." + dir.split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
		int markFlag = Constant.MarkFlag.get(dir);
		if(markFlag == Constant.ImgMap.get(dir).size()) { //判断MarkFlag是否等于总数
			System.out.println("该文件夹已全部标注完");
			//换下一个dir
			int flag = 0;
			for(String s : Constant.DirList) {
				if(Constant.DirName.get(s) == 0
						&& Constant.MarkFlag.get(s) < Constant.ImgMap.get(s).size()) { 
					//上行处未判断图片是否被人为删除   为隐藏bug
					flag = 1;
					Constant.CurrDir = s;
					int markFlagNew = Constant.MarkFlag.get(Constant.CurrDir);
					String imgName1 = Constant.ImgMap.get(Constant.CurrDir).get(Constant.MarkFlag.get(Constant.CurrDir));
					String tmpdir2 = Constant.CurrDir.split(":")[0] + "." + Constant.CurrDir.split(":")[1].substring(1);
					String dataDir2 = tmpdir2.replace('/', '.');
					String path1 = System.getProperty("user.dir").replace("\\", "/");
					String pa1 = "file://" + path1 + "/data/" + dataDir2;
					Constant.CurrImg = pa1 + "/" + 
							Constant.ImgMap.get(s).get(Constant.MarkFlag.get(s));
					boolean copy = FileUtil.copyFile(Constant.CurrDir, path1 + "/data/" + dataDir2, imgName1);
					if(copy) {
						markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(dir, markFlagNew);  //更新当前dir已标注id
						Constant.ImgId += 1;
					}else {
						markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(dir, markFlagNew);
						String offset1 = "";
						for(Integer i : Constant.IdDir.keySet()) {
							if(dir.equals(Constant.IdDir.get(i))) {
								offset1 = "" + i;
							}
						}
						readMark(offset1, path1 + "/data/" + dataDir2);
					}
					break;
				}
			}
			if(flag == 0) {
				System.out.println("所有文件夹已标注完");
				Browser.webEngine.executeScript("showMsg()");
			}else {
				System.out.println("随机进行下一个文件夹标注");
				System.out.println("下一张 " + Constant.CurrImg);
				//标注下一张图片  
				Browser.webEngine.executeScript("label('" + "" + "' ,'"+ "" + "')");
			}
		}else {
			String imgName = Constant.ImgMap.get(dir).get(markFlag);
			boolean copy = FileUtil.copyFile(dir, imgPath, imgName);
			if(copy) {
				markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);  //更新当前dir已标注id
				Constant.ImgId += 1;
				System.out.println("offset" + offset);
				String path = System.getProperty("user.dir").replace("\\", "/");
				Constant.CurrImg = "file://" + path + "/data/" + dataDir + "/" + imgName;
				Browser.webEngine.executeScript("label('" + dataDir + "' ,'"+ imgName + "')");
			}else {
				markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);
				readMark(offset, imgPath);
			}
		}
	}
	
	public void getCurrImg() {
		String img = Constant.CurrImg;
		System.out.println(",img = " + img);
		String[] str = img.split("/");
		String imgname = str[str.length - 1];
		String dir = Constant.CurrDir;
		Browser.webEngine.executeScript("setImg('" + imgname + "','" + dir + "','"+ img + "')");
	}
	
	public void markList() {
		System.out.println("标注列表");
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/csv/imgAllLabel.csv";
		File f2 = new File(filePath);
		if(f2.exists()) {
			List<String[]> list1 = ReadCSV.readCSV(filePath);
			List<Image> list = new ArrayList<Image>();
			for(String[] s : list1) {
				Image img = new Image(s[0], s[1], s[2], s[3], "未标注", s[4]);
				list.add(img);
			}
			String str = JSON.toJSONString(list);
			Browser.webEngine.executeScript("getMarkList(" + list.size() + " ,'"+ str + "')");
		}else {
			Browser.webEngine.executeScript("getMarkList(" + 0 + " ,'"+ "" + "')");
		}
//		Image img = new Image("1", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
//		Image img1 = new Image("2", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
//		Image img2 = new Image("3", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
//		List<Image> list = new ArrayList<Image>();
//		list.add(img);list.add(img1);list.add(img2);
//		String str = JSON.toJSONString(list);
//		Browser.webEngine.executeScript("getMarkList(" + 1 + " ,'"+ str + "')");
	}
	
	public void saveImgLabel(String imgData) {
		System.out.println("获取到 imgData = " + imgData);
		SaveToCSV.saveImgLabelData(imgData);
		
		//生成下一张图片数据
		System.out.println("开始生成下一张图片");
		int markFlag = Constant.MarkFlag.get(Constant.CurrDir);
		String tmpdir = Constant.CurrDir.split(":")[0] + "." + Constant.CurrDir.split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
		String path = System.getProperty("user.dir").replace("\\", "/");
		String pa = "file://" + path + "/data/" + dataDir + "/";
		String dir = Constant.CurrDir;
		if(markFlag == Constant.ImgMap.get(dir).size()) { //判断MarkFlag是否等于总数
			System.out.println("该文件夹已全部标注完");
			//换下一个dir
			int flag = 0;
			for(String s : Constant.DirList) {
				if(Constant.DirName.get(s) == 0
						&& Constant.MarkFlag.get(s) < Constant.ImgMap.get(s).size()) { 
					//上行处未判断图片是否被人为删除   为隐藏bug
					System.out.println("找到还有未标注文件夹");
					flag = 1;
					Constant.CurrDir = s;
					int markFlagNew = Constant.MarkFlag.get(Constant.CurrDir);
					String imgName1 = Constant.ImgMap.get(Constant.CurrDir).get(Constant.MarkFlag.get(Constant.CurrDir));
					String tmpdir2 = Constant.CurrDir.split(":")[0] + "." + Constant.CurrDir.split(":")[1].substring(1);
					String dataDir2 = tmpdir2.replace('/', '.');
					String path1 = System.getProperty("user.dir").replace("\\", "/");
					String pa1 = "file://" + path1 + "/data/" + dataDir2;
					Constant.CurrImg = pa1 + "/" + 
							Constant.ImgMap.get(s).get(Constant.MarkFlag.get(s));
					boolean copy = FileUtil.copyFile(Constant.CurrDir, path1 + "/data/" + dataDir2, imgName1);
					if(copy) {
						markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(Constant.CurrDir, markFlagNew);  //更新当前dir已标注id
						Constant.ImgId += 1;
					}else {
						markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(Constant.CurrDir, markFlagNew);
						String offset = "";
						for(Integer i : Constant.IdDir.keySet()) {
							if(Constant.CurrDir.equals(Constant.IdDir.get(i))) {
								offset = "" + i;
							}
						}
						readMark(offset, path1 + "/data/" + dataDir2);
					}
					break;
				}
			}
			if(flag == 0) {
				System.out.println("所有文件夹已标注完");
				Browser.webEngine.executeScript("logOverOut()");
			}else {
				System.out.println("随机进行下一个文件夹标注");
				System.out.println("下一张 " + Constant.CurrImg);
				//标注下一张图片  
				Browser.webEngine.executeScript("markNext()");
			}
		}else {
			String imgName = Constant.ImgMap.get(Constant.CurrDir).get(Constant.MarkFlag.get(Constant.CurrDir));
			Constant.CurrImg = pa + imgName;
			System.out.println("下一张 " + Constant.CurrImg);
			String imgPath = path + "/data/" + dataDir;
			boolean copy = FileUtil.copyFile(Constant.CurrDir, imgPath, imgName);
			if(copy) {
				markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);  //更新当前dir已标注id
				Constant.ImgId += 1;
			}else {
				markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);
				String offset = "";
				for(Integer i : Constant.IdDir.keySet()) {
					if(dir.equals(Constant.IdDir.get(i))) {
						offset = "" + i;
					}
				}
				readMark(offset, imgPath);
			}
			//标注下一张图片  
			Browser.webEngine.executeScript("markNext()");
		}
	}
	
	public static void main(String[] args) {
		String rootPath = JavaApp.class.getResource("/").getPath().replaceAll("%20", "");
		System.out.println(rootPath);
		String dataPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
		System.out.println(dataPath);
		String path = JavaApp.class.getResource("").getPath();
		System.out.println(path + "=" + path.substring(0, path.length() - 1));
		String root = path.substring(0, path.length() - 1);
		System.out.println(root.substring(0, root.lastIndexOf('/')));
		String dataDir = root.replace('/', '.');
		System.out.println(dataDir);
		
		String dataPath1 = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(dataPath1);
		
	}
}