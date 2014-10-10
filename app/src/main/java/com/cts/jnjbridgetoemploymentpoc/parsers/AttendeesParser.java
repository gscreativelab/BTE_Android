package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Attendees;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * * Parser class to parse attendees from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class AttendeesParser {
	private JSONObject jsonObject;
	private Context context;

	public AttendeesParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
	}

	public HashMap<String, ArrayList<Attendees>> getAttendees() {
		HashMap<String, ArrayList<Attendees>> hashMap = new HashMap<String, ArrayList<Attendees>>();

		try {
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Attendees attendeesModel = new Attendees();
				attendeesModel.setName(jsonObject.getString("name"));
				attendeesModel.setRsvp_status(jsonObject
						.getString("rsvp_status"));
				attendeesModel.setId(jsonObject.getString("id"));

				ArrayList<Attendees> alArrayList = hashMap.get(jsonObject
						.getString("rsvp_status"));

				if (alArrayList == null) {
					alArrayList = new ArrayList<Attendees>();
					alArrayList.add(attendeesModel);
					hashMap.put(jsonObject.getString("rsvp_status"),
							alArrayList);
				} else {
					alArrayList.add(attendeesModel);
				}
			}

		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_ATTENDEESPARSER, "JSONException-->" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		}

		return hashMap;
	}
}
