package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品分类
 * 
 * @author LSD
 *
 */
public class RspGoodsClassList extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public int flag;
		public String lastpulltime;
		public List<GoodsClassify> classifylist;
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public String getLastpulltime() {
			return lastpulltime;
		}
		public void setLastpulltime(String lastpulltime) {
			this.lastpulltime = lastpulltime;
		}
		public List<GoodsClassify> getClassifylist() {
			return classifylist;
		}
		public void setClassifylist(List<GoodsClassify> classifylist) {
			this.classifylist = classifylist;
		}
	}

	public static class GoodsClassify implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public String classifyid;
		public String classifyname;
		public String classifyicon;
		public String caption;
		public List<GoodsClassify> subclassify;

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

		public List<GoodsClassify> getSubclassify() {
			return subclassify;
		}

		public void setSubclassify(List<GoodsClassify> subclassify) {
			this.subclassify = subclassify;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}
	}
}
