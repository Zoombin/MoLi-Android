package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspOrderList.OrderOpEty;

/***
 * 售后服务-退换货商品列表
 * 
 * @author LSD
 * 
 */
public class RspSgoodslist extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public int total;
		public List<SGoodsEty> goodslist;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public List<SGoodsEty> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<SGoodsEty> goodslist) {
			this.goodslist = goodslist;
		}

	}

	public static class SGoodsEty {
		public String orderno;
		public String tradeid;
		public String goodsid;
		public String unique;
		public String image;
		public String name;
		public float price;
		public int num;
		public String statusname;
		public List<OrderOpEty> oplist;

		
		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getTradeid() {
			return tradeid;
		}

		public void setTradeid(String tradeid) {
			this.tradeid = tradeid;
		}

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getUnique() {
			return unique;
		}

		public void setUnique(String unique) {
			this.unique = unique;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getStatusname() {
			return statusname;
		}

		public void setStatusname(String statusname) {
			this.statusname = statusname;
		}

		public List<OrderOpEty> getOplist() {
			return oplist;
		}

		public void setOplist(List<OrderOpEty> oplist) {
			this.oplist = oplist;
		}

	}
}
