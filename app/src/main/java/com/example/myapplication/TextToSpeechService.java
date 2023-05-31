package com.example.myapplication;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.speech.tts.TextToSpeech;
//import android.speech.tts.UtteranceProgressListener;
//import android.util.Log;
//
//import java.util.HashMap;
//import java.util.Locale;
//
//public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener {
//    private TextToSpeech textToSpeech;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        textToSpeech = new TextToSpeech(this, this);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && intent.getExtras() != null) {
//            String text = intent.getStringExtra("text");
//            speak(text);
//        }
//
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//            int result = textToSpeech.setLanguage(Locale.US);
//
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "Language not supported");
//            } else {
//                // TTS initialization successful
//                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
//                    @Override
//                    public void onStart(String utteranceId) {
//                        // Utterance started
//                    }
//
//                    @Override
//                    public void onDone(String utteranceId) {
//                        // Utterance completed
//                        stopSelf(); // Stop the service after completion
//                    }
//
//                    @Override
//                    public void onError(String utteranceId) {
//                        // Utterance error
//                        stopSelf(); // Stop the service on error
//                    }
//                });
//            }
//        } else {
//            Log.e("TTS", "Initialization failed");
//            stopSelf(); // Stop the service on initialization failure
//        }
//    }
//
//    private void speak(String text) {
//        HashMap<String, String> params = new HashMap<>();
//        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageId");
//
//        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
//    }
//
//    @Override
//    public void onDestroy() {
//        if (textToSpeech != null) {
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//        }
//        super.onDestroy();
//    }
//}

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Locale;

public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We won't be using binding, so return null
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            String text = intent.getStringExtra("text");
            speak(text);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Shutdown the TTS engine when the service is destroyed
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for TTS
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported.");
            } else {
                // TTS engine initialized successfully
//                speak("Text-to-speech service is ready.");
            }
        } else {
            Log.e("TTS", "Initialization failed.");
        }
    }

    private void speak(String text) {
        if (tts != null) {
            // Speak the provided text
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
    }


}



