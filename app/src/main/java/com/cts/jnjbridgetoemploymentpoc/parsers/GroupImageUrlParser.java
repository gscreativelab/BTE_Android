package com.cts.jnjbridgetoemploymentpoc.parsers;

import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

/**
 * 
 * Parser class to parse GroupImageUrl from JSONObject retrieved as response
 * 
 * @author neerajareddy
 * 
 */
public class GroupImageUrlParser {

	private JSONObject jsonObject = null;
	private Context context;

	public GroupImageUrlParser(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.context = context;
	}

	public URL getGroupImageUrlParser() {
		URL imageURL = null;

		try {
			imageURL = new URL(jsonObject.get("source").toString());

		} catch (IOException e) {
			Log.d(Constants.LOG_TAG_GROUPIMG, "IOException:" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new IOException(e.getMessage()));
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG_GROUPIMG, "JSONException:" + e);
			ExceptionHandler.makeExceptionAlert(context,
					new JSONException(e.getMessage()));
		}

		return imageURL;
	}
}
