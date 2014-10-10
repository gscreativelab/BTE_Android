package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This Class shows Detailed Announcements.
 * 
 * @author neerajareddy
 * 
 */
public class BTEDetailedAnnouncementActivity extends BaseActivity {

	private String name = "";
	private String desc = "";
	private TextView headerView, description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_description_view);
		Bundle info = getIntent().getExtras();
		name = info.getString("Name");
		desc = info.getString("Desc");
		headerView = (TextView) findViewById(R.id.announcement_Name);
		description = (TextView) findViewById(R.id.information);
		Log.d(Constants.LOG_TAG_ANNOUNCEMENTS, "name-->" + name);
		Log.d(Constants.LOG_TAG_ANNOUNCEMENTS, "header-->" + headerView);

		headerView.setText(name);
		description.setText(desc);

	}

}
