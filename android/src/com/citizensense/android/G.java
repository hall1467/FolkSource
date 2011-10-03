/* Copyright (c) 2006-2011 Regents of the University of Minnesota.
   For licensing terms, see the file LICENSE.
 */

package com.citizensense.android;

import android.content.Context;

import com.citizensense.android.db.CsDbAdapter;
import com.google.android.maps.MapView;

/**
 * This class allows static access to many of the most-used components of 
 * Citizen Sense. This is similar to Cyclopath's G.java.
 * @author Phil Brown
 */

public class G {
	/** The Citizen Sense map*/
	protected static MapView map;
	/** The currently logged-in user*/
	protected static User user;
	/** The DatabaseAdapter that handles most of the database transactions*/
	public static CsDbAdapter db;//FIXME make protected
	/** The application context*/
	public static Context app_context;
}//G