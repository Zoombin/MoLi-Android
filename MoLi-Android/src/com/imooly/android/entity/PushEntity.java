package com.imooly.android.entity;

/***
 * 推送
 * 
 * @author lsd
 * 
 */
public class PushEntity {
	public String activity;// app/page/link,
	public String pagecode;
	public ParamsEty param;
	public String url;

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getPagecode() {
		return pagecode;
	}

	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}

	public ParamsEty getParam() {
		return param;
	}

	public void setParam(ParamsEty param) {
		this.param = param;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static class ParamsEty {
		public String goodsid;
		public String fstoreid;
		public String estoreid;
		public String bclassifyid;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getFstoreid() {
			return fstoreid;
		}

		public void setFstoreid(String fstoreid) {
			this.fstoreid = fstoreid;
		}

		public String getEstoreid() {
			return estoreid;
		}

		public void setEstoreid(String estoreid) {
			this.estoreid = estoreid;
		}

		public String getBclassifyid() {
			return bclassifyid;
		}

		public void setBclassifyid(String bclassifyid) {
			this.bclassifyid = bclassifyid;
		}
	}
}
