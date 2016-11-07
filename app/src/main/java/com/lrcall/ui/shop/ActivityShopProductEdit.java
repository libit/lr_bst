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
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BrandInfo;
import com.lrcall.appbst.models.PicInfo;
import com.lrcall.appbst.models.ProductInfo;
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
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopProductEdit extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopProductEdit.class.getSimpleName();
	private EditText etName, etPrice, etPoint, etMarketPrice, etExpressPrice, etAgentShare, etAddShare, etCount, etSortIndex, etDesc, etConfig, etContent;
	private CheckBox cbNeedExpress;
	private ImageView ivProduct;
	private Spinner spSorts, spBrands;
	private ArrayAdapter spSortsAdapter, spBrandsAdapter;
	private List<ProductSortInfo> mSortsList = new ArrayList<>();
	private List<BrandInfo> mBrandsList = new ArrayList<>();
	private ShopProductService mShopProductService;
	private String mPicId = null;
	private ProductInfo mProductInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_product_edit);
		String product = getIntent().getStringExtra(ConstValues.DATA_PRODUCT);
		mProductInfo = GsonTools.getObject(product, ProductInfo.class);
		if (mProductInfo == null)
		{
			finish();
			Toast.makeText(this, "商品不能为空!", Toast.LENGTH_SHORT).show();
		}
		mShopProductService = new ShopProductService(this);
		mShopProductService.addDataResponse(this);
		viewInit();
		mShopProductService.getProductSortList(null, 0, -1, null, null, null, false);
		mShopProductService.getBrandList(null, 0, -1, null, null, null, false);
		etName.setText(mProductInfo.getName());
		etPrice.setText(mProductInfo.getPrice() + "");
		//		etPoint.setText(mProductInfo.getPrice());
		etMarketPrice.setText(mProductInfo.getMarketPrice() + "");
		etExpressPrice.setText(mProductInfo.getExpressPrice() + "");
		etAgentShare.setText(mProductInfo.getSharePrice() + "");
		etAddShare.setText(mProductInfo.getShareAddPrice() + "");
		etCount.setText(mProductInfo.getCount() + "");
		etSortIndex.setText(mProductInfo.getSortIndex() + "");
		etDesc.setText(mProductInfo.getDescription());
		etConfig.setText(mProductInfo.getConfig());
		etContent.setText(mProductInfo.getContent());
		cbNeedExpress.setChecked(mProductInfo.getNeedExpress() == NeedExpress.NEED.getStatus());
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
		findViewById(R.id.btn_submit).setOnClickListener(this);
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
			case R.id.btn_submit:
			{
				String name = etName.getText().toString();
				String sPrice = etPrice.getText().toString();
				String sPoint = etPoint.getText().toString();
				String sMarketPrice = etMarketPrice.getText().toString();
				String sExpressPrice = etExpressPrice.getText().toString();
				String sAgentShare = etAgentShare.getText().toString();
				String sAddShare = etAddShare.getText().toString();
				String sCount = etCount.getText().toString();
				String sSortIndex = etSortIndex.getText().toString();
				int price = 0, marketPrice = 0, expressPrice = 0, agentShare = 0, addShare = 0, count = 0, sortIndex = 100;
				Integer point = null;
				try
				{
					price = Integer.parseInt(sPrice);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "价格输入错误！");
					etPrice.requestFocus();
					return;
				}
				try
				{
					point = Integer.parseInt(sPoint);
				}
				catch (NumberFormatException e)
				{
				}
				try
				{
					marketPrice = Integer.parseInt(sMarketPrice);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "市场价输入错误！");
					etMarketPrice.requestFocus();
					return;
				}
				try
				{
					expressPrice = Integer.parseInt(sExpressPrice);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "快递费输入错误！");
					etExpressPrice.requestFocus();
					return;
				}
				try
				{
					agentShare = Integer.parseInt(sAgentShare);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "代理商利润输入错误！");
					etAgentShare.requestFocus();
					return;
				}
				try
				{
					addShare = Integer.parseInt(sAddShare);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "无限级返利输入错误！");
					etAddShare.requestFocus();
					return;
				}
				try
				{
					count = Integer.parseInt(sCount);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "库存数量输入错误！");
					etCount.requestFocus();
					return;
				}
				try
				{
					sortIndex = Integer.parseInt(sSortIndex);
				}
				catch (NumberFormatException e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "排序序号输入错误！");
					etSortIndex.requestFocus();
					return;
				}
				String desc = etDesc.getText().toString();
				String config = etConfig.getText().toString();
				String content = etContent.getText().toString();
				byte needExpress = cbNeedExpress.isChecked() ? NeedExpress.NOT_NEED.getStatus() : NeedExpress.NEED.getStatus();//更新的这个为了跟服务端的网页兼容，取相反值
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
				//				if (StringTools.isNull(mPicId))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "未上传图片！");
				//					etContent.requestFocus();
				//					return;
				//				}
				mShopProductService.updateProduct(mProductInfo.getProductId(), sortId, brandId, name, mPicId, price, marketPrice, expressPrice, count, agentShare, addShare, desc, config, content, needExpress, sortIndex, point, "正在更新，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_UPDATE_PRODUCT))
		{
			showServerMsg(result, "更新商品成功！");
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
			int index = 0;
			if (tableData != null)
			{
				List<ProductSortInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (int i = 0; i < list.size(); i++)
					{
						ProductSortInfo productSortInfo = list.get(i);
						mSortsList.add(productSortInfo);
						stringList.add(productSortInfo.getName());
						if (productSortInfo.getName().equals(mProductInfo.getSortId()))
						{
							index = i;
						}
					}
				}
			}
			spSortsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spSortsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spSorts.setAdapter(spSortsAdapter);
			if (index >= 0)
			{
				spSorts.setSelection(index);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_BRAND_LIST))
		{
			//先清空
			mBrandsList.clear();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<String> stringList = new ArrayList<>();
			int index = 0;
			if (tableData != null)
			{
				List<BrandInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BrandInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (int i = 0; i < list.size(); i++)
					{
						BrandInfo brandInfo = list.get(i);
						mBrandsList.add(brandInfo);
						stringList.add(brandInfo.getName());
						if (brandInfo.getName().equals(mProductInfo.getSortId()))
						{
							index = i;
						}
					}
				}
			}
			spBrandsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spBrandsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBrands.setAdapter(spBrandsAdapter);
			if (index >= 0)
			{
				spBrands.setSelection(index);
			}
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
