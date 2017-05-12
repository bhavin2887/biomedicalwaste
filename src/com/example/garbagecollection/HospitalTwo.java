package com.example.garbagecollection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalTwo extends Activity{

	EditText edit_kg1_return,edit_kg2_return,edit_kg3_return,edit_kg4_return; 

	TextView text_total_bag_return;
	Button btn_submit;
	String total_YellowWeight,total_RedWeight,total_BlueWeight,total_BlackWeight,total_YellowBag,total_RedBag,total_BlueBag,total_BlackBag;
	String total_weight;
	int total_bags_collected;
	String hospital_id;
	/** Database object for saving data of device details into Database */
	private GarbageDataStorage garbageDataStorageObject = new GarbageDataStorage(this);
	Button btn_camera;
	String valBase64;
	Bitmap bitm;
    TextView text_id,text_isactive;
	public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences userDetails;
    
	public static String GMAILID = "abcd@gmail.com";
	public static String GMAILPWD = "abcd";
	String currentDateandTime;
	Bundle bd;
	String email_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hospitel_two);
		
		bd = getIntent().getExtras();
		hospital_id = bd.getString("hospital_id");
		total_weight = bd.getString("TotalWeight");
		total_bags_collected = bd.getInt("TotalBagsCollected");
		total_YellowWeight = bd.getString("YellowWeight");
		total_RedWeight = bd.getString("RedWeight");
		total_BlueWeight = bd.getString("BlueWeight");
		total_BlackWeight = bd.getString("BlackWeight");
		total_YellowBag = bd.getString("YellowBag");
		total_RedBag = bd.getString("RedBag");
		total_BlueBag = bd.getString("BlueBag");
		total_BlackBag = bd.getString("BlackBag");
		email_id = bd.getString("email_id");
		btn_camera = (Button)findViewById(R.id.btn_camera);
		
		edit_kg1_return = (EditText)findViewById(R.id.edit_bag1_return);
		edit_kg2_return = (EditText)findViewById(R.id.edit_bag2_return);
		edit_kg3_return = (EditText)findViewById(R.id.edit_bag3_return);
		edit_kg4_return = (EditText)findViewById(R.id.edit_bag4_return);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		text_id = (TextView)findViewById(R.id.textView_eid);
		text_isactive = (TextView)findViewById(R.id.text_isactive);
		
		text_total_bag_return = (TextView)findViewById(R.id.text_total_bag_return);
        userDetails = getApplicationContext().getSharedPreferences( MyPREFERENCES, MODE_PRIVATE);

		text_id.setText(hospital_id);
		
		if(bd.getBoolean("hospital_close") == true){
			btn_camera.setVisibility(View.VISIBLE);
			/*text_id.setVisibility(View.VISIBLE);
			text_isactive.setVisibility(View.VISIBLE);*/
		}else{
			btn_camera.setVisibility(View.GONE);
			/*text_id.setVisibility(View.GONE);
			text_isactive.setVisibility(View.GONE);*/
		}
		
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int val_bag_given = 0;
				
				if(edit_kg1_return.getText().toString().length()>=1)
					val_bag_given=val_bag_given+Integer.parseInt(edit_kg1_return.getText().toString());
				if(edit_kg2_return.getText().toString().length()>=1)
					val_bag_given=val_bag_given+Integer.parseInt(edit_kg2_return.getText().toString());
				if(edit_kg3_return.getText().toString().length()>=1)
					val_bag_given=val_bag_given+Integer.parseInt(edit_kg3_return.getText().toString());
				if(edit_kg4_return.getText().toString().length()>=1)
					val_bag_given=val_bag_given+Integer.parseInt(edit_kg4_return.getText().toString());
				
				text_total_bag_return.setText(""+val_bag_given);
		        
		        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HospitalTwo.this);
		        
		        // Setting Dialog Title
		        alertDialog.setTitle("Confirm Submittion...");
		 
		        // Setting Dialog Message
		        alertDialog.setMessage("Total Bags Returned is ="+val_bag_given);
		 
		        // Setting Icon to Dialog
		        alertDialog.setIcon(R.drawable.help);
		 
		        // Setting Positive "Yes" Button
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            // Write your code here to invoke YES event
		            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		            	currentDateandTime = sdf.format(new Date());
	            		
		            	if(isConnectingToInternet()){
		            		try {
		            			
				            	Toast.makeText(getApplicationContext(), "Data Sent To Server.", Toast.LENGTH_SHORT).show();

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
								 
								 if(total_weight.length()==0f)
									 total_weight = "0";
								 data.put("totalKg", total_weight);
								 
								 if(edit_kg1_return.getText().toString().length()==0)
								 	edit_kg1_return.setText("0");
								 data.put("yellowBagsGiven", edit_kg1_return.getText().toString());
								 
								 if(edit_kg2_return.getText().toString().length()==0)
									 edit_kg2_return.setText("0");
								 data.put("redBagsGiven", edit_kg2_return.getText().toString());
								 
								 if(edit_kg3_return.getText().toString().length()==0)
									 edit_kg3_return.setText("0");
								 data.put("blueBagsGiven", edit_kg3_return.getText().toString());
								 
								 if(edit_kg4_return.getText().toString().length()==0)
									 edit_kg4_return.setText("0");
								 data.put("blackBagsGiven", edit_kg4_return.getText().toString());
								
								 data.put("vehicleID", userDetails.getString("vehicle_store", "1"));
								 
								 data.put("employeeID", userDetails.getString("employee_store", "1"));
								 
								 data.put("routeID", userDetails.getString("route_store", "1"));
								 
								 if(valBase64==null)
									 valBase64 ="0";
								 data.put("strImage", valBase64);
								 
								 data.put("transDateTime", currentDateandTime);
								 
								 jObjectOptionData1.put(data);

								 String abl = "{\"data\":"+jObjectOptionData1.toString()+"}";

								 new WebserviceCaller().execute(abl);
								 
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		         			
		            	}else{
		            		
			            	Toast.makeText(getApplicationContext(), "Data Stored in Database.", Toast.LENGTH_SHORT).show();
			            	
		            		ContentValues cv = new ContentValues();
		            		cv.put(GarbageDataStorage.QEE_ID_STRING, hospital_id);
		            		
		            		String numberOnly= hospital_id.replaceAll("[^0-9]", "");
							   
		            		cv.put(GarbageDataStorage.QEE_ID, numberOnly);
		            		cv.put(GarbageDataStorage.HOSPITAL_NAME, "abab");
		            		cv.put(GarbageDataStorage.YELLOW_KG, total_YellowWeight.toString());
		            		cv.put(GarbageDataStorage.RED_KG, total_RedWeight.toString());
		            		cv.put(GarbageDataStorage.BLUE_KG, total_BlueWeight.toString());
		            		cv.put(GarbageDataStorage.BLACK_KG, total_BlackWeight.toString());
		            		cv.put(GarbageDataStorage.TOTAL_KG, total_weight.toString());
		            		
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
		            		/*cv.put(GarbageDataStorage.YELLOW_BAGS_GIVEN, edit_kg2_return.getText().toString());
		            		cv.put(GarbageDataStorage.RED_BAGS_GIVEN, edit_kg4_return.getText().toString());
		            		cv.put(GarbageDataStorage.BLUE_BAGS_GIVEN, edit_kg1_return.getText().toString());
		            		cv.put(GarbageDataStorage.BLACK_BAGS_GIVEN, edit_kg3_return.getText().toString());*/
		            		
		            		if(edit_kg1_return.getText().toString().length()==0)
							 	edit_kg1_return.setText("0");
		            		cv.put(GarbageDataStorage.YELLOW_BAGS_GIVEN, edit_kg1_return.getText().toString());

							 
							 if(edit_kg2_return.getText().toString().length()==0)
								 edit_kg2_return.setText("0");
							 cv.put(GarbageDataStorage.RED_BAGS_GIVEN, edit_kg2_return.getText().toString());

							 if(edit_kg3_return.getText().toString().length()==0)
								 edit_kg3_return.setText("0");
							 cv.put(GarbageDataStorage.BLUE_BAGS_GIVEN, edit_kg3_return.getText().toString());

							 if(edit_kg4_return.getText().toString().length()==0)
								 edit_kg4_return.setText("0");
							 cv.put(GarbageDataStorage.BLACK_BAGS_GIVEN, edit_kg4_return.getText().toString());
							 
		            		cv.put(GarbageDataStorage.DATE_TIME, currentDateandTime);
		            		cv.put(GarbageDataStorage.IS_ON_SERVER, "False");
		            		
		            		if(valBase64==null)
								 valBase64 ="0";
		            		cv.put(GarbageDataStorage.IMAGE_STRING, valBase64);
		            		cv.put(GarbageDataStorage.VEHICLE_ID, userDetails.getString("vehicle_store", "1"));
		            		cv.put(GarbageDataStorage.EMPLOYEE_ID, userDetails.getString("employee_store", "1"));
		            		cv.put(GarbageDataStorage.ROUTE_ID, userDetails.getString("route_store", "1"));
		            		
		            		garbageDataStorageObject.open();
		            		garbageDataStorageObject.insert(GarbageDataStorage.DATABASE_DATA_COLLECTED, cv);
		            	
		            		
		            		Intent ite= new Intent(HospitalTwo.this, HomeActivity.class);
		      			  //ite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		      			  startActivity(ite);
		      			  finish();
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
		        
			}
		});
		
		final Button photo = (Button) findViewById(R.id.btn_camera);
        
        photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                 
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(HomeActivity.path.toString()+"/DP.png")));
          	    startActivityForResult(intent, 1337);
          	    
            	}    
            
            });
            
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
					File fileObject = new File(HomeActivity.path.toString()+"/DP.png");
					bitm = decodeFile(fileObject);
					valBase64 = getBase64Value(bitm);
					
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
			 soap = WebService.webService_GuesserAskForHint.callWebservice(getApplicationContext(), params[0]);
		   } catch (Exception e) {
			   e.printStackTrace();
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
				  if(email_id != null){
					  if(bd.getBoolean("hospital_close") == true){
					        new Thread(new Runnable() { 
					            public void run(){
	
					            	 try {   
							               GMailSender sender = new GMailSender("qeevadodara@gmail.com", "Vadodara2003");
							               sender.sendMail("BioMedical Collection Report",   
							            		   "Dear Dr."+ hospital_id +", \n\n Kindly note that your Hospital/Clinic/Lab was closed on Date ="+currentDateandTime+". And Waste collected is below\n Yellow is "+total_YellowWeight+" Kg,\n Red is "+total_RedWeight+" Kg,\n Blue is "+total_BlueWeight+" Kg,\n Black is "+total_BlackWeight+"Kg.\n\n Quantam Environment Enginners.\n Vadodara",
							                       "qeevadodara@gmail.com",   
							                       email_id);   
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
							                       "Dear Dr."+ hospital_id +", \n\n Bio Medical waste from your Hospital/Clinic/Lab is collected. \n Yellow is "+total_YellowWeight+" Kg,\n Red is "+total_RedWeight+" Kg,\n Blue is "+total_BlueWeight+" Kg,\n Black is "+total_BlackWeight+" Kg \n on Date ="+currentDateandTime+". \n\n Quantam Environment Enginners.\n Vadodara",
							                       "qeevadodara@gmail.com",   
							                       email_id);
							           } catch (Exception e) {
							               Log.e("SendMail", e.getMessage(), e);
							           }
					            }
					        }).start();
						}
					}
				  Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
			  }else{
				  Toast.makeText(getApplicationContext(), "Not Successfull", Toast.LENGTH_LONG).show();
			  }
			  
			  Intent ite= new Intent(HospitalTwo.this, HomeActivity.class);
			  //ite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			  startActivity(ite);
			  finish();
		  }

		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
			  dialog = new ProgressDialog(HospitalTwo.this);
			  dialog.setMessage("Please wait...");
		      dialog.show();
		  }
		 }
	
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) HospitalTwo.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
