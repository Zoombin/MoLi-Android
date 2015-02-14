package com.imooly.android.entity;

import java.util.List;

/***
 * 确认订单信息-立即购买/购物车选择商品结算
 * 
 * 请求参数
 * 
 * @author LSD
 * 
 */
public class ReqGoodsEntity {
	public String storeid;
	public String remark;
	public List<ReqGoodsInfo> goodslist;

	public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ReqGoodsInfo> getGoodslist() {
		return goodslist;
	}

	public void setGoodslist(List<ReqGoodsInfo> goodslist) {
		this.goodslist = goodslist;
	}
}
