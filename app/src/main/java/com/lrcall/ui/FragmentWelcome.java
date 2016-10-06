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
import com.lrcall.utils.StringTools;

public class FragmentWelcome extends Fragment
{
	private static final String ARG_IMAGE_RES = "ARG_IMAGE_RES";
	private static final String ARG_URL = "ARG_URL";
	private int mImageRes;
	private String mUrl;
	private View rootView;
	private ImageView imageView;

	public FragmentWelcome()
	{
		// Required empty public constructor
	}

	public static FragmentWelcome newInstance(int imageRes, String url)
	{
		FragmentWelcome fragment = new FragmentWelcome();
		Bundle args = new Bundle();
		args.putInt(ARG_IMAGE_RES, imageRes);
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			mImageRes = getArguments().getInt(ARG_IMAGE_RES);
			mUrl = getArguments().getString(ARG_URL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initView();
		imageView.setImageResource(mImageRes);
		if (!StringTools.isNull(mUrl))
		{
			imageView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ActivityWebView.startWebActivity(FragmentWelcome.this.getContext(), "", mUrl);
				}
			});
		}
	}

	public void initView()
	{
		imageView = (ImageView) rootView.findViewById(R.id.iv_bg);
	}
}
