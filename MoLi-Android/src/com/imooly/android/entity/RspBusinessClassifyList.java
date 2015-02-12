package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商家分类
 * 
 * @author daiye
 * 
 */

public class RspBusinessClassifyList extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;

		@SerializedName("classifylist")
		public List<ClassifyEntity> classifylist;

		public List<ClassifyEntity> getClassifylist() {
			return classifylist;
		}

		public void setClassifylist(List<ClassifyEntity> classifylist) {
			this.classifylist = classifylist;
		}
	}

	public class ClassifyEntity implements Serializable {

		private static final long serialVersionUID = 1L;

		public String classifyid;
		public String classifyname;
		public String classifyicon;
		public String onsmallicon;
		public String outsmallicon;

		public List<SubClassifyEntity> subclassify;

		public String getClassifyid() {
			return classifyid;
		}

		public void setClassifyid(String classifyid) {
			this.classifyid = classifyid;
		}

		public String getClassifyname() {
			return classifyname;
		}

		public void setClassifyname(String classifyname) {
			this.classifyname = classifyname;
		}

		public String getClassifyicon() {
			return classifyicon;
		}

		public void setClassifyicon(String classifyicon) {
			this.classifyicon = classifyicon;
		}

		public List<SubClassifyEntity> getSubclassify() {
			return subclassify;
		}

		public void setSubclassify(List<SubClassifyEntity> subclassify) {
			this.subclassify = subclassify;
		}

		public String getOnsmallicon() {
			return onsmallicon;
		}

		public void setOnsmallicon(String onsmallicon) {
			this.onsmallicon = onsmallicon;
		}

		public String getOutsmallicon() {
			return outsmallicon;
		}

		public void setOutsmallicon(String outsmallicon) {
			this.outsmallicon = outsmallicon;
		}

	}

	public class SubClassifyEntity implements Serializable {

		private static final long serialVersionUID = 1L;

		public String classifyid;
		public String classifyname;

		public String getClassifyid() {
			return classifyid;
		}

		public void setClassifyid(String classifyid) {
			this.classifyid = classifyid;
		}

		public String getClassifyname() {
			return classifyname;
		}

		public void setClassifyname(String classifyname) {
			this.classifyname = classifyname;
		}
	}
}
