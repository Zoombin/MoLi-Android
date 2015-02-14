package com.imooly.android.enums;

public enum RequestType {
	/**
	 * appregist
	 */
	App_Register("apps/appregist"),

	/**
	 * getticket
	 */
	App_GetTicket("apps/getticket"),

	/**
	 * newversion
	 */
	App_GetAppNewVersion("apps/newversion"),

	/**
	 * appcover
	 */
	App_GetAppCover("apps/appcover"),

	/**
	 * indexads
	 */
	Advertise_Indexads("advertise/indexads"),
	
	/**
	 * shopads
	 */
	Advertise_Shopads("advertise/shopads"),

	/**
	 * sendvcode
	 */
	User_GetVerCode("user/sendvcode"),

	/**
	 * ckcode
	 */
	User_CheckVerCode("user/ckvcode"),

	/**
	 * 分享内容获取
	 */
	share_SInfo("share/sinfo"),
	
	/**
	 * registterms
	 */
	User_Registterms("user/registterms"),

	/**
	 * registphone
	 */
	User_RegistPhone("user/registphone"),

	/**
	 * registcomplete
	 */
	User_RegistSetPass("user/registcomplete"),

	/**
	 * login
	 */
	User_Login("user/login"),

	/**
	 * forgotpwdckphone
	 */
	User_ForgotPassCkPhone("user/forgotpwdckphone"),

	/**
	 * forgotpwdnew
	 */
	User_ForgotPassSetNew("user/forgotpwdnew"),

	/**
	 * changepw
	 */
	User_ChangePass("user/changepw"),

	/**
	 * traderpw
	 */
	User_TraderPass("user/traderpw"),

	/**
	 * usergrouplist
	 */
	User_UserGroupList("user/usergrouplist"),

	/**
	 * setusergroup
	 */
	User_SetUserGroup("user/setusergroup"),

	/**
	 * userinfo
	 */
	User_UserInfo("user/userinfo"),

	/**
	 * updateuserinfo
	 */
	User_UpdateUserInfo("user/updateuserinfo"),

	/**
	 * electroncard
	 */
	User_MemberCard("user/onlinevipcard"),

	/**
	 * getfeeinfo
	 */
	User_MemberFeeinfo("user/getfeeinfo"),

	/**
	 * addresslist
	 */
	User_Addresslist("user/addresslist"),

	/**
	 * addressinfo
	 */
	User_Addressinfo("user/addressinfo"),

	/**
	 * addaddress
	 */
	User_Addaddress("user/addaddress"),

	/**
	 * updateaddress
	 */
	User_UpdateAddress("user/updateaddress"),

	/**
	 * deleteaddress
	 */
	User_DeleteAddress("user/deleteaddress"),

	/**
	 * storecitylist
	 */
	Business_StoreCityList("business/storecitylist"),

	/**
	 * catelement
	 */
	Business_Catelement("business/catelement"),

	/**
	 * storehot
	 */
	Business_StoreHot("business/storehot"),

	/**
	 * storerand
	 */
	Business_StoreRand("business/storerand"),

	/**
	 * storenear
	 */
	Business_StoreNear("business/storenear"),

	/**
	 * hotkeywords
	 */
	Business_Hotkeywords("business/hotkeywords"),

	/**
	 * businessclassifylist
	 */
	Business_BusinessClassifyList("business/businessclassifylist"),

	/**
	 * businesscirclelist
	 */
	Business_BusinessCirclelist("business/circlelist"),

	/**
	 * businesssearch
	 */
	Business_Businesssearch("business/businesssearch"),

	/**
	 * discountstore
	 */
	Business_Discountstore("business/discountstore"),

	/**
	 * commentlist
	 */
	Business_Commentlist("business/commentlist"),

	/**
	 * comment
	 */
	Business_Comment("business/comment"),

	/**
	 * storeprofile
	 */
	Business_StoreproFile("business/storeprofile"),

	/**
	 * goodsclassifylist
	 */
	Goods_GoodsClassifyList("goods/goodsclassifylist"),

	/**
	 * hotkeywords
	 */
	Goods_HotKeyWords("goods/hotkeywords"),

	/**
	 * storegoodslist
	 */
	Goods_StoreGoodsList("goods/storegoodslist"),

	/**
	 * search
	 */
	Goods_Search("goods/search"),

	/**
	 * goodsarea
	 */
	Goods_Goodsarea("goods/goodsarea"),

	/**
	 * profile
	 */
	Goods_Profile("goods/profile"),

	/**
	 * collectgoods
	 */
	Goods_CollectGoods("goods/collectgoods"),

	/**
	 * goodsspec
	 */
	Goods_GoodsSpec("goods/goodsspec"),

	/**
	 * goodsprice
	 */
	Goods_GoodsPrice("goods/goodsprice"),

	/**
	 * goodscontent
	 */
	Goods_GoodsContent("goods/goodscontent"),

	/**
	 * commentlist
	 */
	Goods_CommentList("goods/commentlist"),

	/**
	 * goodspostage
	 */
	Goods_GoodsPostage("goods/goodspostage"),

	/**
	 * make
	 */
	Order_Make("order/make"),

	/**
	 * save
	 */
	Order_Save("order/save"),

	/**
	 * take
	 */
	Order_Take("order/take"),

	/**
	 * cancel
	 */
	Order_Cancel("order/cancel"),

	/**
	 * delete
	 */
	Order_Delete("order/delete"),

	/**
	 * notice
	 */
	Order_Notice("order/notice"),

	/**
	 * service
	 */
	Order_Service("order/service"),

	/**
	 * serviceadd
	 */
	Order_Serviceadd("order/serviceadd"),

	/**
	 * servicelogistic
	 */
	Order_Servicelogistic("order/servicelogistic"),

	/**
	 * servicebusiness
	 */
	Order_Servicebusiness("order/servicebusiness"),

	/**
	 * logistic
	 */
	Order_Logistic("order/logistic"),

	/**
	 * profile
	 */
	Order_Profile("order/profile"),

	/**
	 * takevoucher
	 */
	Order_Takevoucher("order/takevoucher"),

	/**
	 * takedelay
	 */
	Order_Takedelay("order/takedelay"),

	/**
	 * servicemanual
	 */
	Order_Servicemanual("order/servicemanual"),

	/**
	 * servicecancel
	 */
	Order_Servicecancel("order/servicecancel"),
	

	/**
	 * sgoodslist
	 */
	Order_Sgoodslist("order/sgoodslist"),
	
	/**
	 * sgoodsprofile
	 */
	Order_Sgoodsprofile("order/sgoodsprofile"),
	
	/**
	 * uploadimg
	 */
	Org_Uploadimg("org/uploadimg"),

	/**
	 * pay
	 */
	Order_Pay("order/pay"),

	/**
	 * orderlist
	 */
	Order_Orderlist("order/orderlist"),

	/**
	 * comment
	 */
	Order_Comment("order/comment"),
	
	/**
	 * sendcomment
	 */
	Order_Sendcomment("order/sendcomment"),

	/**
	 * myvoucher
	 */
	Wallet_MyVoucher("wallet/myvoucher"),

	/**
	 * myvoucher
	 */
	Message_Number("message/cknewmsg"),

	/**
	 * add 加入购物车
	 */
	Cart_Add("cart/add"),

	/**
	 * 删除购物车商品
	 */
	Cart_Delete("cart/delete"),
	
	/**
	 * 修改购物车商品数量
	 */
	Cart_Update("cart/update"),

	/**
	 * cart_sync 购物车同步
	 */
	Cart_Sync("cart/sync"),

	/**
	 * newmsg
	 */
	Message_Newmsg("message/newmsg"),

	/**
	 * deletemsg
	 */
	Message_Deletemsg("message/deletemsg"),
	
	/**
	 * 已阅读某条消息
	 */
	Message_ReadMsg("message/readmsg"),

	/**
	 * msginfo
	 */
	Message_Msginfo("message/msginfo"),

	/**
	 * 个人信息
	 */
	User_SetInfo("user/setinfo"),

	/**
	 * 收藏数量
	 */
	Collection_Numbers("user/favinfo"),

	/**
	 * 获取省数据列表
	 */
	Address_Province("public/province"),

	/**
	 * 获取某个省的市、区列表数据
	 */
	Address_City_Town("public/city"),

	/**
	 * 可领取的代金券列表
	 */
	Wallet_NewVoucher("wallet/newvoucher"),

	/**
	 * 领取代金券
	 */
	Wallet_TakeVoucher("wallet/takevoucher"),

	/**
	 * 订单可领取代金券金额
	 */
	Wallet_VoucherAmount("wallet/voucheramount"),

	Wallet_VoucherFlow("wallet/voucherflow"),

	/**
	 * addfavgoods
	 */
	User_Addfavgoods("user/addfavgoods"),

	/**
	 * addfavstore
	 */
	User_Addfavstore("user/addfavstore"),
	
	/**
	 * 获取我收藏的商品
	 */
	User_FavGoodsList("user/favgoodslist"),

	/**
	 * 删除我收藏的某个商品
	 */
	User_DelFavGoods("user/delfavgoods"),

	/**
	 * 获取我收藏的旗舰店
	 */
	User_FavStoreList("user/favstorelist"),

	/**
	 * 删除我收藏的某个旗舰店
	 */
	User_DelFavStore("user/delfavstore"),

	/**
	 * 获取我收藏的实体店
	 */
	User_FavBusinessStore("user/favbusinesslist"),

	/**
	 * addfavbusiness
	 */
	User_Addfavbusiness("user/addfavbusiness"),
	
	/**
	 * 删除我收藏的某个实体店
	 */
	User_DelFavBusiness("user/delfavbusiness"),

	/**
	 * 订单信息概况-不同状态的订单数目等
	 */
	Order_MyOrder("order/myorder"),

	/**
	 * 用户退出
	 */
	User_Logout("user/logout"),

	/**
	 * 修改密码
	 */
	User_SetUserPwd("user/setuserpwd"),

	/**
	 * 检查是否已经设置过交易密码
	 */
	Wallet_CkUserWalletPwd("wallet/ckuserwalletpwd"),
	
	/**
	 * 设置或修改交易密码
	 */
	Wallet_SetUserWalletPwd("wallet/setuserwalletpwd"),
	
	/**
	 * 忘记交易密码-验证手机和注册码
	 */
	Wallet_ForgotUserWalletPwdPhone("wallet/forgotuserwalletpwdckphone"),
	
	/**
	 * 忘记交易密码-设置新密码
	 */
	Wallet_ForgotUserWalletPwdNew("wallet/forgotuserwalletpwdnew"),
	
	/**
	 * 设置某个收货地址为默认地址
	 */
	User_SetDefAddress("user/setdefaddress"),
	

	/**
	 * 客服电话
	 */
	Public_ServiceTel("public/servicetel"),
	
	/**
	 * 支付
	 */
	Pay_PayInfo("pay/payinfo"),
	
	/**
	 * 会员充值、续费-获取付款信息
	 */
	User_VPay("user/vpay"),
	
	Test_Const("9999");

	private String value;

	RequestType(String value) {
		this.value = value;
	}

	public String getConstValue() {
		return this.value;
	}

	public boolean compareValue(String compare) {
		if (compare == null) {
			return false;
		}
		return this.getConstValue().equals(compare);
	}
}
