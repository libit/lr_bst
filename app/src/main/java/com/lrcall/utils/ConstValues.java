/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

/**
 * Created by libit on 15/8/31.
 */
public class ConstValues
{
	// App类型
	public static final int TYPE_ALL = 0;
	public static final int TYPE_SYSTEM = 1;
	public static final int TYPE_USER = 2;
	// App状态
	public static final int STATUS_ALL = 0;
	public static final int STATUS_ENABLED = 1;
	public static final int STATUS_DISABLED = 2;
	// App隐藏属性
	public static final int STATUS_HIDE = 1;
	public static final int STATUS_SHOW = 2;
	// App属性--是否存在
	public static final int STATUS_EXIST = 1;
	public static final int STATUS_NOT_EXIST = 2;
	public static final String DATA_WEB_TITLE = "data.web.layout_title";
	public static final String DATA_WEB_URL = "data.web.url";
	public static final String DATA_NUMBER = "number";
	// 登录结果
	public static final int REQUEST_LOGIN_USER = 1001;//登录代码，跳转到用户中心
	public static final int RESULT_LOGIN_SUCCESS = 2000;//登录成功代码
	public static final int RESULT_LOGIN_ERROR = 2001;//登录失败代码
	// 注册结果
	public static final int REQUEST_REGISTER = 1100;//注册代码
	public static final int RESULT_REGISTER_SUCCESS = 2002;//注册成功代码
	public static final int RESULT_REGISTER_ERROR = 2003;//注册失败代码
	// 重置密码结果
	public static final int REQUEST_RESET_PWD = 2100;//重置密码代码
	public static final int RESULT_RESET_PWD_SUCCESS = 2102;//重置密码成功代码
	// 请求拍照
	public static final int REQUEST_CAPTURE_SET_PIC = 1003;//请求拍照，完成后跳转到设置头像
	public static final int REQUEST_PICK_SET_PIC = 1004;//请求相册，完成后跳转到设置头像
	public static final int REQUEST_EDIT_PIC = 1005;//请求裁剪图片，完成后跳转到设置头像
	//联系人
	public static final String DATA_CONTACT_ID = "contact_id";
	//订单
	public static final String DATA_ORDER_ID = "order_id";
	public static final String DATA_ORDER_TYPE = "order_type";
	public static final String DATA_NEWS_ID = "news_id";
	public static final String DATA_PRODUCT_ID = "product_id";
	public static final String DATA_PRODUCT = "product_info";
	public static final String DATA_PRODUCT_SHOW_PIC_URLS = "product_show_pic_urls";
	public static final String DATA_PRODUCT_SORT_ID = "product_sort_id";
	public static final String DATA_ORDER_PRODUCT_LIST = "order_product_list";
	public static final String DATA_ADDRESS_ID = "address_id";
	public static final String DATA_PAY_TYPE_INFO = "pay_type_info";//支付信息
	public static final String DATA_SHOW_PAD = "show_pad";
	public static final String DATA_PRODUCT_TYPE = "product_type";//商品类型
	public static final String DATA_SHOP_ID = "shop_id";
	public static final String DATA_SHARE_DATA = "share_data";
	public static final String DATA_SHARE_CONTENT = "share_content";
	public static final String DATA_USER_ID = "user_id";
	public static final String DATA_REFERRER_ID = "referrer_id";
}
