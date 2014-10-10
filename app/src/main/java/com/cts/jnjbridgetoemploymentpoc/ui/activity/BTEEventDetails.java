package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.jnjbridgetoemploymentpoc.database.DataBaseManager;
import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Attendees;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.model.User;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;
import com.cts.jnjbridgetoemploymentpoc.utils.NetworkUtility;
import com.cts.jnjbridgetoemploymentpoc.utils.SharedPref;
import com.cts.jnjbridgetoemploymentpoc.webservices.AtendeesAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.EventsAsyncLoader;
import com.cts.jnjbridgetoemploymentpoc.webservices.RsvpAsyncLoader;

/**
 * This Activity shows event details once clicked on events list and performs
 * actions like changing RSVP status, posting comments and sending feedback
 * 
 * @author neerajareddy
 * 
 */
public class BTEEventDetails extends BaseActivity implements OnClickListener {
	private ImageView eventLogo;
	private TextView tvEventTitle;
	private Button btnRSVP, btnPostEvent, btnEventOrganizedby, btnEventDate,
			btnAttendees, btnEventLocation, btnFeedback;
	private LinearLayout llPosts;
	private Events eventsModel;
	private RsvpAsyncLoader rsvpAsyncLoader;
	private SharedPref sharedPref;
	private HashMap<String, ArrayList<Attendees>> hashMap;
	private ImageView ivRsvp;
	private FrameLayout hiddenLayout;
	private String organizedBy;
	private String organizerId;
	private boolean posted = false;
	private boolean rsvpUpdated = false;
	private SimpleDateFormat simpleDateFormat, simpleDateFormatReq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detailedview);
		sharedPref = new SharedPref(this);
		simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_PATTERN);
		simpleDateFormatReq = new SimpleDateFormat(Constants.DATE_PATTERN);
		initializeControllers();
		rsvpAsyncLoader = new RsvpAsyncLoader();
		Bundle info = getIntent().getExtras();

		eventsModel = (Events) info.getSerializable("obj");

		int first = getResources().getString(R.string.attendees).length();
		int second = getResources().getString(R.string.view_attendee).length();
		Spannable span = new SpannableString(getResources().getString(
				R.string.attendees)
				+ "\n" + getResources().getString(R.string.view_attendee));
		// Big font till you find `\n`
		span.setSpan(new RelativeSizeSpan(0.8f), first, (first + second + 1),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// Small font from `\n` to the end
		span.setSpan(new RelativeSizeSpan(0.8f), first, (first + second + 1),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		btnAttendees.setText(span);

		tvEventTitle.setText(eventsModel.getName());
		btnEventDate.setText(getUpdatedFormatTime(eventsModel.getStart_time()));
		btnEventLocation.setText(eventsModel.getLocation());

		setEventImage(eventsModel.getId(), eventLogo, BTEEventDetails.this);

		if (NetworkUtility.isNetworkAvailable(this))
			getEventDetails();
		else {
			if (!DataBaseManager.isEventTableExists(BTEEventDetails.this,
					DatabaseConstants.CONTENT_URI_EVENTSDETAILS,
					eventsModel.getId())) {
				showAlert(getResources().getString(
						R.string.alert_offline_message));
			} else
				getDataOfflineMode();
		}

	}

	public void initializeControllers() {
		eventLogo = (ImageView) findViewById(R.id.eventLogo);
		tvEventTitle = (TextView) findViewById(R.id.textView1);
		btnEventOrganizedby = (Button) findViewById(R.id.eventName);
		btnEventDate = (Button) findViewById(R.id.eventDate);
		btnAttendees = (Button) findViewById(R.id.attendees);
		btnAttendees.setOnClickListener(this);
		btnRSVP = (Button) findViewById(R.id.RSVP);
		btnRSVP.setOnClickListener(this);
		btnPostEvent = (Button) findViewById(R.id.postEvent);
		btnPostEvent.setOnClickListener(this);
		btnFeedback = (Button) findViewById(R.id.feedback);
		btnFeedback.setOnClickListener(this);
		btnEventLocation = (Button) findViewById(R.id.eventLocation);
		llPosts = (LinearLayout) findViewById(R.id.llPosts);
		ivRsvp = (ImageView) findViewById(R.id.ivRsvp);
		hiddenLayout = (FrameLayout) findViewById(R.id.hideLayout);
	}

	/**
	 * This method retrieves event feed and event organizer details
	 * */

	private void getEventDetails() {
		showProgressDialog(getResources().getString(
				R.string.progress_eventdetails));
		EventsAsyncLoader asyncLoader = new EventsAsyncLoader();
		asyncLoader.getEventOrganiserDetails(BTEEventDetails.this,
				eventsModel.getId(), Constants.STATE_EVENTDETAILS_ORGANIZER,
				new DataListner() {

					@Override
					public void onDataRetrieved(Object object) {
						User user = (User) object;
						organizedBy = user.getName();
						organizerId = user.getId();
						btnEventOrganizedby.setText(getResources().getString(
								R.string.organized_by)
								+ organizedBy);
						getEventFeeds();
					}
				});

	}

	private void getEventFeeds() {
		EventsAsyncLoader asyncLoader = new EventsAsyncLoader();
		asyncLoader.getEventFeed(BTEEventDetails.this, eventsModel.getId(),
				Constants.STATE_EVENTDETAILS_FEED, organizedBy,
				new DataListner() {

					@Override
					public void onDataRetrieved(Object object) {
						hideProgressDialog();
						@SuppressWarnings("unchecked")
						ArrayList<Events> alEventsModels = (ArrayList<Events>) object;
						if (alEventsModels != null && alEventsModels.size() > 0) {

							DataBaseManager.insertEventDetailsRecord(
									BTEEventDetails.this,
									eventsModel.getId(),
									DatabaseConstants.CONTENT_URI_EVENTSDETAILS,
									alEventsModels);
							Log.i(Constants.LOG_TAG_EVENTDETAILS,
									alEventsModels.size() + "");
							// eventPostAdapter.refresh(alEventsModels);
							if (alEventsModels != null) {
								addPosts(alEventsModels);
							}
						} else {

							DataBaseManager.insertEventDetailsRecordForOrganizer(
									BTEEventDetails.this,
									eventsModel.getId(),
									DatabaseConstants.CONTENT_URI_EVENTSDETAILS,
									organizedBy);

						}
						if (!posted)
							checkForAttending(eventsModel.getId());
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.RSVP:
			changeRSVPStatus();
			break;
		case R.id.attendees:
			showAttendeesList();
			break;
		case R.id.postEvent:
			postToEvent();
			break;
		case R.id.feedback:
			sendFeedback();
			break;
		}
	}

	private void changeRSVPStatus() {

		if (NetworkUtility.isNetworkAvailable(BTEEventDetails.this)) {
			// online mode. allow user to post rsvp
			final String userId = sharedPref.getString(Constants.USER_ID);

			if (userId.equalsIgnoreCase(organizerId))
				Toast.makeText(
						BTEEventDetails.this,
						getResources().getString(R.string.alert_organizer_rsvp),
						Toast.LENGTH_SHORT).show();
			else if (isEventPast(eventsModel.getStart_time()))
				Toast.makeText(BTEEventDetails.this,
						getResources().getString(R.string.message_passed),
						Toast.LENGTH_SHORT).show();
			else {

				hiddenLayout.setVisibility(View.VISIBLE);
				openOptionsMenu();
			}
		} else {
			// offline mide
			// user can not post rsvp . show alert message
			showAlert(getResources().getString(
					R.string.alert_offline_not_available));
		}

	}

	private void showAttendeesList() {
		Intent intent = new Intent(getApplicationContext(),
				BTEEventAttendeesList.class);
		intent.putExtra("event", eventsModel);
		intent.putExtra("hashmap", hashMap);
		startActivity(intent);
	}

	/**
	 * This method handles both sending feedback and post to event
	 * 
	 * @param v
	 */
	private void postToEvent() {
		if (NetworkUtility.isNetworkAvailable(BTEEventDetails.this)) {
			Intent intent = new Intent(getApplicationContext(),
					BTEPostEvent.class);
			intent.putExtra("eventid", eventsModel.getId());

			startActivityForResult(intent, Constants.requestCode);
		} else {
			showAlert(getResources().getString(
					R.string.alert_offline_not_available));
		}
	}

	private void sendFeedback() {
		if (NetworkUtility.isNetworkAvailable(BTEEventDetails.this)) {
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
					Uri.fromParts(Constants.MAIL, Constants.GROUP_MAILID, null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, eventsModel.getName()
					+ getResources().getString(R.string.feedback));
			startActivity(Intent.createChooser(emailIntent, getResources()
					.getString(R.string.selct_feedbackoption)));
		} else {
			showAlert(getResources().getString(
					R.string.alert_offline_not_available));
		}
	}

	@Override
	protected void onActivityResult(int requstCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requstCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			posted = true;
			getEventDetails();
		}
	}

	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rsvp_menuview, menu);
		for (int i = 0; i < menu.size(); i++) {
			MenuItem item = menu.getItem(i);
			CharSequence rawTitle = menu.getItem(i).getTitle().toString();
			item.setTitleCondensed(rawTitle);

			SpannableString spanString = new SpannableString(rawTitle);

			spanString.setSpan(new ForegroundColorSpan(Color.rgb(20, 90, 225)),
					0, spanString.length(), 0); // fix the color to white
			spanString.setSpan(new AlignmentSpan.Standard(
					Alignment.ALIGN_CENTER), 0, spanString.length(), 0);

			item.setTitle(spanString);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equals("Attend")) {
			updateRsvp(eventsModel.getId(), "attending");
		} else if (item.getTitle().toString().equals("Unsure")) {
			updateRsvp(eventsModel.getId(), "maybe");
		} else if (item.getTitle().toString().equals("Decline")) {
			updateRsvp(eventsModel.getId(), "declined");
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method is called to update RSVP status, it posts either attending,
	 * declined or unsure
	 * 
	 * @param eventId
	 * @param rsvp
	 */

	public void updateRsvp(String eventId, final String rsvp) {
		showProgressDialog(getResources().getString(
				R.string.progress_updatingrsvp));
		rsvpAsyncLoader.postRsvpStatus(BTEEventDetails.this, eventId, rsvp,
				Constants.STATE_EVENTDETAILS_POSTRSVP, new DataListner() {
					@Override
					public void onDataRetrieved(Object object) {
						if (rsvp.equals("attending")) {
							btnRSVP.setText(getResources().getString(
									R.string.attending));
						} else if (rsvp.equals("maybe")) {
							btnRSVP.setText(getResources().getString(
									R.string.usure));
						} else if (rsvp.equals("declined")) {
							btnRSVP.setText(getResources().getString(
									R.string.declined));
						}
						hideProgressDialog();
						Toast.makeText(BTEEventDetails.this, (String) object,
								Toast.LENGTH_SHORT).show();
						updateRsvpImage(rsvp);
						checkForAttending(eventsModel.getId());
					}
				});

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		hiddenLayout = (FrameLayout) findViewById(R.id.hideLayout);
		hiddenLayout.setVisibility(View.INVISIBLE);

		super.onOptionsMenuClosed(menu);
	}

	/**
	 * get event Image url from shared pref if not exists make a service call
	 * 
	 * @param eventID
	 * @param groupImageView
	 * @param context
	 */
	public void setEventImage(String eventID, ImageView groupImageView,
			Context context) {
		EventsAsyncLoader asyncLoader = new EventsAsyncLoader();
		asyncLoader.setEventImage(eventID, groupImageView, context,
				Constants.STATE_EVENTDETAILS_IMAGE);
	}

	/**
	 * This method creates a view with posts on that events
	 * 
	 * @param alEventsModels
	 */

	public void addPosts(ArrayList<Events> alEventsModels) {
		llPosts.removeAllViews();

		// add only recent 5 posts.

		int noOfPosttoShow;
		if (alEventsModels.size() > 5)
			noOfPosttoShow = 5;
		else
			noOfPosttoShow = alEventsModels.size();

		for (int i = 0; i < noOfPosttoShow; i++) {
			LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
					R.layout.eventpost_cell, null);
			TextView tvPostName = (TextView) layout
					.findViewById(R.id.tvPostName);
			TextView tvPostDate = (TextView) layout
					.findViewById(R.id.tvPostDate);
			TextView tvPostDesc = (TextView) layout
					.findViewById(R.id.tvPostDesc);

			Events eventsModel = alEventsModels.get(i);
			tvPostName.setText(eventsModel.getFrom() + "");
			tvPostDate.setText(eventsModel.getCreated_time() + "");
			tvPostDesc.setText(eventsModel.getMessage() + "");
			llPosts.addView(layout);
		}

		hideProgressDialog();

	}

	/**
	 * This method checks if Event has passed.
	 * 
	 * @param mDate
	 * @return
	 */

	public boolean isEventPast(String mDate) {
		try {

			SimpleDateFormat simpleDateFormat;
			Calendar calendar_current = Calendar.getInstance();
			Calendar calendar_event = Calendar.getInstance();
			if (mDate.contains("T")) {
				simpleDateFormat = new SimpleDateFormat(
						Constants.DATE_PATTERN_TIMEZONE);
				Date date = simpleDateFormat.parse(mDate);
				calendar_event.setTime(date);
			} else {
				simpleDateFormat = new SimpleDateFormat(
						Constants.SIMPLE_DATE_PATTERN);
				Date date = simpleDateFormat.parse(mDate);
				calendar_event.set(Calendar.DATE, date.getDate());
				calendar_event.set(Calendar.MONTH, date.getMonth());
				calendar_event.set(Calendar.YEAR, date.getYear() + 1900);
				if (calendar_current.get(Calendar.YEAR) == calendar_event
						.get(Calendar.YEAR))
					if (calendar_current.get(Calendar.MONTH) == calendar_event
							.get(Calendar.MONTH))
						if (calendar_current.get(Calendar.DATE) == calendar_event
								.get(Calendar.DATE))
							return false;

			}

			if (calendar_event.before(calendar_current))
				return true;
		} catch (ParseException e) {
			Log.d(Constants.LOG_TAG_EVENTS, "ParseException -->" + e);
			ExceptionHandler.makeExceptionAlert(BTEEventDetails.this,
					new org.apache.http.ParseException(e.getMessage()));
		}
		return false;
	}

	/**
	 * This methods checks for rsvp status of the members invited.
	 * 
	 * @param eventID
	 */

	public void checkForAttending(String eventID) {

		showProgressDialog(getResources().getString(
				R.string.progress_attendeeslist));
		AtendeesAsyncLoader atendeesAsyncLoader = new AtendeesAsyncLoader();

		atendeesAsyncLoader.getInvitees(BTEEventDetails.this, eventID,
				Constants.STATE_ATTENDEES_LIST, new DataListner() {

					@SuppressWarnings("unchecked")
					@Override
					public void onDataRetrieved(Object object) {
						hideProgressDialog();
						hashMap = (HashMap<String, ArrayList<Attendees>>) object;

						DataBaseManager.insertAttendeesRecord(
								BTEEventDetails.this, eventsModel.getId(),
								DatabaseConstants.CONTENT_URI_EVENTATTENDEES,
								hashMap);
						if (hashMap != null)
							setRSVPData();

						hideProgressDialog();
					}

				});
	}

	private void setRSVPData() {
		final String userId = sharedPref.getString(Constants.USER_ID);
		String attendeeRSVPType = "";
		for (String key : hashMap.keySet()) {
			ArrayList<Attendees> arrayList = hashMap.get(key);

			for (Attendees attendees : arrayList) {
				if (userId.equalsIgnoreCase(attendees.getId())) {
					attendeeRSVPType = attendees.getRsvp_status();
					btnRSVP.setText(attendeeRSVPType.toUpperCase(Locale
							.getDefault()));
					updateRsvpImage(attendeeRSVPType);
					break;
				}
			}
		}
		if (isEventPast(eventsModel.getStart_time())
				&& attendeeRSVPType.equalsIgnoreCase("attending")) {
			btnPostEvent.setVisibility(View.VISIBLE);
			btnFeedback.setVisibility(View.VISIBLE);

		} else if (!isEventPast(eventsModel.getStart_time())
				&& (attendeeRSVPType.equalsIgnoreCase("declined")
						|| attendeeRSVPType.equalsIgnoreCase("unsure") || attendeeRSVPType
							.equalsIgnoreCase("attending")))
			btnPostEvent.setVisibility(View.VISIBLE);
		else {
			btnPostEvent.setVisibility(View.GONE);
			btnFeedback.setVisibility(View.GONE);
		}
	}

	public void updateRsvpImage(String rsvpStatus) {
		if (rsvpStatus.equalsIgnoreCase("attending")) {
			ivRsvp.setImageResource(R.drawable.accepted);
		} else if (rsvpStatus.equalsIgnoreCase("declined")) {
			ivRsvp.setImageResource(R.drawable.declined);
		} else if (rsvpStatus.equalsIgnoreCase("unsure")
				|| rsvpStatus.equalsIgnoreCase("maybe")) {
			ivRsvp.setImageResource(R.drawable.unsure);
		}
	}

	private void getDataOfflineMode() {
		ArrayList<Events> alEventsModels = DataBaseManager
				.getGroupEventDetailsRecords(BTEEventDetails.this,
						eventsModel.getId(),
						DatabaseConstants.CONTENT_URI_EVENTSDETAILS,
						new String[] {
								DatabaseConstants.COL_EVENTDETAILS_ORGANIZEDBY,
								DatabaseConstants.COL_EVENTDETAILS_FROM,
								DatabaseConstants.COL_EVENTDETAILS_CREATED,
								DatabaseConstants.COL_EVENTDETAILS_MESSAGE });
		hashMap = DataBaseManager.getEventAttendeesList(this,
				eventsModel.getId(),
				DatabaseConstants.CONTENT_URI_EVENTATTENDEES, new String[] {
						DatabaseConstants.COL_ATTENDEES_ID,
						DatabaseConstants.COL_ATTENDEES_NAME,
						DatabaseConstants.COL_ATTENDEES_RSVP_STATUS });

		if (hashMap != null) {
			setRSVPData();
		}
		if (alEventsModels != null) {
			addPosts(alEventsModels);
		}
		if (alEventsModels.size() > 0)
			btnEventOrganizedby.setText(getResources().getString(
					R.string.organized_by)
					+ alEventsModels.get(0).getOrganized_by());

		String eventOrganizer = DataBaseManager
				.getEventOrganizer(
						BTEEventDetails.this,
						eventsModel.getId(),
						DatabaseConstants.CONTENT_URI_EVENTSDETAILS,
						new String[] { DatabaseConstants.COL_EVENTDETAILS_ORGANIZEDBY, });

		if (!eventOrganizer.isEmpty()) {
			btnEventOrganizedby.setText(getResources().getString(
					R.string.organized_by)
					+ eventOrganizer);
		}
	}

	public String getUpdatedFormatTime(String sTime) {

		String updatedDate = sTime;
		if (sTime.contains("T"))
			sTime = sTime.substring(0, sTime.indexOf("T"));

		try {
			Date date = simpleDateFormat.parse(sTime);
			updatedDate = simpleDateFormatReq.format(date);

		} catch (ParseException e) {
			Log.d(Constants.LOG_TAG_EVENTSPARSER,
					"parsing ex-->" + e.getMessage());
			ExceptionHandler.makeExceptionAlert(BTEEventDetails.this,
					new org.apache.http.ParseException(e.getMessage()));
		}
		return updatedDate;

	}
}
