package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.app.NotificationCompat;

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
            Log.e(TAG, packageName);
            String text = accessibilityEvent.getText().get(0).toString();
            Intent intent = new Intent(getApplicationContext(), TextToSpeechService.class);
            intent.putExtra("text", text);
            startService(intent);
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "OnInterrupt something went wrong : ");
    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceChangeListener accessibilityServiceChangeListener = new AccessibilityServiceChangeListener(this);
        accessibilityServiceChangeListener.registerAccessibilityServiceChangeListener();
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

    public Notification createNotification(String msg) {
        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel_id";
            String channelName = "My Channel";
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            }
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(channel);
                }
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setContentTitle("Text-to-Speech Service")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        return builder.build();
    }
}
