package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessibilityServiceChangeListener implements AccessibilityManager.AccessibilityStateChangeListener {

    private Context context;

    public AccessibilityServiceChangeListener(Context context) {
        this.context = context;
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        // Handle the accessibility service state change
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        SharedPreferences preferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE);
        List<String> prevActiveServices = Collections.singletonList(preferences.getString("notification_prefs", ""));
        Log.e("LIST1:", prevActiveServices.toString());

        List<String> activeService = new ArrayList<>();
        for (AccessibilityServiceInfo service : enabledServices) {
            // Print or store information about each active accessibility service
            String packageName = service.getResolveInfo().serviceInfo.packageName;
            boolean isAdded = activeService.add(new String(packageName));
            Log.e("Active Service", "Package Name: " + packageName );
        }
        Log.e("LIST2", activeService.toString());
        if(activeService.isEmpty() && prevActiveServices.contains("com.example.myapplication")){
            // send notification
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("msg", "Toast detection service disabled");
            context.startService(serviceIntent);
        }else{
           boolean isMyServiceActive = activeService.contains("com.example.myapplication");
           boolean isMyServicePreviouslyActive = activeService.contains("com.example.myapplication");
           if(!isMyServiceActive && isMyServicePreviouslyActive){
                Intent serviceIntent = new Intent(context, NotificationService.class);
                serviceIntent.putExtra("msg", "Toast detection service disabled");
                context.startService(serviceIntent);
           }
        }
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<>(activeService);
        Log.e("Test", set.toString());
        editor.putStringSet("notification_prefs", set);
//        if (enabled) {
//
//
////            Log.e("LISTENER:","ENABLED" );
//            // Accessibility service is turned ON
//            // Perform any required action here
//            // e.g., Show a message, start a background service, etc.
//            // ...
//        } else {
//            Intent serviceIntent = new Intent(context, NotificationService.class);
//            serviceIntent.putExtra("msg", "Toast detection service disabled");
//            context.startService(serviceIntent);
//            Log.e("LISTENER:","DISABLED" );
//            // Accessibility service is turned OFF
//            // Perform any required action here
//            // e.g., Show a message, stop a background service, etc.
//            // ...
//        }
    }

    public void registerAccessibilityServiceChangeListener() {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
    }

    public void unregisterAccessibilityServiceChangeListener() {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.removeAccessibilityStateChangeListener(this);
    }


}


