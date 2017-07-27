package com.example.garbagecollection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalOne extends Activity{

	EditText edit_yellow_kg,edit_red_kg,edit_blue_kg,edit_black_kg; 
	EditText edit_yellow_bag,edit_red_bag,edit_blue_bag,edit_black_bag,edit_hospital_name;
	TextView text_hospital_name,text_isactive,text_qee_name;
	Button btn_ok;
	Float val_kg;
	int val_bag;
	int val_bag_given;
	String hospital_id;
	String email_id;
	private Pattern pattern;
	private Matcher matcher;
	boolean flagTop = false;
	EditText edit_yellow_return,edit_red_return,edit_blue_return,edit_black_return; 
	TextView text_total_bag_return;
	String currentDateandTime;
	String total_YellowWeight,total_RedWeight,total_BlueWeight,total_BlackWeight,total_YellowBag,total_RedBag,total_BlueBag,total_BlackBag;
	public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences userDetails;
	String valBase64;
	Bitmap bitm;
	LocationManager manager;
	boolean isGPS;
    GPSTracker gps;
	double lat=0;
	double lon=0;
	String isOpenOrClose; 
	String hospitalCloseString;
	//String total_weight;
	/** Database object for saving data of device details into Database */
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	Bundle bd;
	TextView one,two,three;
	Button btn_camera;
	boolean camera_flag = false;
	
	@Override
	protected void onResume() {
		super.onResume();
		setTitle("Bags Collection");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hospitel_one);
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		one = (TextView)findViewById(R.id.text_enter_bags);
		two = (TextView)findViewById(R.id.text_enter_kg);
		three = (TextView)findViewById(R.id.text_enter_bags_given);
		btn_camera = (Button)findViewById(R.id.btn_camera);
		Typeface font = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
		one.setTypeface(font);
		one.setTypeface(one.getTypeface(), Typeface.BOLD);
		one.setText("ભરેલી બેગ લેવાની નંગ ");
		two.setTypeface(font);
		two.setTypeface(two.getTypeface(), Typeface.BOLD);
		two.setText("લીધેલી બેગ વજન kg");
		three.setTypeface(font);
		three.setTypeface(three.getTypeface(), Typeface.BOLD);
		three.setText("ખાલી બેગ આપવાની નંગ");
		
		edit_yellow_kg = (EditText)findViewById(R.id.edit_kg_1);
		edit_red_kg = (EditText)findViewById(R.id.edit_kg_2);
		edit_blue_kg = (EditText)findViewById(R.id.edit_kg_3);
		edit_black_kg = (EditText)findViewById(R.id.edit_kg_4);
        userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

		edit_yellow_bag = (EditText)findViewById(R.id.edit_bag1);
		edit_red_bag = (EditText)findViewById(R.id.edit_bag2);
		edit_blue_bag = (EditText)findViewById(R.id.edit_bag3);
		edit_black_bag = (EditText)findViewById(R.id.edit_bag4);
		
		edit_yellow_return = (EditText)findViewById(R.id.edit_bag1_return);
		edit_red_return = (EditText)findViewById(R.id.edit_bag2_return);
		edit_blue_return = (EditText)findViewById(R.id.edit_bag3_return);
		edit_black_return = (EditText)findViewById(R.id.edit_bag4_return);
		edit_hospital_name = (EditText)findViewById(R.id.edit_hospital_name);
		
		text_qee_name = (TextView)findViewById(R.id.text_qee_name);
		text_hospital_name = (TextView)findViewById(R.id.text_hospital_name);
		text_isactive = (TextView)findViewById(R.id.text_isactive);
		
		btn_ok = (Button)findViewById(R.id.btn_ok);
        bd = getIntent().getExtras();
        //Log.i("","aaaaa"+bd);

        if(bd!=null){
        	isOpenOrClose = "open";
        	flagTop = true;
        	hospital_id = bd.getString("hospital_id");
        	text_hospital_name.setText(hospital_id);
        	edit_hospital_name.setVisibility(View.GONE);
        	text_hospital_name.setVisibility(View.VISIBLE);
        	text_isactive.setVisibility(View.VISIBLE);
    		btn_camera.setVisibility(View.GONE);
    		text_qee_name.setVisibility(View.GONE);
    		edit_yellow_kg.setSelection(edit_yellow_kg.getText().length());
    		
        	if(isConnectingToInternet()){
        		new WebserviceCheckHospital().execute(hospital_id);
        	}else{
        		Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        	}
        }else{
        	isOpenOrClose = "close";
        	edit_hospital_name.setSelection(edit_hospital_name.getText().length());
    		btn_camera.setVisibility(View.VISIBLE);
        	edit_hospital_name.setVisibility(View.VISIBLE);
        	text_hospital_name.setVisibility(View.GONE);
        	text_isactive.setVisibility(View.GONE);
    		text_qee_name.setVisibility(View.VISIBLE);
        }
        
        btn_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(HomeActivity.path.toString()+"/DP.png")));
          	    startActivityForResult(intent, 1337);
			}
		});
        
       
        edit_hospital_name.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
            	if(isConnectingToInternet()){
	                if(!hasFocus){
	                	if(edit_hospital_name.getText().toString().matches(".*\\d+.*")){
	                		flagTop = true;
	                		new WebserviceCheckHospital().execute(edit_hospital_name.getText().toString());
	                		hospital_id = edit_hospital_name.getText().toString();
	                	}
	                }
            	}else{
            		if(!hasFocus){
	                	if(edit_hospital_name.getText().toString().matches(".*\\d+.*")){
	                		flagTop = true;
	                		hospital_id = edit_hospital_name.getText().toString();
	                	}
	                }
            		Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
            	}
            }
        });
		
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpenOrClose.equalsIgnoreCase("open") || isOpenOrClose.equalsIgnoreCase("close") && camera_flag == true){
					
					val_kg = (float) 0.0;
					val_bag = 0;
					val_bag_given = 0;
					
					if(edit_yellow_kg.getText().toString().length()>=1)
						val_kg=val_kg+Float.parseFloat(edit_yellow_kg.getText().toString());
					if(edit_red_kg.getText().toString().length()>=1)
						val_kg=val_kg+Float.parseFloat(edit_red_kg.getText().toString());
					if(edit_blue_kg.getText().toString().length()>=1)
						val_kg=val_kg+Float.parseFloat(edit_blue_kg.getText().toString());
					if(edit_black_kg.getText().toString().length()>=1)
						val_kg=val_kg+Float.parseFloat(edit_black_kg.getText().toString());
					
					if(edit_yellow_bag.getText().toString().length()>=1)
						val_bag=val_bag+Integer.parseInt(edit_yellow_bag.getText().toString());
					if(edit_red_bag.getText().toString().length()>=1)
						val_bag=val_bag+Integer.parseInt(edit_red_bag.getText().toString());
					if(edit_blue_bag.getText().toString().length()>=1)
						val_bag=val_bag+Integer.parseInt(edit_blue_bag.getText().toString());
					if(edit_black_bag.getText().toString().length()>=1)
						val_bag=val_bag+Integer.parseInt(edit_black_bag.getText().toString());
						        
	//================================================================================================================================================================

					//final int val_bag_given = 0;
					
					if(edit_yellow_return.getText().toString().length()>=1)
						val_bag_given=val_bag_given+Integer.parseInt(edit_yellow_return.getText().toString());
					if(edit_red_return.getText().toString().length()>=1)
						val_bag_given=val_bag_given+Integer.parseInt(edit_red_return.getText().toString());
					if(edit_blue_return.getText().toString().length()>=1)
						val_bag_given=val_bag_given+Integer.parseInt(edit_blue_return.getText().toString());
					if(edit_black_return.getText().toString().length()>=1)
						val_bag_given=val_bag_given+Integer.parseInt(edit_black_return.getText().toString());
					
			        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalOne.this);
			        
			        // Setting Dialog Title
			        alertDialog.setTitle("Confirm Submittion...");
			 
			        // Setting Dialog Message
			        alertDialog.setMessage("Total Weight is ="+val_kg +" Kg \n"+"Total Bags is ="+val_bag +"\n"+ "Total Bags Returned is ="+val_bag_given);

			        // Setting Icon to Dialog
			        alertDialog.setIcon(R.drawable.help);
			 
			        // Setting Positive "Yes" Button
			        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            // Write your code here to invoke YES event
			            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			            	currentDateandTime = sdf.format(new Date());
		            		
							 total_BlueWeight = edit_blue_kg.getText().toString();
							 total_YellowWeight = edit_yellow_kg.getText().toString();
							 total_BlackWeight = edit_black_kg.getText().toString();
							 total_RedWeight = edit_red_kg.getText().toString();
							 
							 total_BlueBag = edit_blue_bag.getText().toString();
							 total_YellowBag = edit_yellow_bag.getText().toString();
							 total_BlackBag = edit_black_bag.getText().toString();
							 total_RedBag = edit_red_bag.getText().toString();

							 String val = null;
				            	
		            		if(edit_yellow_kg.getText().toString().length()==0 && edit_yellow_bag.getText().toString().length() >=1 || edit_yellow_kg.getText().toString().length() >=1 && edit_yellow_bag.getText().toString().length() ==0){
				            	val = "Enter proper data in editext.";
				            }
		            		if(edit_black_kg.getText().toString().length()==0 && edit_black_bag.getText().toString().length() >=1 || edit_black_kg.getText().toString().length() >=1 && edit_black_bag.getText().toString().length() ==0){
				            	val = "Enter proper data in editext.";
				            }
		            		if(edit_blue_kg.getText().toString().length()==0 && edit_blue_bag.getText().toString().length() >=1 || edit_blue_kg.getText().toString().length() >=1 && edit_blue_bag.getText().toString().length() ==0){
					        	val = "Enter proper data in editext.";
				            }
		            		if(edit_red_kg.getText().toString().length()==0 && edit_red_bag.getText().toString().length() >=1 || edit_red_kg.getText().toString().length() >=1 && edit_red_bag.getText().toString().length() ==0){			            	
		            			val = "Enter proper data in editext.";
				            }
				            if((/*val==null &&*/ val_kg >= 0 && val_bag >= 0) || val_bag_given >= 0)
					        {
				            	if(isConnectingToInternet()){
				            		try {
							            		if(flagTop){
											            if(hospital_id == null){
											            	hospital_id=edit_hospital_name.getText().toString();
											            }
								            			final JSONArray jObjectOptionData1 = new JSONArray();
								            			//convert parameters into JSON object	
								            			JSONObject data = new JSONObject();
								         	
														 data.put("strQEEID", hospital_id);
														 
														 String numberOnly= hospital_id.replaceAll("[^0-9]", "");
		
														 data.put("QEEID", numberOnly);
														 
														 if(total_BlueWeight.length()==0)
															 total_BlueWeight = "0";
														 data.put("blueKg", total_BlueWeight);
														 
														 if(total_YellowWeight.length()==0)
															 total_YellowWeight = "0";
														 data.put("yellowKg", total_YellowWeight);
		
														 if(total_BlackWeight.length()==0)
															 total_BlackWeight = "0";
														 data.put("blackKg", total_BlackWeight);
														 
														 if(total_RedWeight.length()==0)
															 total_RedWeight = "0";
														 data.put("redKg", total_RedWeight);
														 
														 
														 if(total_YellowBag.length()==0)
															 total_YellowBag = "0";
														 data.put("yellowBagsCollected", total_YellowBag);
														 
														 if(total_RedBag.length()==0)
															 total_RedBag = "0";
														 data.put("redBagsCollected", total_RedBag);
														 
														 if(total_BlueBag.length()==0)
															 total_BlueBag = "0";
														 data.put("blueBagsCollected", total_BlueBag);
														 
														 if(total_BlackBag.length()==0)
															 total_BlackBag = "0";
														 data.put("blackBagsCollected", total_BlackBag);
														 
														 if(val_kg.equals("0"))
															 val_kg = 0f;
														 data.put("totalKg", val_kg.toString());
														 
														 if(edit_yellow_return.getText().toString().length()==0)
														 	edit_yellow_return.setText("0");
														 data.put("yellowBagsGiven", edit_yellow_return.getText().toString());
														 
														 if(edit_red_return.getText().toString().length()==0)
															 edit_red_return.setText("0");
														 data.put("redBagsGiven", edit_red_return.getText().toString());
														 
														 if(edit_blue_return.getText().toString().length()==0)
															 edit_blue_return.setText("0");
														 data.put("blueBagsGiven", edit_blue_return.getText().toString());
														 
														 if(edit_black_return.getText().toString().length()==0)
															 edit_black_return.setText("0");
														 data.put("blackBagsGiven", edit_black_return.getText().toString());
														
														 data.put("vehicleID", userDetails.getString("vehicle_store", "1"));
														 
														 data.put("employeeID", userDetails.getString("employee_store", "1"));
														 data.put("lat", lat);
														 data.put("lon", lon);
														 data.put("routeID", userDetails.getString("route_store", "1"));
														 
														 if(valBase64==null)
															 valBase64 ="0";
														 data.put("strImage", valBase64);
														 data.put("ishospitalopen", isOpenOrClose);
														 data.put("transDateTime", currentDateandTime);
														 
														 jObjectOptionData1.put(data);
		
														 String abl = "{\"data\":"+jObjectOptionData1.toString()+"}";
		
														 new WebserviceCaller().execute(abl);
														 
										            // Write your code here to invoke YES event
										            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
							            		}else{
								            		Toast.makeText(getApplicationContext(), "Please Enter Proper QEE_ID", Toast.LENGTH_SHORT).show();
								            	}
							            	//}else{
							            	//	Toast.makeText(getApplicationContext(), ""+val, Toast.LENGTH_SHORT).show();
							            	//}
									} catch (JSONException e) {
										e.printStackTrace();
									}
				            	}else{
				            		if(flagTop){
					            		ContentValues cv = new ContentValues();
					            		if(hospital_id == null){
							            	hospital_id=edit_hospital_name.getText().toString();
							            }
					            		
					            		String numberOnly= hospital_id.replaceAll("[^0-9]", "");
					            		
					            		cv.put(GarbageDataStorage.QEE_ID_STRING, hospital_id);
					            		cv.put(GarbageDataStorage.QEE_ID, numberOnly);
					            		cv.put(GarbageDataStorage.HOSPITAL_NAME, "abab");
					            		cv.put(GarbageDataStorage.YELLOW_KG, total_YellowWeight.toString());
					            		cv.put(GarbageDataStorage.RED_KG, total_RedWeight.toString());
					            		cv.put(GarbageDataStorage.BLUE_KG, total_BlueWeight.toString());
					            		cv.put(GarbageDataStorage.BLACK_KG, total_BlackWeight.toString());
					            		cv.put(GarbageDataStorage.TOTAL_KG, val_kg.toString());
					            		
					            		if(total_YellowBag.length()==0)
					            			total_YellowBag = "0";
					            		cv.put(GarbageDataStorage.YELLOW_BAGS_TAKEN, total_YellowBag.toString());
					            		
					            		if(total_RedBag.length()==0)
					            			total_RedBag = "0";
					            		cv.put(GarbageDataStorage.RED_BAGS_TAKEN, total_RedBag.toString());
					            		
					            		if(total_BlueBag.length()==0)
					            			total_BlueBag = "0";
					            		cv.put(GarbageDataStorage.BLUE_BAGS_TAKEN, total_BlueBag.toString());
					            		
					            		if(total_BlackBag.length()==0)
					            			total_BlackBag = "0";
					            		cv.put(GarbageDataStorage.BLACK_BAGS_TAKEN, total_BlackBag.toString());
			
					            		if(edit_yellow_return.getText().toString().length()==0)
										 	edit_yellow_return.setText("0");
					            		cv.put(GarbageDataStorage.YELLOW_BAGS_GIVEN, edit_yellow_return.getText().toString());
			
										 
										 if(edit_red_return.getText().toString().length()==0)
											 edit_red_return.setText("0");
										 cv.put(GarbageDataStorage.RED_BAGS_GIVEN, edit_red_return.getText().toString());
			
										 if(edit_blue_return.getText().toString().length()==0)
											 edit_blue_return.setText("0");
										 cv.put(GarbageDataStorage.BLUE_BAGS_GIVEN, edit_blue_return.getText().toString());
			
										 if(edit_black_return.getText().toString().length()==0)
											 edit_black_return.setText("0");
										 cv.put(GarbageDataStorage.BLACK_BAGS_GIVEN, edit_black_return.getText().toString());
										 
					            		cv.put(GarbageDataStorage.DATE_TIME, currentDateandTime);
					            		cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
					            		
					            		if(valBase64==null)
											 valBase64 ="0";
					            		cv.put(GarbageDataStorage.IMAGE_STRING, valBase64);
					            		cv.put(GarbageDataStorage.VEHICLE_ID, userDetails.getString("vehicle_store", "1"));
					            		cv.put(GarbageDataStorage.EMPLOYEE_ID, userDetails.getString("employee_store", "1"));
					            		cv.put(GarbageDataStorage.LAT, lat);
					            		cv.put(GarbageDataStorage.LON, lon);
					            		cv.put(GarbageDataStorage.ROUTE_ID, userDetails.getString("route_store", "1"));
					            		cv.put(GarbageDataStorage.ISOPENCLOSE, isOpenOrClose);
					            		
					            		garbageDataStorageObject.open();
					            		garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DATA_COLLECTED, cv);
						            	Toast.makeText(getApplicationContext(), "Data Stored in Database.", Toast.LENGTH_SHORT).show();
						            	Intent ite= new Intent(HospitalOne.this, HomeActivity.class);
					  			      	ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					  			      	startActivity(ite);
					  			      	finish();						            		
				            		}else{
						            	Toast.makeText(getApplicationContext(), "Enter Hospital id.", Toast.LENGTH_SHORT).show();
				            		}
				            	}
				         }else{
				           	Toast.makeText(getApplicationContext(), "Enter Proper Data", Toast.LENGTH_SHORT).show();
				         }
			            }
			        });
			 
			        // Setting Negative "NO" Button
			        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            // Write your code here to invoke NO event
			            	Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
			            	dialog.cancel();
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();
					
				}else{
			        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalOne.this);
			        alertDialog.setTitle("Message");
			        alertDialog.setMessage("Please take a picture...");
			        alertDialog.setIcon(R.drawable.help);
			        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	dialog.cancel();
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();
				}
			}
		});
		
		manager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// create class object
        gps = new GPSTracker(getApplicationContext());

        // check if GPS enabled
        if(gps.canGetLocation()){
        	lat = gps.getLatitude();
            lon = gps.getLongitude();
            if(lat == 0){
            	gps = new GPSTracker(getApplicationContext());
                // check if GPS enabled
                if(gps.canGetLocation()){
                	lat = gps.getLatitude();
                    lon = gps.getLongitude();
                }
            }
            Toast.makeText(getApplicationContext(), "Lat "+ lat + "Lon "+lon, Toast.LENGTH_LONG).show();
        }else{
            // can't get location GPS or Network is not enabled Ask user to enable GPS/network in settings
            //gps.showSettingsAlert(this);
        }
	}
	
	private class WebserviceCheckHospital extends AsyncTask<String, String, SoapObject> {

		  private String resp;
		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For
			  // example showing ProgessDialog
			  dialog = new ProgressDialog(HospitalOne.this);
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
			   String numberOnly= params[0].replaceAll("[^0-9]", "");
			   soap = WebService.webService_CheckHospital.callWebservice(getApplicationContext(), Integer.parseInt(numberOnly));
		   } catch (Exception e) {
		    e.printStackTrace();
		    resp = e.getMessage();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
			  System.out.println("Myval"+soapObject);
			  if(soapObject!= null){
				  //CheckHospitalIsActiveResponse{CheckHospitalIsActiveResult=anyType{isActive=false; }; }
				  SoapObject myResult =  (SoapObject)soapObject.getProperty("CheckHospitalIsActiveResult");
				  //String isSucess =  soapObject.getProperty("CheckHospitalIsActiveResult").toString();
				  //String isSucess =  soapObject.getProperty("clsActiveInactiveWithEmail").toString();
				  if(myResult.getProperty("isActive").toString().equals("true")){
					  text_isactive.setText("Active");
					  btn_ok.setText("Submit");
					  btn_ok.setEnabled(true);
					  //SoapObject myResultEmail =  (SoapObject)myResult.getProperty("emailID");
					  if(validate(myResult.getProperty("emailID").toString()))//.equals("mkrajgor@gmail.com"))
						  email_id = myResult.getProperty("emailID").toString();
				  }else{
					  text_isactive.setText("Not-Active");
					  btn_ok.setText("Hospital is not Active");
					  btn_ok.setEnabled(false);
				  }
			  }
		  }
		 }
	
	private class WebserviceCaller extends AsyncTask<String, String, SoapObject> {

		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
				  Log.i("Request", "Request=== "+params[0]);
			 soap = WebService.webService_GuesserAskForHint.callWebservice(getApplicationContext(), params[0]);
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  
			try{
				  if (dialog.isShowing()) {
			            dialog.dismiss();
			        }
				  //String count =  soapObject.getProperty("respCount").toString();
				  String isSucess =  soapObject.getProperty("isSuccess").toString();
				  Log.i("Response", "==="+soapObject);
				  if(isSucess.equalsIgnoreCase("true")){
					  if(email_id != null){
						  //if(bd.getBoolean("hospital_close") == true){
						  //if(flagTop == true){
						  if(isOpenOrClose == "open"){
						        new Thread(new Runnable() { 
						            public void run(){
						            	 try {
								               GMailSender sender = new GMailSender("qeevadodara@gmail.com", "Vadodara2003");
								               sender.sendMail("BioMedical Collection Report",   
								                       //"Dear Dr."+ hospital_id +", \n\n Bio Medical waste from your Hospital/Clinic/Lab is collected. \n Yellow = "+total_YellowWeight+" Kg,\n Red = "+total_RedWeight+" Kg,\n Blue = "+total_BlueWeight+" Kg,\n Black = "+total_BlackWeight+" Kg \n on Date ="+currentDateandTime+". \n\n Quantam Environment Enginners.\n Vadodara",
								            		   "Dear Dr. Member, ["+ hospital_id +"]\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).\n\nYour Membership QR Code has been scanned by our staff from your Health Care Unit and Bio Medical Waste (BMW) Collected category wise as under:\n\nYellow = "+total_YellowWeight+" Kg.\nBlue = "+total_BlueWeight+" Kg.\nRed = "+total_RedWeight+" Kg.\nBlack= "+total_BlackWeight+"Kg.\nOn Date = "+currentDateandTime+" \n\nIf you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit www.quantumenvironment.in for the same.\nSincerely,\n\nQuantum Environment Engineers\nVadodara\n\nThe alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.\n\nThis is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.\nCurrently we are under development phase.\n\nQuantum Environment Engineers",
								                       "qeevadodara@gmail.com",   
								                       email_id);
												  //Toast.makeText(getApplicationContext(), "Response : Successfull and Email Sent", Toast.LENGTH_LONG).show();
								           } catch (Exception e) {
								               Log.e("SendMail", e.getMessage(), e);
								           }
						            }
						        }).start();
							}else{
						        new Thread(new Runnable() { 
						            public void run(){
		
						            	 try {
								               GMailSender sender = new GMailSender("qeevadodara@gmail.com", "Vadodara2003");
								               sender.sendMail("BioMedical Collection Report",   
								            		   //"Dear Dr."+ hospital_id +", \n\n Kindly note that your Hospital/Clinic/Lab was closed on Date ="+currentDateandTime+". And Waste collected is below\n Yellow = "+total_YellowWeight+" Kg,\n Red = "+total_RedWeight+" Kg,\n Blue = "+total_BlueWeight+" Kg,\n Black = "+total_BlackWeight+"Kg.\n\n Quantam Environment Enginners.\n Vadodara",
								            		   "Dear Dr. Member, ["+ hospital_id +"]\n\nThank you for being Eco-Friendly Member of Quantum Environment Engineers (QEE).\n\nKindly note that your Health Care Unit found closed either the Scannig of QR Code was not completed On Date ="+currentDateandTime+" and Bio Medical Waste (BMW) Collected category wise as under::\n\nYellow = "+total_YellowWeight+" Kg.\nBlue = "+total_BlueWeight+" Kg.\nRed = "+total_RedWeight+" Kg.\nBlack= "+total_BlackWeight+"Kg.\nOn Date = "+currentDateandTime+" \n\nIf you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit www.quantumenvironment.in for the same.\nSincerely,\n\nQuantum Environment Engineers\nVadodara\n\nThe alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.\n\nThis is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.\nCurrently we are under development phase.\n\nQuantum Environment Engineers",
								            		   //"<p>Dear Dr. Member, ["+ hospital_id +"],</p> <p>Kindly note that your Health Care Unit found closed On Date = ="+currentDateandTime+" and Bio Medical Waste (BMW) Collected category wise as under:</p> <p>Yellow&nbsp;&nbsp;&nbsp;&nbsp; = "+total_YellowWeight+" Kg.</p> <p>Blue&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; = "+total_BlueWeight+" Kg.</p> <p>Red&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; = "+total_RedWeight+" Kg.</p> <p>Black&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; = "+total_BlackWeight+"Kg.</p> <p>On Date = "+currentDateandTime+" </p> <p>If you have any query on above transaction, please call our Helpline number mentioned on your Record Keeping-Book. You may also visit&nbsp;<a href=\"http://www.quantumenvironment.in/\" target=\"_blank\">www.quantumenvironment.in</a>&nbsp;for the same.</p> <p>&nbsp;</p> <p>Sincerely,</p> <p><strong>Quantum Environment Engineers</strong></p> <p>Vadodara</p> <p>&nbsp;</p> <p>The alert has been sent to the registered e-mail ID against your aforementioned enrolment with QEE. In case you wish to register an alternate e-mail ID, please call our Helpline Number.</p> <div> <p>This is an automated e-mail alert to help you keep track of your BMW handled. Hence, please do not reply to this e-mail.</p> </div> <div></div> <div></div> <div> <p> Quantum Environment Engineers </p> </div> <div> <p> Off.: 308, Corner Point, Jetalpur Road, B/h Express Hotel, Alkapuri,Vadodara-390007.</p> </div> <div> <p> Tel: 0265-3951075</p> </div> </div> <div> <p> Cell: +91-7574815771</p> </div>",
								            		   "qeevadodara@gmail.com",
								                       email_id);   
								               //Toast.makeText(getApplicationContext(), "Response : Successfull and Email Sent", Toast.LENGTH_LONG).show();
								           } catch (Exception e) {
								               Log.e("SendMail", e.getMessage(), e);
								           }
						            }
						        }).start();
							}
						}
				  }else{
					  Toast.makeText(getApplicationContext(), "Not Successfull", Toast.LENGTH_LONG).show();
					  
					  if(flagTop){
		            		ContentValues cv = new ContentValues();
		            		if(hospital_id == null){
				            	hospital_id=edit_hospital_name.getText().toString();
				            }
		            		
		            		String numberOnly= hospital_id.replaceAll("[^0-9]", "");
		            		
		            		cv.put(GarbageDataStorage.QEE_ID_STRING, hospital_id);
		            		cv.put(GarbageDataStorage.QEE_ID, numberOnly);
		            		cv.put(GarbageDataStorage.HOSPITAL_NAME, "abab");
		            		cv.put(GarbageDataStorage.YELLOW_KG, total_YellowWeight.toString());
		            		cv.put(GarbageDataStorage.RED_KG, total_RedWeight.toString());
		            		cv.put(GarbageDataStorage.BLUE_KG, total_BlueWeight.toString());
		            		cv.put(GarbageDataStorage.BLACK_KG, total_BlackWeight.toString());
		            		cv.put(GarbageDataStorage.TOTAL_KG, val_kg.toString());
		            		
		            		if(total_YellowBag.length()==0)
		            			total_YellowBag = "0";
		            		cv.put(GarbageDataStorage.YELLOW_BAGS_TAKEN, total_YellowBag.toString());
		            		
		            		if(total_RedBag.length()==0)
		            			total_RedBag = "0";
		            		cv.put(GarbageDataStorage.RED_BAGS_TAKEN, total_RedBag.toString());
		            		
		            		if(total_BlueBag.length()==0)
		            			total_BlueBag = "0";
		            		cv.put(GarbageDataStorage.BLUE_BAGS_TAKEN, total_BlueBag.toString());
		            		
		            		if(total_BlackBag.length()==0)
		            			total_BlackBag = "0";
		            		cv.put(GarbageDataStorage.BLACK_BAGS_TAKEN, total_BlackBag.toString());

		            		if(edit_yellow_return.getText().toString().length()==0)
							 	edit_yellow_return.setText("0");
		            		cv.put(GarbageDataStorage.YELLOW_BAGS_GIVEN, edit_yellow_return.getText().toString());

							 
							 if(edit_red_return.getText().toString().length()==0)
								 edit_red_return.setText("0");
							 cv.put(GarbageDataStorage.RED_BAGS_GIVEN, edit_red_return.getText().toString());

							 if(edit_blue_return.getText().toString().length()==0)
								 edit_blue_return.setText("0");
							 cv.put(GarbageDataStorage.BLUE_BAGS_GIVEN, edit_blue_return.getText().toString());

							 if(edit_black_return.getText().toString().length()==0)
								 edit_black_return.setText("0");
							 cv.put(GarbageDataStorage.BLACK_BAGS_GIVEN, edit_black_return.getText().toString());
							 
		            		cv.put(GarbageDataStorage.DATE_TIME, currentDateandTime);
		            		cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
		            		
		            		if(valBase64==null)
								 valBase64 ="0";
		            		cv.put(GarbageDataStorage.IMAGE_STRING, valBase64);
		            		cv.put(GarbageDataStorage.VEHICLE_ID, userDetails.getString("vehicle_store", "1"));
		            		cv.put(GarbageDataStorage.EMPLOYEE_ID, userDetails.getString("employee_store", "1"));
		            		cv.put(GarbageDataStorage.LAT, lat);
		            		cv.put(GarbageDataStorage.LON, lon);
		            		cv.put(GarbageDataStorage.ROUTE_ID, userDetails.getString("route_store", "1"));
		            		cv.put(GarbageDataStorage.ISOPENCLOSE, isOpenOrClose);
		            		
		            		garbageDataStorageObject.open();
		            		garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DATA_COLLECTED, cv);
			            	Toast.makeText(getApplicationContext(), "Data Stored in Database.", Toast.LENGTH_SHORT).show();
			            	Intent ite= new Intent(HospitalOne.this, HomeActivity.class);
		  			      	ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  			      	startActivity(ite);
		  			      	finish();	
	            		}else{
			            	Toast.makeText(getApplicationContext(), "Enter Hospital id.", Toast.LENGTH_SHORT).show();
	            		}
				  }
			  }catch(Exception e){
				  
			  }finally{
				  Intent ite= new Intent(HospitalOne.this, HomeActivity.class);
				  //ite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			      ite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  startActivity(ite);
				  finish();
			  }
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog = new ProgressDialog(HospitalOne.this);
			  dialog.setMessage("Please wait...");
		      dialog.show();
		  }
		 }
	
	public boolean validate(final String hex) {
		 
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
	/**
	 * Getting the Path of the image selected
	 *
	 * @param uri : Getting uri from OnActivityResult()
	 * @return path : Path of the string from SD Card.
	 */
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	/**
	 * Decoding the file and returning Bitmap's Object
	 * @param file
	 * @return Bitmap
	 */
	private static Bitmap decodeFile(File file) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 150;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	
	/**
	 * Used for converting the Bitmap to String base64
	 * @param bit
	 *            : Bitmap selected by User
	 * @return Base64 in String
	 */
	public String getBase64Value(Bitmap bit) {
		String sBase64Value = null;
		byte[] byteObj = null;
		if (bit != null) {
			byteObj = getBytesFromBitmap(bit);
			sBase64Value = Base64.encodeBytes(byteObj);
		}
		return sBase64Value;
	}
	
	/**
	 *Used for converting bitmap to byte Array
	 * @param bitmap
	 * @return byte[]
	 */
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.JPEG, 50, stream);
	    return stream.toByteArray();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1337) {
			if(resultCode != 0)
			{
				if (data == null) {
					

					camera_flag = true;
					File fileObject = new File(HomeActivity.path.toString()+"/DP.png");
					bitm = decodeFile(fileObject);
					Bitmap workingBitmap = Bitmap.createBitmap(bitm);
					Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
					
					  try {
					        FileOutputStream out = new FileOutputStream(HomeActivity.path.toString()+"/DP1.png");

					        // NEWLY ADDED CODE STARTS HERE [
							Canvas canvas = new Canvas(mutableBitmap);

				            Paint paint = new Paint();
				            paint.setColor(Color.RED); // Text Color
				            paint.setStrokeWidth(45); // Text Size
				            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
				            // some more settings...

				            canvas.drawBitmap(mutableBitmap, 0, 0, paint);
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							String currentDateandTime = sdf.format(new Date());
							canvas.drawText(currentDateandTime , 10, 25, paint);
				            
							mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
							
					        out.flush();
					        out.close();
					    } catch (Exception e) {
					       e.printStackTrace();
					    }
					valBase64 = getBase64Value(mutableBitmap);
				}
			}
		}
	}
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) HospitalOne.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }
		  }
		  return false;
	}
}
