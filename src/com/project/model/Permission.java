package com.project.model;

public class Permission {
	private String permissionName;
	private String permissionFull;
	private String permissionDescription;
	public boolean check;
	
	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionFull() {
		return permissionFull;
	}

	public void setPermissionFull(String permissionFull) {
		this.permissionFull = permissionFull;
	}

	public Permission(String permissionName, String permissionFull, String permissionDescription){
		this.permissionName = permissionName;
		this.permissionFull  = permissionFull;
		this.permissionDescription = permissionDescription;
	}

	public CharSequence getPermissionDescription() {
		return permissionDescription;
	}

	public void setPermissionDescription(String permissionDescription) {
		this.permissionDescription = permissionDescription;
	}
}
