package com.delta.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
	/**
	 * 
	 * @param context
	 * @param data (Bundle)
	 * @param activity (XXXActivity.class)
	 */
	public <T> void JumpActivity (Context context, Bundle data, Class<T> activity)
	{
		Intent i = new Intent (context, activity);
		if (data != null)
		{
			i.putExtras (data);
		}
		startActivity (i);
	}
}
