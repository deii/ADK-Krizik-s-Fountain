package cz.deii.adk.fountain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class AdkMusicFountainActivity extends Activity {

	private static final String TAG = "AdkMusicFountain";
	
	private UsbAccessoryController usb;
	private FountainController fountain;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Quit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "Quit") {
			finish();
			System.exit(0);
		}
		return true;
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate():: start");
		
		this.usb = new UsbAccessoryController();
		usb.onCreate(this);
		
		this.fountain = new FountainController(this, usb);
        
        setContentView(R.layout.main);
    }

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetainNonConfigurationInstance():: start");
		if (usb.isAccessoryOpened()) {
			return usb.getOpenedAccessory();
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume():: start");
		
		usb.onResume();
		fountain.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause():: start");
		
		usb.onPause();
		fountain.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy():: start");
		
		usb.onDestroy(this);
		fountain.onDestroy();
		
		super.onDestroy();
	}

}