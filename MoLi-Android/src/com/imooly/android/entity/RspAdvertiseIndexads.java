package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 首页广告
 * 
 * @author daiye
 * 
 */
public class RspAdvertiseIndexads extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public Data data;

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;

		private String tpl;
		private List<Tplcontent> tplcontent;

		public String getTpl() {
			return tpl;
		}

		public void setTpl(String tpl) {
			this.tpl = tpl;
		}

		public List<Tplcontent> getTplcontent() {
			return tplcontent;
		}

		public void setTplcontent(List<Tplcontent> tplcontent) {
			this.tplcontent = tplcontent;
		}
	}

	public static class Tplcontent implements Serializable {

		private static final long serialVersionUID = 1L;

		private String adid;
		private String type;
		private List<Info> infos;
		
		public String getAdid() {
			return adid;
		}
		public void setAdid(String adid) {
			this.adid = adid;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public List<Info> getInfos() {
			return infos;
		}
		public void setInfos(List<Info> infos) {
			this.infos = infos;
		}
	}

	public static class Info implements Serializable {

		private static final long serialVersionUID = 1L;

		private String title;
		private String imagepath;
		private String redirect;
		private String apppagecode;
		private String paramid;
		private String url;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getImagepath() {
			return imagepath;
		}
		public void setImagepath(String imagepath) {
			this.imagepath = imagepath;
		}
		public String getRedirect() {
			return redirect;
		}
		public void setRedirect(String redirect) {
			this.redirect = redirect;
		}
		public String getApppagecode() {
			return apppagecode;
		}
		public void setApppagecode(String apppagecode) {
			this.apppagecode = apppagecode;
		}
		public String getParamid() {
			return paramid;
		}
		public void setParamid(String paramid) {
			this.paramid = paramid;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
