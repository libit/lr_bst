/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.SignUtils;
import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.AlipayConfigInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 支付宝服务类
 * Created by libit on 16/4/6.
 */
public class AlipayService extends BaseService
{
	public static final int SDK_PAY_FLAG = 1;

	public AlipayService(Context context)
	{
		super(context);
	}

	/**
	 * 获取支付宝配置信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getAlipayConfigInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_ALIPAY_CONIFG, params, tips, needServiceProcessData);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 */
	public void pay(final Activity activity, final Handler handler, AlipayConfigInfo alipayConfigInfo, String subject, String body, String price)
	{
		if (TextUtils.isEmpty(alipayConfigInfo.getPartner()) || TextUtils.isEmpty(alipayConfigInfo.getRsaPrivateKey()) || TextUtils.isEmpty(alipayConfigInfo.getSeller()))
		{
			ToastView.showCenterToast(context, R.drawable.ic_do_fail, "支付宝配置信息错误!");
			return;
		}
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String orderInfo = getOrderInfo(alipayConfigInfo, subject, body, price);
		String sign = SignUtils.sign(orderInfo, alipayConfigInfo.getRsaPrivateKey());
		try
		{
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
		Runnable payRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 */
	private String getOrderInfo(AlipayConfigInfo alipayConfigInfo, String subject, String body, String price)
	{
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + alipayConfigInfo.getPartner() + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + alipayConfigInfo.getSeller() + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + alipayConfigInfo.getNotifyUrl() + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 */
	private String getOutTradeNo()
	{
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ALIPAY_CONIFG))
		{
			AlipayConfigInfo alipayConfigInfo = GsonTools.getReturnObject(result, AlipayConfigInfo.class);
			if (alipayConfigInfo != null)
			{
			}
		}
	}
}
