/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.AlipayConfigInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.services.AlipayService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.lrcall.utils.ConstValues.SDK_PAY_FLAG;

public class ActivityPayByAlipay extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPayByAlipay.class.getSimpleName();
	private TextView tvPrice;
	private AlipayService mAlipayService;
	private String params;
	private PayTypeInfo payTypeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_by_alipay);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			params = bundle.getString(ConstValues.DATA_PAY_TYPE_INFO);
			payTypeInfo = GsonTools.getObject(params, PayTypeInfo.class);
			if (payTypeInfo == null)
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付信息为空！");
				return;
			}
		}
		mAlipayService = new AlipayService(this);
		mAlipayService.addDataResponse(this);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvPrice = (TextView) findViewById(R.id.tv_pay_total);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		tvPrice.setText(String.format("%s元", StringTools.getPrice(payTypeInfo.getPrice())));
	}

	private void initData()
	{
		pay();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_pay:
			{
				pay();
				break;
			}
		}
	}

	private void pay()
	{
		mAlipayService.getAlipayConfigInfo("正在调用支付宝，请稍后...", false);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ALIPAY_CONIFG))
		{
			AlipayConfigInfo alipayConfigInfo = GsonTools.getReturnObject(result, AlipayConfigInfo.class);
			if (alipayConfigInfo != null)
			{
				pay(alipayConfigInfo, payTypeInfo.getSubject(), params, String.format("%.2f", (double) payTypeInfo.getPrice() / 100));
			}
			else
			{
				showServerMsg(result, null);
			}
			return true;
		}
		return false;
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 */
	public void pay(AlipayConfigInfo alipayConfigInfo, String subject, String body, String price)
	{
		if (TextUtils.isEmpty(alipayConfigInfo.getPartner()) || TextUtils.isEmpty(alipayConfigInfo.getRsaPrivateKey()) || TextUtils.isEmpty(alipayConfigInfo.getSeller()))
		{
			ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付宝配置信息错误!");
			return;
		}
		//		price = "0.01";
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
				PayTask alipay = new PayTask(ActivityPayByAlipay.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
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

	private final Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case SDK_PAY_FLAG:
				{
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000"))
					{
						Toast.makeText(ActivityPayByAlipay.this, "支付成功", Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}
					else
					{
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000"))
						{
							Toast.makeText(ActivityPayByAlipay.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
						}
						else
						{
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(ActivityPayByAlipay.this, "支付失败", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}
			}
		}
	};
}
