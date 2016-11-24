/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lrcall.appbst.R;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.apptools.AppFactory;

/**
 * Created by libit on 16/7/12.
 */
public class BitmapCacheTools
{
	public final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	public final static int cacheSize = maxMemory / 8;
	protected static final LruCache<String, Bitmap> memoryCache;

	static
	{
		memoryCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				if (AppFactory.isCompatible(12))
				{
					return bitmap.getByteCount() / 1024;
				}
				else
				{
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			}
		};
	}

	/**
	 * 获取缓存Lru
	 *
	 * @return
	 */
	public static LruCache<String, Bitmap> getMemoryCache()
	{
		//		if (memoryCache == null)
		//		{
		//			memoryCache = new LruCache<String, Bitmap>(cacheSize)
		//			{
		//				@Override
		//				protected int sizeOf(String key, Bitmap bitmap)
		//				{
		//					if (AppFactory.isCompatible(12))
		//					{
		//						return bitmap.getByteCount() / 1024;
		//					}
		//					else
		//					{
		//						return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
		//					}
		//				}
		//			};
		//		}
		return memoryCache;
	}

	//图片加入缓存
	public static void addBitmapToMemoryCache(String key, Bitmap value)
	{
		if (getBitmapFromMemCache(key) == null)
		{
			memoryCache.put(key, value);
		}
		//		else
		//		{
		//			memoryCache.remove(key);
		//			memoryCache.put(key, value);
		//		}
	}

	//从图片取缓存
	public static Bitmap getBitmapFromMemCache(String key)
	{
		return memoryCache.get(key);
	}

	/**
	 * 计算图片实际大小
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth)
		{
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 解码Bitmap
	 *
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
	{
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		try
		{
			return BitmapFactory.decodeResource(res, resId, options);
		}
		catch (OutOfMemoryError e)
		{
			e.printStackTrace();
			LogcatTools.error("decodeSampledBitmapFromResource", "内存不足！");
			//			memoryCache.evictAll();
			//			memoryCache.resize(cacheSize);
			options.inSampleSize = 16;
			return BitmapFactory.decodeResource(res, resId, options);
		}
	}

	/**
	 * 从资源ID中加载图片到ImageView
	 *
	 * @param context
	 * @param imageView
	 * @param resId
	 */
	public static void loadBitmap(Context context, ImageView imageView, int resId)
	{
		int width = getImageViewWidth(context, imageView);
		final String imageKey = String.valueOf(resId) + "_" + width;
		//		LogcatTools.debug("BitmapWorkerTask", "loadBitmap imageKey:" + imageKey);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			imageView.setImageResource(R.drawable.loading);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView, resId);
			task.execute(context);
		}
	}

	private static int getImageViewWidth(Context context, ImageView imageView)
	{
		ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
		int width = layoutParams.width;
		if (width < 100)
		{
			width = 100;
		}
		if (width > DisplayTools.getWindowWidth(context))
		{
			width = DisplayTools.getWindowWidth(context);
		}
		width = 100;
		return width;
	}

	static class BitmapWorkerTask extends AsyncTask<Context, Void, Bitmap>
	{
		private final ImageView imageView;
		private final int resId;
		private int width;
		private int height;

		public BitmapWorkerTask(ImageView imageView, int resId)
		{
			this.imageView = imageView;
			this.resId = resId;
			ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
			width = layoutParams.width;
			height = layoutParams.height;
			//			LogcatTools.debug("BitmapWorkerTask", "width:" + width + ",height:" + height);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Context... params)
		{
			Context context = params[0];
			if (width < 100)
			{
				width = 100;
			}
			if (width > DisplayTools.getWindowWidth(context))
			{
				width = DisplayTools.getWindowWidth(context);
			}
			if (height < 100)
			{
				height = 100;
			}
			if (height > DisplayTools.getWindowHeight(context))
			{
				height = DisplayTools.getWindowHeight(context);
			}
			width = 100;
			height = 100;
			final Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(), resId, width, height);
			String imageKey = String.valueOf(resId) + "_" + width;
			addBitmapToMemoryCache(imageKey, bitmap);
			//			LogcatTools.debug("BitmapWorkerTask", "doInBackground imageKey:" + imageKey);
			//			LogcatTools.debug("BitmapWorkerTask", "doInBackground memoryCache:" + memoryCache.toString());
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}
	}
}
