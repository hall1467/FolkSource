/* Copyright (c) 2006-2011 Regents of the University of Minnesota.
   For licensing terms, see the file LICENSE.
 */

package com.citizensense.android.conf;

/** Configuration constants
 * @author Phil Brown
 */
public interface Constants {

	/** This tag is used to determine whether or not to print a Log statement
	 * in many places throughout the app.*/
	public static final boolean DEBUG=true;
	
	/** The name of the sharedPreferences file where G.memory is located.*/
	public static final String MEM_LOC = "citizenSenseMem";
	
	/** Reference to a sharedPreferences item in MEM_LOC*/
	public static final String DB_SIZE = "db_size";
	
	public static final String DB_DATE_FORMAT = "MM/dd/yyyy";

	public static final long SERVICE_GPS_TIME_BETWEEN_UPDATES = 0;//should be updated

	public static final float MIN_DISTANCE_BETWEEN_UPDATES = 0;//should be updated

	public static final boolean EXACT_LOCATION = true;

	public static final boolean NONEXACT_LOCATION = false;
	
	
	public static final int NO_EXPIRATION = -1;
}//Constants
