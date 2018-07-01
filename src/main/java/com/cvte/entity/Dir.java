package com.cvte.entity;
/** 
* @author: jan 
* @date: 2018年5月11日 上午11:06:24 
*/
public class Dir {

	private String id;
	
	private String user;
	
	private String dir;
	
	private String markedId;
	
	private String total;
	
	private String addTime;
	
	private String deleted;

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

	public String getMarkedId() {
		return markedId;
	}

	public void setMarkedId(String markedId) {
		this.markedId = markedId;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Dir(String id, String user, String dir, String markedId, String total, String addTime, String deleted) {
		super();
		this.id = id;
		this.user = user;
		this.dir = dir;
		this.markedId = markedId;
		this.total = total;
		this.addTime = addTime;
		this.deleted = deleted;
	}

	public Dir() {
		super();
	}

	@Override
	public String toString() {
		return "Dir [id=" + id + ", user=" + user + ", dir=" + dir + ", markedId=" + markedId + ", total=" + total
				+ ", addTime=" + addTime + ", deleted=" + deleted + "]";
	}
	
}
