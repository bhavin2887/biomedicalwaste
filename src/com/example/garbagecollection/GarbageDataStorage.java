package com.example.garbagecollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GarbageDataStorage {

	/** Database Opening count is Maintained */
	public static int dbOpenCount=0;

	/** Database Closing count is Maintained */
	public static int dbCloseCount=0;
	
	/** Object used for creating, insertion into Database object */
	private static SQLiteDatabase sqlitedb;
	private final Context context;					// Context for passing in current classes constructor
	private DatabaseHelper DBHelperObject;			// DatabaseHelper object for getting getting object
	
	/** Database Name Declaration */
	public static final String DATABASE_NAME = "Garbage.db";
	
	/** Database Version Number */
	private static final int DATABASE_VERSION = 1;

	/** Inner class object created for synchronizing when RLRDataStorage object is Created */
	private static DatabaseHelper iDbHelper;
	
	// Devices Table Fields


	public static final String DATABASE_MAIN_DATA= "MainData";
	
	public static final String DOC_INCHARGE= "Doc_incharge";
	
	public static final String CREATEDID= "CreatedId";
	
	public static final String PIC_CODE= "Pincode";
	
	public static final String DATABASE_DATA_COLLECTED= "Data_collected";
	public static final String EMPLOYEE= "Employee";
	public static final String ROUTE= "Route";
	public static final String VEHICLE= "Vehicle";
	public static final String DATABASE_START_DATE= "Start_date";
	
	public static final String YELLOW_KG= "Yellow_kg";
	public static final String RED_KG= "Red_kg";
	public static final String BLUE_KG= "Blue_kg";
	public static final String BLACK_KG= "Black_kg";
	public static final String TOTAL_KG= "Total_kg";
	public static final String YELLOW_BAGS_TAKEN= "Yellow_bags_taken";
	public static final String RED_BAGS_TAKEN= "Red_bags_taken";
	public static final String BLUE_BAGS_TAKEN= "Blue_bags_taken";
	public static final String BLACK_BAGS_TAKEN= "Black_bags_taken";
	public static final String YELLOW_BAGS_GIVEN= "Yellow_bags_given";
	public static final String RED_BAGS_GIVEN= "Red_bags_given";
	public static final String BLUE_BAGS_GIVEN= "Blue_bags_given";
	public static final String BLACK_BAGS_GIVEN= "Black_bags_given";
	
	public static final String DATE_TIME= "Date_time";
	public static final String START_DATE_TIME= "Start_date_time";
	public static final String END_DATE_TIME= "End_date_time";
	public static final String START_ID= "Start_ID";
	public static final String IS_ON_SERVER= "IsOnServer";
	public static final String IMAGE_STRING= "ImageString";
	public static final String VEHICLE_ID= "vehicleID";
	public static final String EMPLOYEE_ID= "employeeID";
	public static final String LAT= "lat";
	public static final String ISOPENCLOSE= "isOpenClose";
	public static final String LON= "lon";
	public static final String ROUTE_ID= "routeID";
	
	public static final String CORRECTANSWER= "CorrectAnswer";
	
	public static final String DISTRICT= "District";
	
	public static final String QEE_ID= "QEE_id";
	public static final String QEE_ID_STRING= "strQEEID";
	public static final String TALLUKA= "Talluka";
	public static final String HOSPITAL_NAME= "Hospital_name";
	public static final String AREA= "Area";
	public static final String IMAGEUPLOAD= "ImageUpload";
	public static final String ADDRESS= "Address";
	public static final String TELEPHONE= "Telephone";
	public static final String INCHARGE_MOBILE= "Incharge_mobile";
	public static final String NO_OF_BED= "No_of_bed";
	public static final String CERTIFICATE_EXPIRY= "Certificate_expiry";

	public static final String GPCB_ID= "GPCB_ID";

	public static final String PASSWORD= "Password";
	public static final String AUTH_NO= "Auth_no";
	public static final String AUTH_EXPIRY_DATE= "Auth_exp_date";
	public static final String STATUS= "Status";
	public static final String ROUTE_NO= "Route_no";
	public static final String FREQUENCY= "Frequency";
	public static final String R_SEQUENCE= "R_Sequence";
	public static final String CAT= "Cat";
	public static final String Email_Id= "Email_id";
	public static final String Service_Start_From= "Service_start_from";
	public static final String Closed_From= "Closed_from";

	
	/** Database Object for Singleton */
	private static GarbageDataStorage instance;
	/**
	 * Getting object of DatabaseHelper
	 * @param ctx
	 */
	public GarbageDataStorage(Context ctx) {
		Log.i("RLRDataStorage","Context");
		this.context = ctx;
		DBHelperObject = new DatabaseHelper(context);
		synchronized (DATABASE_NAME) {
			if (iDbHelper == null)
				iDbHelper = new DatabaseHelper(ctx);
			}
	}

	/**
	 * Opens the database
	 * @return RLRDataStorage object
	 * @throws SQLException
	 */
	public GarbageDataStorage open() {
		try {
			dbOpenCount = dbOpenCount + 1;
			sqlitedb = DBHelperObject.getWritableDatabase();
			System.out.println("RLRDataStorage.open()"+dbOpenCount);
		} catch (SQLException ex) {
			sqlitedb = DBHelperObject.getReadableDatabase();
			Log.d("log_tag", "Exception is Thrown open get Readable");
			ex.printStackTrace();
		}
		return this;
	}

	/**
	 * Closes the database
	 * @return RLRDataStorage object
	 */
	public void close() {
			DBHelperObject.close();
			System.out.println("RLRDataStorage.close()"+dbCloseCount);
	}
	
	
	private static final String TABLE_EMP = "CREATE TABLE "+EMPLOYEE+" (employee_id text, isActive text);";
	private static final String TABLE_VEH = "CREATE TABLE "+VEHICLE+" (vehicle_id text, isActive text);";
	private static final String TABLE_ROU= "CREATE TABLE "+ROUTE+" (route_id text, isActive text);";
	
		// Creating Devices table
		private static final String TABLE_DATA_COLLECTED = "CREATE TABLE "
			+ DATABASE_DATA_COLLECTED + " ("
			+ QEE_ID_STRING + " text,"
			+ QEE_ID + " text,"
			+ HOSPITAL_NAME + " text,"
			+ YELLOW_KG + " float DEFAULT 0,"
			+ RED_KG + " float DEFAULT 0,"
			+ BLUE_KG + " float DEFAULT 0,"
			+ BLACK_KG + " float DEFAULT 0,"
			+ TOTAL_KG + " float DEFAULT 0,"
			+ YELLOW_BAGS_TAKEN + " int DEFAULT 0,"
			+ RED_BAGS_TAKEN + " int DEFAULT 0,"
			+ BLUE_BAGS_TAKEN + " int DEFAULT 0,"
			+ BLACK_BAGS_TAKEN + " int DEFAULT 0,"
			+ YELLOW_BAGS_GIVEN + " int DEFAULT 0,"
			+ RED_BAGS_GIVEN + " int DEFAULT 0,"
			+ BLUE_BAGS_GIVEN + " int DEFAULT 0,"
			+ BLACK_BAGS_GIVEN + " int DEFAULT 0,"
			+ DATE_TIME + " date,"
			+ IS_ON_SERVER + " text,"
			+ IMAGE_STRING + " text,"
			+ VEHICLE_ID + " text,"
			+ EMPLOYEE_ID + " text,"
			+ LAT + " float DEFAULT 0,"
			+ LON + " float DEFAULT 0,"
			+ ISOPENCLOSE + " text,"
			+ ROUTE_ID + " text);";
		
		// Creating Devices table
				private static final String TABLE_START_DATE = "CREATE TABLE "
					+ DATABASE_START_DATE + " ("
					+ START_DATE_TIME + " text,"
					+ START_ID + " text,"
					+ END_DATE_TIME + " text, employee_id text, vehicle_id text, route_id text, handset_id text,"
					+ IS_ON_SERVER + " text);";

	/**
	singleton BamDataStorage.
	 *
	 * @param context
	 * @return BamDataStorage database instance.
	 */
	public static GarbageDataStorage GetFor(Context context) {
		if (instance == null)
			instance = new GarbageDataStorage(context);
		// if (!instance.isOpen())
		// instance.open();
		return instance;
	}
	
	/**
	 * Creation of Database Tables and Upgradation is done.
	 * @author Bhavin
	 *
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		/**
		 * Get records in a particular table according to sql query
		 * @param tablename
		 * @return a cursor object pointed to the record(s) selected by sql query.
		 */
		public synchronized static Cursor getRecordBySelection(String tablename) {
			return sqlitedb.query(tablename, null, null, null, null, null,null);
		}

		/**
		 * Constructor created for DatabaseHelper
		 * @param context
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		//	Database Tables are created
		@Override
		public void onCreate(SQLiteDatabase db) {
			//db.execSQL(DATABASE_TEST);			
			db.execSQL(TABLE_EMP);
			db.execSQL(TABLE_ROU);
			db.execSQL(TABLE_VEH);
			db.execSQL(TABLE_DATA_COLLECTED);
			db.execSQL(TABLE_START_DATE);
			
			//Log.i("DATABASE_TEST",""+DATABASE_TEST);
			//Log.i("DATABASE_DETAIL","Creation = "+TABLE_EMP);
			//Log.i("DATABASE_DETAIL","Creation = "+TABLE_ROU);
			//Log.i("DATABASE_DETAIL","Creation = "+TABLE_VEH);
			//Log.i("DATABASE_DETAIL","Creation = "+TABLE_DATA_COLLECTED);
			//Log.i("DATABASE_DETAIL","Creation = "+TABLE_START_DATE);

		}
		
		// Database Upgradation is done
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("BAMDB", "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			//db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TEST);			
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMP);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_ROU);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_VEH);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_DATA_COLLECTED);	
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_START_DATE);	

			onCreate(db);
		}

	}

	/**
	 * Insert a record into particular table
	 *
	 * @param tablename
	 * @param values
	 * @returnthe row ID of the newly inserted row, or -1 if an error occurred
	 */
	public synchronized long insert(String tablename, ContentValues values) {
		return sqlitedb.insert(tablename, null, values);
	}
	/**
	 * Getting Profile Value from the Database
	 * @return cursor
	 */
	public Cursor getSkillCodeQuestionId(String Value, int item) {
		return sqlitedb.rawQuery("select * from "+DATABASE_DATA_COLLECTED+" where option = '"+Value+"' and "+DOC_INCHARGE+" ='"+item+"'", null);
	}
	public void deleteAllData(String table){
		sqlitedb.delete(table,null,null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getTableData(String type) {
		return sqlitedb.rawQuery("select * from "+type, null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getTotalRows() {
		return sqlitedb.rawQuery("select * from "+DATABASE_MAIN_DATA, null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getCountAnswer(int answer) {
		return sqlitedb.rawQuery("select count(*) from "+DATABASE_MAIN_DATA+" where "+PIC_CODE+"= '"+answer+"' and "+TELEPHONE+" = '1'", null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getCountIsScoreThisQuestion() {
		return sqlitedb.rawQuery("select count(*) from "+DATABASE_MAIN_DATA+" where "+TELEPHONE+" = '0'", null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getQuestionByType(int type) {
		return sqlitedb.rawQuery("select * from "+DATABASE_MAIN_DATA+" where "+DISTRICT+"='"+ type +"'", null);
	}
	/**
	 * Setting Default Profile to set 0 from 1
	 * @return cursor
	 */
	public int correctRadioButton(int skillCodeID, int score) {
		ContentValues args = new ContentValues();
		args.put(PIC_CODE, score);
		return sqlitedb.update(DATABASE_MAIN_DATA,args,DOC_INCHARGE+" ='"+skillCodeID+"'",null);
	}
	/**
	 * Setting Default Profile to set 0 from 1
	 * @return cursor
	 */
	public int IsTestComplete() {
		ContentValues args = new ContentValues();
		args.put(CERTIFICATE_EXPIRY, 1);
		return sqlitedb.update(DATABASE_MAIN_DATA,args,DOC_INCHARGE+" >=1",null);
	}
	
	public Cursor getDataNotOnServer(String database_name) {
		// Select All Query
		String selectQuery = "SELECT * FROM " + database_name + " where "+ IS_ON_SERVER + " = 'False'";
		// SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}
	
	//UPDATE Start_date SET IsOnServer = 'True' where IsOnServer = 'False' and Start_date_time = '04-11-2015 23:26:03'
	public int UpdateStartDate(String time, String val_id){
		try {
			ContentValues args = new ContentValues();
			args.put(START_ID,val_id);
			args.put(IS_ON_SERVER,"True");
			String val = IS_ON_SERVER+"='False' and "+ START_DATE_TIME +"='"+time+"'";
			Log.i("UpdateStartDate"+val, "UpdateStartDate="+val_id);
			return sqlitedb.update(DATABASE_START_DATE,args,val,null);
		} catch (SQLException e) {
			
		}
		return 0;
	}
	
	public Cursor updateUploadedFile() {

		String query = "UPDATE "+DATABASE_DATA_COLLECTED+" SET "+IS_ON_SERVER+" = 'True' where "+IS_ON_SERVER+" = 'False'";
		Cursor cu = sqlitedb.rawQuery(query, null);
		cu.moveToFirst();
		cu.close();

		return cu;
	}
	    
}
