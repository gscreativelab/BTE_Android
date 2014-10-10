package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cts.jnjbridgetoemploymentpoc.database.DataBaseManager;
import com.cts.jnjbridgetoemploymentpoc.model.Groups;
import com.cts.jnjbridgetoemploymentpoc.model.Permissions;
import com.cts.jnjbridgetoemploymentpoc.parsers.GroupParser;
import com.cts.jnjbridgetoemploymentpoc.parsers.PermissionParser;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.utils.SharedPref;
import com.cts.jnjbridgetoemploymentpoc.webservices.BaseAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.FacebookResponseHandler;
import com.cts.jnjbridgetoemploymentpoc.webservices.SessionStatusCallback;
import com.facebook.Session;
import com.facebook.model.GraphUser;

/**
 * This Activity handles facebook login process and is the entry point of the
 * application
 * 
 * @author neerajareddy
 * 
 */
public class BTELoginActivity extends BaseActivity {

	private Button fbLogin = null;
	private boolean isGroupMember = false;
	private String groupName;
	private SharedPref pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		fbLogin = (Button) findViewById(R.id.login_Buttons);
		fbLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkUtility.isNetworkAvailable(BTELoginActivity.this)) {
					showProgressDialog(getResources().getString(
							R.string.progress_loginfb));
					fbLogin();
				} else {
					if (DataBaseManager.isTableExists(BTELoginActivity.this,
							DatabaseConstants.CONTENT_URI)) {
						showOfflineAlert(getResources().getString(
								R.string.alert_offline_mode));
					} else
						showAlert(getResources().getString(
								R.string.alert_offline_message));

				}

			}

		});
		pref = new SharedPref(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * To get FB Hash key
	 * 
	 */
	private void getHashKeyforFB() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.cts.jnjbridgetoemploymentpoc.ui.activity",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			Log.d(Constants.LOG_TAG_LOGIN, "NameNotFoundException:" + e);
		} catch (NoSuchAlgorithmException e) {
			Log.d(Constants.LOG_TAG_LOGIN, "NoSuchAlgorithmException:" + e);
		}
	}

	/**
	 * This method opens facebook active session and retrieves Access Token
	 * 
	 */
	private void fbLogin() {

		SessionStatusCallback callBack = new SessionStatusCallback(
				BTELoginActivity.this, new FacebookResponseHandler() {

					@Override
					public void handleResponse(Object response) {
						if ((GraphUser) response != null) {
							hideProgressDialog();
							pref.putString(Constants.USER_ID,
									((GraphUser) response).getId());
							pref.putString("status", "LoggedIn");
							checkPermissions();
						}
					}
				});
		BaseAsyncLoader.getCurrentSession(BTELoginActivity.this, callBack);

	}

	private void checkPermissions() {
		showProgressDialog(getResources().getString(R.string.progress_login));
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				getGroupList();
				ArrayList<Permissions> alPermissionList = new ArrayList<Permissions>();
				PermissionParser permissionParser = new PermissionParser(
						(JSONObject) response, BTELoginActivity.this);
				alPermissionList = permissionParser.getPermissions();
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(Constants.GET_PERMISSIONS, null,
				this, null, Constants.STATE_PERMISSIONS, handler);
	}

	/**
	 * This method retrieves user group ids and validates if user is member of
	 * BTE Group. If found valid application lets user to enter home page Query
	 * made : https://graph.facebook.com/me/groups?access_token=<Access_Token>
	 */
	private void getGroupList() {
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {

				GroupParser groupParser = new GroupParser(
						(JSONObject) response, BTELoginActivity.this);
				ArrayList<Groups> alGroupModels = new ArrayList<Groups>();
				alGroupModels = groupParser.getGroups();
				if (alGroupModels.size() > 0) {
					for (int i = 0; i < alGroupModels.size(); i++) {
						if (getResources().getString(R.string.group_id)
								.equalsIgnoreCase(alGroupModels.get(i).getId())) {
							isGroupMember = true;
							groupName = alGroupModels.get(i).getName();
							DataBaseManager
									.insertGroupRecord(BTELoginActivity.this,
											DatabaseConstants.CONTENT_URI,
											DatabaseConstants.COL_GROUP_NAME,
											groupName);
						}
					}
					if (isGroupMember) {
						hideProgressDialog();
						Intent intent = new Intent(getApplicationContext(),
								BTEHomePageActivity.class);
						intent.putExtra(Constants.CONST_ISGROUP_MEMBER, true);
						intent.putExtra("groupName", groupName);
						startActivity(intent);
					} else {
						hideProgressDialog();
						Intent intent = new Intent(getApplicationContext(),
								BTEHomePageActivity.class);
						intent.putExtra(Constants.CONST_ISGROUP_MEMBER, false);
						startActivity(intent);
					}

				} else {
					hideProgressDialog();
					Intent intent = new Intent(getApplicationContext(),
							BTEHomePageActivity.class);
					intent.putExtra(Constants.CONST_ISGROUP_MEMBER, false);
					startActivity(intent);
					// showAlert(getResources().getString(
					// R.string.facebook_not_bte_group));
				}

			}
		};
		BaseAsyncLoader.invokeAsyncWebService(Constants.GET_GROUPS, null, this,
				null, Constants.STATE_LOGIN, handler);
	}

	private void getDataOfflineMode() {
		groupName = DataBaseManager.getGroupRecords(BTELoginActivity.this,
				DatabaseConstants.CONTENT_URI,
				new String[] { DatabaseConstants.COL_GROUP_NAME });
		Intent intent = new Intent(getApplicationContext(),
				BTEHomePageActivity.class);
		intent.putExtra("groupName", groupName);
		intent.putExtra(Constants.CONST_ISGROUP_MEMBER, true);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// showProgressDialog(getResources().getString(R.string.progress_login));
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	public void showOfflineAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNeutralButton(getResources().getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDataOfflineMode();
						dialog.dismiss();
					}
				});
		builder.show();

	}

}
