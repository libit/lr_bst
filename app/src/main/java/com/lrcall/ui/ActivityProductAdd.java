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

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BrandInfo;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

import static com.lrcall.appbst.R.id.et_config;
import static com.lrcall.appbst.R.id.et_content;
import static com.lrcall.appbst.R.id.et_desc;
import static com.lrcall.appbst.R.id.et_sort_index;

public class ActivityProductAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityProductAdd.class.getSimpleName();
	private EditText etName, etPrice, etPoint, etMarketPrice, etExpressPrice, etAgentShare, etAddShare, etCount, etSortIndex, etDesc, etConfig, etContent;
	private ArrayAdapter spSortsAdapter, spBrandsAdapter;
	private Spinner spSorts, spBrands;
	private List<ProductSortInfo> mSortsList = new ArrayList<>();
	private List<BrandInfo> mBrandsList = new ArrayList<>();
	private ShopProductService mShopProductService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_add);
		mShopProductService = new ShopProductService(this);
		mShopProductService.addDataResponse(this);
		viewInit();
		mShopProductService.getProductSortList(null, 0, -1, null, null, false, null, false);
		mShopProductService.getBrandList(null, 0, -1, null, null, false, null, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etName = (EditText) findViewById(R.id.et_name);
		spSorts = (Spinner) findViewById(R.id.sp_sorts);
		spBrands = (Spinner) findViewById(R.id.sp_brands);
		etPrice = (EditText) findViewById(R.id.et_price);
		etPoint = (EditText) findViewById(R.id.et_point);
		etMarketPrice = (EditText) findViewById(R.id.et_market_price);
		etExpressPrice = (EditText) findViewById(R.id.et_express_price);
		etAgentShare = (EditText) findViewById(R.id.et_agent_share);
		etAddShare = (EditText) findViewById(R.id.et_add_share);
		etCount = (EditText) findViewById(R.id.et_count);
		etSortIndex = (EditText) findViewById(et_sort_index);
		etDesc = (EditText) findViewById(et_desc);
		etConfig = (EditText) findViewById(et_config);
		etContent = (EditText) findViewById(et_content);
		findViewById(R.id.btn_add).setOnClickListener(this);
		spSorts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spSorts:" + mSortsList.get(position).getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		spBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spBrands:" + mBrandsList.get(position).getName());
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
			case R.id.btn_add:
			{
				String name = etName.getText().toString();
				int price = Integer.parseInt(etPrice.getText().toString());
				int point = Integer.parseInt(etPoint.getText().toString());
				int marketPrice = Integer.parseInt(etMarketPrice.getText().toString());
				int expressPrice = Integer.parseInt(etExpressPrice.getText().toString());
				int agentShare = Integer.parseInt(etAgentShare.getText().toString());
				int addShare = Integer.parseInt(etAddShare.getText().toString());
				int count = Integer.parseInt(etCount.getText().toString());
				int sortIndex = Integer.parseInt(etSortIndex.getText().toString());
				String desc = etDesc.getText().toString();
				String config = etConfig.getText().toString();
				String content = etContent.getText().toString();
				String picId = "";
				byte needExpress = 1;
				int position = spSorts.getSelectedItemPosition();
				String sortId = "";
				if (position >= 0)
				{
					sortId = mSortsList.get(position).getSortId();
				}
				position = spBrands.getSelectedItemPosition();
				String brandId = "";
				if (position >= 0)
				{
					brandId = mBrandsList.get(position).getBrandId();
				}
				if (StringTools.isNull(name))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "名称不能为空！");
					etName.requestFocus();
					return;
				}
				if (StringTools.isNull(sortId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择省份!");
					return;
				}
				if (StringTools.isNull(brandId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择市!");
					return;
				}
				if (StringTools.isNull(desc))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "描述不能为空！");
					etDesc.requestFocus();
					return;
				}
				if (StringTools.isNull(config))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "配置不能为空！");
					etConfig.requestFocus();
					return;
				}
				if (StringTools.isNull(content))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "内容不能为空！");
					etContent.requestFocus();
					return;
				}
				mShopProductService.addProduct(sortId, brandId, name, picId, price, marketPrice, expressPrice, count, agentShare, addShare, desc, config, content, needExpress, sortIndex, point, "正在添加，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_ADD_PRODUCT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "添加地址成功！");
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "添加地址失败：" + msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_PRODUCT_SORT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				//先清空分类
				mSortsList.clear();
				List<ProductSortInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
				if (list != null)
				{
					List<String> stringList = new ArrayList<>();
					for (ProductSortInfo productSortInfo : list)
					{
						mSortsList.add(productSortInfo);
						stringList.add(productSortInfo.getName());
					}
					spSortsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
					spSortsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSorts.setAdapter(spSortsAdapter);
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_BRAND_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				//先清空分类
				mBrandsList.clear();
				List<BrandInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BrandInfo>>()
				{
				}.getType());
				if (list != null)
				{
					List<String> stringList = new ArrayList<>();
					for (BrandInfo brandInfo : list)
					{
						mBrandsList.add(brandInfo);
						stringList.add(brandInfo.getName());
					}
					spBrandsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
					spBrandsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spBrands.setAdapter(spBrandsAdapter);
				}
			}
			return true;
		}
		return false;
	}
}
