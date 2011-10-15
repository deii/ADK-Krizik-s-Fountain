package cz.deii.adk.fountain;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsbAccessoryController implements UsbAccessoryService, Runnable {
	
	private static final String TAG = "UsbAccessoryController";

	private static final String ACTION_USB_PERMISSION = "cz.deii.adk.fountain.action.USB_PERMISSION";

	private UsbManager usbManager;
	private PendingIntent permissionIntent;
	private boolean permissionRequestPending;
	
	private UsbAccessory openedAccessory;
	private ParcelFileDescriptor fileDescriptor;
	private FileInputStream inputStream;
	private FileOutputStream outputStream;
	
	private List<OnAccessoryDetachListener> detachedListeners = new ArrayList<OnAccessoryDetachListener>();
	private List<OnAccessoryOpenListener> openedListeners = new ArrayList<OnAccessoryOpenListener>();
	
	public void onCreate(Activity hostActivity) {
		Log.d(TAG, "onCreate():: [usbManager=" + usbManager + ", openedAccessory=" + openedAccessory + "]");
		usbManager = UsbManager.getInstance(hostActivity);
        permissionIntent = PendingIntent.getBroadcast(hostActivity, 0, new Intent(ACTION_USB_PERMISSION), 0);
        
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		hostActivity.registerReceiver(usbReceiver, filter);
		
		// TODO vyresit deprecated metodu getLastNonConfigurationInstance
		if (hostActivity.getLastNonConfigurationInstance() != null) {
			Log.d(TAG, "onCreate():: loading opennedAccessory from config");
			openedAccessory = (UsbAccessory) hostActivity.getLastNonConfigurationInstance();
			openAccessory(openedAccessory);
		}
	}
	
	public void onResume() {
		if (inputStream != null && outputStream != null) {
			Log.d(TAG, "onResume():: nothing to do - done");
			return;
		}

		UsbAccessory[] accessories = usbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (usbManager.hasPermission(accessory)) {
				Log.d(TAG, "onResume():: usb manager has permission.");
				openAccessory(accessory);
			} else {
				Log.d(TAG, "onResume():: usb manager needs permission");
				synchronized (usbReceiver) {
					if (!permissionRequestPending) {
						usbManager.requestPermission(accessory, permissionIntent);
						permissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "openedAccessory is null");
		}
	}
	
	public void onPause() {
		Log.d(TAG, "onPause():: accessory closed");
		closeAccessory();
	}
	
	public void onDestroy(Activity hostActivity) {
		hostActivity.unregisterReceiver(usbReceiver);
	}

    public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (inputStream!= null && ret >= 0) {
			try {
				ret = inputStream.read(buffer);
			} catch (IOException e) {
				break;
			}

			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				default:
					// Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}
			}

		}
	}

	/**
     * Pripojeni zarizeni (otevreni in/out komunikace).
     * @param accessory usb zarizeni k pripojeni
     */
    private void openAccessory(UsbAccessory accessory) {
		Log.d(TAG, "openAccessory():: start - [accessory=" + accessory + "]");
        fileDescriptor = usbManager.openAccessory(accessory);
        if (fileDescriptor != null) {
			openedAccessory = accessory;
            FileDescriptor fd = fileDescriptor.getFileDescriptor();
            inputStream = new FileInputStream(fd);
            outputStream = new FileOutputStream(fd);
            Thread thread = new Thread(null, this, "AccessoryThread");
            thread.start();
			Log.d(TAG, "accessory opened");
			
			// TODO enable controls
        } else {
			Log.d(TAG, "accessory open fail");
        }
    }
    
    /**
     * Odpojeni zarizeni (uzavreni in/out komunikace).
     */
	private void closeAccessory() {
		Log.d(TAG, "closeAccessory():: start");
		
		// TODO disable controls
		
		try {
			if (fileDescriptor != null) {
				fileDescriptor.close();
				Log.d(TAG, "closeAccessory():: fileDescriptor closed");
			}
		} catch (IOException e) {
		} finally {
			fileDescriptor = null;
			openedAccessory = null;
		}
		
		try {
			if (inputStream != null) {
				inputStream.close();
				Log.d(TAG, "closeAccessory():: inputStream closed");
			}
		} catch (IOException e) {
		} finally {
			inputStream = null;
		}
		
		try {
			if (outputStream != null) {
				outputStream.close();
				Log.d(TAG, "closeAccessory():: outputStream closed");
			}
		} catch (IOException e) {
		} finally {
			outputStream = null;
		}
	}

	public void sendCommand(ArduinoCommand command, ArduinoPin targetPin, int value) {
//    	Log.i(TAG, "Sending command: [" + command.getAddress() + ", " + targetPin.getAddress() + ", " + value + "]");
		
    	byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command.getAddress();
		buffer[1] = targetPin.getAddress();
		buffer[2] = (byte) value;
		if (outputStream != null && buffer[1] != -1) {
			try {
				outputStream.write(buffer);
			} catch (IOException e) {
//				Log.e(TAG, "write failed"/*, e*/);
			}
		}
    }

	public UsbAccessory getOpenedAccessory() {
		return openedAccessory;
	}
	
	public boolean isAccessoryOpened() {
		return openedAccessory != null;
	}
    
    public void addOpenedListener(OnAccessoryOpenListener openedListener) {
		if (openedListener != null)
			this.openedListeners.add(openedListener);
	}
    
    public void addDetachedListener(OnAccessoryDetachListener detachedListener) {
		if (detachedListener != null)
			this.detachedListeners.add(detachedListener);
	}



	/**
	 * To explicitly obtain permission, first create a broadcast receiver. This receiver listens for the intent that
	 * gets broadcast when you call requestPermission(). The call to requestPermission() displays a dialog to the user
	 * asking for permission to connect to the accessory. The following sample code shows how to create the broadcast
	 * receiver:
	 */
	private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "onRecieve():: start");
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				Log.d(TAG, "onRecieve():: ACTION_USB_PERMISSION");
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						// nastaveni komunikace se zarizenim
						openAccessory(accessory);
						notifyOpenedListeners();
					} else {
						Log.d(TAG, "permission denied for accessory " + accessory);
					}
					permissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				Log.d(TAG, "onRecieve():: ACTION_USB_ACCESSORY_DETACHED");
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(openedAccessory)) {
					// ukonceni komunikace se zarizenim
					notifyDetachedListeners();
					closeAccessory();
				}
			}
			
		}

		private void notifyOpenedListeners() {
			for (OnAccessoryOpenListener l : openedListeners) {
				l.accessoryOpeneded();
			}
		}

		private void notifyDetachedListeners() {
			for (OnAccessoryDetachListener l : detachedListeners) {
				l.accessoryDetached();
			}
		}
	};
	
	public static interface OnAccessoryOpenListener {
		public void accessoryOpeneded();
	}
	
	public static interface OnAccessoryDetachListener {
		public void accessoryDetached();
	}
}
