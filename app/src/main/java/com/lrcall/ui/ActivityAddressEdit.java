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
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.CityInfo;
import com.lrcall.appbst.models.CountryInfo;
import com.lrcall.appbst.models.ProvinceInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.AreaService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressEdit extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityAddressEdit.class.getSimpleName();
	private EditText etName, etNumber, etCountry, etProvince, etCity, etDistrict, etAddress;
	private ArrayAdapter spProvinceAdapter, spCityAdapter, spCountryAdapter;
	private Spinner spProvince, spCity, spCountry;
	private List<ProvinceInfo> mProvinceList = new ArrayList<>();
	private List<CityInfo> mCityList = new ArrayList<>();
	private List<CountryInfo> mCountryList = new ArrayList<>();
	private AreaService mAreaService;
	private AddressService mAddressService;
	private String addressId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_edit);
		addressId = getIntent().getStringExtra(ConstValues.DATA_ADDRESS_ID);
		if (StringTools.isNull(addressId))
		{
			finish();
			Toast.makeText(this, "地址为空！", Toast.LENGTH_LONG).show();
		}
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mAreaService = new AreaService(this);
		mAreaService.addDataResponse(this);
		viewInit();
		mAreaService.getProvinceList(null, false);
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etName = (EditText) findViewById(R.id.et_name);
		etNumber = (EditText) findViewById(R.id.et_number);
		etCountry = (EditText) findViewById(R.id.et_country);
		etProvince = (EditText) findViewById(R.id.et_province);
		etCity = (EditText) findViewById(R.id.et_city);
		etDistrict = (EditText) findViewById(R.id.et_district);
		etAddress = (EditText) findViewById(R.id.et_address);
		spProvince = (Spinner) findViewById(R.id.sp_province);
		spCity = (Spinner) findViewById(R.id.sp_city);
		spCountry = (Spinner) findViewById(R.id.sp_country);
		findViewById(R.id.btn_update).setOnClickListener(this);
		spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spProvince:" + mProvinceList.get(position).getProvinceId());
				mAreaService.getCityList(mProvinceList.get(position).getProvinceId(), null, false);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spCity:" + mCityList.get(position).getCityId());
				mAreaService.getCountryList(mCityList.get(position).getCityId(), null, false);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	private void initData()
	{
		UserAddressInfo userAddressInfo = DbUserAddressInfoFactory.getInstance().getUserAddressInfo(addressId);
		if (userAddressInfo != null)
		{
			etName.setText(userAddressInfo.getName());
			etNumber.setText(userAddressInfo.getNumber());
			etCountry.setText(userAddressInfo.getCountry());
			etProvince.setText(userAddressInfo.getProvince());
			etCity.setText(userAddressInfo.getCity());
			etDistrict.setText(userAddressInfo.getDistrict());
			etAddress.setText(userAddressInfo.getAddress());
		}
		else
		{
			mAddressService.getUserAddressInfo(addressId, null, true);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_update:
			{
				String name = etName.getText().toString();
				String number = etNumber.getText().toString();
				String country = etCountry.getText().toString();
				String province = etProvince.getText().toString();
				String city = etCity.getText().toString();
				String district = etDistrict.getText().toString();
				String address = etAddress.getText().toString();
				int position = spProvince.getSelectedItemPosition();
				String provinceId = "";
				if (position >= 0)
				{
					provinceId = mProvinceList.get(position).getProvinceId();
				}
				position = spCity.getSelectedItemPosition();
				String cityId = "";
				if (position >= 0)
				{
					cityId = mCityList.get(position).getCityId();
				}
				position = spCountry.getSelectedItemPosition();
				String countryId = "";
				if (position >= 0)
				{
					countryId = mCountryList.get(position).getCountryId();
				}
				if (StringTools.isNull(name))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "收件人姓名不能为空！");
					etName.requestFocus();
					return;
				}
				if (StringTools.isNull(number))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "联系电话不能为空！");
					etNumber.requestFocus();
					return;
				}
				if (StringTools.isNull(provinceId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择省份!");
					return;
				}
				if (StringTools.isNull(cityId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择市!");
					return;
				}
				if (StringTools.isNull(countryId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择区!");
					return;
				}
				if (StringTools.isNull(address))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "详细地址不能为空！");
					etAddress.requestFocus();
					return;
				}
				mAddressService.updateUserAddressInfo(addressId, name, number, country, provinceId, cityId, countryId, address, "正在处理，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.UPDATE_ADDRESS_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "更新地址成功！");
				//				initData();
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "更新地址失败：" + msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				initData();
			}
		}
		else if (url.endsWith(ApiConfig.GET_PROVINCE_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				//先清空分类
				mProvinceList.clear();
				mCityList.clear();
				mCountryList.clear();
				List<ProvinceInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProvinceInfo>>()
				{
				}.getType());
				if (list != null)
				{
					List<String> stringList = new ArrayList<>();
					int selected = -1;
					String province = etProvince.getText().toString();
					int index = 0;
					for (ProvinceInfo provinceInfo : list)
					{
						mProvinceList.add(provinceInfo);
						stringList.add(provinceInfo.getName());
						if (provinceInfo.getName().equals(province))
						{
							selected = index;
						}
						index++;
					}
					spProvinceAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
					spProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spProvince.setAdapter(spProvinceAdapter);
					if (selected >= 0)
					{
						spProvince.setSelection(selected);
					}
					spCity.setAdapter(null);
					spCountry.setAdapter(null);
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_CITY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				//先清空分类
				mCityList.clear();
				mCountryList.clear();
				List<CityInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CityInfo>>()
				{
				}.getType());
				if (list != null)
				{
					List<String> stringList = new ArrayList<>();
					int selected = -1;
					String ctiy = etCity.getText().toString();
					int index = 0;
					for (CityInfo cityInfo : list)
					{
						mCityList.add(cityInfo);
						stringList.add(cityInfo.getName());
						if (cityInfo.getName().equals(ctiy))
						{
							selected = index;
						}
						index++;
					}
					spCityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
					spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCity.setAdapter(spCityAdapter);
					if (selected >= 0)
					{
						spCity.setSelection(selected);
					}
					spCountry.setAdapter(null);
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_COUNTRY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				//先清空分类
				mCountryList.clear();
				List<CountryInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CountryInfo>>()
				{
				}.getType());
				if (list != null)
				{
					List<String> stringList = new ArrayList<>();
					int selected = -1;
					String country = etDistrict.getText().toString();
					int index = 0;
					for (CountryInfo countryInfo : list)
					{
						mCountryList.add(countryInfo);
						stringList.add(countryInfo.getName());
						if (countryInfo.getName().equals(country))
						{
							selected = index;
						}
						index++;
					}
					spCountryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
					spCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCountry.setAdapter(spCountryAdapter);
					if (selected >= 0)
					{
						spCountry.setSelection(selected);
					}
				}
			}
			return true;
		}
		return false;
	}
}
