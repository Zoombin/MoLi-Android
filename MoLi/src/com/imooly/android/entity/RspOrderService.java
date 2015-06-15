package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspAddressList.Address;

/***
 * 售后服务-获取商品售后信息
 * 
 * @author LSD
 * 
 */
public class RspOrderService extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		ServiceStoreEty store;
		ServiceGoodsEty goods;
		ServiceEty service;
		ServiceAddressEty address;

		public ServiceStoreEty getStore() {
			return store;
		}

		public void setStore(ServiceStoreEty store) {
			this.store = store;
		}

		public ServiceGoodsEty getGoods() {
			return goods;
		}

		public void setGoods(ServiceGoodsEty goods) {
			this.goods = goods;
		}

		public ServiceEty getService() {
			return service;
		}

		public void setService(ServiceEty service) {
			this.service = service;
		}

		public ServiceAddressEty getAddress() {
			return address;
		}

		public void setAddress(ServiceAddressEty address) {
			this.address = address;
		}

	}

	public static class ServiceStoreEty {
		String storeid;
		String storename;
		float totalprice;

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

	}

	public static class ServiceGoodsEty {
		String goodsid;
		String tradeid;
		String image;
		String name;
		float price;
		int num;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getTradeid() {
			return tradeid;
		}

		public void setTradeid(String tradeid) {
			this.tradeid = tradeid;
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

	public static class ServiceEty {
		@SerializedName("return")
		int returns;
		int change;

		public int getReturns() {
			return returns;
		}

		public void setReturns(int returns) {
			this.returns = returns;
		}

		public int getChange() {
			return change;
		}

		public void setChange(int change) {
			this.change = change;
		}

	}

	public static class ServiceAddressEty {
		public String addressid;
		public String name;
		public String tel;
		public String address;
		public String postcode;

		public String getAddressid() {
			return addressid;
		}

		public void setAddressid(String addressid) {
			this.addressid = addressid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPostcode() {
			return postcode;
		}

		public void setPostcode(String postcode) {
			this.postcode = postcode;
		}

	}

}
