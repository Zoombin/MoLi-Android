package com.imooly.android.entity;

import java.io.Serializable;

/***
 * 商品详情
 * 
 * 请求参数
 * 
 * @author LSD
 * 
 */
public class ReqGoodsInfo implements Serializable{
	public String goodsid;
	public String spec;
	public int num;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
