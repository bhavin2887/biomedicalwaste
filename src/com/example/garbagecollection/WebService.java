package com.example.garbagecollection;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class WebService {

	
	private static final String NAMESPACE="http://tempuri.org/";
	
	private static final String URL="http://60.254.38.13:8080/StorageWebService.asmx";
	
	//private static final String URL="http://quantumenvironment.in/StorageWebService.asmx";
	//private static final String URL="http://quantum.mtajsolutions.com/storagewebservice.asmx";
	
	public static class webService_GuesserAskForHint{
		
		private static final String SOAP_ACTION="http://tempuri.org/SaveStorageDetails";
		private static final String METHOD_NAME="SaveStorageDetails";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;

		
		public static SoapObject callWebservice(Context con,String val){
			
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;
			
			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			
			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
				return deserializeSoap((SoapObject)envelope.bodyIn);
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("SaveStorageDetailsResult")){
				soapObject = (SoapObject) paramObject.getProperty("SaveStorageDetailsResult");
					/*String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();*/
			}
			return soapObject;
		}
	}
	
public static class webService_Setting{
		
		private static final String SOAP_ACTION="http://tempuri.org/GetAllActiveData";
		private static final String METHOD_NAME="GetAllActiveData";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;

		
		public static SoapObject callWebservice(){
			
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;
			
			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			
			envelope.setOutputSoapObject(requestSoap);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
		}
	}

	public static class webService_CheckHospital{
		
		private static final String SOAP_ACTION="http://tempuri.org/CheckHospitalIsActive";
		private static final String METHOD_NAME="CheckHospitalIsActive";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;

		
		public static SoapObject callWebservice(Context con,int val){
			
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;
			
			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			
			requestSoap.addProperty("QEEID", val);

			envelope.setOutputSoapObject(requestSoap);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		/*public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("SaveStorageDetailsResult")){
				soapObject = (SoapObject) paramObject.getProperty("SaveStorageDetailsResult");
					String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();
			}
			return soapObject;
		}*/
	}

public static class webService_SaveDailyAssignment{
		
		private static final String SOAP_ACTION="http://tempuri.org/SaveDailyAssignment";
		private static final String METHOD_NAME="SaveDailyAssignment";
		public static SoapObject soapObject;
		public static SoapObject soapObjectInner;
		public static SoapObject soapObjectInnerTable;

		
		public static SoapObject callWebservice(Context con,String val){
			
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.implicitTypes = true;
			envelope.dotNet=true;
			
			SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
			
			requestSoap.addProperty("jSONString", val);

			envelope.setOutputSoapObject(requestSoap);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
			try{
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (androidHttpTransport.debug){
					Log.d("ws", androidHttpTransport.requestDump);
				}
				return (SoapObject)envelope.bodyIn;
			}catch(Exception excp){
				excp.printStackTrace();
			}
			return null;
			//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
		}
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
		/*public static SoapObject deserializeSoap(SoapObject paramObject){
			if(paramObject.hasProperty("SaveStorageDetailsResult")){
				soapObject = (SoapObject) paramObject.getProperty("SaveStorageDetailsResult");
					String count =  soapObject.getProperty("respCount").toString();
					String isSucess =  soapObject.getProperty("isSuccess").toString();
					String msg =  soapObject.getProperty("errorMsg").toString();
			}
			return soapObject;
		}*/
	}

public static class webService_HelloWorld{
	
	private static final String SOAP_ACTION="http://tempuri.org/HelloWorld";
	private static final String METHOD_NAME="HelloWorld";
	public static String soapString;
	public static SoapObject soapObjectInner;
	public static SoapObject soapObjectInnerTable;

	
	public static String callWebserviceCheck(Context con){
		
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.implicitTypes = true;
		envelope.dotNet=true;
		
		SoapObject requestSoap=new SoapObject(NAMESPACE, METHOD_NAME);
		
		//requestSoap.addProperty("jSONString", val);

		envelope.setOutputSoapObject(requestSoap);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
		try{
			androidHttpTransport.call(SOAP_ACTION, envelope);
			if (androidHttpTransport.debug){
				Log.d("ws", androidHttpTransport.requestDump);
			}//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg=Object reference not set to an instance of an object.,InnerEx:; }; }
			return deserializeSoap((SoapObject)envelope.bodyIn);
		}catch(Exception excp){
			excp.printStackTrace();
		}
		return null;
		//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=0; isSuccess=false; errorMsg= is not a valid value for Int32.,InnerEx:System.IndexOutOfRangeException: Index was outside the bounds of the array.
	}
	//SaveStorageDetailsResponse{SaveStorageDetailsResult=anyType{respCount=1; isSuccess=true; errorMsg=All Records Saved Successfully; }; }
	public static String deserializeSoap(SoapObject paramObject){
		if(paramObject.hasProperty("HelloWorldResult")){
			//String SObject = (String) paramObject.getProperty("HelloWorldResult");
			soapString =  paramObject.getPropertyAsString("HelloWorldResult");
				/*String count =  soapObject.getProperty("respCount").toString();
				String isSucess =  soapObject.getProperty("isSuccess").toString();
				String msg =  soapObject.getProperty("errorMsg").toString();*/
		}
		return soapString;
	}
}



}
	/*public static class Webservice_GetTechnicianAvailableWorkOrders {
		// private static final String URL =
		// "http://api.ogoing.com/user.asmx?op=ValidUser";
		private static final String URL = "http://192.168.1.21/ilneas/services/iLearnNEarnAS.asmx";
		private static final String SOAP_ACTION_NEW = "http://tempuri.org/GetAllPackages";

		public static String GetTechnicianAvailableWorkOrders(String uname, String pwd,
				String AppName, int iPlatformCode, int UserRole, String IsVerizonUser,Context con) {

			String ValidLogin = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soap:Header>"
					+ "<AuthHeader xmlns=\"http://tempuri.org/\">"
					+ "<Username>" + uname + "</Username>" + "<Password>" + pwd
					+ "</Password>" + "</AuthHeader>" + "</soap:Header>"
					+ "<soap:Body>"
					+ "<GetAllPackages xmlns=\"http://tempuri.org/\">"
					+ "<AppName>" + AppName + "</AppName>" + "<iPlatformCode>" + iPlatformCode
					+ "</iPlatformCode>" + "<UserRole>" + UserRole + "</UserRole>"
					+ "<IsVerizonUser>" + IsVerizonUser + "</IsVerizonUser>"+ "</GetAllPackages>"
					+ "</soap:Body>" + "</soap:Envelope>";

			DefaultHttpClient httpClient = new DefaultHttpClient();
			String envoloper = String.format(ValidLogin);
			// request parameters
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 30000);
			HttpConnectionParams.setSoTimeout(params, 60000);
			// set parameter
			HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);
			// POST the envelope
			HttpPost httppost = new HttpPost(URL);
			// add headers
			// Set Method Action
			httppost.setHeader("soapaction", SOAP_ACTION_NEW);
			httppost.setHeader("Content-Type", "text/xml; charset=utf-8");
			String response = "";
			try {
				// the entity holds the request
				HttpEntity entity = new StringEntity(envoloper);
				httppost.setEntity(entity);
				// Response Handler
				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
					@Override
					public String handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {
						// get response entity
						HttpEntity entity = response.getEntity();
						// read the response as byte array
						StringBuffer out = new StringBuffer();
						byte[] b = EntityUtils.toByteArray(entity);
						// write the response byte array to a string buffer
						out.append(new String(b, 0, b.length));
						return out.toString();
					}
				};
				// Getting the response
				response = httpClient.execute(httppost, responseHandler);
				
				Log.i("","temp ="+response);
				Toast.makeText(con, "Parsed", Toast.LENGTH_LONG).show();
			} catch (Exception exc) {
				Toast.makeText(con, "Invalid", Toast.LENGTH_LONG).show();
				exc.printStackTrace();
			}
			return response;
		}
	}*/