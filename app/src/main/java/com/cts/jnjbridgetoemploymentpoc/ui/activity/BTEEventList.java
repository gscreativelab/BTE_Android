package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.util.ArrayList;
import org.json.JSONObject;

import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.webservices.BaseAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.FacebookResponseHandler;
import com.cts.jnjbridgetoemploymentpoc.adapter.BTEEvents;
import com.cts.jnjbridgetoemploymentpoc.database.DataBaseManager;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.parsers.EventParser;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This Activity shows Events list with minimum details like event image, time,
 * name
 * 
 * @author neerajareddy
 * 
 */
public class BTEEventList extends BaseActivity {
	private BTEEvents adapter;
	private String groupid;
	private ArrayList<Events> alEventsModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.event_lsitview);
		groupid = getIntent().getExtras().getString("groupid");
		adapter = new BTEEvents(this, null);
		ListView listView = (ListView) findViewById(R.id.eventListView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						BTEEventDetails.class);

				startActivity(intent);
			}
		});
		if (NetworkUtility.isNetworkAvailable(this))
			fetchEvents(groupid);
		else {
			if (!DataBaseManager.isTableExists(BTEEventList.this,
					DatabaseConstants.CONTENT_URI_EVENTSLIST)) {
				showAlert(getResources().getString(
						R.string.alert_offline_message));
			} else
				getDataOfflineMode();
		}
	}

	/**
	 * This method fetches events list Query made :
	 * https://graph.facebook.com/<GroupID>?fields=events&access_token
	 * =<Access_Token>
	 * 
	 * @param groupID
	 */
	private void fetchEvents(String groupId) {
		showProgressDialog(getResources().getString(
				R.string.progress_events_list));
		Bundle params = new Bundle();
		params.putString("fields", "events");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				EventParser eventParser = new EventParser(
						(JSONObject) response, BTEEventList.this);
				alEventsModels = eventParser.getEvents();
				adapter.refresh(alEventsModels);
				DataBaseManager.insertEventsListRecord(BTEEventList.this,
						DatabaseConstants.CONTENT_URI_ANNOUNCEMENTS,
						alEventsModels);
				hideProgressDialog();
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(groupId, params, this, null,
				Constants.STATE_EVENTLIST, handler);
	}

	private void getDataOfflineMode() {
		alEventsModels = DataBaseManager.getGroupEventsRecords(
				BTEEventList.this, DatabaseConstants.CONTENT_URI_EVENTSLIST,
				new String[] { DatabaseConstants.COL_EVENT_ID,
						DatabaseConstants.COL_EVENT_NAME,
						DatabaseConstants.COL_EVENT_LOCATION,
						DatabaseConstants.COL_EVENT_TIMEZONE,
						DatabaseConstants.COL_EVENT_STARTTIME });
		adapter.refresh(alEventsModels);
	}

}
