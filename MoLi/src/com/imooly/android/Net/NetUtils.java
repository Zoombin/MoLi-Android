package com.imooly.android.Net;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CProgressDialog;

public class NetUtils {
	static boolean isCopy = false;
	static StringRequest retry;

	public interface NetCallBack<T> {
		public void success(T rspData);
		public void failed(String msg);
	}

	//public static final String BASE_URL = "http://appdev.imooly.com:8088/moolyapp/api/v1.0/";

	//测试地址
	//public static final String BASE_URL = "http://222.92.197.76/MoolyApp/";

	//public static final String BASE_URL = "http://192.168.10.6/MoolyApp/";
	
	// 正式环境
	public static final String BASE_URL = "http://61.155.215.163/";
	

	private static RequestQueue mQueue = Volley.newRequestQueue(MoLiApplication.getContext());

	// GET
	public static void getValue(Context context, String method, final String msg, final NetCallBack<ServiceResult> callBack,
			final Class<?> rspCls) {
		getValue(context, method, msg, false, callBack, rspCls);
	}

	public static void getValue(Context context, String method, final String msg, boolean copy, final NetCallBack<ServiceResult> callBack,
			final Class<?> rspCls) {

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
		//Log.i("NetUtils", url);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				if (progressDialog != null)
					progressDialog.dismiss();
				//Log.i("NetUtils", "get rsp : " + new String(response.getBytes()));
				int errorCode = getErrorCode(response);
				if (0 != errorCode) {
					if (callBack != null)
						callBack.failed(getErrorMsg(response));
					return;
				}
				if(rspCls == null){
					return;
				}
				ServiceResult rsp = null;
				try {
					Gson gson = new Gson();
					rsp = (ServiceResult) gson.fromJson(response, rspCls);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("NetUtils", "数据解析出错 !");
					e.printStackTrace();
				}
				if (callBack != null)
					if (rsp != null) {
						callBack.success(rsp);
					} else {
						// 数据解析出错!
						callBack.failed("");
					}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				if (progressDialog != null)
					progressDialog.dismiss();
				if (error instanceof NetworkError) {
					if (callBack != null)
						callBack.failed("网络异常!");
					return;
				} else if (error instanceof ServerError) {
				} else if (error instanceof AuthFailureError) {
				} else if (error instanceof ParseError) {
				} else if (error instanceof NoConnectionError) {
				} else if (error instanceof TimeoutError) {
					if (callBack != null)
						callBack.failed("网络连接超时!");
					return;
				}
				if (callBack != null)
					callBack.failed(error.getMessage());
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(4 * 1000, 1, 1.0f));
		if (copy) {
			isCopy = copy;
			retry = request;
		}
		mQueue.add(request);
		mQueue.start();
	}

	// POST
	public static void post(Context context, String method, final Map<String, String> params, final String msg,
			final NetCallBack<ServiceResult> callBack, final Class<?> rspCls) {
		post(context, method, params, msg, false, callBack, rspCls);
	}

	public static void post(Context context, String method, final Map<String, String> params, final String msg, boolean copy,
			final NetCallBack<ServiceResult> callBack, final Class<?> rspCls) {
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
		//Log.i("NetUtils", "post req : " + url + "  pama : " + Api.getReqPama(params));
		StringRequest request = new StringRequest(Request.Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String jsonObject) {
				if (progressDialog != null)
					progressDialog.dismiss();
				//Log.i("NetUtils", "post rsp : " + new String(jsonObject.getBytes()));
				int errorCode = getErrorCode(jsonObject);
				if (0 != errorCode) {
					if (callBack != null)
						callBack.failed(getErrorMsg(jsonObject));
					return;
				}
				if(rspCls == null){
					return;
				}
				ServiceResult rsp = null;
				try {
					Gson gson = new Gson();
					rsp = (ServiceResult) gson.fromJson(jsonObject.toString(), rspCls);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("NetUtils", "数据解析出错 !");
					e.printStackTrace();
				}
				if (callBack != null)
					if (rsp != null) {
						callBack.success(rsp);
					} else {
						// 数据解析出错!
						callBack.failed("");
					}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (progressDialog != null)
					progressDialog.dismiss();
				if (error instanceof NetworkError) {
					if (callBack != null)
						callBack.failed("网络异常!");
					return;
				} else if (error instanceof ServerError) {
				} else if (error instanceof AuthFailureError) {
				} else if (error instanceof ParseError) {
				} else if (error instanceof NoConnectionError) {
				} else if (error instanceof TimeoutError) {
					if (callBack != null)
						callBack.failed("网络连接超时!");
					return;
				}
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
		if (copy) {
			isCopy = copy;
			retry = request;
		}
		mQueue.add(request);
		mQueue.start();
	}

	public static void reRequest() {
		if (retry != null && isCopy) {
			mQueue.add(retry);
			mQueue.start();
		}
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
