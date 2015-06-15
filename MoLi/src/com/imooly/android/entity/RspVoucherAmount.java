package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 可兑换的代金券金额
 * 
 * @author
 * 
 */
public class RspVoucherAmount extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		public float voucher;

		public float getVoucher() {
			return voucher;
		}

		public void setVoucher(int voucher) {
			this.voucher = voucher;
		}

	}

}
