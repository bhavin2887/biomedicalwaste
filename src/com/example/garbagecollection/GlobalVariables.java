package com.example.garbagecollection;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Here Global Variables are Defined which are being used throughout the application.
 */
public class GlobalVariables {

	/** Checking the internet is available or not */
	public static boolean isConnected = false;

	/** Used for working with Json or Not */
	public static boolean USING_JSON = true;

	/** Making zero as Constant */
	public static final int CONSTANT_0 = 0;

	/** Making one as Constant */
	public static final int CONSTANT_1 = 1;

	/** Making Minus one as Constant	 */
	public static final int CONSTANT_MINUS1 = -1;

	/** HTTP status code when no server error has occurred */
	public static final int HTTP_STATUS_OK = 200;

	/** Setting DEBUG to false */
	public static final boolean DEBUG = false;

	/**Setting boolean value from intent set  defalut  false */
	public static final boolean Default_False = false ;

	/** Used for matching anyType */
	public final static String anytype="anyType";

	/**
	 * This is a broadcast receiver that will be registered for receiving the network status broadcast from the OS.
	 * and will set the <strong>isConnected</strong> flag to according value.
	 */
	public static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getActiveNetworkInfo();
			Log.i("BroadCastReceiver", "Connected ="+isConnected);
			if(ni!=null && ni.isConnected()){
				isConnected=true;
			}else{
				isConnected=false;
			}
        }
	};
}
