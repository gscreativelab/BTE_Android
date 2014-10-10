package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * Parser class to parse Events from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class EventParser {

	private JSONObject jsonObject;
	private Context context;
	private SimpleDateFormat simpleDateFormat, simpleDateFormatReq,simpleDateFormat_timezone;
	private Calendar calendar_current;
	private Calendar calendar_event;
	public EventParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
		simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_PATTERN);
		simpleDateFormat_timezone= new SimpleDateFormat(Constants.DATE_PATTERN_TIMEZONE);
		simpleDateFormatReq = new SimpleDateFormat(Constants.DATE_PATTERN);
		calendar_current = Calendar.getInstance();
		calendar_event = Calendar.getInstance();
		calendar_current.add(Calendar.DAY_OF_MONTH, -14);
	}

	public ArrayList<Events> getEvents() {
		ArrayList<Events> alEventsModels = new ArrayList<Events>();
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("data");

			for (int i = 0; i < jsonArray.length(); i++) {
				Events eventsModel = new Events();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				eventsModel.setId(jsonObject.getString("id"));
				eventsModel.setName(jsonObject.getString("name"));

				String sTime = jsonObject.getString("start_time");
					eventsModel.setStart_time(sTime);;
				if (jsonObject.has("location")) {
					eventsModel.setLocation(jsonObject.getString("location"));
				} else {
					eventsModel.setLocation(context
							.getString(R.string.not_specified));
				}

				if (jsonObject.has("timezone")) {
					eventsModel.setTimezone(jsonObject.getString("timezone"));
				} else {
					eventsModel.setTimezone(context
							.getString(R.string.not_specified));
				}

				// timezone
				

				// check if the event is future or in past two weeks.
				// then only add it to list
				if (isEventfromPastweek(eventsModel.getStart_time(), context)) {
					alEventsModels.add(eventsModel);
				}

			}
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_EVENTSPARSER, "ex-->" + e.getMessage());
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		} catch (Exception e) {
			Log.d(Constants.LOG_TAG_EVENTSPARSER,
					"exception-->" + e.getMessage());
			ExceptionHandler.makeExceptionAlert(context,
					new Exception(e.getMessage()));
		}
		return alEventsModels;
	}

	public boolean isEventfromPastweek(String mDate, Context context) {

		// check if the event is future or in past two weeks.
		// then only add it to list
		SimpleDateFormat simpleDateFormat;
		if(mDate.contains("T"))
		{
			simpleDateFormat = new SimpleDateFormat(
					Constants.DATE_PATTERN_TIMEZONE);
		}
		else
		{
			simpleDateFormat = new SimpleDateFormat(
					Constants.SIMPLE_DATE_PATTERN);
		}
		

		try {
			Date date = simpleDateFormat.parse(mDate);
			calendar_event.setTime(date);

			if (calendar_event.after(calendar_current))
				return true;
		} catch (ParseException e) {
			Log.d(Constants.LOG_TAG_EVENTS, "ParseException -->" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new org.apache.http.ParseException("ParseException"));
		}
		return false;
	}
}
