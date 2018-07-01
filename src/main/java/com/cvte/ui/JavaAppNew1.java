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
import com.alibaba.fastjson.JSONObject;
import com.cvte.cons.Constant;
import com.cvte.entity.Image;
import com.cvte.util.FileUtil;
import com.cvte.util.PropertyUtil;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * @author: jan
 * @date: 2018年5月9日 下午2:14:58
 */
//JavaScript interface object
public class JavaAppNew1 {
	
	public static WebEngine webEngineNew;

	// login登录    有时间加一个人工session
	public void login(String account, String password) {
		System.out.println("登录 = " + account + " , " + password);
		if("".equals(account)) {
			Browser.webEngine.executeScript("loginJava('-1')");
		}else {
			if("".equals(password)) {
				Browser.webEngine.executeScript("loginJava('-2')");
			}else {
				String md5 = "";
				try {
					md5 = getMD5Str(password.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
				PropertyUtil prop = new PropertyUtil();
				prop.loadProperty();
				if(account.trim().equals(PropertyUtil.Account) && md5.equals(PropertyUtil.Password)) {
					Browser.webEngine.executeScript("loginJava('0')");
				}else {
					//Browser.webEngine.executeScript("loginJava('1')");
					Browser.webEngine.executeScript("loginJava('0')");
				}
			}
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
	    System.out.println("dir = " + selectedFile);
	    String dir = selectedFile.getAbsolutePath().replace('\\', '/');
	    int code = 0;
	    if(Constant.DirName.size() == 0) {
	    	Constant.DirName.put(dir, 1);
	    	System.out.println("dirr = " + dir);
		    File[] files = new File(dir).listFiles();
		    System.out.println("name = " + files[0].getName());
		    code = checkDir(files);
	    }else {
	    	for(String s : Constant.DirName.keySet()) {  //判断文件夹是否已选择
	    		if(s.equals(dir)) {
	    			Constant.Flag = 1;
	    			if(Constant.DirName.get(dir) == 0) {
	    			    Constant.Flag = 0;
	    			}else {
	    				code = -1;
	    			}
	    			break;
	    		}
	    	}
			if (Constant.Flag == 0) {
				Constant.DirName.put(dir, 1);
				System.out.println("dirr = " + dir);
				File[] files = new File(dir).listFiles();
				System.out.println("name = " + files[0].getName());
				code = checkDir(files);
			}
	    }
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
	    if(code > 0) {
	    	Constant.Flag = 0;
	    	Constant.DirNum += 1;
	    	Constant.DirList.add(dir);
	    	//Constant.MarkFlag.add(0);
	    	addImageMap(dir);
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory('" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }else {
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory('" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
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
		System.out.println("dirNum = " + offset + "=" + Constant.DirList.get(Integer.parseInt(offset) - 1));
		Constant.DirName.put(Constant.DirList.get(Integer.parseInt(offset) - 1), 0);
	}
	
	//标注图片
	public void markImage(String offset) {
		String dir = Constant.DirList.get(Integer.parseInt(offset) - 1);
		//int markFlag = Constant.MarkFlag.get(Integer.parseInt(offset) - 1);
		String tmpdir = dir.split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
		String root = this.getClass().getResource("").getPath().replaceAll("%20", "");
		String rootPath = root.substring(0, root.length() - 1);
		String dataPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
		String imgPath = dataPath + "/data/" + dataDir;
		File file = new File(imgPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		readMark(offset, imgPath);
	}
	
	//标注图片  未判断是否已到达文件夹长度
	public static void readMark(String offset, String imgPath) {
		String dir = Constant.DirList.get(Integer.parseInt(offset) - 1);
		String tmpdir = dir.split(":")[1].substring(1);
		String dataDir = tmpdir.replace('/', '.');
		int markFlag = Constant.MarkFlag.get(Integer.parseInt(offset) - 1);
		String imgName = Constant.ImgMap.get(dir).get(markFlag);
		boolean copy = FileUtil.copyFile(dir, imgPath, imgName);
		if(copy) {
			markFlag = markFlag + 1;
			//Constant.MarkFlag.set(Integer.parseInt(offset) - 1, markFlag);
			System.out.println("offset" + offset);
			Browser.webEngine.executeScript("label('" + dataDir + "' ,'"+ imgName + "')");
		}else {
			markFlag = markFlag + 1;
			//Constant.MarkFlag.set(Integer.parseInt(offset) - 1, markFlag);
			readMark(offset, imgPath);
		}
	}
	
	public void markList() {
		System.out.println("标注列表");
		Image img = new Image("1", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
		Image img1 = new Image("2", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
		Image img2 = new Image("3", "jan", "c:/new", "2.jpg", "未标注", "2018-01-01 12:12:12");
		List<Image> list = new ArrayList<Image>();
		list.add(img);list.add(img1);list.add(img2);
		String str = JSON.toJSONString(list);
		Browser.webEngine.executeScript("getMarkList(" + 1 + " ,'"+ str + "')");
	}
	
	public static void main(String[] args) {
		String rootPath = JavaAppNew1.class.getResource("/").getPath().replaceAll("%20", "");
		System.out.println(rootPath);
		String dataPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
		System.out.println(dataPath);
		String path = JavaAppNew1.class.getResource("").getPath();
		System.out.println(path + "=" + path.substring(0, path.length() - 1));
		String root = path.substring(0, path.length() - 1);
		System.out.println(root.substring(0, root.lastIndexOf('/')));
		String dataDir = root.replace('/', '.');
		System.out.println(dataDir);
	}
}