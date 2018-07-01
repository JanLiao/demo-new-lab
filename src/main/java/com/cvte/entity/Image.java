package com.cvte.entity;
/** 
* @author: jan 
* @date: 2018年5月10日 下午5:36:45 
*/
public class Image {

	private String id;
	
	private String user;
	
	private String dir;
	
	private String imgname;
	
	private String state;
	
	private String marktime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMarktime() {
		return marktime;
	}

	public void setMarktime(String marktime) {
		this.marktime = marktime;
	}

	public Image(String id, String user, String dir, String imgname, String state, String marktime) {
		super();
		this.id = id;
		this.user = user;
		this.dir = dir;
		this.imgname = imgname;
		this.state = state;
		this.marktime = marktime;
	}

	public Image() {
		super();
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", user=" + user + ", dir=" + dir + ", imgname=" + imgname + ", state=" + state
				+ ", marktime=" + marktime + "]";
	}
}
