package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspOrderList.OrderOpEty;

/***
 * 订单详情
 * 
 * @author LSD
 * 
 */
public class RspOrderProfile extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		String orderno;
		OrderAddrEty address;
		logisticEty logistic;
		OrderStoEty store;
		OrderStatusEty status;
		List<OrderGoodsEty> goods;
		Settle settle;
		String remark;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public OrderAddrEty getAddress() {
			return address;
		}

		public void setAddress(OrderAddrEty address) {
			this.address = address;
		}

		public logisticEty getLogistic() {
			return logistic;
		}

		public void setLogistic(logisticEty logistic) {
			this.logistic = logistic;
		}

		public OrderStoEty getStore() {
			return store;
		}

		public void setStore(OrderStoEty store) {
			this.store = store;
		}

		public OrderStatusEty getStatus() {
			return status;
		}

		public void setStatus(OrderStatusEty status) {
			this.status = status;
		}

		public List<OrderGoodsEty> getGoods() {
			return goods;
		}

		public void setGoods(List<OrderGoodsEty> goods) {
			this.goods = goods;
		}

		public Settle getSettle() {
			return settle;
		}

		public void setSettle(Settle settle) {
			this.settle = settle;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}

	public static class OrderAddrEty {
		public String addressid;
		public String address;
		public String postcode;
		public String name;
		public String telephone;

		public String getAddressid() {
			return addressid;
		}

		public void setAddressid(String addressid) {
			this.addressid = addressid;
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

	}

	public static class logisticEty {
		public String msg;
		public String time;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	public static class OrderStoEty {
		public String storeid;
		public String storename;
		public String telephone;

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

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

	}

	public static class OrderStatusEty {
		String current;
		List<OrderLog> log;

		public String getCurrent() {
			return current;
		}

		public void setCurrent(String current) {
			this.current = current;
		}

		public List<OrderLog> getLog() {
			return log;
		}

		public void setLog(List<OrderLog> log) {
			this.log = log;
		}
	}

	/***
	 * 物流
	 * 
	 * @author LSD
	 * 
	 */
	public static class OrderLog {
		String title;
		String time;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	/***
	 * 订单中的商品
	 * 
	 * @author LSD
	 * 
	 */
	public static class OrderGoodsEty {
		String goodsid;
		String tradeid;
		String unique;
		String image;
		String name;
		float price;
		int num;
		String spec;
		OrderServiceEty service;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getTradeid() {
			return tradeid;
		}

		public String getUnique() {
			return unique;
		}

		public void setUnique(String unique) {
			this.unique = unique;
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

		public OrderServiceEty getService() {
			return service;
		}

		public void setService(OrderServiceEty service) {
			this.service = service;
		}

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

	}

	public class Settle {
		float totalprice;
		float voucher;

		public float getTotalprice() {
			return totalprice;
		}

		public void setTotalprice(float totalprice) {
			this.totalprice = totalprice;
		}

		public float getVoucher() {
			return voucher;
		}

		public void setVoucher(float voucher) {
			this.voucher = voucher;
		}
	}

	/***
	 * 商品状态
	 * 
	 * @author LSD
	 * 
	 */
	public static class OrderServiceEty {
		String status;
		String type;
		List<OrderOpEty> oplist;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<OrderOpEty> getOplist() {
			return oplist;
		}

		public void setOplist(List<OrderOpEty> oplist) {
			this.oplist = oplist;
		}

	}
}
