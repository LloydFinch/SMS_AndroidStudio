package com.delta.smsandroidproject.view.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.delta.smsandroidproject.R;

public class LocationActivity extends AppCompatActivity{
	 private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
	    private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
	    private CollapsingToolbarLayout collapsingToolbarLayout;
	    private RecyclerView Charger_info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_info);
		ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
		supportPostponeEnterTransition();
//		 setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
	        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
	        collapsingToolbarLayout.setTitle(itemTitle);
	        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

	        final ImageView image = (ImageView) findViewById(R.id.image);
//	        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.dvpvklr)  
//	        .error(R.drawable.dvpvklr).into(image);

	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText(itemTitle);
	}
}
