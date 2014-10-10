package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Announcements;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * Parser class to parse feeds and get Announcements from JSONObject retrieved
 * as response
 */
public class FeedParser {

	private JSONObject jsonObject;
	private Context context;
	private SimpleDateFormat simpleDateFormat, mSimpleDateFormat;
	private SimpleDateFormat simpleDateFormatUpdate;

	public FeedParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		simpleDateFormat = new SimpleDateFormat(
				Constants.SIMPLE_DATE_TIME_PATTERN);

		mSimpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_PATTERN);
		simpleDateFormatUpdate = new SimpleDateFormat(
				Constants.SIMPLE_DAY_PATTERN);
	}

	@SuppressLint("NewApi")
	public ArrayList<Announcements> getAnouncements() {
		ArrayList<Announcements> announcementModels = new ArrayList<Announcements>();
		try {
			JSONArray array = jsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Announcements announcementModel = new Announcements();
				JSONObject innerJsonObject = jsonObject.getJSONObject("from");
				announcementModel.setName(innerJsonObject.getString("name"));
				announcementModel.setDate(jsonObject.getString("created_time"));

				try {
					Date date = simpleDateFormat.parse(jsonObject
							.getString("created_time"));

					String mDate = simpleDateFormatUpdate.format(date)
							.toString();
					announcementModel.setDate(mDate);
				} catch (ParseException e) {
					Log.d(Constants.LOG_TAG_FEEDPARSER, "Exception-->" + e);
				}

				if (jsonObject.has("type")
						& jsonObject.get("type").toString()
								.equalsIgnoreCase("status")) {
					if (jsonObject.has("message"))
						announcementModel.setDescription(jsonObject
								.getString("message"));
					if (!announcementModel.getDescription().isEmpty())
						announcementModels.add(announcementModel);
				}

			}
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_FEEDPARSER, "Exception-->" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		}

		return announcementModels;
	}

	public ArrayList<Events> getEventDetails(String organizedBy) {
		ArrayList<Events> alArrayList = new ArrayList<Events>();
		try {
			JSONArray array = jsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Events eventsModel = new Events();

				eventsModel.setId(jsonObject.getString("id"));
				eventsModel.setOrganized_by(organizedBy);
				if (jsonObject.has("from")) {
					// save from
					JSONObject jsonObjectFrom = jsonObject
							.getJSONObject("from");
					eventsModel.setFrom(jsonObjectFrom.getString("name"));
				}
				if (jsonObject.has("created_time")) {
					// save created_time here

					String sDate = jsonObject.getString("created_time")
							.substring(
									0,
									jsonObject.getString("created_time")
											.indexOf("T"));

					Date date;
					try {
						date = mSimpleDateFormat.parse(sDate);
						String mDate = simpleDateFormatUpdate.format(date)
								.toString();

						eventsModel.setCreated_time(mDate);
					} catch (ParseException e) {
						Log.d(Constants.LOG_TAG_FEEDPARSER, "parseException-->"
								+ e);
						ExceptionHandler.makeExceptionAlert(
								context,
								new org.apache.http.ParseException(e
										.getMessage()));
					}

				}
				if (jsonObject.has("message")) {
					String message = jsonObject.getString("message");
					eventsModel.setMessage(message);
					alArrayList.add(eventsModel);
				}

			}

			return alArrayList;

		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_FEEDPARSER, "Exception-->" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		}
		return alArrayList;
	}

}
