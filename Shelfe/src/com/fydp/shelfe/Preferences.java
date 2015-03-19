package com.fydp.shelfe;

import android.content.SharedPreferences;

public class Preferences {

    static SharedPreferences mSettings;
    
    
    public Preferences(SharedPreferences settings) {
        mSettings = settings;
    }
    public static int getThreshold(){
    	return Integer.parseInt((mSettings.getString("notificationThreshold", "50%").split("%")[0]));
    }
    
    public static boolean getAdminMode(){
    	return mSettings.getBoolean("adminMode", false);
    }
   /*public boolean isMetric() {
        return mSettings.getString("units", "imperial").equals("metric");
    }
    
    public float getStepLength() {
        try {
            return Float.valueOf(mSettings.getString("step_length", "20").trim());
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }
    
    public float getBodyWeight() {
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim());
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }

    public boolean isRunning() {
        return mSettings.getString("exercise_type", "running").equals("running");
    }
    
    //
    // Internal
    
    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", Utils.currentTimeInMillis());
        editor.commit();
    }
    
    public void saveServiceRunningWithNullTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }
    
    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < Utils.currentTimeInMillis() - 1000*60*10;
    }*/

}
