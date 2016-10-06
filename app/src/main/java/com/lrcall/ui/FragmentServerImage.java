/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.services.PicService;
import com.lrcall.utils.StringTools;

public class FragmentServerImage extends Fragment
{
	private static final String ARG_IMAGE_URL = "ARG_IMAGE_URL";
	private static final String ARG_IMAGE_WIDTH = "ARG_IMAGE_WIDTH";
	private static final String ARG_CLICK_URL = "ARG_CLICK_URL";
	private String mImageUrl;
	private int mImageWidth;
	private String mClickUrl;
	private View rootView;
	private ImageView imageView;

	public FragmentServerImage()
	{
		// Required empty public constructor
	}

	public static FragmentServerImage newInstance(String imageUrl, int width, String clickUrl)
	{
		FragmentServerImage fragment = new FragmentServerImage();
		Bundle args = new Bundle();
		args.putString(ARG_IMAGE_URL, imageUrl);
		args.putInt(ARG_IMAGE_WIDTH, width);
		args.putString(ARG_CLICK_URL, clickUrl);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			mImageUrl = getArguments().getString(ARG_IMAGE_URL);
			mImageWidth = getArguments().getInt(ARG_IMAGE_WIDTH);
			mClickUrl = getArguments().getString(ARG_CLICK_URL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_server_image, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initView();
		PicService.ajaxGetPic(imageView, mImageUrl, mImageWidth);
		if (!StringTools.isNull(mClickUrl))
		{
			imageView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ActivityWebView.startWebActivity(FragmentServerImage.this.getContext(), "", mClickUrl);
				}
			});
		}
	}

	public void initView()
	{
		imageView = (ImageView) rootView.findViewById(R.id.iv_bg);
	}
}
