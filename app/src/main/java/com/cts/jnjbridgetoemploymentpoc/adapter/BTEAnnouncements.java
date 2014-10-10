package com.cts.jnjbridgetoemploymentpoc.adapter;

import java.util.ArrayList;

import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.model.Announcements;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.BTEDetailedAnnouncementActivity;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * This class is an extends ArrayAdapter to build Announcements
 * 
 * @author neerajareddy
 * 
 */
public class BTEAnnouncements extends ArrayAdapter<Announcements> {

	private Context context = null;
	private ArrayList<Announcements> itemsArrayList = null;

	public BTEAnnouncements(Context context) {
		super(context, R.layout.announcement_list_rowlayout);
		this.context = context;
	}

	public BTEAnnouncements(Context context,
			ArrayList<Announcements> itemsArrayList) {

		super(context, R.layout.announcement_list_rowlayout, itemsArrayList);
		this.context = context;
		this.itemsArrayList = itemsArrayList;
	}

	@Override
	public int getCount() {
		if (itemsArrayList != null && itemsArrayList.size() > 0)
			if (itemsArrayList.size() > Constants.ANNOUNCEMENTS_COUNT)
				return Constants.ANNOUNCEMENTS_COUNT;
			else
				return itemsArrayList.size();

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.announcement_list_rowlayout,
				parent, false);
		// LinearLayout layout = (LinearLayout) findViewById(R.id.showMore);

		TextView headingView = (TextView) rowView.findViewById(R.id.Heading);
		TextView dateView = (TextView) rowView.findViewById(R.id.date);
		TextView descView = (TextView) rowView.findViewById(R.id.description);
		Button showMore = (Button) rowView.findViewById(R.id.button1);
		showMore.setBackgroundColor(Color.WHITE);
		showMore.setTextColor(Color.BLUE);
		if (itemsArrayList.get(position).getDescription() != null
				&& itemsArrayList.get(position).getDescription().length() < 70) {
			showMore.setText("");
			descView.setText(itemsArrayList.get(position).getDescription());
		} else {
			if (itemsArrayList.get(position).getDescription() != null) {
				showMore.setVisibility(View.VISIBLE);
				showMore.setText(context.getResources().getString(
						R.string.show_more));
				descView.setText(itemsArrayList.get(position).getDescription()
						.substring(0, 60)
						+ context.getResources().getString(R.string.large_desc));
			}

			showMore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							BTEDetailedAnnouncementActivity.class);
					intent.putExtra("Name", itemsArrayList.get(position)
							.getName());
					intent.putExtra("Desc", itemsArrayList.get(position)
							.getDescription());
					context.startActivity(intent);
				}
			});
		}
		headingView.setText(itemsArrayList.get(position).getName());
		dateView.setText(itemsArrayList.get(position).getDate());

		return rowView;
	}

	public void refresh(ArrayList<Announcements> itemsArrayList) {
		this.itemsArrayList = itemsArrayList;
		this.notifyDataSetChanged();
	}
}
