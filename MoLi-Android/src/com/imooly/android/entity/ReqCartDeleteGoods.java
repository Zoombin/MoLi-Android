package com.imooly.android.entity;

import java.io.Serializable;

/***
 * 购物车删除商品
 * 
 * 请求参数
 * 
 * @author daiye
 * 
 */
public class ReqCartDeleteGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	public String goodsid;
	public String spec;

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
