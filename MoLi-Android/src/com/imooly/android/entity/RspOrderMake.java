package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspAddressList.Address;

/***
 * 确认订单信息-立即购买/购物车选择商品结算
 * 
 * 
 * 
 * @author LSD
 * 
 */
public class RspOrderMake extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {
		public int success;
		public int vipmember;
		public List<GoodserrorEty> goodserror;
		public Address address;
		public List<OrderStoreProEty> goodslist;
		public String voucherimage;
		public int voucher;
		public int totalvoucher;
		public float totalprice;

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public int getVipmember() {
			return vipmember;
		}

		public void setVipmember(int vipmember) {
			this.vipmember = vipmember;
		}

		public List<GoodserrorEty> getGoodserror() {
			return goodserror;
		}

		public void setGoodserror(List<GoodserrorEty> goodserror) {
			this.goodserror = goodserror;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public List<OrderStoreProEty> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<OrderStoreProEty> goodslist) {
			this.goodslist = goodslist;
		}

		public String getVoucherimage() {
			return voucherimage;
		}

		public void setVoucherimage(String voucherimage) {
			this.voucherimage = voucherimage;
		}

		public int getVoucher() {
			return voucher;
		}

		public void setVoucher(int voucher) {
			this.voucher = voucher;
		}

		public int getTotalvoucher() {
			return totalvoucher;
		}

		public void setTotalvoucher(int totalvoucher) {
			this.totalvoucher = totalvoucher;
		}

		public float getTotalprice() {
			return totalprice;
		}

		public void setTotalprice(float totalprice) {
			this.totalprice = totalprice;
		}

	}

	/**
	 * 地址
	 * 
	 * @author LSD
	 * 
	 */
	public class GoodserrorEty {
		String goodsid;
		String spec;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

	}

	public class OrderStoreProEty {
		public String storeid;
		public String storename;
		public int num;
		public float totalprice;
		public String postageway;
		public float postage;
		public List<OrderGoodsEty> goods;

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

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public float getTotalprice() {
			return totalprice;
		}

		public void setTotalprice(float totalprice) {
			this.totalprice = totalprice;
		}

		public String getPostageway() {
			return postageway;
		}

		public void setPostageway(String postageway) {
			this.postageway = postageway;
		}

		public float getPostage() {
			return postage;
		}

		public void setPostage(float postage) {
			this.postage = postage;
		}

		public List<OrderGoodsEty> getGoods() {
			return goods;
		}

		public void setGoods(List<OrderGoodsEty> goods) {
			this.goods = goods;
		}

	}

	public class OrderGoodsEty {
		public String goodsid;
		public String goodsname;
		public String goodsimage;
		public String spec;
		public String specshow;
		public float price;
		public int num;

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

		public String getGoodsimage() {
			return goodsimage;
		}

		public void setGoodsimage(String goodsimage) {
			this.goodsimage = goodsimage;
		}

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

		public String getSpecshow() {
			return specshow;
		}

		public void setSpecshow(String specshow) {
			this.specshow = specshow;
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

}
