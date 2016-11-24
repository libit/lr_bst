/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.enums.DataTrafficStatus;
import com.lrcall.enums.DataTrafficType;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class DataTrafficAdapter extends BaseUserAdapter<DataTrafficInfo>
{
	private final IItemClick iItemClick;

	public DataTrafficAdapter(Context context, List<DataTrafficInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DataTrafficViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (DataTrafficViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_data_traffic, null);
			viewHolder = new DataTrafficViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final DataTrafficInfo dataTrafficInfo = list.get(position);
		String validateDate = "";
		if (dataTrafficInfo.getValidateDate() != null)
		{
			validateDate = "  有效期" + dataTrafficInfo.getValidateDate() + "天";
		}
		PicService.ajaxGetPic(viewHolder.ivPic, ApiConfig.getServerPicUrl(dataTrafficInfo.getPicUrl()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 4);
		viewHolder.tvName.setText(dataTrafficInfo.getName() + validateDate);
		viewHolder.tvRemark.setText(dataTrafficInfo.getDescription());
		viewHolder.btnRecharge.setText(StringTools.getPrice(dataTrafficInfo.getPrice()) + "元");
		if (dataTrafficInfo.getStatus() == DataTrafficStatus.HOT.getStatus())
		{
			viewHolder.ivDefault.setVisibility(View.VISIBLE);
		}
		else if (dataTrafficInfo.getStatus() == DataTrafficStatus.ENABLED.getStatus())
		{
			if (iItemClick != null)
			{
				viewHolder.btnRecharge.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//				Intent intent = new Intent(context, ActivityAddressEdit.class);
						//				intent.putExtra(ConstValues.DATA_ADDRESS_ID, dataTrafficInfo.getAddressId());
						//				context.startActivity(intent);
						iItemClick.onDataTrafficClicked(dataTrafficInfo);
					}
				});
			}
		}
		else if (dataTrafficInfo.getStatus() == DataTrafficStatus.DISABLED.getStatus())
		{
			viewHolder.btnRecharge.setEnabled(false);
		}
		if (dataTrafficInfo.getDataType().equals(DataTrafficType.PACKAGE.getType()))
		{
			viewHolder.ivDefault.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onDataTrafficClicked(DataTrafficInfo dataTrafficInfo);
	}

	public static class DataTrafficViewHolder
	{
		public ImageView ivPic;
		public ImageView ivDefault;
		public TextView tvName;
		public TextView tvRemark;
		public Button btnRecharge;

		public void viewInit(View convertView)
		{
			ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvRemark = (TextView) convertView.findViewById(R.id.tv_remark);
			ivDefault = (ImageView) convertView.findViewById(R.id.iv_default);
			btnRecharge = (Button) convertView.findViewById(R.id.btn_recharge);
		}

		public void clear()
		{
			ivPic.setImageBitmap(null);
			ivDefault.setVisibility(View.GONE);
			tvName.setText("");
			tvRemark.setText("");
			btnRecharge.setText("");
		}
	}
}
