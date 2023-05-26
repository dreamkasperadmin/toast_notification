package com.example.myapplication;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    private String text;
    private Boolean isTTSReady=false;

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
            this.text=text;
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
                isTTSReady=true;
                speak(text);
            }
        } else {
            Log.e("TTS", "Initialization failed.");
        }
    }

    private void speak(String text) {
        if (tts != null) {
            if(!isTTSReady){
                return;
            }
            // Speak the provided text
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);

        }
    }
}



