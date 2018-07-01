package com.cvte.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cvte.cons.Constant;
import com.cvte.entity.Dir;
import com.cvte.entity.Image;
import com.cvte.util.FileUtil;
import com.cvte.util.PropertyUtil;
import com.cvte.util.ReadCSV;
import com.cvte.util.SaveToCSV;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ButtonType;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author: jan
 * @date: 2018年5月9日 下午2:14:58
 */
//JavaScript interface object
public class JavaApp1 {
	
	public static WebEngine webEngineNew;
	
	private JFXDialog dialog;
	private JFXButton alertButton;
	private StackPane root;
	//private ViewFlowContext context;
	public static final String CONTENT_PANE = "ContentPane";
	
	public void openCloseMune(String flag) {
		if(Integer.parseInt(flag) == 0) {
			Browser.browser.setContextMenuEnabled(true);
		}else {
			Browser.browser.setContextMenuEnabled(true);
		}
		
	}
	
	public void loadData() {
		System.out.println("主页加载数据");
		List<Dir> list = new ArrayList<Dir>();
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/csv/dir.csv";
		File f = new File(rootPath +"/" + Constant.User + "/csv");
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
		
		//图片流   

		String md5 = "";
		try {
			md5 = getMD5Str(password.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PropertyUtil prop = new PropertyUtil();
		prop.loadProperty();
		String[] user = PropertyUtil.Account.split(",");
		String[] psw = PropertyUtil.Password.split(",");
		for(int i = 0; i < user.length; i++) {
			if(account.trim().equals(user[i]) && md5.equals(psw[i])) {
				cleanConstant();  //该种方法不明智  但能解决代码处理出现的问题
				Constant.User = user[i];
				initConstant();
//				if(Constant.login == 0) {
//					initConstant();
//					Constant.login = 1;
//				}else {
//					System.out.println("已登录过,且未关闭UI");
//				}
				Browser.webEngine.load(Browser.class.getResource("index.html").toExternalForm());
				break;
				//Browser.webEngine.executeScript("loginJava('0')");
			}else {
				//initConstant();
				//Browser.webEngine.executeScript("mess('" + dataURI + "')");
				Browser.webEngine.executeScript("loginJava('1')");
				//Browser.webEngine.executeScript("loginJava('0')");
			}
		}
	}
	
	public String getImgIo(String imgPath) {
		File f = new File(imgPath);
		String[] st = imgPath.split("/");
		String name = st[st.length - 1];
		String[] str = name.split("[.]");
		BufferedImage bi;
		String dataURI = "";
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, str[1], baos);
			byte[] bytes = baos.toByteArray();
			String imageMimeType = "image/" + str[1]; // Replace this for the correct mime of the image
			dataURI = "data:" + imageMimeType + ";base64," + javax.xml.bind.DatatypeConverter.printBase64Binary(bytes);
			System.out.println("URI = " + dataURI);
			return dataURI;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void logout() {
		Browser.webEngine.load(Browser.class.getResource("login.html").toExternalForm());
	}
	
	public void toIndex() {
		//Browser.webEngine.reload();new WebView().getEngine()
		Browser.webEngine.load(Browser.class.getResource("index.html").toExternalForm());
		Browser.webEngine.reload();
	}
	
	public void labelImg() {
		Browser.webEngine.load(Browser.class.getResource("label.html").toExternalForm());
	}
	
	public void markView() {
		Browser.webEngine.load(Browser.class.getResource("mark.html").toExternalForm());
		//Browser.webEngine.reload();
	}
	
	public void cleanConstant() {
		Constant.User = "";
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
		Constant.LData = "";
		Constant.ViewDir = "";
		Constant.ViewImgName = "";
		Constant.ViewImgPath = "";
		Constant.ImgAll = new HashMap<String, Integer>();
		Constant.ImgAllList = new ArrayList<String>();
		Constant.ShuffleFlag = 0;
		Constant.ShuffleList = new ArrayList<Integer>();
		Constant.OrderFlag = 0;
		Constant.MarkedOrderFlag = 0;
		Constant.ImgId = 0;
		Constant.OverFlag = 0;
	}

	public void initConstant() {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/csv/dir.csv";
		String filePath1 = rootPath + "/" + Constant.User + "/csv/imgAllLabel.csv";
		File f = new File(rootPath +"/" + Constant.User + "/csv");
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
			Constant.ImgId = num + 1;
		}
		
		//初始化ImgAll等
		initImgAll();
	}
	
	public void initImgAll() {
		for(String s : Constant.DirName.keySet()) {
			if(Constant.DirName.get(s) == 0) {
				String dir = s;
				String[] ss = dir.split(":");
				String dataDir = ss[0] + ss[1];
				dataDir = dataDir.replace('/', '.');
				File[] files = new File(dir).listFiles();
				String rootPath = System.getProperty("user.dir").replace("\\", "/");
				for(File file : files) {
					if(!file.isDirectory()) {
						String[] str = file.getName().split("[.]");
						if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
								|| "jpeg".equals(str[1].toLowerCase())) {
							String csvpath = rootPath + "/" + Constant.User + "/csv/" +
								dataDir + "/" + file.getName().split("[.]")[0] + ".csv";
							System.out.println("csvPath = " + csvpath);
							File f = new File(csvpath);
							if(f.exists()) {
								Constant.ImgAll.put(dir + "," + file.getName(), 1);
								System.out.println(dir + "," + file.getName() + "=" + 1);
							}
							else {
								System.out.println(dir + "," + file.getName() + "=" + 0);
								Constant.ImgAll.put(dir + "," + file.getName(), 0);
							}
							Constant.ImgAllList.add(dir + "," + file.getName());
						}
					}
				}
			}
		}
		
		//图片打乱编号
		shuffle();
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DirectoryChooser folderChooser = new DirectoryChooser();
	    folderChooser.setTitle("Choose Folder");
	    File selectedFile = folderChooser.showDialog(fileStage);
	    System.out.println("file open = " + selectedFile.getName() + " =" + selectedFile);
	    //System.out.println("dir = " + selectedFile);
	    String dir = selectedFile.getAbsolutePath().replace('\\', '/');
	    System.out.println("dir-new = " + dir);
	    File[] files = new File(dir).listFiles();
	    int code = 0;  //文件夹listFile数量  亦判断文件夹存在jpg文件
	    int flag = 0;  //判断文件夹曾经被选择过
	    if(files.length == 0) {
	    	System.out.println("文件夹为空");
	    	Constant.Flag = 0;
	    	code = 0;
	    	String date = format.format(new Date());
	    	Browser.webEngine.executeScript("getDirectory("+ Constant.MarkFlag.get(dir) + ",'" + date + "'," + Constant.DirNum + "," + code + ",'" + dir + "')");
	    }
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
	    		addAllImage(dir);  //添加文件下所有图片
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
	
	public void addAllImage(String dir) {
		File[] files = new File(dir).listFiles();
		for(File file : files) {
			if(!file.isDirectory()) {
				String[] str = file.getName().split("[.]");
				if("jpg".equals(str[1].toLowerCase()) || "png".equals(str[1].toLowerCase())
						|| "jpeg".equals(str[1].toLowerCase())) {
					Constant.ImgAll.put(dir + "," + file.getName(), 0);
					Constant.ImgAllList.add(dir + "," + file.getName());
				}
			}
		}
		//图片打乱编号
		shuffle();
	}
	
	//图片打乱编号list
	public void shuffle() {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < Constant.ImgAll.size(); i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		Constant.ShuffleList = list;
		for(int i : list) {
			System.out.print(i + " , ");
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
	
	public void clearData(String dir) {
		List<String> list = new ArrayList<String>();
		for(String s : Constant.ImgAll.keySet()) {
			String[] str = s.split(",");
			if(dir.equals(str[0])) {
				list.add(s);
			}
		}
		
		//删除ImgAll中dir有的img
		for(String ss : list) {
			Constant.ImgAll.remove(ss);
			Constant.ImgAllList.remove(ss);
		}
		//重新打乱顺序
		shuffle();
		Constant.ShuffleFlag = 0;
		Constant.OrderFlag = 0;
		Constant.MarkedOrderFlag = 0;
		Constant.CurrMarked = 0;
		Constant.MarkType = "";
		Constant.OverFlag = 0;
		
	}
	
	public void func_save_label() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("保  存  操  作");
		alert.setHeaderText("确  定  保  存  该  图 片 标注  吗");
		alert.setContentText("Are you ok with this?");
		
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Browser.webEngine.executeScript("func_save_label()");
		}else {
			System.out.println("不保存图片标注信息");
		}
	}
	
	public void func_update_label() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("修  改  操  作");
		alert.setHeaderText("确  定  修  改  该  图 片 标注  吗");
		alert.setContentText("Are you ok with this?");
		
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Browser.webEngine.executeScript("func_update_label()");
		}else {
			System.out.println("不修改图片标注信息");
		}
	}
	
	public void delDirectory(String offset) {
		//System.out.println("dirNum = " + offset + "=" + Constant.DirList.get(Integer.parseInt(offset) - 1));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("删   除   操  作");
		alert.setHeaderText("确  定  删  除 该  文  件  吗");
		alert.setContentText("Are you ok with this?");

		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    System.out.println("删  除  文  件  夹");
		    String dir = Constant.IdDir.get(Integer.parseInt(offset));
			Constant.DirName.put(Constant.IdDir.get(Integer.parseInt(offset)), 1);
			//删除进行相关操作
			clearData(dir);
			
			String rootPath = System.getProperty("user.dir").replace("\\", "/");
			File file = new File(rootPath + "/" + Constant.User + "/csv");
			if(!file.exists()) {
				file.mkdirs();
				System.out.println("不存在该文件夹路径");
			}else {
				updateCSV(dir, "1");
			}
			Browser.webEngine.executeScript("delDirReal('" + offset + "')");
		} else {
		    System.out.println("不  删  除");
		}
	}
	
	public void updateCSV(String dir, String deleted) {
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String csvPath = rootPath + "/" + Constant.User + "/csv/dir.csv";
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
		String csvPath = rootPath + "/" + Constant.User + "/csv/" + csv;
		List<String[]> list = ReadCSV.readCSV(csvPath);
		return list;
	}
	
	//分2中情况 主页进入  下一张
	public void readyMark1(String markType) {
		readyMark(markType);
		if(Constant.OrderFlag >= Constant.ImgAllList.size() && "3".equals(Constant.MarkType)) {
			System.out.println("没有未标注图片");
			Browser.webEngine.executeScript("showMs()");
		}
//		if(Constant.MarkedOrderFlag == Constant.ImgAllList.size() && "2".equals(Constant.MarkType)) {
//			System.out.println("没有未标注图片");
//			Browser.webEngine.executeScript("showMs()");
//		}
		else {
			if(Constant.MarkedOrderFlag >= Constant.ImgAll.size()) {
				System.out.println("没有已标注图片");
				Constant.MarkedOrderFlag = 0;
			}
			if(Constant.ShuffleFlag >= Constant.ImgAll.size()) {
				System.out.println("所有标注图片轮询完毕");
				Constant.ShuffleFlag = 0;
			}else {
				Browser.webEngine.executeScript("label('" + "" + "' ,'"+ "" + "')");
			}
		}
	}
	
	public void readyMark2(String markType) {
		readyMark(markType);
		Browser.webEngine.executeScript("markNext()");
	}
	
	//标注需求改变
	public void readyMark(String markType) {
		if(Constant.ImgAll.size() == 0) {
			Browser.webEngine.executeScript("showMs()");
		}
		else {
			String dataPath = System.getProperty("user.dir").replace("\\", "/");
			String imgPath = dataPath + "/" + Constant.User + "/data/";
			if("1".equals(markType)) {
				Constant.MarkType = markType;
				System.out.println("mark type = " + Constant.MarkType);
				System.out.println("所有标注进入");
				if(Constant.ShuffleFlag >= Constant.ImgAll.size()) {
					Constant.ShuffleFlag = 0;
					System.out.println("所有图片已经标注完成,将跳转到主界面" + Constant.ImgAll.size());
					Constant.OverFlag = 1;
					Browser.webEngine.load(Browser.class.getResource("index.html").toExternalForm());
				}
				else {
					System.out.println(Constant.ShuffleFlag + "==" + Constant.ImgAll.size());
					int offset = Constant.ShuffleList.get(Constant.ShuffleFlag);
					String dirAndName = Constant.ImgAllList.get(offset);
					String[] str = dirAndName.split(",");
					String dataDir = str[0].split(":")[0] + "." + str[0].split(":")[1].substring(1);
					dataDir = dataDir.replace('/', '.');
					File file = new File(imgPath + dataDir + "/" + str[1]);
					System.out.println("imgpath mark = " + imgPath + dataDir + "/" + str[1]);
					if(file.exists()) {
						System.out.println("所有标注  文件已存在  不用复制");
						Constant.CurrDir = str[0];
						//getImgIo(String imgPath)
						Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
						Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
						//Constant.ImgId += 1;
					}else {
						System.out.println("图片不存在");
						Constant.CurrDir = str[0];
						boolean copy = FileUtil.copyFile(str[0], imgPath + dataDir, str[1]);
						if(copy) {
							Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
							Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
							//Constant.ImgId += 1;
							System.out.println("OKKKK");
						}else {
							System.out.println("文件复制出错");
							Constant.ShuffleFlag += 1;
							readyMark("1");
						}
					}
					
					//获取图片label_data
					String csvPath = dataPath + "/" + Constant.User
							+ "/csv/" + dataDir + "/" + str[1].split("[.]")[0] + ".csv";
					File ff1 = new File(dataPath + "/" + Constant.User + "/csv/" + dataDir);
					if(!ff1.exists()) {
						ff1.mkdirs();
					}
					File ff = new File(csvPath);
					if(ff.exists()) {
						System.out.println("quanbu " + csvPath);
						List<String[]> list = ReadCSV.readCSV(csvPath);
						String[] s = list.get(list.size() - 1);
						Constant.ViewImgPath = s[2] + "/" + str[1];
						Constant.ViewDir = s[8];
						Constant.ViewImgName = str[1];
						Constant.LData = s[7];
					}else {
						System.out.println("该图片未标注");
						Constant.LData = "";
						Constant.ViewImgPath = "";
						Constant.ViewDir = "";
						Constant.ViewImgName = "";
					}
					System.out.println("label data = " + Constant.LData);
					//Browser.webEngine.executeScript("label('" + "" + "' ,'"+ "" + "')");
				}
				
			}
			else if("2".equals(markType)) {
				Constant.MarkType = markType;
				System.out.println("mark type = " + Constant.MarkType);
				System.out.println("已标注进入");
				int offset = Constant.MarkedOrderFlag;
				System.out.println(offset + "==" + Constant.ImgAll.size());
				if(offset >= Constant.ImgAll.size()) {
					System.out.println("标注图片查看完毕,将跳转到主界面");
					Constant.MarkedOrderFlag = 0;
					Constant.OverFlag = 1;
					Browser.webEngine.load(Browser.class.getResource("index.html").toExternalForm());
				}else {
					int fflag = 0;
					String dirAndName = Constant.ImgAllList.get(offset);
					while(Constant.ImgAll.get(dirAndName) == 0) {
						Constant.MarkedOrderFlag += 1;
						dirAndName = Constant.ImgAllList.get(Constant.MarkedOrderFlag);
						if(Constant.MarkedOrderFlag >= Constant.ImgAll.size()) {
							fflag = 1;
							break;
						}
					}
					if(fflag == 1) {
						System.out.println("标注图片已达最大值");
					}
					else {
						String[] str = dirAndName.split(",");
						String dataDir = str[0].split(":")[0] + "." + str[0].split(":")[1].substring(1);
						dataDir = dataDir.replace('/', '.');
						File file = new File(imgPath + dataDir + "/" + str[1]);
						System.out.println("imgpath mark = " + imgPath + dataDir + "/" + str[1]);
						if(file.exists()) {
							System.out.println("已标注  文件已存在  不用复制");
							Constant.CurrDir = str[0];
							Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
							Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
						}else {
							Constant.CurrDir = str[0];
							boolean copy = FileUtil.copyFile(str[0], imgPath + dataDir, str[1]);
							if(copy) {
								Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
								Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
							}else {
								System.out.println("文件复制出错");
								Constant.OrderFlag += 1;
								readyMark("2");
							}
						}
						
						//获取图片label_data
						String csvPath = dataPath + "/" + Constant.User
								+ "/csv/" + dataDir + "/" + str[1].split("[.]")[0] + ".csv";
						File ff1 = new File(dataPath + "/" + Constant.User + "/csv/" + dataDir);
						if(!ff1.exists()) {
							ff1.mkdirs();
						}
						File ff = new File(csvPath);
						if(ff.exists()) {
							System.out.println("quanbu " + csvPath);
							List<String[]> list = ReadCSV.readCSV(csvPath);
							String[] s = list.get(list.size() - 1);
							Constant.ViewImgPath = s[2] + "/" + str[1];
							Constant.ViewDir = s[8];
							Constant.ViewImgName = str[1];
							Constant.LData = s[7];
						}else {
							System.out.println("该图片未标注");
						}
						System.out.println("label data = " + Constant.LData);
					}
				}
				//Browser.webEngine.executeScript("label('" + "" + "' ,'"+ "" + "')");
			}
			else if("3".equals(markType)) {
				Constant.MarkType = markType;
				System.out.println("mark type = " + Constant.MarkType);
				System.out.println("未标注进入");
				int offset = Constant.OrderFlag;
				System.out.println(offset + "==" + Constant.ImgAll.size());
				if(offset >= Constant.ImgAll.size()) {
					System.out.println("没有未标注图片,将跳转到主界面");
					Constant.OverFlag = 1;
					Browser.webEngine.load(Browser.class.getResource("index.html").toExternalForm());
				}else {
					String dirAndName = Constant.ImgAllList.get(offset);
					while(Constant.ImgAll.get(dirAndName) == 1) {
						Constant.OrderFlag += 1;
						dirAndName = Constant.ImgAllList.get(Constant.OrderFlag);
						if(Constant.OrderFlag >= Constant.ImgAllList.size()) {
							break;
						}
					}
					
					String[] str = dirAndName.split(",");
					String dataDir = str[0].split(":")[0] + "." + str[0].split(":")[1].substring(1);
					dataDir = dataDir.replace('/', '.');
					File file = new File(imgPath + dataDir + "/" + str[1]);
					System.out.println("imgpath mark = " + imgPath + dataDir + "/" + str[1]);
					if(file.exists()) {
						System.out.println("未标注  文件已存在  不用复制");
						//Constant.CurrDir = str[0];
						Constant.CurrDir = str[0];
						//Constant.CurrImg = str[0] + "/" + Constant.User + "/data/" + dataDir + "/" + str[1];
						//File ff = new File(imgPath + dataDir + "/" + str[1]);
						//URI ss = ff.toURI();
						//Constant.CurrImg = ss.toString();
						Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
						Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
						//Constant.ImgId += 1;
						Constant.LData = "";
					}else {
						Constant.CurrDir = str[0];
						boolean copyFlag = FileUtil.copyFile(str[0], imgPath + dataDir, str[1]);
						if(copyFlag) {
							System.out.println("nali");
							Constant.CurrImg = getImgIo(imgPath + dataDir + "/" + str[1]);
							//File ff = new File(imgPath + dataDir + "/" + str[1]);
							//URI ss = ff.toURI();
							//Constant.CurrImg = ss.toString();
							Constant.CurrMarked = Constant.ImgAll.get(dirAndName);
							//Constant.ImgId += 1;
							Constant.LData = "";
						}else {
							System.out.println("文件复制出错");
							Constant.OrderFlag += 1;
							Constant.LData = "";
							readyMark("3");
						}
					}
					//Browser.webEngine.executeScript("label('" + "" + "' ,'"+ "" + "')");
				}
				
			}
			System.out.println("返回界面");
			
		}
	}
	
	//标注图片
	public void markImage(String offset) {
		//开启弹出菜单
		Browser.browser.setContextMenuEnabled(true);
		//读CSV文件更新MarkFlag 防止出现不一致bug
//		List<String[]> list = readCSVData("dir.csv");
//		for(String[] s : list) {
//			if(s[2].equals(Constant.IdDir.get(Integer.parseInt(offset)))) {
//				Constant.MarkFlag.put(s[2], Integer.parseInt(s[3]));
//				break;
//			}
//		}
		
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
		String imgPath = dataPath + "/" + Constant.User + "/data/" + dataDir;
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
					String pa1 = "../../../../" + Constant.User + "/data/" + dataDir2;
					Constant.CurrImg = pa1 + "/" + 
							Constant.ImgMap.get(s).get(Constant.MarkFlag.get(s));
					boolean copy = FileUtil.copyFile(Constant.CurrDir, path1 + "/" + Constant.User + "/data/" + dataDir2, imgName1);
					if(copy) {
						//markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(dir, markFlagNew);  //更新当前dir已标注id
						Constant.ImgId += 1;
					}else {
						//markFlagNew = markFlagNew + 1;
						Constant.MarkFlag.put(dir, markFlagNew);
						String offset1 = "";
						for(Integer i : Constant.IdDir.keySet()) {
							if(dir.equals(Constant.IdDir.get(i))) {
								offset1 = "" + i;
							}
						}
						readMark(offset1, path1 + "/" + Constant.User + "/data/" + dataDir2);
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
				//markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);  //更新当前dir已标注id
				Constant.ImgId += 1;
				System.out.println("offset" + offset);
				String path = System.getProperty("user.dir").replace("\\", "/");
				Constant.CurrImg = "../../../../" + Constant.User + "/data/" + dataDir + "/" + imgName;
				Browser.webEngine.executeScript("label('" + dataDir + "' ,'"+ imgName + "')");
			}else {
				//markFlag = markFlag + 1;
				Constant.MarkFlag.put(dir, markFlag);
				readMark(offset, imgPath);
			}
		}
	}
	
	public void getCurrImg() {
		String img = Constant.CurrImg;
		System.out.println(",img = " + img);
		String imgname = "";
		if("1".equals(Constant.MarkType)) {
			imgname = Constant.ImgAllList.get(Constant.ShuffleFlag).split(",")[1];
		}
		else if("2".equals(Constant.MarkType)) {
			imgname = Constant.ImgAllList.get(Constant.MarkedOrderFlag).split(",")[1];
		}
		else if("3".equals(Constant.MarkType)) {
			imgname = Constant.ImgAllList.get(Constant.OrderFlag).split(",")[1];
		}
		//String[] str = img.split("/");
		//String imgname = str[str.length - 1];
		String dir = Constant.CurrDir;
		int markedflag = Constant.CurrMarked;
		Browser.webEngine.executeScript("setImg1(" + markedflag + ",'" + imgname + "','" + dir + "','"+ img + "','" + Constant.LData + "')");
	}
	
	public void markList() {
		System.out.println("标注列表");
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/csv/imgAllLabel.csv";
		String dirPath = rootPath + "/" + Constant.User + "/csv/dir.csv";
		File f3 = new File(dirPath);
		List<String[]> list2 = new ArrayList<String[]>();
		if(f3.exists()) {
			list2 = ReadCSV.readCSV(dirPath);
		}
		File f2 = new File(filePath);
		if(f2.exists()) {
			List<String[]> list1 = ReadCSV.readCSV(filePath);
			List<Image> list = new ArrayList<Image>();
			for(String[] s : list1) {
				if(!imgPass(s, list2)) {
				    System.out.println("该图所对应文件夹已被删除");	
				}
				else {
					Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
					System.out.println("img = " + img);
					list.add(img);
				}
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
	
	public boolean imgPass(String[] s, List<String[]> list) {
		boolean flag = true;
		for(String[] str : list) {
			if(str[2].equals(s[8])) {
				if(str[7].equals("1")) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	public void nextImg() {
		if("1".equals(Constant.MarkType)) {
			Constant.ShuffleFlag += 1;
		}
		else if("2".equals(Constant.MarkType)) {
			Constant.MarkedOrderFlag += 1;
		}
		// 获取下一张图片信息
		readyMark2(Constant.MarkType);
		if (Constant.OverFlag == 0) {
			Browser.webEngine.executeScript("markNext()");
		} else {
			Constant.OverFlag = 0;
			System.out.println("已经返回到主界面");
		}
	}
	
	// 标注修改
	public void updateImgLabelNew(String imgData) {
		System.out.println("获取到 修改imgData = " + imgData);
		SaveToCSV.updateImgLabelDataNew(imgData);

		// 获取下一张图片信息
		readyMark2(Constant.MarkType);
		if (Constant.OverFlag == 0) {
			Browser.webEngine.executeScript("markNext()");
		} else {
			Constant.OverFlag = 0;
			System.out.println("已经返回到主界面");
		}
	}
	
	//标注保存 new
	public void saveImgLabelNew(String imgData) {
		System.out.println("获取到 imgData = " + imgData);
		SaveToCSV.savaImgLabelDataNew(imgData);
		
		//获取下一张图片信息
		readyMark2(Constant.MarkType);
		if(Constant.OverFlag == 0) {
			Browser.webEngine.executeScript("markNext()");
		}else {
			Constant.OverFlag = 0;
			System.out.println("已经返回到主界面");
		}
	}
	
	//标注保存  old
	public void saveImgLabel(String imgData) {
		System.out.println("获取到 imgData = " + imgData);
		SaveToCSV.saveImgLabelData(imgData);
		
		if(Constant.CSVFlag == 1) {
			Browser.webEngine.executeScript("showErr()");
			Constant.CSVFlag = 0;
		}else {
			//生成下一张图片数据
			System.out.println("开始生成下一张图片");
			int markFlag = Constant.MarkFlag.get(Constant.CurrDir);
			String tmpdir = Constant.CurrDir.split(":")[0] + "." + Constant.CurrDir.split(":")[1].substring(1);
			String dataDir = tmpdir.replace('/', '.');
			String path = System.getProperty("user.dir").replace("\\", "/");
			String pa = "/" + Constant.User + "/data/" + dataDir + "/";
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
						String pa1 = "../../../../" + Constant.User + "/data/" + dataDir2;
						Constant.CurrImg = pa1 + "/" + 
								Constant.ImgMap.get(s).get(Constant.MarkFlag.get(s));
						boolean copy = FileUtil.copyFile(Constant.CurrDir, path1 + "/" + Constant.User + "/data/" + dataDir2, imgName1);
						
						if(copy) {
							//markFlagNew = markFlagNew + 1;
							Constant.MarkFlag.put(Constant.CurrDir, markFlagNew);  //更新当前dir已标注id
							Constant.ImgId += 1;
						}else {
							//markFlagNew = markFlagNew + 1;
							Constant.MarkFlag.put(Constant.CurrDir, markFlagNew);
							String offset = "";
							for(Integer i : Constant.IdDir.keySet()) {
								if(Constant.CurrDir.equals(Constant.IdDir.get(i))) {
									offset = "" + i;
								}
							}
							readMark(offset, path1 + "/" + Constant.User + "/data/" + dataDir2);
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
				String imgPath = path + "/" + Constant.User + "/data/" + dataDir;
				boolean copy = FileUtil.copyFile(Constant.CurrDir, imgPath, imgName);
				if(copy) {
					//markFlag = markFlag + 1;
					Constant.MarkFlag.put(dir, markFlag);  //更新当前dir已标注id
					Constant.ImgId += 1;
				}else {
					//markFlag = markFlag + 1;
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
		
	}
	
	public void imgView(String dir, String imgname){
		System.out.println(dir + "=" + imgname);
		String path = System.getProperty("user.dir").replace("\\", "/");
		String dataDir = dir.split("/")[dir.split("/").length - 1];
		String filePath = path + "/" + Constant.User + "/csv/" + dataDir
				 + "/" + imgname.split("[.]")[0] + ".csv";
		System.out.println("mark view = " + filePath);
		File file = new File(filePath);
		if(file.exists()) {
			List<String[]> list = ReadCSV.readCSV(filePath);
			String[] s = list.get(list.size() - 1);
			Constant.ViewImgPath = s[2] + "/" + imgname;
			Constant.ViewDir = s[8];
			Constant.ViewImgName = imgname;
			Constant.LData = s[7];
			
		}else {
			System.out.println("未找到文件");
		}
		Browser.webEngine.load(Browser.class.getResource("label_view.html").toExternalForm());
	}
	
	public void viewImg() {
		System.out.println("dir = " + Constant.ViewDir);
		System.out.println("name = " + Constant.ViewImgName);
		System.out.println("path = " + Constant.ViewImgPath);
		System.out.println("data = " + Constant.LData);
		Browser.webEngine.executeScript("setImg('" + Constant.ViewImgName + 
				 "','" + Constant.ViewDir + "','" + Constant.ViewImgPath +
				 "','" + Constant.LData + "')");
		JSONObject obj = JSON.parseObject(Constant.LData);
		System.out.println("data = " + obj.getString("label_data"));
	}
	
	public static void main(String[] args) {
		String rootPath = JavaApp1.class.getResource("/").getPath().replaceAll("%20", "");
		System.out.println(rootPath);
		String dataPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
		System.out.println(dataPath);
		String path = JavaApp1.class.getResource("").getPath();
		System.out.println(path + "=" + path.substring(0, path.length() - 1));
		String root = path.substring(0, path.length() - 1);
		System.out.println(root.substring(0, root.lastIndexOf('/')));
		String dataDir = root.replace('/', '.');
		System.out.println(dataDir);
		
		String dataPath1 = System.getProperty("user.dir").replace("\\", "/");
		System.out.println(dataPath1);
		
	}
}