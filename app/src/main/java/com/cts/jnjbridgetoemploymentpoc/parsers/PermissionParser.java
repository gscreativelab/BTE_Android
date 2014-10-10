package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Permissions;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * Parser class to parse Group from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class PermissionParser {

	private JSONObject jsonObject;
	private Context context;
	private JSONArray jArray = null;

	public PermissionParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
	}

	public ArrayList<Permissions> getPermissions() {

		ArrayList<Permissions> alPermissionsModels = new ArrayList<Permissions>();
		try {

			jArray = jsonObject.getJSONArray("data");

			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject element = null;
					element = jArray.getJSONObject(i);

					Permissions permissionModel = new Permissions();
					permissionModel.setPermission(element
							.getString("permission"));
					permissionModel.setStatus(element.getString("status"));
					alPermissionsModels.add(permissionModel);

				}
			}
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_PERMMISSIONPARSER, "JSON EXCEPTION:" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));

		}
		return alPermissionsModels;
	}

}
