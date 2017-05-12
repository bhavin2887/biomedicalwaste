package com.example.garbagecollection;

import java.util.List;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ZBarScannerActivity extends Activity implements Camera.PreviewCallback, ZBarConstants {

	//private static final String TAG = "ZBarScannerActivity";
	private CameraPreview mPreview;
	private Camera mCamera;
	private ImageScanner mScanner;
	private Handler mAutoFocusHandler;
	private boolean mPreviewing = true;
	Bitmap bitImg;
	ImageView btnSetting, btnHelp;
	Boolean isFlashOn, isSoundOn;
	//MediaPlayer mp;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(!isCameraAvailable()) {
			// Cancel request if there is no rear-facing camera.
			cancelRequest();
			return;
		}
		
		/** LOADING THE SOUND FROM RAW FILE **/
		//TODO
		//mp = MediaPlayer.create(this, R.raw.beep);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera_prev);

		/** SETTING UP THE WIDZETS **/
		btnHelp = (ImageView) findViewById(R.id.btnHelp);
		btnSetting = (ImageView) findViewById(R.id.btnSetting);

		mAutoFocusHandler = new Handler();

		// Create and configure the ImageScanner;
		setupScanner();

		/** Create a RelativeLayout container that will hold a SurfaceView,
		 *  and set it as the content of our activity.  **/
		mPreview = new CameraPreview(this, this, autoFocusCB);
		LinearLayout zbarLayout = (LinearLayout) findViewById(R.id.zbar_layout_area);
		mPreview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		zbarLayout.addView(mPreview);

		/** TOUCH EVENT ON THE SETTINGS BUTTON 
		 *  SWITCHING OFF THE FLASH BEFORE NAVIGATING TO ANOTHER SCREEN **/
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent intSetting = new Intent(ZBarScannerActivity.this,Settings.class);
				setTorch(false);
				startActivity(intSetting);*/
			}
		});

		/** TOUCH EVENT ON THE HELP BUTTON 
		 *  SWITCHING OFF THE FLASH BEFORE NAVIGATING TO ANOTHER SCREEN **/
		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent inthelp = new Intent(ZBarScannerActivity.this,Help.class);
				setTorch(false);
				startActivity(inthelp);*/
			}
		});
	}

	public void setupScanner() {
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);

		int[] symbols = new int[]{Symbol.QRCODE};
		if (symbols != null) {
			mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
			for (int symbol : symbols) {
				mScanner.setConfig(symbol, Config.ENABLE, 1);
			}
		}
	} 

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		SharedPreferences prefs = getSharedPreferences("IDvalue", 0); 
		isFlashOn = prefs.getBoolean("Flash", false);
		isSoundOn = prefs.getBoolean("Sound", true);

		if (mCamera == null){
			mCamera = Camera.open();
		}
		
		if(mCamera == null) {
			// Cancel request if mCamera is null.
			cancelRequest();
			return;
		}

		
		mPreview.setCamera(mCamera);
		mPreview.showSurfaceView();
		mPreviewing = true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();

			/** According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
			 *  there might be surface recreation problems when the device goes to sleep. So lets just hide it and
			 *  recreate on resume  **/
			mPreview.hideSurfaceView();
			mPreviewing = false;
			mCamera = null;
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public void cancelRequest() {
		Intent dataIntent = new Intent();
		dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
		setResult(Activity.RESULT_CANCELED, dataIntent);
		finish();
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		Camera.Size size = parameters.getPreviewSize();

		Image barcode = new Image(size.width, size.height, "Y800");
		barcode.setData(data);

		int result = mScanner.scanImage(barcode);

		if (result != 0) {
			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mPreviewing = false;
			SymbolSet syms = mScanner.getResults();
			for (Symbol sym : syms) {
				String symData = sym.getData();
				if (!TextUtils.isEmpty(symData)) {
					Intent dataIntent = new Intent();
					dataIntent.putExtra(SCAN_RESULT, symData);
					dataIntent.putExtra(SCAN_RESULT_TYPE, sym.getType());
					setResult(Activity.RESULT_OK, dataIntent);
					if(isSoundOn)
					{
						/** PLAY SOUND **/
						//TODO
						//mp.start();
					}
					finish();
					break;
				}
			}
		}
	}
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if(mCamera != null && mPreviewing) {
				mCamera.autoFocus(autoFocusCB);
			}
		}
	};

	// Mimic continuous auto-focusing
	Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
	
	public void setTorch(boolean enabled) {
		if (mCamera == null){
			mCamera = Camera.open();
		}
		try {
			Parameters cp = mCamera.getParameters();
			List<String> flashModes = cp.getSupportedFlashModes();
			if (flashModes != null && flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
				if (enabled)
					cp.setFlashMode(Parameters.FLASH_MODE_TORCH);
				else
					cp.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(cp);
			}
		} catch (Exception e) {
		}
	}
}
