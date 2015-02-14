package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品评价列表
 * 
 * @author LSD
 * 
 */
public class RspCommentList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String goodsid;
		public String goodsname;
		public int highopinion;
		public int totalcomment;
		public List<Comment> commentlist;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getGoodsname() {
			return goodsname;
		}

		public void setGoodsname(String goodsname) {
			this.goodsname = goodsname;
		}

		public int getHighopinion() {
			return highopinion;
		}

		public void setHighopinion(int highopinion) {
			this.highopinion = highopinion;
		}

		public int getTotalcomment() {
			return totalcomment;
		}

		public void setTotalcomment(int totalcomment) {
			this.totalcomment = totalcomment;
		}

		public List<Comment> getCommentlist() {
			return commentlist;
		}

		public void setCommentlist(List<Comment> commentlist) {
			this.commentlist = commentlist;
		}


	}

	public static class Comment {
		public String username;
		public String spec;
		public String senddate;
		public String content;
		public String adddate;
		public String addcontent;
		public List<String>images;


		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

		public String getSenddate() {
			return senddate;
		}

		public void setSenddate(String senddate) {
			this.senddate = senddate;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getAdddate() {
			return adddate;
		}

		public void setAdddate(String adddate) {
			this.adddate = adddate;
		}

		public String getAddcontent() {
			return addcontent;
		}

		public void setAddcontent(String addcontent) {
			this.addcontent = addcontent;
		}

		public List<String> getImages() {
			return images;
		}

		public void setImages(List<String> images) {
			this.images = images;
		}

	}
}
