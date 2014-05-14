package com.project.model;

public class User 
{
	private long userid;
	private String username;
	private String bucketname;
	
	public User(){}
	public User(long userid, String username, String bucketname) {
		super();
		this.userid = userid;
		this.username = username;
		this.bucketname = bucketname;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBucketname() {
		return bucketname;
	}
	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}
	
	
}
