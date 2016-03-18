package org.bluetooth.bledemo.base;

import android.app.Application;

public class TrackingApplicationVisibility extends Application {
	
	
	//This will not function properly if not added to every activity class
	
		public static boolean isActivityVisible() {
		    return activityVisible;
		  }  

		  public static void activityResumed() {
		    activityVisible = true;
		  }

		  public static void activityPaused() {
		    activityVisible = false;
		  }

		  private static boolean activityVisible;

}
