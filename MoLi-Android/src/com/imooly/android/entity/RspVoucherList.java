package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 可领取的代金券列表
 * 
 * @author 
 * 
 */
public class RspVoucherList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private List<Voucher> voucherlist;

		public List<Voucher> getVoucherlist() {
			return voucherlist;
		}

		public void setVoucherlist(List<Voucher> voucherlist) {
			this.voucherlist = voucherlist;
		}

	}

	public class Voucher {
		private String orderno;
		private String storename;
		private String goodsname;
		private int voucher;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getStorename() {
			return storename;
		}

		public void setStorename(String storename) {
			this.storename = storename;
		}

		public String getGoodsname() {
			return goodsname;
		}

		public void setGoodsname(String goodsname) {
			this.goodsname = goodsname;
		}

		public int getVoucher() {
			return voucher;
		}

		public void setVoucher(int voucher) {
			this.voucher = voucher;
		}
	}
}
