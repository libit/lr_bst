/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.CityInfo;
import com.lrcall.appbst.models.CountryInfo;
import com.lrcall.appbst.models.ProvinceInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.AreaService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.enums.UserType;
import com.lrcall.utils.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectArea extends Dialog implements OnClickListener, IAjaxDataResponse
{
	private View layoutProvince, layoutCity, layoutCountry;
	private Spinner spProvince, spCity, spCountry;
	private final OnSelectAreaListenser listenser;
	private ArrayAdapter spProvinceAdapter, spCityAdapter, spCountryAdapter;
	private List<ProvinceInfo> mProvinceList = new ArrayList<>();
	private List<CityInfo> mCityList = new ArrayList<>();
	private List<CountryInfo> mCountryList = new ArrayList<>();
	private AreaService mAreaService;
	private byte agentType;

	public DialogSelectArea(Context context, byte userType, OnSelectAreaListenser listenser)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		this.agentType = userType;
		this.listenser = listenser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_area);
		mAreaService = new AreaService(this.getContext());
		mAreaService.addDataResponse(this);
		initView();
		if (agentType == UserType.CITY.getType())
		{
			layoutCountry.setVisibility(View.GONE);
		}
		else if (agentType == UserType.PROVINCE.getType())
		{
			layoutCity.setVisibility(View.GONE);
			layoutCountry.setVisibility(View.GONE);
		}
		mAreaService.getProvinceList(null, false);
	}

	private void initView()
	{
		View root = findViewById(R.id.dialog_root_view);
		ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(getContext()) * 2 / 3;
		root.setLayoutParams(layoutParams);
		layoutProvince = findViewById(R.id.layout_province);
		layoutCity = findViewById(R.id.layout_city);
		layoutCountry = findViewById(R.id.layout_country);
		spProvince = (Spinner) findViewById(R.id.sp_province);
		spCity = (Spinner) findViewById(R.id.sp_city);
		spCountry = (Spinner) findViewById(R.id.sp_country);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
		spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogcatTools.debug("AdapterView", "spProvince:" + mProvinceList.get(position).getProvinceId());
				if (layoutCity.getVisibility() == View.VISIBLE)
				{
					mAreaService.getCityList(mProvinceList.get(position).getProvinceId(), null, false);
				}
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
				if (layoutCountry.getVisibility() == View.VISIBLE)
				{
					mAreaService.getCountryList(mCityList.get(position).getCityId(), null, false);
				}
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
			case R.id.dialog_btn_ok:
			{
				if (listenser != null)
				{
					int position = spProvince.getSelectedItemPosition();
					//					LogcatTools.debug("DialogSelectArea", "position:" + position);
					String provinceId = "";
					if (position >= 0)
					{
						provinceId = mProvinceList.get(position).getProvinceId();
					}
					position = spCity.getSelectedItemPosition();
					//					LogcatTools.debug("DialogSelectArea", "position:" + position);
					String cityId = "";
					if (position >= 0)
					{
						cityId = mCityList.get(position).getCityId();
					}
					position = spCountry.getSelectedItemPosition();
					//					LogcatTools.debug("DialogSelectArea", "position:" + position);
					String countryId = "";
					if (position >= 0)
					{
						countryId = mCountryList.get(position).getCountryId();
					}
					if (agentType == UserType.DISTRICT.getType())
					{
						if (StringTools.isNull(provinceId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择省份!");
							return;
						}
						if (StringTools.isNull(cityId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择市!");
							return;
						}
						if (StringTools.isNull(countryId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择区!");
							return;
						}
					}
					else if (agentType == UserType.CITY.getType())
					{
						if (StringTools.isNull(provinceId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择省份!");
							return;
						}
						if (StringTools.isNull(cityId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择市!");
							return;
						}
					}
					else if (agentType == UserType.PROVINCE.getType())
					{
						if (StringTools.isNull(provinceId))
						{
							ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "请选择省份!");
							return;
						}
					}
					listenser.onOkClick(agentType, provinceId, cityId, countryId);
				}
				dismiss();
				break;
			}
			case R.id.dialog_btn_cancel:
			{
				if (listenser != null)
				{
					listenser.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PROVINCE_LIST))
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
					for (ProvinceInfo provinceInfo : list)
					{
						mProvinceList.add(provinceInfo);
						stringList.add(provinceInfo.getName());
					}
					spProvinceAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, stringList);
					spProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spProvince.setAdapter(spProvinceAdapter);
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
					for (CityInfo cityInfo : list)
					{
						mCityList.add(cityInfo);
						stringList.add(cityInfo.getName());
					}
					spCityAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, stringList);
					spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCity.setAdapter(spCityAdapter);
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
					for (CountryInfo countryInfo : list)
					{
						mCountryList.add(countryInfo);
						stringList.add(countryInfo.getName());
					}
					spCountryAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, stringList);
					spCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCountry.setAdapter(spCountryAdapter);
				}
			}
			return true;
		}
		return false;
	}

	public interface OnSelectAreaListenser
	{
		void onOkClick(byte agentType, String provinceId, String cityId, String countryId);

		void onCancelClick();
	}
}
