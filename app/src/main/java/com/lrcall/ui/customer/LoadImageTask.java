/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.ContactInfo;

/**
 * Created by libit on 16/7/17.
 */
public class LoadImageTask
{
	public static void loadBitmap(Long cId, ImageView imageView)
	{
		final Bitmap bitmap = BitmapCacheTools.getBitmapFromMemCache(getStoreKey(cId));
		if (bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			imageView.setImageResource(R.drawable.default_header);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			task.execute(cId);
		}
	}

	//设置存储到缓存的联系人图片key
	private static String getStoreKey(Long contactId)
	{
		return "c" + contactId;
	}

	public static class BitmapWorkerTask extends AsyncTask<Long, Void, Bitmap>
	{
		private final Context context;
		private final ImageView imageView;

		public BitmapWorkerTask(ImageView imageView)
		{
			this.imageView = imageView;
			context = MyApplication.getContext();
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Long... params)
		{
			ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(context, params[0], true);
			Bitmap bitmap = contactInfo.getContactPhoto();
			if (bitmap != null)
			{
				BitmapCacheTools.addBitmapToMemoryCache(getStoreKey(params[0]), bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			if (result != null)
			{
				imageView.setImageBitmap(result);
			}
			else
			{
				BitmapCacheTools.loadBitmap(context, imageView, R.drawable.default_header);
			}
		}
	}
}
