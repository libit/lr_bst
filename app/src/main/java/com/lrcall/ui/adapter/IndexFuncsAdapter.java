/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ClientIndexFuncInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexFuncsAdapter extends BaseUserAdapter<ClientIndexFuncInfo>
{
	private IItemClicked iItemClicked;

	public IndexFuncsAdapter(Context context, List<ClientIndexFuncInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		FuncViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (FuncViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_func_index, null);
			viewHolder = new FuncViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ClientIndexFuncInfo funcInfo = list.get(position);
		int[] resList = {R.drawable.mine_icon_waitpay, R.drawable.mine_icon_ordrers, R.drawable.mine_icon_address, R.drawable.mine_icon_care, R.drawable.mine_icon_website, R.drawable.mine_icon_shipped, R.drawable.mine_icon_changps, R.drawable.mine_icon_wallet, R.drawable.mine_icon_changps, R.drawable.mine_icon_wallet};
		if (StringTools.isNull(funcInfo.getPicId()))
		{
			viewHolder.ivHead.setImageResource(resList[position % 10]);
		}
		else
		{
			PicService.ajaxGetRoundPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(funcInfo.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 8, resList[position % 10]);
		}
		viewHolder.tvName.setText(funcInfo.getName());
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onFuncClicked(funcInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onFuncClicked(ClientIndexFuncInfo clientIndexFuncInfo);
	}

	public static class FuncViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
		}
	}
}
