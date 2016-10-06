/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lrcall.appbst.R;

public class FragmentFind extends MyBaseFragment implements View.OnClickListener
{
	private static final String TAG = FragmentFind.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_find, container, false);
		viewInit(rootView);
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		rootView.findViewById(R.id.btn_go_shopping).setOnClickListener(this);
		super.viewInit(rootView);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_go_shopping:
			{
				if (ActivityMain.getInstance() != null)
				{
					ActivityMain.getInstance().setCurrentPage(ActivityMain.INDEX);
				}
				break;
			}
		}
	}
}
