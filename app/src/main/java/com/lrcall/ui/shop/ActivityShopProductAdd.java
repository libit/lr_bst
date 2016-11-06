/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.shop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BrandInfo;
import com.lrcall.appbst.models.PicInfo;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.enums.NeedExpress;
import com.lrcall.ui.MyBaseActivity;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopProductAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopProductAdd.class.getSimpleName();
	private EditText etName, etPrice, etPoint, etMarketPrice, etExpressPrice, etAgentShare, etAddShare, etCount, etSortIndex, etDesc, etConfig, etContent;
	private CheckBox cbNeedExpress;
	private ImageView ivProduct;
	private Spinner spSorts, spBrands;
	private ArrayAdapter spSortsAdapter, spBrandsAdapter;
	private List<ProductSortInfo> mSortsList = new ArrayList<>();
	private List<BrandInfo> mBrandsList = new ArrayList<>();
	private ShopProductService mShopProductService;
	private String mPicId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_product_add);
		mShopProductService = new ShopProductService(this);
		mShopProductService.addDataResponse(this);
		viewInit();
		mShopProductService.getProductSortList(null, 0, -1, null, null, null, false);
		mShopProductService.getBrandList(null, 0, -1, null, null, null, false);
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
		etSortIndex = (EditText) findViewById(R.id.et_sort_index);
		etDesc = (EditText) findViewById(R.id.et_desc);
		etConfig = (EditText) findViewById(R.id.et_config);
		etContent = (EditText) findViewById(R.id.et_content);
		cbNeedExpress = (CheckBox) findViewById(R.id.cb_need_express);
		ivProduct = (ImageView) findViewById(R.id.iv_product);
		findViewById(R.id.btn_add).setOnClickListener(this);
		ivProduct.setOnClickListener(this);
		spSorts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				//				LogcatTools.debug("AdapterView", "spSorts:" + mSortsList.get(position).getName());
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
				//				LogcatTools.debug("AdapterView", "spBrands:" + mBrandsList.get(position).getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	@Override
	protected void picSelected(Bitmap bitmap)
	{
		super.picSelected(bitmap);
		ShopService shopService = new ShopService(this);
		shopService.addDataResponse(this);
		shopService.updateShopPic(bitmap, "正在上传图片,请稍后...", true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_product:
			{
				selectPhoto();
				break;
			}
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
				byte needExpress = cbNeedExpress.isChecked() ? NeedExpress.NEED.getStatus() : NeedExpress.NOT_NEED.getStatus();
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
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择分类!");
					return;
				}
				if (StringTools.isNull(brandId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择品牌!");
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
				if (StringTools.isNull(mPicId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "未上传图片！");
					return;
				}
				mShopProductService.addProduct(sortId, brandId, name, mPicId, price, marketPrice, expressPrice, count, agentShare, addShare, desc, config, content, needExpress, sortIndex, point, "正在添加，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_ADD_PRODUCT))
		{
			showServerMsg(result, "添加商品成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_PRODUCT_SORT_LIST))
		{
			//先清空
			mSortsList.clear();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<String> stringList = new ArrayList<>();
			if (tableData != null)
			{
				List<ProductSortInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (ProductSortInfo productSortInfo : list)
					{
						mSortsList.add(productSortInfo);
						stringList.add(productSortInfo.getName());
					}
				}
			}
			spSortsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spSortsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spSorts.setAdapter(spSortsAdapter);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_BRAND_LIST))
		{
			//先清空
			mBrandsList.clear();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<String> stringList = new ArrayList<>();
			if (tableData != null)
			{
				List<BrandInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BrandInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (BrandInfo brandInfo : list)
					{
						mBrandsList.add(brandInfo);
						stringList.add(brandInfo.getName());
					}
				}
			}
			spBrandsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spBrandsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBrands.setAdapter(spBrandsAdapter);
			return true;
		}
		else if (url.endsWith(ApiConfig.SHOP_UPDATE_PIC))
		{
			PicInfo picInfo = GsonTools.getReturnObject(result, PicInfo.class);
			if (picInfo != null)
			{
				mPicId = picInfo.getPicId();
				PicService.ajaxGetPic(ivProduct, ApiConfig.getServerPicUrl(picInfo.getPicUrl()), DisplayTools.getWindowWidth(this) / 3);
			}
			else
			{
				showServerMsg(result, null);
			}
			return true;
		}
		return false;
	}
}
