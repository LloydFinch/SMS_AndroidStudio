package com.delta.smsandroidproject.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.view.fragment.LocationFragment;

public class CameraActivity extends Activity implements OnClickListener{
	private LinearLayout llCamera;
	private LinearLayout llGallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.camera_activity);

		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

		initView();
		initData();
	}

	public void initView() {
		llCamera = (LinearLayout) this
				.findViewById(R.id.ll_ticket_response_camera);
		llCamera.setOnClickListener(this);
		llGallery = (LinearLayout) this
				.findViewById(R.id.ll_ticket_response_gallery);
		llGallery.setOnClickListener(this);
	}

	public void initData() {
		// TODO get the picture form server
	}

	@Override
	public void onClick(View v) {

		if (LocationFragment.photos.size() >= 3) {
			setResult(LocationFragment.NULL);
			finish();
		} else {
			switch (v.getId()) {
			case R.id.ll_ticket_response_camera:
				setResult(LocationFragment.PASS_CAMERA);
				finish();
				break;
			case R.id.ll_ticket_response_gallery:
				setResult(LocationFragment.PASS_PHOTO);
				finish();
				break;
			default:
				break;
			}
		}
	}

	
}
