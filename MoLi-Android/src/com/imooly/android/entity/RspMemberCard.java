package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 电子会员卡
 * 
 * @author
 * 
 */
public class RspMemberCard extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private int expiresec;

		private String showmsg;

		private int vipflag;

		private Barcode barcode;
		private Qrcode qrcode;

		public int getExpiresec() {
			return expiresec;
		}

		public void setExpiresec(int expiresec) {
			this.expiresec = expiresec;
		}

		public String getShowmsg() {
			return showmsg;
		}

		public void setShowmsg(String showmsg) {
			this.showmsg = showmsg;
		}

		public int getVipflag() {
			return vipflag;
		}

		public void setVipflag(int vipflag) {
			this.vipflag = vipflag;
		}
		
		
		public Barcode getBarcode() {
			return barcode;
		}

		public void setBarcode(Barcode barcode) {
			this.barcode = barcode;
		}

		public Qrcode getQrcode() {
			return qrcode;
		}

		public void setQrcode(Qrcode qrcode) {
			this.qrcode = qrcode;
		}
		

		public class Barcode {
			private int show;
			private String image;

			public int getShow() {
				return show;
			}

			public void setShow(int show) {
				this.show = show;
			}

			public String getImage() {
				return image;
			}

			public void setImage(String image) {
				this.image = image;
			}
		}

		public class Qrcode {

			private int show;
			private String image;

			public int getShow() {
				return show;
			}

			public void setShow(int show) {
				this.show = show;
			}

			public String getImage() {
				return image;
			}

			public void setImage(String image) {
				this.image = image;
			}
		}



	}
}
