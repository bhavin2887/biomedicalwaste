package com.example.garbagecollection;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
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
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	SharedPreferences sharedpreferences;
	TextView text_view;
	
	public static final String MyPREFERENCES = "MyPrefs" ;
	Button scan, home_entry_exit, home_settings;
	LinearLayout linear_status;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	public static String myVehicle;
	TextView text_employee_no,text_vehicle_no,text_route;
	Button btn_employee,btn_vehicle,btn_route,home_Start_Date ;
	String str_emp, str_veh, str_rou;
	int str_emp_pos, str_veh_pos, str_rou_pos;
    //String val;
    static File path;
    SharedPreferences userDetails;
    String android_id;
    
    LocationManager manager;
	boolean isGPS;
    GPSTracker gps;
	double lat=0;
	double lon=0;
	String val_Start_id;
    @Override
	protected void onResume() {
		super.onResume();
		setTitle("Home");
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    @Override
    protected void onStop() {
    	super.onStop();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initConnectivityStatus();
                
        text_view = (TextView)findViewById(R.id.textView1);
        scan = (Button)findViewById(R.id.home_scan);
        home_entry_exit = (Button)findViewById(R.id.home_entry_exit);
        linear_status =(LinearLayout)findViewById(R.id.linear_status);
        text_employee_no = (TextView)findViewById(R.id.text_employee_no);
        text_vehicle_no = (TextView)findViewById(R.id.text_vehicle_no);
        text_route = (TextView)findViewById(R.id.text_route);
        home_settings = (Button)findViewById(R.id.home_settings);
        btn_employee = (Button)findViewById(R.id.btn_employee);
        home_Start_Date = (Button)findViewById(R.id.home_Start_Date);
       /* btn_vehicle = (Button)findViewById(R.id.btn_vehicle);
        btn_route = (Button)findViewById(R.id.btn_route);*/
        
        path = new File(Environment.getExternalStorageDirectory()+"/GarbageCollection/");
		path.mkdirs();
		
        userDetails = getApplicationContext().getSharedPreferences( MyPREFERENCES, MODE_PRIVATE);
        
        Bundle bd = getIntent().getExtras();
        
		//android_id = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID); 
		//android_id  = "d8bc1df341facdbb";
		
		TelephonyManager mngr = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		android_id = mngr.getDeviceId();
		
		//358867043681485
		//Log.i("android", "android ="+android_id);
		//Toast.makeText(getApplicationContext(), "="+android_id, Toast.LENGTH_LONG).show();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
	    editor.putString("device_id", android_id);
	    editor.commit();
	    
        text_employee_no.setText(userDetails.getString("employee_store", ""));
        text_vehicle_no.setText(userDetails.getString("vehicle_store", ""));
        text_route.setText(userDetails.getString("route_store", ""));
	    //if(bd!=null){
        if(bd!=null && bd.getString("super")!=null){
        	//val = bd.get("super").toString();TODO
			LayoutInflater li = LayoutInflater.from(HomeActivity.this);
			View promptsView = li.inflate(R.layout.prompt_dialog, null);
			garbageDataStorageObject.open();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
			alertDialogBuilder.setView(promptsView);

			final Spinner spin1 = (Spinner) promptsView.findViewById(R.id.spinner1);
			final Spinner spin2 = (Spinner) promptsView.findViewById(R.id.spinner2);
			final Spinner spin3 = (Spinner) promptsView.findViewById(R.id.spinner3);
			
			Cursor cursor = garbageDataStorageObject.getTableData(GarbageDataStorage.EMPLOYEE);
			List<String> list1 = new ArrayList<String>();
			if (cursor.moveToFirst()) {
			    do {
					 list1.add(cursor.getString(cursor.getColumnIndex("employee_id")));
			    } while (cursor.moveToNext());
		   }
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin1.setAdapter(dataAdapter);
			spin1.setOnItemSelectedListener(new CustomOnItemSelectedListener1());
			spin1.setSelection(userDetails.getInt("employee_store_p", 0));

			Cursor cursor2 = garbageDataStorageObject.getTableData(GarbageDataStorage.VEHICLE);
			List<String> list2 = new ArrayList<String>();
			if (cursor2.moveToFirst()) {
			    do {
					 list2.add(cursor2.getString(cursor2.getColumnIndex("vehicle_id")));
			    } while (cursor2.moveToNext());
			}
			
			ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
			dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin2.setAdapter(dataAdapter2);
			spin2.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
			spin2.setSelection(userDetails.getInt("vehicle_store_p", 0));

			
			Cursor cursor3 = garbageDataStorageObject.getTableData(GarbageDataStorage.ROUTE);
			List<String> list3 = new ArrayList<String>();
			if (cursor3.moveToFirst()) {
			    do {
					 list3.add(cursor3.getString(cursor3.getColumnIndex("route_id")));
			    } while (cursor3.moveToNext());
		   }
			
			ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list3);
			dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin3.setAdapter(dataAdapter3);
			spin3.setOnItemSelectedListener(new CustomOnItemSelectedListener3());
			spin3.setSelection(userDetails.getInt("route_store_p", 0));
			
			// set dialog message
			Log.i("HOME"+text_employee_no.getText().toString(), "HOME========="+text_employee_no.getText().length());
			if(text_employee_no.getText().length()>=1){
				spin1.setEnabled(false);
				spin2.setEnabled(false);
				spin3.setEnabled(false);				
			}
			if(text_employee_no.getText().length()>=1){
				alertDialogBuilder
				.setCancelable(true)
				.setNegativeButton("End Day",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
					    if(isConnectingToInternet())
					    	new SaveDailyAssignment("end").execute();
					    else{
					    	garbageDataStorageObject.open();
					    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			        		String currentDateandTime = sdf.format(new Date());
			         		ContentValues cv = new ContentValues();
						  	cv.put(GarbageDataStorage.START_DATE_TIME, currentDateandTime);
						  	cv.put("employee_id", userDetails.getString("employee_store", "1"));
			        		cv.put("vehicle_id", userDetails.getString("vehicle_store", "1"));
			        		cv.put("route_id", userDetails.getString("route_store", "1"));
			        		cv.put("handset_id", android_id);
							cv.put(GarbageDataStorage.END_DATE_TIME, currentDateandTime);
							cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
			        		garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_START_DATE, cv);
			        		garbageDataStorageObject.close();
			        		//----------------
			        		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedpreferences.edit();
						    editor.putString("employee_store", "");
						    editor.putString("vehicle_store", "");
						    editor.putString("route_store","");
						    editor.putInt("employee_store_p", 0);
						    editor.putInt("vehicle_store_p", 0);
						    editor.putInt("route_store_p",0);
						    //editor.putString("start_dateTime", "");
						    editor.commit();
						    text_employee_no.setText("");
					    	text_vehicle_no.setText("");
					    	text_route.setText("");
					    }
				    }
				  });
			}else{
				alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("Start Day",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
					    dialog.dismiss();
					    if(isConnectingToInternet())
					    	new SaveDailyAssignment("start").execute();
					    else{
					    	garbageDataStorageObject.open();
					    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			        		String currentDateandTime = sdf.format(new Date());
			         		ContentValues cv = new ContentValues();
						  	cv.put(GarbageDataStorage.START_DATE_TIME, currentDateandTime);
			        		cv.put("employee_id", userDetails.getString("employee_store", "1"));
			        		cv.put("vehicle_id", userDetails.getString("vehicle_store", "1"));
			        		cv.put("route_id", userDetails.getString("route_store", "1"));
			        		cv.put("handset_id", android_id);
							//cv.put(GarbageDataStorage.END_DATE_TIME, currentDateandTime);
							cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
			        		garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_START_DATE, cv);
			        		garbageDataStorageObject.close();
			        		text_employee_no.setText(str_emp);
					  		text_vehicle_no.setText(str_veh);
					  		text_route.setText(str_rou);
					  		
			        		/*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedpreferences.edit();
						    editor.putString("start_dateTime", currentDateandTime);
						    editor.commit();*/
					    }
				    }
				  });
				
			}
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			garbageDataStorageObject.close();
        }
        
        btn_employee.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*if(isConnectingToInternet()){
					new WebserviceCheck().execute();
				}else{
					showPasswordSupervisor();
				}*/
				finish();
				Intent i = new Intent();
				i.setClass(HomeActivity.this, MainScreen.class);
				startActivity(i);
			}
		});
        
        home_Start_Date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isConnectingToInternet()){
					new AsyncTaskRunnerStart().execute();
				}
			}
		});
        
        home_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					Intent i = new Intent(HomeActivity.this, Settings.class);
					startActivity(i);
			}
		});
        
        scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(text_employee_no.getText().length()>=1 && text_vehicle_no.getText().length()>=1 && text_route.getText().length()>=1 ){
					HomeActivity.this.finish();
					Intent i = new Intent();
					i.setClass(HomeActivity.this, MainScreen.class);
					startActivity(i);				
				}else{
					Toast.makeText(getApplicationContext(), "Please fill All the Information", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        home_entry_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(text_employee_no.getText().length()>=1 && text_vehicle_no.getText().length()>=1 && text_route.getText().length()>=1 ){
					Intent ite= new Intent(HomeActivity.this, HospitalOne.class);
					startActivity(ite);
				}else{
					Toast.makeText(getApplicationContext(), "Please fill All the Information", Toast.LENGTH_LONG).show();
				}
			}
		});
        Cursor cur = null;
        try {
        	garbageDataStorageObject.open();
			cur = garbageDataStorageObject.getDataNotOnServer(GarbageDataStorage.DATABASE_START_DATE);
			   Log.i("ava", "adfa_Start"+cur.getCount());
			   if(cur.getCount()>=1){
				   if (cur.moveToFirst()) {
						   String serv = cur.getString(cur.getColumnIndex(GarbageDataStorage.IS_ON_SERVER));
						   String dt = cur.getString(cur.getColumnIndex(GarbageDataStorage.START_DATE_TIME));
						   String startid = cur.getString(cur.getColumnIndex(GarbageDataStorage.START_ID));

						   Log.i("==="+ startid, "server="+serv);
						   Log.i("===", "dt="+dt);
						   home_Start_Date.setText("Sync Start Date ("+cur.getCount()+")");
				   }
			   }else{
				   home_Start_Date.setText("Sync Start Date ("+cur.getCount()+")");
			   }
			   
		   } catch (Exception e) {
		    e.printStackTrace();
		   } finally{
			   cur.close();
		   }
        
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

        
        new AsyncTaskRunner().execute();

    }
    
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
        .setCancelable(false)
        .setPositiveButton("Goto Settings Page To Enable GPS",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
            	finish();
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		  private String resp;
		  private ProgressDialog dialog_task;
		  Cursor cur;
		  GarbageDataStorage garbageDataStorageObjectInner = new GarbageDataStorage(HomeActivity.this);
		  @Override
		  protected String doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   String val = null;
		   try {
			   garbageDataStorageObjectInner.open();
			   cur = garbageDataStorageObjectInner.getDataNotOnServer(GarbageDataStorage.DATABASE_DATA_COLLECTED);
			   Log.i("ava", "adfa"+cur.getCount());
			   if(cur.getCount()>=1){
				   val= "Many";
			   }else{
				   val= "Blank";
			   }
			   
		   } catch (Exception e) {
		    e.printStackTrace();
		    resp = e.getMessage();
		   } finally{
			   cur.close();
		   }
		   return val;
		  }

		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
			  if (dialog_task.isShowing()) {
				  dialog_task.dismiss();
		        }
			  if(result.equalsIgnoreCase("Many")){
				  linear_status.setBackgroundColor(Color.RED);
				  if(isConnectingToInternet())
					  new AsyncTaskSender().execute();
			  }else{
				  linear_status.setBackgroundColor(Color.GREEN);
			  }
			   garbageDataStorageObjectInner.close();

		  }

		  @Override
		  protected void onPreExecute() {
			  dialog_task = new ProgressDialog(HomeActivity.this);
			  dialog_task.setMessage("Please wait...");
			  dialog_task.show();
		  }
		 }

	private class AsyncTaskRunnerStart extends AsyncTask<String, String, String> {

		  private String resp;
		  private ProgressDialog dialog_task_Start;
		  Cursor cur;
		  GarbageDataStorage garbageDataStorageObjectInner = new GarbageDataStorage(HomeActivity.this);
		  @Override
		  protected String doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   String val = null;
		   try {
			   garbageDataStorageObjectInner.open();
			   cur = garbageDataStorageObjectInner.getDataNotOnServer(GarbageDataStorage.DATABASE_START_DATE);
			   Log.i("ava", "adfa_Start"+cur.getCount());
			   if(cur.getCount()>=1){
				   if (cur.moveToFirst()) {
						   String serv = cur.getString(cur.getColumnIndex(GarbageDataStorage.IS_ON_SERVER));
						   String dt = cur.getString(cur.getColumnIndex(GarbageDataStorage.START_DATE_TIME));
						   String startid = cur.getString(cur.getColumnIndex(GarbageDataStorage.START_ID));

						   Log.i("==="+ startid, "server="+serv);
						   Log.i("===", "dt="+dt);
						   val= "Many";
				   }
			   }else{
				   val= "Blank";
			   }
			   
		   } catch (Exception e) {
		    e.printStackTrace();
		    resp = e.getMessage();
		   } finally{
			   cur.close();
		   }
		   return val;
		  }

		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
			  if (dialog_task_Start.isShowing()) {
				  dialog_task_Start.dismiss();
		        }
			  if(result.equalsIgnoreCase("Many")){
				  //linear_status.setBackgroundColor(Color.RED);
				  if(isConnectingToInternet())
					  new SaveDailyAssignmentLoop().execute();
			  }else{
				   home_Start_Date.setText("Sync Start Date (0)");
				  //linear_status.setBackgroundColor(Color.GREEN);
			  }
			   garbageDataStorageObjectInner.close();

		  }

		  @Override
		  protected void onPreExecute() {
			  dialog_task_Start = new ProgressDialog(HomeActivity.this);
			  dialog_task_Start.setMessage("Please wait...");
			  dialog_task_Start.show();
		  }
		 }
	
	
	private class AsyncTaskSender extends AsyncTask<String, String, String> {

		  private String resp;
		  private ProgressDialog dialog_sender;
		  Cursor cursor;
		  
		  @Override
		  protected String doInBackground(String... params) {
			   publishProgress("Sleeping..."); // Calls onProgressUpdate()
			   String val = null;
			   try {
				 //SoapObject soap = WebService.webService_GuesserAskForHint.callWebservice(getApplicationContext(), params[0]);
				   garbageDataStorageObject.open();
				   cursor = garbageDataStorageObject.getDataNotOnServer(GarbageDataStorage.DATABASE_DATA_COLLECTED);
				   if(cursor.getCount()==1){
					   if (cursor.moveToFirst()) {
						    do {
		            			final JSONArray jObjectOptionData1 = new JSONArray();
						    	JSONObject dataLoop = new JSONObject();
								 dataLoop.put("QEEID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.QEE_ID)));
								 dataLoop.put("strQEEID",  cursor.getString(cursor.getColumnIndex(GarbageDataStorage.QEE_ID_STRING)));
								 dataLoop.put("blueKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.BLUE_KG)));
								 dataLoop.put("yellowKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.YELLOW_KG)));
								 dataLoop.put("blackKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.BLACK_KG)));
								 dataLoop.put("redKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.RED_KG)));
								 dataLoop.put("totalKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.TOTAL_KG)));
								 dataLoop.put("blueBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLUE_BAGS_GIVEN)));
								 dataLoop.put("yellowBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.YELLOW_BAGS_GIVEN)));
								 dataLoop.put("redBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.RED_BAGS_GIVEN)));
								 dataLoop.put("blackBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLACK_BAGS_GIVEN)));
								 dataLoop.put("transDateTime", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.DATE_TIME)));
								 dataLoop.put("vehicleID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.VEHICLE_ID)));
								 dataLoop.put("employeeID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.EMPLOYEE_ID)));
								 dataLoop.put("routeID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.ROUTE_ID)));
								 dataLoop.put("strImage", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.IMAGE_STRING)));
								 dataLoop.put("yellowBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.YELLOW_BAGS_TAKEN)));
								 dataLoop.put("redBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.RED_BAGS_TAKEN)));
								 dataLoop.put("blueBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLUE_BAGS_TAKEN)));
								 dataLoop.put("blackBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLACK_BAGS_TAKEN)));
								 dataLoop.put("lat", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.LAT)));
								 dataLoop.put("lon", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.LON)));
								 dataLoop.put("ishospitalopen", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.ISOPENCLOSE)));
								 
								 jObjectOptionData1.put(dataLoop);
	
								 val = "{\"data\":"+jObjectOptionData1.toString()+"}";
	
						    } while (cursor.moveToNext());
					   }
				   }else if(cursor.getCount()>=1){
					   final JSONArray jObjectOptionData1 = new JSONArray();
					   if (cursor.moveToFirst()) {
						    do {
						    	JSONObject dataLoop = new JSONObject();
								 dataLoop.put("QEEID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.QEE_ID)));
								 dataLoop.put("strQEEID",  cursor.getString(cursor.getColumnIndex(GarbageDataStorage.QEE_ID_STRING)));
								 dataLoop.put("blueKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.BLUE_KG)));
								 dataLoop.put("yellowKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.YELLOW_KG)));
								 dataLoop.put("blackKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.BLACK_KG)));
								 dataLoop.put("redKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.RED_KG)));
								 dataLoop.put("totalKg", cursor.getFloat(cursor.getColumnIndex(GarbageDataStorage.TOTAL_KG)));
								 dataLoop.put("blueBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLUE_BAGS_GIVEN)));
								 dataLoop.put("yellowBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.YELLOW_BAGS_GIVEN)));
								 dataLoop.put("redBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.RED_BAGS_GIVEN)));
								 dataLoop.put("blackBagsGiven", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLACK_BAGS_GIVEN)));
								 dataLoop.put("transDateTime", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.DATE_TIME)));
								 dataLoop.put("vehicleID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.VEHICLE_ID)));
								 dataLoop.put("employeeID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.EMPLOYEE_ID)));
								 dataLoop.put("routeID", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.ROUTE_ID)));
								 dataLoop.put("strImage", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.IMAGE_STRING)));
								 dataLoop.put("yellowBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.YELLOW_BAGS_TAKEN)));
								 dataLoop.put("redBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.RED_BAGS_TAKEN)));
								 dataLoop.put("blueBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLUE_BAGS_TAKEN)));
								 dataLoop.put("blackBagsCollected", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.BLACK_BAGS_TAKEN)));
								 dataLoop.put("lat", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.LAT)));
								 dataLoop.put("lon", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.LON)));
								 dataLoop.put("ishospitalopen", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.ISOPENCLOSE)));

								 jObjectOptionData1.put(dataLoop);
	
						    } while (cursor.moveToNext());
					   }
						 val = "{\"data\":"+jObjectOptionData1.toString()+"}";
				   }
			   } catch (Exception e) {
			    e.printStackTrace();
			    resp = e.getMessage();
			   }finally{
				   cursor.close();
				   Log.i("==",""+val);
			   }
			   return val;
			  }

		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
			  if (dialog_sender.isShowing()) {
				  dialog_sender.dismiss();
		        }
			  if(result!=null){
				  new WebserviceCaller().execute(result);
			  }else{
				  linear_status.setBackgroundColor(Color.GREEN);
			  }
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog_sender = new ProgressDialog(HomeActivity.this);
			  dialog_sender.setMessage("Please wait...");
			  dialog_sender.show();
		  }

		  @Override
		  protected void onProgressUpdate(String... text) {
		   // Things to be done while execution of long running operation is in
		   // progress. For example updating ProgessDialog
		  }
		 }
	
	private class WebserviceCaller extends AsyncTask<String, String, SoapObject> {

		  private String resp;
		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Please Wait..."); // Calls onProgressUpdate()
		   try {
			 soap = WebService.webService_GuesserAskForHint.callWebservice(getApplicationContext(), params[0]);
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
			  String count =  soapObject.getProperty("respCount").toString();
			  String isSucess =  soapObject.getProperty("isSuccess").toString();
			  
			  if(isSucess.equalsIgnoreCase("true")){
				  linear_status.setBackgroundColor(Color.GREEN);
				  //garbageDataStorageObject.updateUploadedFile();
				  garbageDataStorageObject.deleteAllData(GarbageDataStorage.DATABASE_DATA_COLLECTED);
			  }else{
				  linear_status.setBackgroundColor(Color.RED);
			  }
			  garbageDataStorageObject.close();
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog = new ProgressDialog(HomeActivity.this);
			  dialog.setMessage("Please wait...");
		      dialog.show();
		  }
		 }
	
	private class WebserviceStartEndCaller extends AsyncTask<String, String, SoapObject> {

		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {   				
			   
			   SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
       		   String getDateTime= sdf.format(new Date());
       			
			   final JSONArray jObjectOptionData2 = new JSONArray();
		   
			   JSONObject dataLoop = new JSONObject();
				 dataLoop.put("QEEID", "2");
				 dataLoop.put("strQEEID",  "QEE-2");
				 dataLoop.put("blueKg", "0");
				 dataLoop.put("yellowKg", "0");
				 dataLoop.put("blackKg", "0");
				 dataLoop.put("redKg", "0");
				 dataLoop.put("totalKg", "0");
				 dataLoop.put("blueBagsGiven", "0");
				 dataLoop.put("yellowBagsGiven", "0");
				 dataLoop.put("redBagsGiven", "0");
				 dataLoop.put("blackBagsGiven", "0");
				 dataLoop.put("transDateTime", getDateTime);
				 dataLoop.put("vehicleID", str_veh);
				 dataLoop.put("employeeID", str_emp);
				 dataLoop.put("routeID", str_rou);
				 dataLoop.put("strImage", "0");
				 dataLoop.put("yellowBagsCollected", "0");
				 dataLoop.put("redBagsCollected", "0");
				 dataLoop.put("blueBagsCollected", "0");
				 dataLoop.put("blackBagsCollected", "0");
				 dataLoop.put("lat", "22.280347");
				 dataLoop.put("lon", "73.167942");
				 dataLoop.put("ishospitalopen", "Open");

				 jObjectOptionData2.put(dataLoop);
				 
				 String set_data = "{\"data\":"+jObjectOptionData2.toString()+"}";
				 
				 Log.i("Request", "Request STARTEND=== "+set_data);
			 soap = WebService.webService_GuesserAskForHint.callWebservice(getApplicationContext(), set_data);
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  Log.i("Request", "Request PostExecute=== ");

			  if (dialog.isShowing()) {
		            dialog.dismiss();
		      }
			  
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog = new ProgressDialog(HomeActivity.this);
			  dialog.setMessage("Please wait...");
		      dialog.show();
		  }
		 }
	
	private class WebserviceCheck extends AsyncTask<String, String, String> {

		  private String resp;
		  String soap;
		  private ProgressDialog dialog;

		  @Override
		  protected String doInBackground(String... params) {
		   publishProgress("Please Wait..."); // Calls onProgressUpdate()
		   try {
			 soap = WebService.webService_HelloWorld.callWebserviceCheck(getApplicationContext());
		   } catch (Exception e) {
		    e.printStackTrace();
		    resp = e.getMessage();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(String soapObject) {
			  if (dialog.isShowing()) {
		            dialog.dismiss();
		      }
			  if(soapObject.equalsIgnoreCase("Hello World"))
			  {
	  			    finish();
					Intent i = new Intent();
					i.setClass(HomeActivity.this, MainScreen.class);
					startActivity(i);
			  }else{
				  showPasswordSupervisor();
			  }
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog = new ProgressDialog(HomeActivity.this);
			  dialog.setMessage("Please wait...");
		      dialog.show();
		  }
		 }
	
	public void showPasswordSupervisor() {
	    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
	    dialogBuilder.setView(dialogView);

	    final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

	    dialogBuilder.setTitle("Custom dialog");
	    dialogBuilder.setMessage("Enter Supervisor Password :");
	    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        	if(edt.getText().toString().equalsIgnoreCase("super.7995") || edt.getText().toString().equalsIgnoreCase("super.1457") || edt.getText().toString().equalsIgnoreCase("super.2358") || edt.getText().toString().equalsIgnoreCase("super.4779")){
	        		Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
	        		Intent ite= new Intent(HomeActivity.this, HomeActivity.class);
					ite.putExtra("super", "strUrl");
					startActivity(ite);
	        	}else{
	        		Toast.makeText(getApplicationContext(),"Not Success", Toast.LENGTH_LONG).show();
	        	}
	        }
	    });
	    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            //pass
	        	dialog.dismiss();
	        }
	    });
	    AlertDialog b = dialogBuilder.create();
	    b.show();
	}
	   
	
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) HomeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
	
	
	private class SaveDailyAssignment extends AsyncTask<String, String, SoapObject> {

		  SoapObject soap;
		  private ProgressDialog dialog;
		  String val_date;
		  
		  public SaveDailyAssignment(String string) {
			  val_date = string;
		  }

		  @Override
		  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For
			  // example showing ProgessDialog
			  dialog = new ProgressDialog(HomeActivity.this);
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
			   
   				final JSONArray jObjectOptionData1 = new JSONArray();
   				
   				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        		String currentDateandTime = sdf.format(new Date());
        		
   				JSONObject data = new JSONObject();
	         	
				 data.put("assignDate", currentDateandTime);
				 data.put("employeeID", str_emp);//userDetails.getString("employee_store", "1"));
				 data.put("vehicleRegNo", str_veh);//userDetails.getString("vehicle_store", "1"));
				 data.put("routeID", str_rou);//userDetails.getString("route_store", "1"));
				 data.put("handsetid", android_id);//"d8bc1df341facdbb");
				 if(val_date.equalsIgnoreCase("end")){
					 data.put("EndDate", currentDateandTime);
					 data.put("PkAssignID", userDetails.getString("start_id", "1"));
				 }
				 
				 jObjectOptionData1.put(data);

				 String abl = "{\"data\":"+jObjectOptionData1.toString()+"}";
				 Log.i("Data", "Data"+abl);
			   soap = WebService.webService_SaveDailyAssignment.callWebservice(getApplicationContext(), abl);
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  SoapObject myResult =  (SoapObject)soapObject.getProperty("SaveDailyAssignmentResult");
			  String isSucess =  myResult.getProperty("isSuccess").toString();
			  if(isSucess.equalsIgnoreCase("true")){
				  if(val_date.equalsIgnoreCase("start")){
				  		text_employee_no.setText(str_emp);
				  		text_vehicle_no.setText(str_veh);
				  		text_route.setText(str_rou);
				  		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedpreferences.edit();
					    editor.putString("start_id", myResult.getProperty("ID").toString());
					    editor.commit();
					    
					    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					    editor.putString("employee_store", str_emp);
					    editor.putInt("employee_store_p", str_emp_pos);
					    editor.commit();
					    
					    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					    editor.putString("vehicle_store", str_veh);
					    editor.putInt("vehicle_store_p", str_veh_pos);
					    editor.commit();
					    
					    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					    editor.putString("route_store",str_rou);
					    editor.putInt("route_store_p",str_rou_pos);
					    editor.commit();
				  }else{
					  	sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedpreferences.edit();
					    editor.putString("employee_store", "");
					    editor.putString("vehicle_store", "");
					    editor.putString("route_store","");
					    editor.putInt("employee_store_p", 0);
					    editor.putInt("vehicle_store_p", 0);
					    editor.putInt("route_store_p",0);
					    editor.commit();
					    text_employee_no.setText("");
				    	text_vehicle_no.setText("");
				    	text_route.setText("");
				  }
				  //Log.i("=====","===== WebserviceStartEndCaller");
				  
				  //new WebserviceStartEndCaller().execute();
			  }else{
				  Toast.makeText(HomeActivity.this, "Problem with starting date" ,Toast.LENGTH_SHORT).show();
			  }
			  if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
		  }

		 }
	
	private class SaveDailyAssignmentLoop extends AsyncTask<String, String, SoapObject> {

		  SoapObject soap;
		  private ProgressDialog dialog;
		  int cur_int = 0;
		  public SaveDailyAssignmentLoop() {}

		  @Override
		  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For
			  // example showing ProgessDialog
			  garbageDataStorageObject.open();
			  dialog = new ProgressDialog(HomeActivity.this);
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
			   Cursor cursor = garbageDataStorageObject.getDataNotOnServer(GarbageDataStorage.DATABASE_START_DATE);
			   //Cursor cursorStart = garbageDataStorageObject.getDataNotOnServerStart(GarbageDataStorage.DATABASE_START_DATE);
			   
			   cur_int = cursor.getCount();
				   if(cursor.getCount()>=1){
					   if (cursor.moveToFirst()) {
						    	JSONArray jObjectOptionData1 = new JSONArray();
								JSONObject data = new JSONObject();
								 data.put("assignDate", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.START_DATE_TIME)));
								 data.put("employeeID", cursor.getString(cursor.getColumnIndex("employee_id")));
								 data.put("vehicleRegNo", cursor.getString(cursor.getColumnIndex("vehicle_id")));
								 data.put("routeID", cursor.getString(cursor.getColumnIndex("route_id")));
								 data.put("handsetid", android_id);//"d8bc1df341facdbb");
								 if(cursor.getString(cursor.getColumnIndex(GarbageDataStorage.END_DATE_TIME))!=null){
									 data.put("EndDate", cursor.getString(cursor.getColumnIndex(GarbageDataStorage.END_DATE_TIME)));
									 if(val_Start_id!= null)
										 data.put("PkAssignID", val_Start_id);
									 else
										 data.put("PkAssignID", userDetails.getString("start_id", "1"));
								 }
								 jObjectOptionData1.put(data);
								 String abl = "{\"data\":"+jObjectOptionData1.toString()+"}";
								 Log.i("Loop SaveDailyAssignmentLoop", "Loop SaveDailyAssignmentLoop="+abl);
								 soap = WebService.webService_SaveDailyAssignment.callWebservice(getApplicationContext(), abl);
					   }
				}
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soap) {
			  SoapObject myResult =  (SoapObject)soap.getProperty("SaveDailyAssignmentResult");
			  String isSucess =  myResult.getProperty("isSuccess").toString();
			  if(isSucess.equalsIgnoreCase("true")){
				  String strDate = null;
				    try
				    {
				      if(myResult.getProperty("EndDate") == null)
				    	  strDate = myResult.getProperty("AssignDate").toString().replace("T", " ");
				      else
				    	  strDate = myResult.getProperty("EndDate").toString().replace("T", " ");
				      
				      SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//dd/MM/yy");
				      Date date = sdfSource.parse(strDate);
				      SimpleDateFormat sdfDestination = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				      strDate = sdfDestination.format(date);
				    }
				    catch(ParseException pe)
				    {
				      System.out.println("Parse Exception : " + pe);
				    }
				  Log.i("Loop Before Update", "onPostExecute ID="+myResult.getProperty("ID").toString());
				  val_Start_id = myResult.getProperty("ID").toString();
				  int value_Ret = garbageDataStorageObject.UpdateStartDate(strDate,myResult.getProperty("ID").toString());
				  Log.i("Loop"+value_Ret, "onPostExecute Date="+strDate);
				  home_Start_Date.setText("Sync Start Date ("+(cur_int-1)+")");
			  }else{
				  Toast.makeText(HomeActivity.this, "Problem with starting date" ,Toast.LENGTH_SHORT).show();
			  }
			  if (dialog.isShowing()) {
		            dialog.dismiss();
			  }
			  garbageDataStorageObject.close();
		  }
		 }
	
	public class CustomOnItemSelectedListener1 implements OnItemSelectedListener {

		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString()  + "="+pos,Toast.LENGTH_SHORT).show();
			str_emp = parent.getItemAtPosition(pos).toString();
			str_emp_pos = pos;
			
	    	/*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedpreferences.edit();
		    editor.putString("employee_store", str_emp);
		    editor.putInt("employee_store_p", str_emp_pos);
		    editor.commit();*/
		  }

		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }

		}
	
	public class CustomOnItemSelectedListener2 implements OnItemSelectedListener {

		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString() + "="+pos,	Toast.LENGTH_SHORT).show();
			str_veh = parent.getItemAtPosition(pos).toString();
			str_veh_pos = pos;
			
	    	/*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedpreferences.edit();
		    editor.putString("vehicle_store", str_veh);
		    editor.putInt("vehicle_store_p", str_veh_pos);
		    editor.commit();*/
		  }

		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }

		}
	
	public class CustomOnItemSelectedListener3 implements OnItemSelectedListener {

		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString()  + "="+pos,Toast.LENGTH_SHORT).show();
			str_rou = parent.getItemAtPosition(pos).toString();
			str_rou_pos = pos;
			
	    	/*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedpreferences.edit();
		    editor.putString("route_store",str_rou);
		    editor.putInt("route_store_p",str_rou_pos);
		    editor.commit();*/
		  }

		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }

		}
}