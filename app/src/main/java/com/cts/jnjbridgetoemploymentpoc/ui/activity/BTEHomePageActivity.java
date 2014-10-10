package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.utils.SharedPref;
import com.cts.jnjbridgetoemploymentpoc.webservices.BaseAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.FacebookResponseHandler;
import com.cts.jnjbridgetoemploymentpoc.adapter.BTEAnnouncements;
import com.cts.jnjbridgetoemploymentpoc.database.DataBaseManager;
import com.cts.jnjbridgetoemploymentpoc.model.Announcements;
import com.cts.jnjbridgetoemploymentpoc.parsers.FeedParser;
import com.cts.jnjbridgetoemploymentpoc.parsers.GroupImageUrlParser;
import com.facebook.Session;
import com.squareup.picasso.Picasso;

/**
 * This Activity shows Home Page with a Group Image and latest 5 announcements
 * made
 * 
 * @author neerajareddy
 * 
 */
public class BTEHomePageActivity extends BaseActivity {

	private Button btnGroupName = null;
	private TextView header = null;
	private ImageView GroupLogo;
	private BTEAnnouncements adapter;
	private String groupName;
	private URL img_url;
	private boolean isGroupMember;
	private Button btnRequesttoJoinGroup;
	private ImageView ivLogout;
	private String directoryPath;
	private SharedPref pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bte_group_information_view);

		isGroupMember = getIntent().getExtras().getBoolean(
				Constants.CONST_ISGROUP_MEMBER);
		btnRequesttoJoinGroup = (Button) findViewById(R.id.btnRequesttoJoinGroup);
		GroupLogo = (ImageView) findViewById(R.id.GroupLogo);
		header = (TextView) findViewById(R.id.textView1);
		btnGroupName = (Button) findViewById(R.id.GroupName);
		ivLogout = (ImageView) findViewById(R.id.ivLogout);
		header.setText(getResources().getText(R.string.group_header));
		header.setTextColor(Color.WHITE);
		btnGroupName.setText(getResources().getString(R.string.group_name));
		pref = new SharedPref(this);
		if (isGroupMember) {
			// user is in BTE Group
			groupName = getIntent().getExtras().getString("groupName");

			btnGroupName.setText(groupName);
			btnGroupName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getApplicationContext(),
							BTEGroupOptionList.class);
					intent.putExtra("groupName", groupName);
					intent.putExtra("groupid",
							getResources().getString(R.string.group_id));
					startActivity(intent);
				}
			});

			adapter = new BTEAnnouncements(this);
			ListView listView = (ListView) findViewById(R.id.announcementList);
			listView.setDividerHeight(10);
			listView.setAdapter(adapter);

			if (NetworkUtility.isNetworkAvailable(this)) {
				getGroupImage();
				getAnouncements();
			} else {
				if (!DataBaseManager.isTableExists(BTEHomePageActivity.this,
						DatabaseConstants.CONTENT_URI_ANNOUNCEMENTS)) {
					showAlert(getResources().getString(
							R.string.alert_offline_message));
				} else
					getDataOfflineMode();
			}
		} else {
			// user is not in BTE Group , show Request to join group
			btnRequesttoJoinGroup.setVisibility(View.VISIBLE);
			GroupLogo.setImageResource(R.drawable.bte_logo);

			btnRequesttoJoinGroup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (NetworkUtility
							.isNetworkAvailable(BTEHomePageActivity.this)) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(Constants.JOIN_GROUP_URL));
						startActivity(browserIntent);
					} else {
						showAlert(getResources().getString(
								R.string.alert_no_internet));
					}

				}
			});
		}

		ivLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkUtility.isNetworkAvailable(BTEHomePageActivity.this)) {
					Session session = Session.getActiveSession();
					if (session != null) {

						if (!session.isClosed()) {
							session.closeAndClearTokenInformation();
							// clear your preferences if saved
						}
					} else {

						session = new Session(BTEHomePageActivity.this);
						Session.setActiveSession(session);

						session.closeAndClearTokenInformation();
						// clear your preferences if saved

					}

				} else {
					Toast.makeText(
							BTEHomePageActivity.this,
							getResources().getString(
									R.string.alert_offline_logout),
							Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		/*
		 * final Session openSession = Session.getActiveSession(); if
		 * (openSession != null) { openSession.closeAndClearTokenInformation();
		 * }
		 */

	}

	private void getGroupImage() {
		setGroupImage(getResources().getString(R.string.group_id), GroupLogo,
				BTEHomePageActivity.this);

	}

	/**
	 * This method retrieves Group image url and sets the image as Group Logo
	 * Query made :
	 * https://graph.facebook.com/<GroupID>?fields=cover&access_token
	 * =<Access_Token>
	 * 
	 * @param GroupID
	 * @param groupImageView
	 * @param context
	 */

	private void setGroupImage(String GroupID, final ImageView groupImageView,
			final Context context) {
		Bundle params = new Bundle();
		params.putString("fields", "cover");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				GroupImageUrlParser groupImageUrlParser = new GroupImageUrlParser(
						(JSONObject) response, BTEHomePageActivity.this);
				img_url = groupImageUrlParser.getGroupImageUrlParser();
				pref.putString(Constants.COVER_IMAGE_URL, img_url.toString());

				Picasso.with(BTEHomePageActivity.this).load(img_url.toString())
						.placeholder(R.drawable.event).error(R.drawable.event)
						.into(GroupLogo);

			}
		};
		BaseAsyncLoader.invokeAsyncWebService(GroupID, params, this, null,
				Constants.STATE_HOME, handler);
	}

	/**
	 * Fetch the feed for the particular group and parse the anouncements from
	 * that feed Query made https://graph
	 * .facebook.com/<GroupID>?fields=feed&access_token=<Access_Token>
	 * 
	 */

	private void getAnouncements() {
		showProgressDialog(getResources().getString(
				R.string.progress_announcements));
		Bundle params = new Bundle();
		params.putString("fields", "feed");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				FeedParser feedParser = new FeedParser((JSONObject) response,
						BTEHomePageActivity.this);
				ArrayList<Announcements> alAnnouncementModels = feedParser
						.getAnouncements();
				adapter.refresh(alAnnouncementModels);
				DataBaseManager.insertAnnouncementsRecord(
						BTEHomePageActivity.this,
						DatabaseConstants.CONTENT_URI_ANNOUNCEMENTS,
						alAnnouncementModels);

				hideProgressDialog();
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(
				getResources().getString(R.string.group_id), params, this,
				null, Constants.STATE_HOMEFEED, handler);
	}

	@SuppressLint("NewApi")
	private void getDataOfflineMode() {

		Picasso.with(BTEHomePageActivity.this)
				.load(pref.getString(Constants.COVER_IMAGE_URL).toString())
				.placeholder(R.drawable.event).error(R.drawable.event)
				.into(GroupLogo);

		ArrayList<Announcements> alAnnouncementModels = DataBaseManager
				.getAnnouncementsRecords(BTEHomePageActivity.this,
						DatabaseConstants.CONTENT_URI_ANNOUNCEMENTS,
						new String[] { DatabaseConstants.COL_ANNOUNCEMNTS_NAME,
								DatabaseConstants.COL_ANNOUNCEMNTS_DATE,
								DatabaseConstants.COL_ANNOUNCEMNTS_DESC });
		adapter.refresh(alAnnouncementModels);
	}
}
