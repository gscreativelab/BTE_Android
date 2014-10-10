package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import java.util.ArrayList;

import com.cts.jnjbridgetoemploymentpoc.model.Members;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This Class shows Members list of the Group
 * 
 * @author neerajareddy
 * 
 */
public class BTEMembersList extends BaseActivity {

	private LinearLayout membersView;
	private ArrayList<Members> alMembersModels;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.members);
		Bundle info = getIntent().getExtras();
		alMembersModels = (ArrayList<Members>) info.getSerializable("list");
		TextView header = (TextView) findViewById(R.id.announcement_Name);
		header.setText(getResources().getString(R.string.members_header));
		membersView = (LinearLayout) findViewById(R.id.members_layout);

		prepareLayout(alMembersModels);

	}

	/**
	 * This method prepares layout for the members.
	 * 
	 * @param alAttendeesModels
	 */

	public void prepareLayout(ArrayList<Members> alAttendeesModels) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		params.setMargins(0, 0, 0, 5);
		if (alAttendeesModels != null) {
			for (int i = 0; i < alAttendeesModels.size(); i++) {
				TextView textView = new TextView(this);
				textView.setText(alAttendeesModels.get(i).getName());
				textView.setPadding(30, 30, 30, 30);
				textView.setTypeface(Typeface.DEFAULT_BOLD);
				textView.setTextSize(16);
				textView.setBackgroundColor(Color.WHITE);
				membersView.addView(textView, params);
			}
		}
	}
}
