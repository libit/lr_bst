/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.enums.ProductType;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.apptools.AppFactory;

public class FragmentProductWeb extends MyBaseFragment implements IAjaxDataResponse
{
	private static final String TAG = FragmentProductWeb.class.getSimpleName();
	private XListView xListView;
	private View headView;
	private WebView webView;
	private ProgressBar progressBar;
	private String productId;
	private String productType = "";
	private ProductService mProductService;
	private PointProductService mPointProductService;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			productId = getArguments().getString(ConstValues.DATA_PRODUCT_ID);
			productType = getArguments().getString(ConstValues.DATA_PRODUCT_TYPE);
		}
		mProductService = new ProductService(this.getContext());
		mProductService.addDataResponse(this);
		mPointProductService = new PointProductService(this.getContext());
		mPointProductService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
		viewInit(rootView);
		if (productType.equals(ProductType.PRODUCT.getType() + ""))
		{
			ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(productId);
			if (productInfo != null)
			{
				loadWebData(productInfo.getContent());
			}
		}
		refreshData();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		headView = LayoutInflater.from(this.getContext()).inflate(R.layout.activity_web_view_head, null);
		xListView.setPullRefreshEnable(false);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
		webView = (WebView) rootView.findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearCache(true);
		webView.clearHistory();
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
			}
		});
		// 禁止浏览器外部攻击
		if (AppFactory.isCompatible(11))
		{
			webView.removeJavascriptInterface("searchBoxJavaBredge_");
		}
		super.viewInit(rootView);
	}

	private void loadWebData(String data)
	{
		//		String html = String.format("<html><body style=\"width:100%;\">%s</body></html>", data);
		LogcatTools.debug("loadWebData", "data:" + data);
		webView.loadDataWithBaseURL(ApiConfig.getServerUrl(), data, "text/html", "utf-8", null);
	}

	private void refreshData()
	{
		if (productType.equals(ProductType.PRODUCT.getType() + ""))
		{
			mProductService.getProductInfo(productId, null, true);
		}
		else if (productType.equals(ProductType.POINT_PRODUCT.getType() + ""))
		{
			mPointProductService.getPointProduct(productId, null, true);
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
		{
			ProductInfo productInfo = GsonTools.getReturnObject(result, ProductInfo.class);
			if (productInfo != null)
			{
				loadWebData(productInfo.getContent());
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_POINT_PRODUCT))
		{
			PointProductInfo pointProductInfo = GsonTools.getReturnObject(result, PointProductInfo.class);
			if (pointProductInfo != null)
			{
				loadWebData(pointProductInfo.getContent());
			}
			return true;
		}
		return false;
	}
}
