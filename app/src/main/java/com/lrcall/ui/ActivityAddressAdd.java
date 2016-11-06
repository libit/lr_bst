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
import com.lrcall.appbst.models.CityInfo;
import com.lrcall.appbst.models.CountryInfo;
import com.lrcall.appbst.models.ProvinceInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.AreaService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityAddressAdd.class.getSimpleName();
	private EditText etName, etNumber, etCountry, etProvince, etCity, etDistrict, etAddress;
	private ArrayAdapter spProvinceAdapter, spCityAdapter, spDistrictAdapter;
	private Spinner spProvince, spCity, spDistrict;
	private List<ProvinceInfo> mProvinceList = new ArrayList<>();
	private List<CityInfo> mCityList = new ArrayList<>();
	private List<CountryInfo> mDistrictList = new ArrayList<>();
	private AreaService mAreaService;
	private AddressService mAddressService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_add);
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mAreaService = new AreaService(this);
		mAreaService.addDataResponse(this);
		viewInit();
		mAreaService.getProvinceList(null, false);
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
		spDistrict = (Spinner) findViewById(R.id.sp_district);
		findViewById(R.id.btn_add).setOnClickListener(this);
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

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_add:
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
				position = spDistrict.getSelectedItemPosition();
				String countryId = "";
				if (position >= 0)
				{
					countryId = mDistrictList.get(position).getCountryId();
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
				mAddressService.addUserAddressInfo(name, number, country, provinceId, cityId, countryId, address, "正在处理，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.ADD_ADDRESS_INFO))
		{
			showServerMsg(result, "添加地址成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				finish();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PROVINCE_LIST))
		{
			//先清空
			mProvinceList.clear();
			mCityList.clear();
			mDistrictList.clear();
			spCity.setAdapter(null);
			spDistrict.setAdapter(null);
			List<String> stringList = new ArrayList<>();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProvinceInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProvinceInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (ProvinceInfo provinceInfo : list)
					{
						mProvinceList.add(provinceInfo);
						stringList.add(provinceInfo.getName());
					}
				}
			}
			spProvinceAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spProvince.setAdapter(spProvinceAdapter);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_CITY_LIST))
		{
			//先清空
			mCityList.clear();
			mDistrictList.clear();
			spDistrict.setAdapter(null);
			List<String> stringList = new ArrayList<>();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<CityInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CityInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (CityInfo cityInfo : list)
					{
						mCityList.add(cityInfo);
						stringList.add(cityInfo.getName());
					}
				}
			}
			spCityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spCity.setAdapter(spCityAdapter);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_COUNTRY_LIST))
		{
			//先清空
			mDistrictList.clear();
			List<String> stringList = new ArrayList<>();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<CountryInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CountryInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (CountryInfo countryInfo : list)
					{
						mDistrictList.add(countryInfo);
						stringList.add(countryInfo.getName());
					}
				}
			}
			spDistrictAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
			spDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spDistrict.setAdapter(spDistrictAdapter);
			return true;
		}
		return false;
	}
}
