package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品分类
 * 
 * @author LSD
 * 
 */
public class RspOrderList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public List<OrderEty> orderlist;

		public List<OrderEty> getOrderlist() {
			return orderlist;
		}

		public void setOrderlist(List<OrderEty> orderlist) {
			this.orderlist = orderlist;
		}
	}

	public static class OrderEty {
		public String orderno;
		public String storeid;
		public String storename;
		public float totalprice;
		List<OrderGdsEty> goods;
		List<OrderOpEty> oplist;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getStoreid() {
			return storeid;
		}

		public void setStoreid(String storeid) {
			this.storeid = storeid;
		}

		public String getStorename() {
			return storename;
		}

		public void setStorename(String storename) {
			this.storename = storename;
		}

		public float getTotalprice() {
			return totalprice;
		}

		public void setTotalprice(float totalprice) {
			this.totalprice = totalprice;
		}

		public List<OrderGdsEty> getGoods() {
			return goods;
		}

		public void setGoods(List<OrderGdsEty> goods) {
			this.goods = goods;
		}

		public List<OrderOpEty> getOplist() {
			return oplist;
		}

		public void setOplist(List<OrderOpEty> oplist) {
			this.oplist = oplist;
		}

	}

	public static class OrderGdsEty {
		public String goodsid;
		public String image;
		public String name;
		public float price;
		public int num;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
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

	}

	public static class OrderOpEty {
		public String code;
		public String name;
		public String bgcolor;
		public String fontcolor;
		public String bordercolor;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBgcolor() {
			return bgcolor;
		}

		public void setBgcolor(String bgcolor) {
			this.bgcolor = bgcolor;
		}

		public String getFontcolor() {
			return fontcolor;
		}

		public void setFontcolor(String fontcolor) {
			this.fontcolor = fontcolor;
		}

		public String getBordercolor() {
			return bordercolor;
		}

		public void setBordercolor(String bordercolor) {
			this.bordercolor = bordercolor;
		}

	}
}
