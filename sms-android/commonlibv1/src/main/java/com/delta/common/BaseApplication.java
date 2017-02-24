package com.delta.common;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;
import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application
{
	public static RequestQueue	requestQueue;
	public static Context mContext;
	@Override
	public void onCreate ()
	{
		super.onCreate ();
		requestQueue = Volley.newRequestQueue (getApplicationContext ());
		mContext=getApplicationContext();
		@SuppressWarnings({ "deprecation", "unused" })
		DisplayImageOptions options = new DisplayImageOptions.Builder ().cacheInMemory (true).cacheOnDisc (true)
				.imageScaleType (ImageScaleType.IN_SAMPLE_INT).bitmapConfig (Bitmap.Config.RGB_565).showImageOnLoading (R.drawable.ic_launcher)
				.showImageForEmptyUri (R.drawable.ic_launcher).showImageOnFail (R.drawable.ic_launcher).build ();

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (this).memoryCacheExtraOptions (480, 800)
				.discCacheExtraOptions (800, 800, null).memoryCache (new WeakMemoryCache ()).memoryCacheSize (2 * 1024 * 1024)
				.discCacheSize (50 * 1024 * 1024).discCacheFileCount (1000).defaultDisplayImageOptions (getDisplayOptions()).build ();
		ImageLoader.getInstance ().init (config);
	}

	public static RequestQueue getRequestQueue ()
	{
		return requestQueue;
	}
	public static Context getAppContext(){
		return mContext;
	}
	
	@SuppressWarnings("deprecation")
	private DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.displayer(new RoundedBitmapDisplayer(20))
				.displayer(new FadeInBitmapDisplayer(100))
				.build();
		return options;
}
}
