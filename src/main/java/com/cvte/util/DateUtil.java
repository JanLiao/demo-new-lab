package com.cvte.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cvte.cons.Constant;
import com.cvte.entity.Image;
import com.cvte.ui.Browser;

/** 
* @author: jan 
* @date: 2018年6月6日 下午11:15:50 
*/
public class DateUtil {

	public static void queryOneDay(String start, String imgname) {
		System.out.println("标注列表条件相等");
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/file/imgAllLabel";
		String dirPath = rootPath + "/" + Constant.User + "/file/dir";
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
					String[] st = s[4].split(" ");
					if(start.equals(st[0])) {
						Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
						System.out.println("img = " + img);
						list.add(img);
					}
				}
			}
			String str = JSON.toJSONString(list);
			Browser.webEngine.executeScript("getMarkList(" + list.size() + " ,'"+ str + "','" + Constant.User + "')");
		}
	}
	
	public static boolean imgPass(String[] s, List<String[]> list) {
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

	public static void queryOneCondition(String date, int flag, String imgname) {
		System.out.println("标注列表单条件");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/file/imgAllLabel";
		String dirPath = rootPath + "/" + Constant.User + "/file/dir";
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
					String[] st = s[4].split(" ");
					Date d1 = null;
					try {
						d1 = format.parse(st[0]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Date d2 = null;
					try {
						d2 = format.parse(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(flag == 0) {
						if(d1.before(d2)) {
//							Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
//							System.out.println("img = " + img);
//							list.add(img);
						}
						else {
							Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
							if(s[3].indexOf(imgname) != -1) {								
								System.out.println("img = " + img);
								list.add(img);
							}else {
								System.out.println("可惜了--" + img);
							}
						}
					}
					else if(flag == 1) {
						if(d2.before(d1)) {
//							Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
//							System.out.println("img = " + img);
//							list.add(img);
						}
						else {
							Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
							if(s[3].indexOf(imgname) != -1) {								
								System.out.println("img = " + img);
								list.add(img);
							}else {
								System.out.println("可惜了--" + img);
							}
						}
					}
				}
			}
			String str = JSON.toJSONString(list);
			Browser.webEngine.executeScript("getMarkList(" + list.size() + " ,'"+ str + "','" + Constant.User + "')");
		}
	}

	public static void queryTwoCondition(String start, String end, String imgname) {
		System.out.println("标注列表双条件");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String rootPath = System.getProperty("user.dir").replace("\\", "/");
		String filePath = rootPath + "/" + Constant.User + "/file/imgAllLabel";
		String dirPath = rootPath + "/" + Constant.User + "/file/dir";
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
					String[] st = s[4].split(" ");
					Date d1 = null;
					Date d2 = null;
					Date d3 = null;
					try {
						d1 = format.parse(start);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
						d2 = format.parse(end);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
						d3 = format.parse(st[0]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(d3.after(d1) && d3.before(d2)) {
						Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
						if(s[3].indexOf(imgname) != -1) {								
							System.out.println("img = " + img);
							list.add(img);
						}else {
							System.out.println("可惜了--" + img);
						}
					}
					else {
						if(start.equals(st[0]) || end.equals(st[0])) {
							Image img = new Image(s[0], s[1], s[2], s[3], "已标注", s[4]);
							if(s[3].indexOf(imgname) != -1) {								
								System.out.println("img = " + img);
								list.add(img);
							}else {
								System.out.println("可惜了--" + img);
							}
						}
					}
				}
			}
			String str = JSON.toJSONString(list);
			Browser.webEngine.executeScript("getMarkList(" + list.size() + " ,'"+ str + "','" + Constant.User + "')");
		}
	}
}
