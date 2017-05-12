package com.example.garbagecollection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

public class MainScreen extends Activity {

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	public static Boolean isFirstTime = true;
	Boolean exit = false;
	//String strDeviceName, strManufacturer, buildVersion;
	String strFinalUrl;
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		/*buildVersion = Build.VERSION.RELEASE.toString();
		strManufacturer = android.os.Build.MANUFACTURER;
		strDeviceName = android.os.Build.MODEL;
		Log.i("", "buildVersion: "+buildVersion);
		Log.i("", "strManufacturer: "+strManufacturer);
		Log.i("", "strDeviceName: "+strDeviceName);*/
	}

	 /** To scan QR Code **/ 
	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
			exit = true;
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}

	/** CHECK CAMERA AVAILABLITY **/
	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	/** CALLED AFTER SCANNING THE QR CODE **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:

			if (resultCode == RESULT_OK) {
				String string = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				
				//String string= "H4sIAAAAAAAAAAt0dY03AAB1hzACBQAAAA=="; QEE
				//String string= "H4sIAAAAAAAAAAsuLUgtKssszi+KDyxNzCspzQUAoUe3BRIAAAA="; Supervisor
				byte[] datadecode;
				try {
					datadecode = Base64.decode(string);
					String strUrl = new String(datadecode, "UTF-8");
					System.out.println("String"+strUrl);
				
					if(strUrl.contains("Supervisor_Quantum")){
						Intent ite= new Intent(MainScreen.this, HomeActivity.class);
						ite.putExtra("super", ""+strUrl);
						startActivity(ite);
					}else if(strUrl.contains("QEE")){
						Intent ite= new Intent(MainScreen.this, HospitalOne.class);
						strUrl = strUrl.replace("_", "-");
						ite.putExtra("hospital_id", ""+strUrl);
						startActivity(ite);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/** UPDATING THE SHARED PREFERENCES VALUE **//*
				SharedPreferences preferences = getSharedPreferences("IDvalue", 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("result", "result");
				editor.commit();
			
				if (strUrl.charAt(strUrl.length()-1)=='?') {
					strUrl = strUrl.substring(0, strUrl.length()-1);
				}

				if(isValidUrl(strUrl)) {
					
					editor.putString("url", "url");
					editor.commit();
					 *//** CHECKING THE URL
					  *  ADDING THE ANDROID VERSION, MANUFACTURER NAME, DEVICE NAME TO THE URL
					  *  OPENING THE URL IN WEB VIEW **//*
					if(strUrl.contains("local.intelcomply.com") || strUrl.contains("itsv.co")) {
						// strNew=[NSString stringWithFormat:@"%@?src=IOS_%@_%@",hiddenData,iOSVersion,deviceNme];
						strFinalUrl = strUrl.concat("?src=AND_"+buildVersion+"_"+strManufacturer+"_"+strDeviceName); 
						strUrl = "";
						Intent intent = new Intent(getApplicationContext(),Webclass.class);
						intent.putExtra("URL", strFinalUrl);
						startActivity(intent);
					}else {
						*//** OPENING URL'S IN WEB BROWSER **//*
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(strUrl));
						startActivity(i);
					}
				}else {
					launchQRScanner(getCurrentFocus());
				}*/
				onBackPressed();

			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
				//	Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
			}else{
				onBackPressed();
			}
			break;
		}
	}

	/** This is used to check the given URL is valid or not **/
	private boolean isValidUrl(String url) {
		Pattern p = Patterns.WEB_URL;
		Matcher m = p.matcher(url);
		if(m.matches())
			return true;
		else
			return false;
	}


	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("IDvalue", 0);
		//	preferences = getSharedPreferences("IDvalue", 0);
		String strResult = prefs.getString("result", "");
		String strUrl = prefs.getString("url", "");
		
		/*if(strResult.equals("result")){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("result", "");
			editor.commit();
			if(!exit){
				launchQRScanner(getCurrentFocus());
			}else{
				if(strUrl.equals("url")){
					editor.putString("url", "");
					editor.commit();
				}
			}
		}else{*/
			if(!exit){
				launchQRScanner(getCurrentFocus());
			}else{
				launchQRScanner(getCurrentFocus());
			}
		//}
		
	}

	/** EXITING THE APPLICATION **/
	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}
}
