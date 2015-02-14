package com.imooly.android.Net;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CProgressDialog;

public class NetUtils {
	public interface NetCallBack<T> {
		public void success(T rspData);
		public void failed(String msg);
	}

	public static final String BASE_URL = "http://appdev.imooly.com:8088/moolyapp/api/v1.0/";

	private static RequestQueue mQueue = Volley.newRequestQueue(MoLiApplication.getContext());

	// GET
	public static void getValue(Context context, String method, final String msg, final NetCallBack<ServiceResult> callBack, final Class<?> rspCls) {

		if (!Utils.isNetworkConnected(context)) {
			if (callBack != null)
				callBack.failed("网络异常!");
			return;
		}

		final CProgressDialog progressDialog = new CProgressDialog(context, R.style.Dialog);

		if (msg != null) {
			progressDialog.setMessage(msg);
			progressDialog.setCancelable(true);
			try {
				if (!progressDialog.isShowing()) {
					progressDialog.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		String url = BASE_URL + method;
		Log.i("NetUtils", url);
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				progressDialog.dismiss();
				//Log.i("NetUtils", "get rsp : " + jsonObject.toString());
				int errorCode = getErrorCode(jsonObject.toString());
				if (0 != errorCode) {
					if (callBack != null)
						callBack.failed(getErrorMsg(jsonObject.toString()));
					return;
				}
				ServiceResult rsp = null;
				try {
					Gson gson = new Gson();
					rsp = (ServiceResult) gson.fromJson(jsonObject.toString(), rspCls);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("NetUtils", "get RspMsgError !");
					e.printStackTrace();
				}
				if (callBack != null)
					if (rsp != null) {
						callBack.success(rsp);
					} else {
						callBack.failed("数据解析出错!");
					}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.dismiss();
				if (callBack != null)
					callBack.failed(error.getMessage());
			}
		});

		request.setRetryPolicy(new DefaultRetryPolicy(4 * 1000, 1, 1.0f));
		mQueue.add(request);
		mQueue.start();
	}

	// POST
	public static void post(Context context, String method, final Map<String, String> params, final String msg, final NetCallBack<ServiceResult> callBack, final Class<?> rspCls) {
		if (!Utils.isNetworkConnected(context)) {
			if (callBack != null)
				callBack.failed("网络异常!");
			return;
		}
		final CProgressDialog progressDialog = new CProgressDialog(context, R.style.Dialog);
		if (msg != null) {
			progressDialog.setMessage(msg);
			progressDialog.setCancelable(true);
			try {
				if (!progressDialog.isShowing()) {
					progressDialog.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		String url = BASE_URL + method;
		//Log.i("NetUtils", "post req : " + url  +"  pama : "+ Api.getReqPama(params));
		StringRequest request = new StringRequest(Request.Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String jsonObject) {
				progressDialog.dismiss();
				//Log.i("NetUtils", "post rsp : " + new String(jsonObject.getBytes()));
				int errorCode = getErrorCode(jsonObject);
				if (0 != errorCode) {
					if (callBack != null)
						callBack.failed(getErrorMsg(jsonObject));
					return;
				}
				ServiceResult rsp = null;
				try {
					Gson gson = new Gson();
					rsp = (ServiceResult) gson.fromJson(jsonObject.toString(), rspCls);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("NetUtils", "post RspMsgError !");
					e.printStackTrace();
				}
				if (callBack != null)
					if (rsp != null) {
						callBack.success(rsp);
					} else {
						callBack.failed("数据解析出错!");
					}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (callBack != null)
					callBack.failed(error.getMessage());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(4 * 1000, 1, 1.0f));
		mQueue.add(request);
		mQueue.start();
	}

	public static int getErrorCode(String str) {
		try {
			JSONObject object = new JSONObject(str);
			return object.optInt("error");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String getErrorMsg(String str) {
		try {
			JSONObject object = new JSONObject(str);
			return object.optString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getBaseUrl() {
		return BASE_URL;
	}
}
