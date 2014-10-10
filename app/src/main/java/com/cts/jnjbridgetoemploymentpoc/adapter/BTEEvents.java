package com.cts.jnjbridgetoemploymentpoc.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.BTEEventDetails;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.webservices.EventsAsyncLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class extends ArrayAdapter to build layout for Events screen.
 * 
 * @author neerajareddy
 * 
 */
public class BTEEvents extends ArrayAdapter<Events> {
	private Context context = null;
	private ArrayList<Events> itemsArrayList = null;
	private SimpleDateFormat simpleDateFormat, simpleDateFormatReq;
	public BTEEvents(Context context, ArrayList<Events> itemsArrayList) {

		super(context, R.layout.event_list_rowview, itemsArrayList);
		this.context = context;
		this.itemsArrayList = itemsArrayList;
		
		simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_PATTERN);
		simpleDateFormatReq = new SimpleDateFormat(Constants.DATE_PATTERN);
	}

	@Override
	public int getCount() {
		if (itemsArrayList != null && itemsArrayList.size() > 0)
			return itemsArrayList.size();
		else
			return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.event_list_rowview, parent,
				false);
		ImageView eventIcon = (ImageView) rowView.findViewById(R.id.EventIcon);
		TextView headingView = (TextView) rowView.findViewById(R.id.EventName);
		TextView dateView = (TextView) rowView.findViewById(R.id.EventDate);
		headingView.setText(itemsArrayList.get(position).getName());
		dateView.setText(getUpdatedTime(itemsArrayList.get(position).getStart_time()));
		setEventImage(itemsArrayList.get(position).getId(), eventIcon);
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, BTEEventDetails.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("obj", itemsArrayList.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return rowView;
	}

	public void refresh(ArrayList<Events> itemsArrayList) {
		this.itemsArrayList = itemsArrayList;
		this.notifyDataSetChanged();
	}

	public void setEventImage(String eventID, ImageView eventImageView) {
		EventsAsyncLoader eventsAsyncLoader = new EventsAsyncLoader();
		eventsAsyncLoader.setEventImage(eventID, eventImageView, context,
				Constants.STATE_EVENTDETAILS_IMAGE);
	}
	
	public String getUpdatedTime(String sTime)
	{
		String updatedDate = sTime;
		if (sTime.contains("T"))
			sTime = sTime.substring(0, sTime.indexOf("T"));

		try {
			Date date = simpleDateFormat.parse(sTime);
			updatedDate = simpleDateFormatReq.format(date);

		} catch (ParseException e) {
			Log.d(Constants.LOG_TAG_EVENTSPARSER,
					"parsing ex-->" + e.getMessage());
			ExceptionHandler.makeExceptionAlert(context,
					new org.apache.http.ParseException(e.getMessage()));
		}
		return updatedDate;
		
	}

}
