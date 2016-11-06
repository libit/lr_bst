/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductCommentInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderProductCommentService;
import com.lrcall.enums.CommentType;
import com.lrcall.ui.adapter.ProductCommentAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class FragmentProductComments extends MyBasePageFragment implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = FragmentProductComments.class.getSimpleName();
	private String productId;
	private OrderProductCommentService mProductCommentService;
	private ProductCommentAdapter mProductCommentAdapter;
	private final List<ProductCommentInfo> mProductCommentInfoList = new ArrayList<>();
	private Byte type = null;
	private Button btnAllComments, btnGoodComments, btnMidComments, btnBadComments;
	private final int selectedColor = R.color.white_20;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			productId = getArguments().getString(ConstValues.DATA_PRODUCT_ID);
		}
		mProductCommentService = new OrderProductCommentService(this.getContext());
		mProductCommentService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_product_comments, container, false);
		viewInit(rootView);
		onRefresh();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		btnAllComments = (Button) rootView.findViewById(R.id.btn_all_comments);
		btnGoodComments = (Button) rootView.findViewById(R.id.btn_good_comments);
		btnMidComments = (Button) rootView.findViewById(R.id.btn_mid_comments);
		btnBadComments = (Button) rootView.findViewById(R.id.btn_bad_comments);
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		btnAllComments.setOnClickListener(this);
		btnGoodComments.setOnClickListener(this);
		btnMidComments.setOnClickListener(this);
		btnBadComments.setOnClickListener(this);
		resetBtnBackground();
		btnAllComments.setBackgroundColor(getResources().getColor(selectedColor));
		super.viewInit(rootView);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_all_comments:
			{
				type = null;
				resetBtnBackground();
				btnAllComments.setBackgroundColor(getResources().getColor(selectedColor));
				onRefresh();
				break;
			}
			case R.id.btn_good_comments:
			{
				type = CommentType.GOOD.getType();
				resetBtnBackground();
				btnGoodComments.setBackgroundColor(getResources().getColor(selectedColor));
				onRefresh();
				break;
			}
			case R.id.btn_mid_comments:
			{
				type = CommentType.MID.getType();
				resetBtnBackground();
				btnMidComments.setBackgroundColor(getResources().getColor(selectedColor));
				onRefresh();
				break;
			}
			case R.id.btn_bad_comments:
			{
				type = CommentType.BAD.getType();
				resetBtnBackground();
				btnBadComments.setBackgroundColor(getResources().getColor(selectedColor));
				onRefresh();
				break;
			}
		}
	}

	private void resetBtnBackground()
	{
		btnAllComments.setBackgroundColor(getResources().getColor(R.color.transparent));
		btnGoodComments.setBackgroundColor(getResources().getColor(R.color.transparent));
		btnMidComments.setBackgroundColor(getResources().getColor(R.color.transparent));
		btnBadComments.setBackgroundColor(getResources().getColor(R.color.transparent));
	}

	@Override
	public void refreshData()
	{
		mProductCommentInfoList.clear();
		mProductCommentAdapter = null;
		loadMoreData();
	}

	/**
	 * 加载更多数据
	 */
	@Override
	public void loadMoreData()
	{
		mProductCommentService.getProductCommentInfoListCount(productId, null, null, false);
		mProductCommentService.getProductCommentInfoListCount(productId, CommentType.GOOD.getType(), null, false);
		mProductCommentService.getProductCommentInfoListCount(productId, CommentType.MID.getType(), null, false);
		mProductCommentService.getProductCommentInfoListCount(productId, CommentType.BAD.getType(), null, false);
		mProductCommentService.getProductCommentInfoList(productId, type, mDataStart, getPageSize(), null, true);
	}

	synchronized private void setAdapter(List<ProductCommentInfo> productCommentInfoList)
	{
		if (productCommentInfoList == null || productCommentInfoList.size() < 0)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		xListView.setPullLoadEnable(productCommentInfoList.size() >= getPageSize());
		for (ProductCommentInfo productCommentInfo : productCommentInfoList)
		{
			mProductCommentInfoList.add(productCommentInfo);
		}
		if (mProductCommentAdapter == null)
		{
			mProductCommentAdapter = new ProductCommentAdapter(this.getContext(), mProductCommentInfoList, null);
			xListView.setAdapter(mProductCommentAdapter);
		}
		else
		{
			mProductCommentAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_PRODUCT_COMMENT_LIST))
		{
			List<ProductCommentInfo> productCommentInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				productCommentInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductCommentInfo>>()
				{
				}.getType());
			}
			setAdapter(productCommentInfoList);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_COMMENT_COUNT))
		{
			int index = result.indexOf(",");
			if (index > -1)
			{
				String type = result.substring(0, index);
				String count = result.substring(index + 1);
				if (type.equals(CommentType.GOOD.getType() + ""))
				{
					btnGoodComments.setText(CommentType.GOOD.getDesc() + "\n" + count);
				}
				else if (type.equals(CommentType.MID.getType() + ""))
				{
					btnMidComments.setText(CommentType.MID.getDesc() + "\n" + count);
				}
				else if (type.equals(CommentType.BAD.getType() + ""))
				{
					btnBadComments.setText(CommentType.BAD.getDesc() + "\n" + count);
				}
				else
				{
					btnAllComments.setText("全部评价\n" + count);
				}
			}
			return true;
		}
		return false;
	}
}
