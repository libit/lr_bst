/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.content.Context;
import android.widget.TextView;

import com.lrcall.appbst.services.NumberService;
import com.lrcall.db.DbNumberLocalInfoFactory;

/**
 * Created by libit on 16/7/17.
 */
public class LocalTools
{
	/**
	 * 设置归属地信息
	 *
	 * @param context
	 * @param tvLocal 要显示的TextView
	 * @param number  要查询归属地的号码
	 * @param append  归属地信息是否附加到TextView的内容之后
	 */
	public static void setLocal(Context context, TextView tvLocal, String number, boolean append)
	{
		number = CallTools.convertToCallPhoneNumber(number);
		String areacode = number;
		String local = "";
		if (CallTools.isChinaMobilePhoneNumber(areacode))
		{
			areacode = areacode.substring(0, 7);
			local = DbNumberLocalInfoFactory.getInstance().getLocalInfo(areacode);
		}
		else
		{
			if (areacode.startsWith("0") && areacode.length() > 8)
			{
				areacode = areacode.substring(0, 4);
				local = DbNumberLocalInfoFactory.getInstance().getLocalInfo(areacode);
			}
		}
		if (!StringTools.isNull(local))
		{
			if (append)
			{
				tvLocal.setText(tvLocal.getText() + "  " + local);
			}
			else
			{
				tvLocal.setText(local);
			}
			new NumberService(context, tvLocal).getNumberLabelInfo(number, true);
		}
		else
		{
			new NumberService(context, tvLocal).getLocalInfo(areacode, true);
		}
	}
}
