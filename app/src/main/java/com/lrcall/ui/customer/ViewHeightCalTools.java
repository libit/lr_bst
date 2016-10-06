/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 16/7/11.
 */
public class ViewHeightCalTools
{
	/**
	 * 设置GridView的高度，让所有Item都显示
	 *
	 * @param gridView
	 * @param colNum   列数
	 * @param isSame   每列高度是否相同
	 */
	public static void setGridViewHeight(GridView gridView, final int colNum, boolean isSame)
	{
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null || listAdapter.getCount() < 1)
		{
			return;
		}
		int totalHeight = 0;
		if (isSame)
		{
			View listItem = listAdapter.getView(0, null, gridView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight() * (listAdapter.getCount() / colNum + (listAdapter.getCount() % colNum == 0 ? 0 : 1)); // 统计所有子项的总高度
		}
		else
		{
			for (int i = 0; i < listAdapter.getCount(); )
			{
				// listAdapter.getCount()返回数据项的数目
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
				i = i + colNum;
			}
		}
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;// + ((AppFactory.isCompatible(16) ? gridView.getVerticalSpacing() : 1) * (gridView.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		LogcatTools.debug("ViewHeightCalTools", "setGridViewHeight:" + params.height);
		gridView.setLayoutParams(params);
	}

	public static void setListViewHeight(ListView listView, boolean isSame)
	{
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null || listAdapter.getCount() < 1)
		{
			return;
		}
		int totalHeight = 0;
		if (isSame)
		{
			View listItem = listAdapter.getView(0, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight() * listView.getCount(); // 统计所有子项的总高度
		}
		else
		{
			for (int i = 0; i < listAdapter.getCount(); i++)
			{
				// listAdapter.getCount()返回数据项的数目
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
}
