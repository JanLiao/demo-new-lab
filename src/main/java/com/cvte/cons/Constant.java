package com.cvte.cons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.stage.Stage;

/** 
* @author: jan 
* @date: 2018年5月9日 下午8:52:07 
*/
public class Constant {
	
	//判断图片标志
	public static Integer imgf1 = 1;  //图片命名规则   1___XX_LR.jpg   2___XX.jpg
	public static Integer imgf2 = 2;
	
	//判断是否登录
	public static Integer login = 0;
	
	//保存用户名
	public static String User = "";
	
	//flag标志
	public static Integer Flag = 0;

	//添加文件夹序号
	public static Integer DirNum = 0;
	
	//保存已添加的文件夹  弃用
	public static Set<String> DirSetName = new HashSet<String>();
	
	//保存已添加的文件夹   value 是否删除     1-删除      0-使用
	public static Map<String, Integer> DirName = new HashMap<String, Integer>();
	
	//顺序保存所有已添加的文件夹
	public static List<String> DirList = new ArrayList<String>();
	
	//保存文件夹下图片
	public static Map<String, List<String>> ImgMap = new HashMap<String, List<String>>();
	
	//保存所选文件夹所有文件
	public static Map<String, Integer> ImgAll = new HashMap<String, Integer>();
	//保存所选文件夹所有文件
	public static List<String> ImgAllList = new ArrayList<String>();
	//所有文件打乱的编号
	public static List<Integer> ShuffleList = new ArrayList<Integer>();
	//当前Shuffle flag
	public static Integer ShuffleFlag = 0;
	//顺序标注flag  -- 未标注 flag
	public static Integer OrderFlag = 0;
	//顺序标注flag  -- 已标注 flag
	public static Integer MarkedOrderFlag = 0;
	//当前图片是否标注
	public static Integer CurrMarked = 0;
	//用户所选标注方式
	public static String MarkType = "";
	//判断是否已经全部标注
	public static Integer OverFlag = 0;
	
	//保存文件夹Mark 已标志id
	public static Map<String, Integer> MarkFlag = new HashMap<String, Integer>();
	
	//保存文件夹id对应的文件
	public static Map<Integer, String> IdDir = new HashMap<Integer, String>();
	
	//保存当前要标注的图片路径  image标签路径
	public static String CurrImg = "";
	
	//记录所有image id
	public static Integer ImgId = 0;
	
	//保存当前所选文件夹
	public static String CurrDir = "";
	
	//canvas json数据
	public static String LData = "";
	//标注图片原路径(添加任务路径)
	public static String ViewDir = "";
	//已标注图片路径
	public static String ViewImgPath = "";
	//已标注图片名
	public static String ViewImgName = "";
	
	//读写CSV文件是否成功
	public static Integer CSVFlag = 0;
	
	public static Integer writeFlag = 0;
	
	public static Stage stage = new Stage();
		
}
