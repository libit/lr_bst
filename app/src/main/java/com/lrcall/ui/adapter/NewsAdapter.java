/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.enums.NewsStatus;
import com.lrcall.models.CallLogInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class NewsAdapter extends BaseUserAdapter<NewsInfo>
{
	protected final INewsAdapterItemClicked newsAdapterItemClicked;

	public NewsAdapter(Context context, List<NewsInfo> list, INewsAdapterItemClicked newsAdapterItemClicked)
	{
		super(context, list);
		this.newsAdapterItemClicked = newsAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		NewsViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (NewsViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new NewsViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final NewsInfo newsInfo = list.get(position);
		viewHolder.tvTitle.setText(newsInfo.getTitle());
		viewHolder.tvDate.setText(CallLogInfo.getCustomerDate(newsInfo.getUpdateDateLong()));
		viewHolder.tvDesc.setText(newsInfo.getDescripition());
		if (newsInfo.getIsRead() == NewsStatus.UNREAD.getStatus())
		{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.unread_news_bg));
		}
		else
		{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.read_news_bg));
		}
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (newsAdapterItemClicked != null)
				{
					newsAdapterItemClicked.onNewsClicked(v, newsInfo);
				}
			}
		});
		return convertView;
	}

	public interface INewsAdapterItemClicked
	{
		void onNewsClicked(View v, NewsInfo newsInfo);
	}

	public static class NewsViewHolder
	{
		public TextView tvTitle;
		public TextView tvDate;
		public TextView tvDesc;

		public void clear()
		{
			tvTitle.setText("");
			tvDate.setText("");
			tvDesc.setText("");
		}
	}
}
