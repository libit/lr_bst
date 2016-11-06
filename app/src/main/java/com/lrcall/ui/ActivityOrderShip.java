/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ExpressInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopOrderService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityOrderShip extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityOrderShip.class.getSimpleName();
	private View layoutExpress, layoutNoExpress;
	private TextView tvName, tvNumber, tvAddress;
	private EditText etExpressId;
	private ArrayAdapter spExpressAdapter;
	private Spinner spExpress;
	private List<String> mExpressList = new ArrayList<>();
	private ShopOrderService mShopOrderService;
	private AddressService mAddressService;
	private String mOrderSubId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_ship);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mOrderSubId = bundle.getString(ConstValues.DATA_ORDER_ID);
		}
		if (StringTools.isNull(mOrderSubId))
		{
			finish();
			Toast.makeText(this, "订单号不能为空！", Toast.LENGTH_LONG).show();
		}
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mShopOrderService = new ShopOrderService(this);
		mShopOrderService.addDataResponse(this);
		viewInit();
		refreshExpressList(Arrays.asList("顺丰", "EMS"));
		mShopOrderService.getOrderExpressInfo(mOrderSubId, "请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutExpress = findViewById(R.id.layout_express_info);
		layoutNoExpress = findViewById(R.id.layout_no_express_info);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		etExpressId = (EditText) findViewById(R.id.et_express_id);
		spExpress = (Spinner) findViewById(R.id.sp_express);
		findViewById(R.id.btn_send).setOnClickListener(this);
		spExpress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spExpress:" + mExpressList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_send:
			{
				String expressId = etExpressId.getText().toString();
				int position = spExpress.getSelectedItemPosition();
				String expressName = "";
				if (position >= 0)
				{
					expressName = mExpressList.get(position);
				}
				if (StringTools.isNull(expressName))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择快递!");
					return;
				}
				mShopOrderService.orderShip(mOrderSubId, expressName, expressId, "正在提交，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_ORDER_SHIP))
		{
			showServerMsg(result, "发货成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_EXPRESS_LIST))
		{
			List<String> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<String>>()
				{
				}.getType());
			}
			refreshExpressList(list);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_ORDER_EXPRESS_INFO))
		{
			ExpressInfo expressInfo = GsonTools.getReturnObject(result, ExpressInfo.class);
			if (expressInfo != null)
			{
				//				tvExpress.setText(expressInfo.getExpressName());
				mAddressService.getUserAddressInfo(expressInfo.getAddressId(), null, false);
				layoutExpress.setVisibility(View.VISIBLE);
				layoutNoExpress.setVisibility(View.GONE);
			}
			else
			{
				tvName.setText("");
				tvNumber.setText("");
				tvAddress.setText("");
				layoutExpress.setVisibility(View.GONE);
				layoutNoExpress.setVisibility(View.VISIBLE);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				tvName.setText(userAddressInfo.getName());
				tvNumber.setText(userAddressInfo.getNumber());
				tvAddress.setText(userAddressInfo.getProvince() + " " + userAddressInfo.getCity() + " " + userAddressInfo.getDistrict() + " " + userAddressInfo.getAddress());
			}
			else
			{
				tvName.setText("");
				tvNumber.setText("");
				tvAddress.setText("");
			}
		}
		return false;
	}

	private void refreshExpressList(List<String> list)
	{
		//先清空
		mExpressList.clear();
		if (list != null)
		{
			for (String str : list)
			{
				mExpressList.add(str);
			}
		}
		spExpressAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mExpressList);
		spExpressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spExpress.setAdapter(spExpressAdapter);
	}
}
