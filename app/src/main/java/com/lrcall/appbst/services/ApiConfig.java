/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import com.lrcall.utils.AppConfig;
import com.lrcall.utils.PreferenceUtils;

/**
 * Created by libit on 16/4/6.
 */
public class ApiConfig
{
	public static final String API_VERSION = "1";
	private static final String RELEASE_URL = "http://115.29.140.222:8088/lr_bst/user";
	private static final String DEBUG_URL = "http://192.168.168.6:8080/lr_bst/user";
	//	private static final String RELEASE_URL = "http://ht.dyp8.com:8080/sgqq/user";
	//	private static final String DEBUG_URL = "http://ht.dyp8.com:8080/sgqq/user";
	public static final String SUBMIT_BUG = RELEASE_URL + "/ajaxAddClientBugInfo";//BUG日志提交
	public static final String CHECK_UPDATE = RELEASE_URL + "/ajaxGetLastClientInfo";//检查更新
	public static final String UPLOAD_DEBUG_FILE = RELEASE_URL + "/uploadDebugFile";//上传BUG文件
	public static final String SUBMIT_ADVICE = getServerUrl() + "/ajaxAddAdviceInfo";//提交意见反馈
	public static final String USER_LOGIN = getServerUrl() + "/ajaxLogin";//用户登录
	public static final String GET_SMS_CODE = getServerUrl() + "/../ajaxGetSmsCode";//获取手机验证码
	public static final String USER_CHANGE_PWD = getServerUrl() + "/ajaxChangePwd";//用户修改密码
	public static final String USER_RESET_PWD = getServerUrl() + "/ajaxResetPwd";//用户重置密码
	public static final String USER_REGISTER = getServerUrl() + "/ajaxRegister";//用户注册
	public static final String GET_USER_INFO = getServerUrl() + "/ajaxGetUserInfo";//获取用户信息
	public static final String GET_USER_BALANCE_INFO = getServerUrl() + "/ajaxGetUserBalanceInfo";//获取用户余额信息
	public static final String UPDATE_PIC = getServerUrl() + "/uploadPic";//用户上传图片
	public static final String USER_UPDATE_PIC_INFO = getServerUrl() + "/ajaxUpdateUserPicInfo";//用户更新头像信息
	public static final String GET_BACKUP_CONFIG = getServerUrl() + "/ajaxUserToGetBackupConfig";//获取用户配置备份信息
	public static final String UPDATE_BACKUP_CONFIG = getServerUrl() + "/ajaxUserToUpdateBackupConfig";//用户配置备份
	public static final String USER_SHARE = getServerUrl() + "/ajaxShareApp";//用户分享App
	public static final String GET_CLIENT_CONFIG = getServerUrl() + "/ajaxGetClientConfig";//客户端公共配置信息
	//用户升级
	public static final String USER_UPGRADE_PRICE = getServerUrl() + "/ajaxGetUserUpgradePrice";//用户升级费用
	public static final String USER_UPGRADE_BY_SHOP_CARD = getServerUrl() + "/ajaxUserUpgradeByShopCard";//用户商城卡升级
	public static final String USER_UPGRADE_BY_BALANCE = getServerUrl() + "/ajaxUserUpgradeByBalance";//用户通过余额升级
	//号码相关
	public static final String GET_NUMBER_LOCAL = getServerUrl() + "/ajaxGetNumberLocal";//获取电话号码归属地
	public static final String GET_NUMBER_LABEL = getServerUrl() + "/ajaxGetNumberLabel";//获取电话号码标记
	//备份恢复
	public static final String USER_BACKUP_CONTACTS = getServerUrl() + "/ajaxBackupContactList";//用户备份联系人
	public static final String USER_GET_CONTACTS = getServerUrl() + "/ajaxGetContactList";//用户获取联系人备份
	public static final String USER_GET_CONTACTS_COUNT = getServerUrl() + "/ajaxGetContactCount";//用户获取联系人备份数量
	public static final String USER_BACKUP_HISOTRY = getServerUrl() + "/ajaxBackupHistoryList";//用户备份通话记录
	public static final String USER_GET_HISOTRY = getServerUrl() + "/ajaxGetHistoryList";//用户获取通话记录备份
	public static final String USER_GET_HISOTRY_COUNT = getServerUrl() + "/ajaxGetHistoryCount";//用户获取通话记录备份数量
	//商品
	public static final String GET_PRODUCT_SORT_LIST = getServerUrl() + "/ajaxGetProductSortList";//获取商品分类列表
	public static final String GET_PRODUCT_LIST = getServerUrl() + "/ajaxGetProductList";//获取商品列表
	public static final String GET_RECOMMEND_PRODUCT_LIST = getServerUrl() + "/ajaxGetRecommendProductList";//获取推荐商品列表
	public static final String GET_CONCESSION_PRODUCT_LIST = getServerUrl() + "/ajaxGetConcessionProductList";//获取促销商品列表
	public static final String GET_NEW_PRODUCT_LIST = getServerUrl() + "/ajaxGetNewProductList";//获取最新商品列表
	public static final String GET_PRODUCT_INFO = getServerUrl() + "/ajaxGetProductInfo";//获取商品信息
	public static final String GET_PRODUCT_PIC_LIST = getServerUrl() + "/ajaxGetProductPicList";//获取商品图片信息列表
	//消息
	public static final String GET_NEWS_LIST = getServerUrl() + "/ajaxGetNewsList";//获取消息列表
	public static final String GET_NEWS_INFO = getServerUrl() + "/ajaxGetNewsInfo";//获取消息信息
	public static final String GET_BANNER_LIST = getServerUrl() + "/ajaxGetClientBannerList";//获取首页Banner列表
	//商品收藏
	public static final String GET_STAR_LIST = getServerUrl() + "/ajaxGetProductStarList";//获取商品收藏列表
	public static final String GET_STAR_LIST_COUNT = getServerUrl() + "/ajaxGetProductStarListCount";//获取商品收藏列表数量
	public static final String ADD_STAR_INFO = getServerUrl() + "/ajaxAddProductStarInfo";//添加商品收藏列表
	public static final String DELETE_STAR_INFO = getServerUrl() + "/ajaxDeleteProductStarInfo";//删除商品收藏列表
	public static final String GET_STAR_INFO = getServerUrl() + "/ajaxGetProductStarInfo";//获取商品收藏
	//用户地址
	public static final String GET_ADDRESS_LIST = getServerUrl() + "/ajaxGetUserAddressList";//获取用户地址列表
	public static final String ADD_ADDRESS_INFO = getServerUrl() + "/ajaxAddUserAddressInfo";//添加用户地址
	public static final String UPDATE_ADDRESS_INFO = getServerUrl() + "/ajaxUpdateUserAddressInfo";//更新用户地址
	public static final String UPDATE_ADDRESS_STATUS = getServerUrl() + "/ajaxUpdateUserAddressInfoStatus";//更新用户地址状态
	public static final String DELETE_ADDRESS_INFO = getServerUrl() + "/ajaxDeleteUserAddressInfo";//删除用户地址
	public static final String GET_ADDRESS_INFO = getServerUrl() + "/ajaxGetUserAddressInfo";//获取用户地址
	//购物车
	public static final String GET_SHOP_CART_LIST = getServerUrl() + "/ajaxGetShopCartInfoList";//获取用户购物车列表
	public static final String ADD_SHOP_CART_INFO = getServerUrl() + "/ajaxAddShopCartInfo";//添加用户购物车
	public static final String UPDATE_SHOP_CART_INFO = getServerUrl() + "/ajaxUpdateShopCartInfo";//更新用户购物车
	public static final String DELETE_SHOP_CART_INFO = getServerUrl() + "/ajaxDeleteShopCartInfo";//删除用户购物车
	public static final String GET_SHOP_CART_INFO = getServerUrl() + "/ajaxGetShopCartInfo";//获取用户购物车
	//订单
	public static final String ADD_ORDER = getServerUrl() + "/ajaxAddOrder";//下单
	public static final String GET_ORDER_LIST = getServerUrl() + "/ajaxGetOrderInfoList";//获取用户订单列表
	public static final String DELETE_ORDER = getServerUrl() + "/ajaxDeleteOrderInfo";//删除用户订单
	public static final String GET_ORDER_INFO = getServerUrl() + "/ajaxGetOrderInfo";// 获取用户订单
	public static final String ORDER_FINISH = getServerUrl() + "/ajaxOrderConfirmReceive";// 订单完成
	public static final String GET_ORDER_EXPRESS_LIST = getServerUrl() + "/ajaxGetOrderExpressInfoList";//订单快递列表
	//商品评价
	public static final String ADD_PRODUCT_COMMENT = getServerUrl() + "/ajaxAddProductCommentInfo";//添加商品评价
	public static final String GET_PRODUCT_COMMENT_LIST = getServerUrl() + "/ajaxGetProductCommentInfoList";//获取商品评价列表
	public static final String GET_PRODUCT_COMMENT_COUNT = getServerUrl() + "/ajaxGetProductCommentCount";//获取商品评价数量
	public static final String DELETE_PRODUCT_COMMENT = getServerUrl() + "/ajaxDeleteOrderInfo";//删除商品评价
	public static final String GET_PRODUCT_COMMENT_INFO = getServerUrl() + "/ajaxGetProductCommentInfo";// 获取商品评价
	public static final String GET_PRODUCT_COMMENT_INFO_COUNT = getServerUrl() + "/ajaxGetProductCommentInfoCount";// 获取商品评价数量，用于判断订单商品是否已评论
	//支付方式
	public static final String GET_PAY_LIST = getServerUrl() + "/ajaxGetPayList";//获取支付方式列表
	public static final String GET_PAY_INFO = getServerUrl() + "/ajaxGetPayInfo";//获取支付方式信息
	public static final String PAY_BY_BALANCE = getServerUrl() + "/ajaxOrderPayByBalance";//余额方式支付订单
	//支付宝
	public static final String GET_ALIPAY_CONIFG = getServerUrl() + "/ajaxGetAlipayConfigInfo";//获取支付宝配置信息
	//微信
	public static final String WX_PRE_PAY = getServerUrl() + "/../wechart/ajaxGetWxPrePay";//获取微信预支付信息
	public static final String GET_WX_PAY_INFO = getServerUrl() + "/../wechart/ajaxGetWxPayInfo";//获取微信预支付信息
	//流量充值商品
	public static final String GET_DATA_TRAFFIC_LIST = getServerUrl() + "/ajaxGetDataTrafficInfoList";//获取流量充值商品列表
	public static final String GET_NEW_DATA_TRAFFIC_LIST = getServerUrl() + "/ajaxGetNewDataTrafficInfoList";//获取最新流量充值商品列表
	public static final String GET_DATA_TRAFFIC_SORT_LIST = getServerUrl() + "/ajaxGetDataTrafficSortInfoList";//获取流量充值商品分类列表
	public static final String GET_DATA_TRAFFIC_INFO = getServerUrl() + "/ajaxGetDataTrafficInfo";//获取流量充值商品信息
	public static final String ADD_DATA_TRAFFIC_ORDER = getServerUrl() + "/ajaxAddDataTrafficOrder";//下单
	public static final String GET_DATA_TRAFFIC_ORDER_LIST = getServerUrl() + "/ajaxGetDataTrafficOrderInfoList";//获取用户订单列表
	public static final String DELETE_DATA_TRAFFIC_ORDER = getServerUrl() + "/ajaxDeleteDataTrafficOrderInfo";//删除用户订单
	public static final String GET_DATA_TRAFFIC_ORDER_INFO = getServerUrl() + "/ajaxGetDataTrafficOrderInfo";// 获取用户订单
	public static final String PAY_DATA_TRAFFIC_BY_BALANCE = getServerUrl() + "/ajaxDataTrafficOrderPayByBalance";//余额方式支付订单
	public static final String DATA_TRAFFIC_RECHARGE_BY_CARD = getServerUrl() + "/ajaxAddDataTrafficOrderByCard";//流量卡充值
	public static final String GET_PACKAGE_DATA_TRAFFIC = getServerUrl() + "/ajaxUserGetPackageDataTraffic";//套餐用户每月领取流量卡
	//回拨接口
	public static final String CALLBACK_REGISTER = getServerUrl() + "/ajaxCallbackRegister";//回拨注册
	public static final String CALLBACK_GET_BALANCE_INFO = getServerUrl() + "/ajaxCallbackGetBalance";//回拨查余额
	public static final String CALLBACK_RECHARGE = getServerUrl() + "/ajaxCallbackRecharge";//回拨充值
	public static final String CALLBACK_MAKE_CALL = getServerUrl() + "/ajaxCallbackMakecall";//回拨呼叫
	public static final String CALLBACK_GET_CALL_LOG_LIST = getServerUrl() + "/ajaxCallbackCallList";//回拨呼叫记录列表
	public static final String CALLBACK_GET_RECHARGE_LOG_LIST = getServerUrl() + "/ajaxCallbackRechargeList";//回拨充值记录列表
	public static final String CALLBACK_GET_NUMBER_LIST = getServerUrl() + "/ajaxGetCallbackNumbers";//回拨回铃号码列表
	//商品积分抵扣
	public static final String GET_PRICE_POINT_INFO = getServerUrl() + "/ajaxGetProductPricePointInfo";//获取商品积分抵扣信息
	public static final String GET_PRICE_POINT_INFO_LIST = getServerUrl() + "/ajaxGetProductPricePointInfoList";//批量获取商品积分抵扣信息
	public static final String USER_SIGN_TODAY = getServerUrl() + "/ajaxUserSignToday";//用户签到
	//用户代理
	public static final String USER_APPLY_AGENT = getServerUrl() + "/ajaxAddUserApplyAgent";//用户申请代理
	public static final String GET_USER_LAST_APPLY_AGENT_INFO = getServerUrl() + "/ajaxGetLastUserApplyAgentInfo";//查询用户最后一次申请代理记录
	public static final String GET_USER_AGENT_INFO = getServerUrl() + "/ajaxGetUserAgentInfo";//获取用户代理信息
	public static final String GET_REFERRER_USER_LIST = getServerUrl() + "/ajaxGetUserReferrerList";//获取用户推荐列表
	//区域
	public static final String GET_PROVINCE_LIST = getServerUrl() + "/ajaxGetProvinceList";//获取省列表
	public static final String GET_CITY_LIST = getServerUrl() + "/ajaxGetCityList";//获取市列表
	public static final String GET_COUNTRY_LIST = getServerUrl() + "/ajaxGetCountryList";//获取区列表
	//商品浏览记录
	public static final String GET_PRODUCT_HISTORY_LIST = getServerUrl() + "/ajaxGetProductHistoryList";//用户商品浏览记录列表
	public static final String GET_PRODUCT_HISTORY_LIST_COUNT = getServerUrl() + "/ajaxGetProductHistoryListCount";//用户商品浏览记录数量
	public static final String ADD_PRODUCT_HISTORY_INFO = getServerUrl() + "/ajaxAddProductHistoryInfo";//添加用户商品流量记录
	public static final String GET_PRODUCT_HISTORY_INFO = getServerUrl() + "/ajaxGetProductStarInfo";//获取用户商品浏览信息
	//用户余额变动日志
	public static final String GET_USER_BALANCE_LOG_LIST = getServerUrl() + "/ajaxGetUserBalanceLogList";//用户余额变动日志列表
	public static final String GET_USER_SHARE_PROFIT_LIST = getServerUrl() + "/ajaxGetUserShareProfitList";//获取用户分润列表
	public static final String GET_TOTAL_USER_SHARE_PROFIT = getServerUrl() + "/ajaxGetTotalUserShareProfit";//获取用户分润总额
	//商家相关
	public static final String SHOP_REGISTER = getServerUrl() + "/../shop/ajaxRegister";//商家注册
	public static final String SHOP_AUTH = getServerUrl() + "/../shop/ajaxShopAuth";//商家认证
	public static final String GET_SHOP_INFO = getServerUrl() + "/../shop/ajaxGetShopInfo";//获取商家信息
	public static final String GET_ADMIN_PRODUCT_LIST = getServerUrl() + "/../shop/ajaxGetAdminProductList";//获取平台商品列表
	public static final String GET_SHOP_PRODUCT_LIST = getServerUrl() + "/../shop/ajaxGetProductList";//获取商家商品列表
	public static final String GET_SHOP_AGENT_PRODUCT_LIST = getServerUrl() + "/../shop/ajaxGetProductAgentList";//获取商家代理商品列表
	public static final String SHOP_ADD_PRODUCT = getServerUrl() + "/../shop/ajaxAddProductInfo";//商家添加商品
	public static final String GET_SHOP_PRODUCT_SORT_LIST = getServerUrl() + "/../shop/ajaxGetProductSortList";//获取商家商品类别列表
	public static final String GET_BRAND_LIST = getServerUrl() + "/../shop/ajaxGetBrandList";//获取商家品牌列表
	public static final String SHOP_UPDATE_PIC = getServerUrl() + "/../shop/uploadPic";//商家上传图片
	public static final String SHOP_UPDATE_PRODUCT = getServerUrl() + "/../shop/ajaxUpdateProductInfo";//商家更新商品
	public static final String GET_SHOP_ORDER_LIST = getServerUrl() + "/../shop/ajaxGetOrderSubInfoList";//商家订单列表
	public static final String SHOP_ORDER_CHANGE_EXPRESS_PRICE = getServerUrl() + "/../shop/ajaxOrderSubChangeExpressPrice";//商家订单修改运费
	public static final String SHOP_ORDER_SHIP = getServerUrl() + "/../shop/ajaxOrderSubShip";//商家订单发货
	public static final String GET_SHOP_ORDER_INFO = getServerUrl() + "/../shop/ajaxGetOrderSubInfo";//商家订单详情
	public static final String GET_SHOP_ORDER_EXPRESS_INFO = getServerUrl() + "/../shop/ajaxGetOrderSubExpressInfo";//商家订单快递信息
	public static final String GET_EXPRESS_LIST = getServerUrl() + "/ajaxGetExpressInfoList";//快递列表
	public static final String SHOP_ADD_PRODUCT_AGENT = getServerUrl() + "/../shop/ajaxAddProductAgent";//添加代理商品
	public static final String SHOP_DELETE_PRODUCT_AGENT = getServerUrl() + "/../shop/ajaxDeleteProductAgent";//取消代理商品
	public static final String GET_SHOP_SALE_DATA = getServerUrl() + "/../shop//ajaxGetSaleData";//获取商家营收数据
	//积分商城
	public static final String GET_POINT_PRODUCT_LIST = getServerUrl() + "/ajaxGetPointProductList";//获取所有积分商品列表
	public static final String GET_POINT_PRODUCT = getServerUrl() + "/ajaxGetPointProduct";//获取积分商品信息
	public static final String ADD_POINT_ORDER = getServerUrl() + "/ajaxAddPointOrder";//积分商品下单
	public static final String DELETE_POINT_ORDER = getServerUrl() + "/ajaxDeletePointOrder";//删除积分商品订单
	public static final String GET_POINT_ORDER_LIST = getServerUrl() + "/ajaxGetPointOrderList";//获取所有积分商品订单列表
	public static final String GET_POINT_ORDER = getServerUrl() + "/ajaxGetPointOrder";//获取积分商品订单
	public static final String GET_POINT_ORDER_EXPRESS_INFO = getServerUrl() + "/ajaxGetPointOrderExpressInfo";//获取积分商品订单快递信息
	public static final String PAY_POINT_ORDER_BY_BALANCE = getServerUrl() + "/ajaxPointOrderPayByBalance";//余额方式支付积分订单
	//微店
	public static final String GET_LOCATION_SHOP_PRODUCT_LIST = getServerUrl() + "/ajaxGetShopProductList";//获取附近商家商品列表
	public static final String GET_SHOP_LIST = getServerUrl() + "/ajaxGetShopList";//获取商家列表

	//获取服务器地址
	public static String getServerUrl()
	{
		if (AppConfig.isDebug())
		{
			return DEBUG_URL;
		}
		else
		{
			return RELEASE_URL;
		}
	}

	/**
	 * 获取服务器图片地址
	 *
	 * @param picUrl
	 * @return
	 */
	public static String getServerPicUrl(String picUrl)
	{
		return getServerUrl() + "/../" + picUrl;
	}

	/**
	 * 获取服务器消息地址
	 *
	 * @param newsId
	 * @return
	 */
	public static String getServerNewsUrl(String newsId)
	{
		return getServerUrl() + "/news?newsId=" + newsId;
	}

	/**
	 * 获取服务器商品地址
	 *
	 * @param productId
	 * @return
	 */
	public static String getServerProductUrl(String productId)
	{
		String url = getServerUrl() + "/product?productId=" + productId + "&shareUserId=" + PreferenceUtils.getInstance().getUsername();
		return url;
	}

	/**
	 * 获取服务器注册地址
	 *
	 * @param shareUserId
	 * @return
	 */
	public static String getServerRegisterUrl(String shareUserId)
	{
		String url = getServerUrl() + "/register?referrerId=" + shareUserId + "&shareUserId=" + PreferenceUtils.getInstance().getUsername();
		return url;
	}

	/**
	 * 获取关于我们地址
	 *
	 * @return
	 */
	public static String getServerAboutUrl()
	{
		return getServerUrl() + "/../about";
	}

	/**
	 * 获取更多应用地址
	 *
	 * @return
	 */
	public static String getServerMoreAppUrl()
	{
		return getServerUrl() + "/../moreApp";
	}
}
