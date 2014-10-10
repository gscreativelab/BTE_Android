package com.cts.jnjbridgetoemploymentpoc.ui.activity;

import com.cts.jnjbridgetoemploymentpoc.exception.DefaultExceptionHandler;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

/**
 * 
 * This is the Base class for all the activities It will have the code that are
 * used in most or all the activities
 * 
 * @author neerajareddy
 * 
 */
public class BaseActivity extends FragmentActivity {

	private ProgressDialog dialog_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DefaultExceptionHandler.exceptionHandler == null) {
			Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
					getApplicationContext(), Thread
							.getDefaultUncaughtExceptionHandler()));
		}
	}

	/**
	 * Shows progress Dialog
	 * 
	 * @param message
	 */

	public void showProgressDialog(String message) {
		dialog_ = ProgressDialog.show(this, "", message);
	}

	/**
	 * 
	 * Hides progress Dialog if already shown
	 */
	public void hideProgressDialog() {
		if (dialog_ != null && dialog_.isShowing()) {
			dialog_.dismiss();
		}
	}

	/**
	 * To show Alert message
	 * 
	 * @param message
	 */

	public void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNeutralButton(getResources().getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
