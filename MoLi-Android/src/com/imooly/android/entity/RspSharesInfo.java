package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 分享内容获取
 * 
 * @author daiye
 * 
 */
public class RspSharesInfo extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		String stype;
		String title;
		String word;
		String image;
		String link;
		
		public String getStype() {
			return stype;
		}
		public void setStype(String stype) {
			this.stype = stype;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
	}
}
