package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.webservices.PostToEventAsync;

/**
 * This class allows user to post to an Event
 * 
 * @author neerajareddy
 * 
 */
public class BTEPostEvent extends BaseActivity {

	private String eventid;
	private Button btnPost, btnCancel;
	private PostToEventAsync postToEventAsync;
	private EditText etMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_to_eventview);

		Bundle bundle = getIntent().getExtras();
		eventid = bundle.getString("eventid");
		etMessage = (EditText) findViewById(R.id.editText1);
		btnPost = (Button) findViewById(R.id.Post);
		btnCancel = (Button) findViewById(R.id.Cancel);

		postToEventAsync = new PostToEventAsync();

		btnPost.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				if (NetworkUtility.isNetworkAvailable(BTEPostEvent.this)) {
					String message = etMessage.getText().toString();
					if (!message.isEmpty()) {
						showProgressDialog(getResources().getString(
								R.string.message_posting));

						postToEventAsync.postToEvent(BTEPostEvent.this,
								eventid, message, Constants.STATE_POSTTOEVENT,
								new DataListner() {

									@Override
									public void onDataRetrieved(Object object) {
										hideProgressDialog();
									}
								});
					} else
						Toast.makeText(
								BTEPostEvent.this,
								getResources().getString(
										R.string.alert_message_post),
								Toast.LENGTH_SHORT).show();
				} else {
					showAlert(getResources().getString(
							R.string.alert_offline_message));
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
