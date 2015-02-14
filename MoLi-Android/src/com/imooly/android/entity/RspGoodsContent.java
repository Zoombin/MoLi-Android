package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品图文详情
 * 
 * @author LSD
 * 
 */
public class RspGoodsContent extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String goodsid;
		public String goodsname;
		public String linkaddress;
		public String lastpulltime;
		public List<String> goodscontent;

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

		public String getLinkaddress() {
			return linkaddress;
		}

		public void setLinkaddress(String linkaddress) {
			this.linkaddress = linkaddress;
		}

		public String getLastpulltime() {
			return lastpulltime;
		}

		public void setLastpulltime(String lastpulltime) {
			this.lastpulltime = lastpulltime;
		}

		public List<String> getGoodscontent() {
			return goodscontent;
		}

		public void setGoodscontent(List<String> goodscontent) {
			this.goodscontent = goodscontent;
		}

	}

}
