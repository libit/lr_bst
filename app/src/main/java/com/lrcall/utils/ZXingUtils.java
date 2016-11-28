/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.ui.ActivityProduct;
import com.lrcall.ui.ActivityRegister;
import com.lrcall.ui.ActivityWebView;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by JC on 2016/11/13.
 */
public class ZXingUtils
{
	private static final int BLACK = 0xff000000;

	//生成二维码
	public static Bitmap Create2QR(String data, int width)
	{
		Bitmap bitmap = create2DCoderBitmap(data, width, width);
		return bitmap;
	}

	/**
	 * 解析扫描结果
	 *
	 * @param context
	 * @param rawResult
	 * @return
	 */
	public static boolean parseResult(Context context, Result rawResult)
	{
		String str = rawResult.getText();
		if (str.startsWith(ApiConfig.getServerUrl()))
		{
			String url = str;
			if (url.contains("/product?"))
			{
				Intent intent = new Intent(context, ActivityProduct.class);
				String productId = StringTools.getValue(url, "productId=", "&shareUserId=");
				String referrerId = StringTools.getValue(url, "shareUserId=", "");
				intent.putExtra(ConstValues.DATA_PRODUCT_ID, productId);
				intent.putExtra(ConstValues.DATA_REFERRER_ID, referrerId);
				context.startActivity(intent);
			}
			else if (url.contains("/register?"))
			{
				Intent intent = new Intent(context, ActivityRegister.class);
				String userId = StringTools.getValue(url, "referrerId=", "&shareUserId=");
				intent.putExtra(ConstValues.DATA_REFERRER_ID, userId);
				context.startActivity(intent);
			}
			else
			{
				ActivityWebView.startWebActivity(context, "来自二维码扫描", url);
			}
			return true;
		}
		else if (str.startsWith("http://") || str.startsWith("https://"))
		{
			String url = str;
			ActivityWebView.startWebActivity(context, "来自二维码扫描", url);
			return true;
		}
		return false;
	}

	/**
	 * 解析扫描的图片
	 *
	 * @param scanBitmap
	 * @return
	 */
	public static Result scanningImage(Bitmap scanBitmap)
	{
		if (scanBitmap == null)
		{
			return null;
		}
		MultiFormatReader multiFormatReader = new MultiFormatReader();
		// 解码的参数
		Hashtable<DecodeHintType, Object> hints = new Hashtable<>(2);
		// 可以解析的编码类型
		Vector<BarcodeFormat> decodeFormats = new Vector<>();
		if (decodeFormats == null || decodeFormats.isEmpty())
		{
			decodeFormats = new Vector<>();
			// 这里设置可扫描的类型，我这里选择了都支持
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		// 设置继续的字符编码格式为UTF8
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
		// 设置解析配置参数
		multiFormatReader.setHints(hints);
		// 开始对图像资源解码
		Result rawResult = null;
		try
		{
			int[] pixels = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
			scanBitmap.getPixels(pixels, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
			RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), pixels);
			rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
		}
		catch (NotFoundException e)
		{
			e.printStackTrace();
		}
		return rawResult;
	}

	/**
	 * 生成一个二维码图像
	 *
	 * @param url       传入的字符串，通常是一个URL
	 * @param QR_WIDTH  宽度（像素值px）
	 * @param QR_HEIGHT 高度（像素值px）
	 * @return
	 */
	public static final Bitmap create2DCoderBitmap(String url, int QR_WIDTH, int QR_HEIGHT)
	{
		try
		{
			// 判断URL合法性
			if (StringTools.isNull(url))
			{
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			return bitmap;
		}
		catch (WriterException e)
		{
			Log.i("log", "生成二维码错误" + e.getMessage());
			return null;
		}
	}

	/**
	 * 生成一个二维码图像
	 *
	 * @param str            传入的字符串，通常是一个URL
	 * @param widthAndHeight 图像的宽高
	 * @return
	 */
	public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException
	{
		Hashtable<EncodeHintType, String> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (matrix.get(x, y))
				{
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
