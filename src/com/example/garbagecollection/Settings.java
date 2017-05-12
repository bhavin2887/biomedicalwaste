package com.example.garbagecollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings  extends Activity{
	
	Button btn;
    SharedPreferences userDetails;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	TextView txt_emp,txt_veh,txt_route,txt_imei;
	Cursor cursor1, cursor2, cursor3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		getDatabase();
		btn = (Button)findViewById(R.id.btn_sync);
		txt_emp = (TextView)findViewById(R.id.txt_emp);
		txt_veh = (TextView)findViewById(R.id.txt_veh);
		txt_route = (TextView)findViewById(R.id.txt_route);
		txt_imei = (TextView)findViewById(R.id.txt_imei);
		TelephonyManager mngr = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		txt_imei.setText(mngr.getDeviceId());
		userDetails = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		
		new WebserviceStarted().execute();
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
	        	if(isConnectingToInternet()){
	        		new WebserviceSetting().execute();
	        	}else{
	        		Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
	        	}
			}
		});
		
		btn.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				garbageDataStorageObject.open();
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.DATABASE_DATA_COLLECTED);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.EMPLOYEE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.VEHICLE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.ROUTE);
				garbageDataStorageObject.deleteAllData(GarbageDataStorage.DATABASE_START_DATE);
				garbageDataStorageObject.close();
				
				sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
			    editor.putString("employee_store", "");
			    editor.putString("vehicle_store", "");
			    editor.putString("route_store","");
			    editor.putInt("employee_store_p", 0);
			    editor.putInt("vehicle_store_p", 0);
			    editor.putInt("route_store_p",0);
			    editor.commit();
			    
			    System.exit(0);
			    
				Toast.makeText(getApplicationContext(), "Data got Empty.", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
	private void getDatabase(){
		try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/Garbage.db";
                String backupDBPath = "GarbageNew.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
	}
	private class WebserviceSetting extends AsyncTask<String, String, SoapObject> {

		  private String resp;
		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected void onPreExecute() {
			  dialog = new ProgressDialog(Settings.this);
			  garbageDataStorageObject.open();
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   try {
			   soap = WebService.webService_Setting.callWebservice();
			   
				  if(soap!= null){
					  garbageDataStorageObject.deleteAllData(GarbageDataStorage.EMPLOYEE);
					  garbageDataStorageObject.deleteAllData(GarbageDataStorage.ROUTE);
					  garbageDataStorageObject.deleteAllData(GarbageDataStorage.VEHICLE);
					  SoapObject myResult =  (SoapObject)soap.getProperty("GetAllActiveDataResult");
					  Log.i("==","myResult"+myResult);
					  if(myResult.getProperty("isSuccess").toString().equals("true")){
						  SoapObject lstEmpSoap = (SoapObject) myResult.getProperty("lstEmp");
						  SoapObject lstVehicleSoap = (SoapObject) myResult.getProperty("lstVehicle");
						  SoapObject lstRouteSoap = (SoapObject) myResult.getProperty("lstRoute");

						  for (int i = 0; i < lstEmpSoap.getPropertyCount(); i++) {
							    Object objectResponse = (Object) lstEmpSoap.getProperty(i);
							    SoapObject r =(SoapObject) objectResponse;
							    String isAct =(String) r.getProperty("IsActive").toString();
							    if(isAct.equalsIgnoreCase("true")){
				            		ContentValues cv = new ContentValues();            		
				            		cv.put("employee_id", (String) r.getProperty("EmployeeID").toString());
				            		cv.put("isActive", isAct);
				            		garbageDataStorageObject.insert(GarbageDataStorage.EMPLOYEE, cv);

							    }
						  }
						  
						  for (int i = 0; i < lstVehicleSoap.getPropertyCount(); i++) {
							    Object objectResponse = (Object) lstVehicleSoap.getProperty(i);
							    SoapObject r =(SoapObject) objectResponse;
							    String isAct =(String) r.getProperty("IsActive").toString();
							    if(isAct.equalsIgnoreCase("true")){
							    	ContentValues cv = new ContentValues();            		
				            		cv.put("vehicle_id", (String) r.getProperty("VehicleRegNo").toString());
				            		cv.put("isActive", isAct);
				            		garbageDataStorageObject.insert(GarbageDataStorage.VEHICLE, cv);
							    }
							}
						  
						  for (int i = 0; i < lstRouteSoap.getPropertyCount(); i++) {
							    Object objectResponse = (Object) lstRouteSoap.getProperty(i);
							    SoapObject r =(SoapObject) objectResponse;
							    String isAct =(String) r.getProperty("IsActive").toString();
							    if(isAct.equalsIgnoreCase("true")){
							    	ContentValues cv = new ContentValues();            		
				            		cv.put("route_id", (String) r.getProperty("RouteID").toString());
				            		cv.put("isActive", isAct);
				            		garbageDataStorageObject.insert(GarbageDataStorage.ROUTE, cv);
							    }
							}
				  }
			  }
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
			  garbageDataStorageObject.close();
			  finish();
		 }
	}
	
	private class WebserviceStarted extends AsyncTask<String, String, SoapObject> {

		  private String resp;
		  SoapObject soap;
		  private ProgressDialog dialog;

		  @Override
		  protected void onPreExecute() {
			  dialog = new ProgressDialog(Settings.this);
			  garbageDataStorageObject.open();
			  dialog.setMessage("Please wait...");
			  dialog.show();
		  }

		  @Override
		  protected SoapObject doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   return soap;
		  }
		  
		  @Override
		  protected void onPostExecute(SoapObject soapObject) {
			  
			  try {
					cursor1 = garbageDataStorageObject.getTableData(GarbageDataStorage.EMPLOYEE);
					if(cursor1.getCount()>=1)
						txt_emp.setText(""+cursor1.getCount());
						
					cursor2 = garbageDataStorageObject.getTableData(GarbageDataStorage.VEHICLE);
					if(cursor2.getCount()>=1)
						txt_veh.setText(""+cursor2.getCount());
					
					cursor3 = garbageDataStorageObject.getTableData(GarbageDataStorage.ROUTE);
					if(cursor3.getCount()>=1)
						txt_route.setText(""+cursor3.getCount());

				   } catch (Exception e) {
				    e.printStackTrace();
				   } finally{
					   garbageDataStorageObject.close();
					   cursor1.close();
					   cursor2.close();
					   cursor3.close();
				   }
			  
			  if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
		 }
	}
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) Settings.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
