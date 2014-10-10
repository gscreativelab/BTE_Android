package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.model.Members;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * 
 * Parser class to parse Members from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class MembersListParser {
	private JSONObject jsonObject;
	private Context context;

	public MembersListParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
	}

	public ArrayList<Members> getMembersList() {
		ArrayList<Members> alMembersModels = new ArrayList<Members>();
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("data");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Members membersModel = new Members();
				membersModel.setId(jsonObject.getString("id"));
				membersModel.setName(jsonObject.getString("name"));
				membersModel.setAdministrator(jsonObject
						.getString("administrator"));
				alMembersModels.add(membersModel);
			}
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_MEMBERS_lISTPARSER, "Exception-->" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		}

		return alMembersModels;
	}

}
