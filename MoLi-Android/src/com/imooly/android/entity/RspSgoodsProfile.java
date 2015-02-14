package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspAddressList.Address;

/***
 * 售后服务-商品售后详情
 * 
 * @author LSD
 * 
 */
public class RspSgoodsProfile extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		SgoodsGodsEty goods;
		SgoodsServiceEty service;
		SgoodsAddressEty address;

		public SgoodsGodsEty getGoods() {
			return goods;
		}

		public void setGoods(SgoodsGodsEty goods) {
			this.goods = goods;
		}

		public SgoodsServiceEty getService() {
			return service;
		}

		public void setService(SgoodsServiceEty service) {
			this.service = service;
		}

		public SgoodsAddressEty getAddress() {
			return address;
		}

		public void setAddress(SgoodsAddressEty address) {
			this.address = address;
		}

	}

	public static class SgoodsGodsEty {
		String goodsid;
		String unique;
		String name;
		String image;
		float price;
		int num;
		int tnum;

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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
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

		public int getTnum() {
			return tnum;
		}

		public void setTnum(int tnum) {
			this.tnum = tnum;
		}

	}

	public static class SgoodsServiceEty {
		String type;
		String typename;
		String statusname;
		String createtime;
		String refundamount;
		String uremark;
		String bremark;
		List<String> images;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTypename() {
			return typename;
		}

		public void setTypename(String typename) {
			this.typename = typename;
		}

		public String getStatusname() {
			return statusname;
		}

		public void setStatusname(String statusname) {
			this.statusname = statusname;
		}

		public String getCreatetime() {
			return createtime;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public String getRefundamount() {
			return refundamount;
		}

		public void setRefundamount(String refundamount) {
			this.refundamount = refundamount;
		}

		public String getUremark() {
			return uremark;
		}

		public void setUremark(String uremark) {
			this.uremark = uremark;
		}

		public String getBremark() {
			return bremark;
		}

		public void setBremark(String bremark) {
			this.bremark = bremark;
		}

		public List<String> getImages() {
			return images;
		}

		public void setImages(List<String> images) {
			this.images = images;
		}

	}

	public static class SgoodsAddressEty {
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
