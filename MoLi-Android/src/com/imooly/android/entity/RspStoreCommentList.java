package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 实体店评价列表
 * 
 * @author LSD
 * 
 */
public class RspStoreCommentList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private String businessid;
		private int highopinion;
		private int totalcomment;
		public List<StoreComment> commentlist;

		public String getBusinessid() {
			return businessid;
		}

		public void setBusinessid(String businessid) {
			this.businessid = businessid;
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

		public List<StoreComment> getCommentlist() {
			return commentlist;
		}

		public void setCommentlist(List<StoreComment> commentlist) {
			this.commentlist = commentlist;
		}

	}

	public static class StoreComment {
		private String username;
		private String senddate;
		private String content;
		private int star;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
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

		public int getStar() {
			return star;
		}

		public void setStar(int star) {
			this.star = star;
		}

	}
}
