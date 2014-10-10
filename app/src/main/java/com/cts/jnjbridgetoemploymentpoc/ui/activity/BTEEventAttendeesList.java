package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cts.jnjbridgetoemploymentpoc.model.Attendees;
import com.cts.jnjbridgetoemploymentpoc.model.Events;

/**
 * This Class shows Attendees list on a particular event
 * 
 * @author neerajareddy
 * 
 */
public class BTEEventAttendeesList extends BaseActivity {

	private LinearLayout attendeeslayout = null;
	private TextView textView1;
	private HashMap<String, ArrayList<Attendees>> hashMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendes_listview);
		attendeeslayout = (LinearLayout) findViewById(R.id.attendeeslayout);

		textView1 = (TextView) findViewById(R.id.textView1);
		Events event = (Events) getIntent().getExtras()
				.getSerializable("event");
		hashMap = (HashMap<String, ArrayList<Attendees>>) getIntent()
				.getExtras().getSerializable("hashmap");
		final SpannableStringBuilder sb = new SpannableStringBuilder(
				event.getName() + " Attendees");

		final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span
																				// to
																				// make
																				// text
																				// bold
		final StyleSpan iss = new StyleSpan(android.graphics.Typeface.NORMAL);// Span
																				// to
																				// make
																				// text
																				// italic
		sb.setSpan(iss, 0, event.getName().length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters
														// Bold
		sb.setSpan(bss, event.getName().length(),
				(event.getName() + " Attendees").length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make last 2 characters
														// Italic
		textView1.setText(sb);
	//	textView1.setText(event.getName());

		if (hashMap != null)
			prepareAttendeesList(hashMap);
	}

	/**
	 * This method seperates attendees list view according to their RSVP status
	 * 
	 * @param hashMap
	 */
	public void prepareAttendeesList(
			HashMap<String, ArrayList<Attendees>> hashMap) {
		Set<String> rsvpType = hashMap.keySet();
		for (String key : rsvpType) {
			prepareLayout(key, hashMap.get(key));
		}

		hideProgressDialog();
	}

	/**
	 * This method prepares attendees list view
	 * 
	 * @param type
	 * @param alAttendeesModels
	 */

	public void prepareLayout(String type,
			ArrayList<Attendees> alAttendeesModels) {
		if (type.equalsIgnoreCase("not_replied"))
			return;
		TextView textView_header = new TextView(this);
		if (type.equalsIgnoreCase("attending"))
			type = getResources().getString(R.string.attending);
		if (type.equalsIgnoreCase("unsure"))
			type = getResources().getString(R.string.usure);
		if (type.equalsIgnoreCase("declined"))
			type = getResources().getString(R.string.declined);
		textView_header.setText(type);
		textView_header.setPadding(10, 30, 30, 30);
		attendeeslayout.addView(textView_header);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		params.setMargins(0, 0, 0, 5);

		for (int i = 0; i < alAttendeesModels.size(); i++) {
			TextView textView = new TextView(this);
			textView.setText(alAttendeesModels.get(i).getName());
			textView.setPadding(30, 30, 30, 30);
			textView.setBackgroundColor(Color.WHITE);
			attendeeslayout.addView(textView, params);
		}
	}
}
