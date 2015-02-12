package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspGoodsSearch.GoodsEty;

/***
 * 旗舰店商品列表
 * 
 * @author LSD
 *
 */
public class RspStoreGoodsList extends ServiceResult{
	@SerializedName("data")
	public Data data;
	
	public static class Data {
		public int totalpage;
		@SerializedName("goodslist")
		public List<GoodsEty> goodslist;

		public List<GoodsEty> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<GoodsEty> goodslist) {
			this.goodslist = goodslist;
		}

		public int getTotalpage() {
			return totalpage;
		}

		public void setTotalpage(int totalpage) {
			this.totalpage = totalpage;
		}
		
		
	}
}
