package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.util.ArrayList;
import org.json.JSONObject;

import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.webservices.BaseAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.FacebookResponseHandler;
import com.cts.jnjbridgetoemploymentpoc.database.DataBaseManager;
import com.cts.jnjbridgetoemploymentpoc.model.Members;
import com.cts.jnjbridgetoemploymentpoc.parsers.MembersListParser;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * This Activity is showed when Group name is pressed on Home Page and retrieves
 * Members and has an option to navigate to Events
 * 
 * @author neerajareddy
 * 
 */
public class BTEGroupOptionList extends BaseActivity {

	private Bundle info;
	private Button members, events;
	private String groupid;
	private ArrayList<Members> alMembersModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bte_group_menu);
		info = getIntent().getExtras();
		TextView header = (TextView) findViewById(R.id.announcement_Name);
		header.setText(info.getString("groupName"));
		members = (Button) findViewById(R.id.members);
		events = (Button) findViewById(R.id.events);
		groupid = info.getString("groupid");
		members.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BTEMembersList.class);
				intent.putExtra("list", alMembersModels);
				startActivity(intent);
			}
		});
		events.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BTEEventList.class);
				intent.putExtra("groupid", groupid);
				startActivity(intent);
			}
		});
		if (NetworkUtility.isNetworkAvailable(this))
			fetchGroupMembers(groupid);
		else {
			if (!DataBaseManager.isTableExists(BTEGroupOptionList.this,
					DatabaseConstants.CONTENT_URI_MEMBERSLIST)) {
				showAlert(getResources().getString(
						R.string.alert_offline_message));
			} else
				getDataOfflineMode();
		}
	}

	/**
	 * This method fetches group members and adds group member count Query made
	 * : https://graph.facebook.com/<GroupID>?fields=members&access_token
	 * =<Access_Token>
	 * 
	 * @param groupID
	 */
	private void fetchGroupMembers(String groupId) {
		showProgressDialog(getResources().getString(
				R.string.progress_groups_events));
		Bundle params = new Bundle();
		params.putString("fields", "members");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				MembersListParser membersListParser = new MembersListParser(
						(JSONObject) response, BTEGroupOptionList.this);
				alMembersModels = membersListParser.getMembersList();
				Log.d(Constants.LOG_TAG_GROUP_OPTION, alMembersModels.size()
						+ "");
				members.setText(alMembersModels.size() + " "
						+ getResources().getString(R.string.members_header));
				DataBaseManager.insertGroupOptionsRecord(
						BTEGroupOptionList.this,
						DatabaseConstants.CONTENT_URI_MEMBERSLIST,
						alMembersModels);
				hideProgressDialog();
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(groupId, params, this, null,
				Constants.STATE_GROUP, handler);
	}

	private void getDataOfflineMode() {
		alMembersModels = DataBaseManager.getGroupOptionsRecords(
				BTEGroupOptionList.this,
				DatabaseConstants.CONTENT_URI_MEMBERSLIST, new String[] {
						DatabaseConstants.COL_MEMBERS_ID,
						DatabaseConstants.COL_MEMBERS_NAME,
						DatabaseConstants.COL_MEMBERS_ADMINISTRATOR });
		members.setText(alMembersModels.size() + " "
				+ getResources().getString(R.string.members_header));
	}
}
