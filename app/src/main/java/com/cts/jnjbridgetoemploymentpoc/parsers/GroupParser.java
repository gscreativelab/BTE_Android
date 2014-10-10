package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Groups;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * Parser class to parse Group from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class GroupParser {

	private JSONObject jsonObject;
	private Context context;
	private JSONArray jArray = null;

	public GroupParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
	}

	public ArrayList<Groups> getGroups() {

		ArrayList<Groups> alGroupModels = new ArrayList<Groups>();
		try {

			jArray = jsonObject.getJSONArray("data");

			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject element = null;
					element = jArray.getJSONObject(i);

					Groups groupModel = new Groups();
					groupModel.setId(element.getString("id"));
					groupModel.setName(element.getString("name").toString());
					alGroupModels.add(groupModel);

				}
			}
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_GROUPPARSER, "JSON EXCEPTION:" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));

		}
		return alGroupModels;
	}

}
