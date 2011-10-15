package cz.deii.adk.fountain;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Pouze spusti hlavni aktivitu.
 * Aktivita by se mela startovat na akci USB_ACCESSORY_ATTACHED.
 */
public class UsbAccessoryActivity extends Activity {

	private static final String TAG = "UsbAccessoryActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate():: start");
		
		Intent intent = new Intent(this, AdkMusicFountainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "unable to start AdkFountainDebugActivity activity", e);
		}
		finish();
	}

}
