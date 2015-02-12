package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品详情
 * 
 * @author LSD
 * 
 */
public class RspProfile extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String goodsid;
		public List<String> goodsimages;
		public String goodsname;
		public PriceEty goodsprice;
		public int highopinion;
		public String goodsfrom;
		public String goodsto;
		public String postageway;
		public float postage;

		public String goodscaption;
		public int salesvolume;
		public String choose;
		public String voucherimage;
		public int isfavorite;
		public int isstock;
		public List<String> voucher;

		public int totalcomment;

		List<AreaEty> arealist;
		List<ServiceEty> service;
		List<IntroduceEty> introduce;
		StoreEty store;
		List<GoodsrandEty> goodsrand;

		public String getVoucherimage() {
			return voucherimage;
		}

		public void setVoucherimage(String voucherimage) {
			this.voucherimage = voucherimage;
		}

		public int getHighopinion() {
			return highopinion;
		}

		public void setHighopinion(int highopinion) {
			this.highopinion = highopinion;
		}

		public List<String> getVoucher() {
			return voucher;
		}

		public void setVoucher(List<String> voucher) {
			this.voucher = voucher;
		}

		public void setSalesvolume(int salesvolume) {
			this.salesvolume = salesvolume;
		}

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public List<String> getGoodsimages() {
			return goodsimages;
		}

		public void setGoodsimages(List<String> goodsimages) {
			this.goodsimages = goodsimages;
		}

		public String getGoodsname() {
			return goodsname;
		}

		public void setGoodsname(String goodsname) {
			this.goodsname = goodsname;
		}

		public PriceEty getGoodsprice() {
			return goodsprice;
		}

		public void setGoodsprice(PriceEty goodsprice) {
			this.goodsprice = goodsprice;
		}

		public String getGoodsfrom() {
			return goodsfrom;
		}

		public void setGoodsfrom(String goodsfrom) {
			this.goodsfrom = goodsfrom;
		}

		public String getGoodsto() {
			return goodsto;
		}

		public void setGoodsto(String goodsto) {
			this.goodsto = goodsto;
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

		public String getGoodscaption() {
			return goodscaption;
		}

		public void setGoodscaption(String goodscaption) {
			this.goodscaption = goodscaption;
		}

		public int getSalesvolume() {
			return salesvolume;
		}

		public String getChoose() {
			return choose;
		}

		public void setChoose(String choose) {
			this.choose = choose;
		}

		public int getTotalcomment() {
			return totalcomment;
		}

		public void setTotalcomment(int totalcomment) {
			this.totalcomment = totalcomment;
		}

		public List<AreaEty> getArealist() {
			return arealist;
		}

		public void setArealist(List<AreaEty> arealist) {
			this.arealist = arealist;
		}

		public List<ServiceEty> getService() {
			return service;
		}

		public void setService(List<ServiceEty> service) {
			this.service = service;
		}

		public List<IntroduceEty> getIntroduce() {
			return introduce;
		}

		public void setIntroduce(List<IntroduceEty> introduce) {
			this.introduce = introduce;
		}

		public int getIsfavorite() {
			return isfavorite;
		}

		public void setIsfavorite(int isfavorite) {
			this.isfavorite = isfavorite;
		}

		public int getIsstock() {
			return isstock;
		}

		public void setIsstock(int isstock) {
			this.isstock = isstock;
		}

		public StoreEty getStore() {
			return store;
		}

		public void setStore(StoreEty store) {
			this.store = store;
		}

		public List<GoodsrandEty> getGoodsrand() {
			return goodsrand;
		}

		public void setGoodsrand(List<GoodsrandEty> goodsrand) {
			this.goodsrand = goodsrand;
		}

	}

	/** 查询邮费 可选区域 */
	public static class PriceEty {
		public float market;
		public float viprmb;

		public float getMarket() {
			return market;
		}

		public void setMarket(float market) {
			this.market = market;
		}

		public float getViprmb() {
			return viprmb;
		}

		public void setViprmb(float viprmb) {
			this.viprmb = viprmb;
		}

	}

	/** 查询邮费 可选区域 */
	public static class AreaEty {
		public String id;
		public String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	/** 服务保障 */
	public static class ServiceEty {
		public String icon;
		public String caption;

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

	}

	/** 规格参数 */
	public static class IntroduceEty {
		public String title;
		public List<ListEty> list;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<ListEty> getList() {
			return list;
		}

		public void setList(List<ListEty> list) {
			this.list = list;
		}

		public class ListEty {
			public String name;
			public String value;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}

		}

	}

	/** 旗舰店 */
	public static class StoreEty {
		public String businessid;
		public String businessname;
		public String businessicon;

		public String getBusinessid() {
			return businessid;
		}

		public void setBusinessid(String businessid) {
			this.businessid = businessid;
		}

		public String getBusinessname() {
			return businessname;
		}

		public void setBusinessname(String businessname) {
			this.businessname = businessname;
		}

		public String getBusinessicon() {
			return businessicon;
		}

		public void setBusinessicon(String businessicon) {
			this.businessicon = businessicon;
		}

	}

	/** 猜你喜欢 */
	public static class GoodsrandEty {
		public String goodsid;
		public String goodsname;
		public String goodsimage;
		public float price;
		public int salesvolume;
		public int hignopinion;
		public int isvoucher;

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

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public int getSalesvolume() {
			return salesvolume;
		}

		public void setSalesvolume(int salesvolume) {
			this.salesvolume = salesvolume;
		}

		public int getHignopinion() {
			return hignopinion;
		}

		public void setHignopinion(int hignopinion) {
			this.hignopinion = hignopinion;
		}

		public int getIsvoucher() {
			return isvoucher;
		}

		public void setIsvoucher(int isvoucher) {
			this.isvoucher = isvoucher;
		}

	}

}
