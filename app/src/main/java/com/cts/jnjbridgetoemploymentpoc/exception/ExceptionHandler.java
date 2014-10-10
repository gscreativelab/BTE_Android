package com.cts.jnjbridgetoemploymentpoc.exception;

import com.cts.jnjbridgetoemploymentpoc.ui.activity.BaseActivity;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * This class handles all the exceptions and shows relevant error message.
 * 
 * @author neerajareddy
 * 
 */
public class ExceptionHandler {

	public static void makeExceptionAlert(Context context, Throwable ex) {
		String headerText = "";
		String messageText = "";
		((BaseActivity) context).hideProgressDialog();
		if (ex instanceof NullPointerException
				|| ex instanceof RuntimeException || ex instanceof Exception) {
			Log.d("print message", "" + ex.getMessage());
			if (ex.getMessage().equalsIgnoreCase("200")
					|| ex.getMessage().equalsIgnoreCase("299"))
				messageText = context
						.getText(R.string.error_message_permission).toString();
			else
				messageText = context.getText(R.string.error_message)
						.toString();
			headerText = context.getText(R.string.error).toString();

		}

		showErrorDialog(context, headerText, messageText);
	}

	/**
	 * 
	 * Method to show error Dialog
	 * 
	 * @param context
	 * @param titletext
	 * @param messagetext
	 */

	private static void showErrorDialog(Context context, String titletext,
			String messagetext) {
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(titletext)
				.setMessage(messagetext)
				.setPositiveButton(context.getText(R.string.close).toString(),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}

						}).show();
	}

}
