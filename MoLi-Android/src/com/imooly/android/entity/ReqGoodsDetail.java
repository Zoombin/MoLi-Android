package com.imooly.android.entity;

/***
 * 
 * 商品详情 付款时使用
 * 
 * @author lsd
 * 
 */
public class ReqGoodsDetail {
	public String goodsid;
	public String goodsname;
	public String goodsimage;
	public String spec;
	public String specshow;
	public float price;

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

}
