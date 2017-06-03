package com.sow.brazilianportuguese.free;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import com.sow.brazilianportuguese.free.listeners.SpeechActivityDetected;

import java.util.HashMap;
import java.util.Locale;

public class MyApplication extends Application implements
        TextToSpeech.OnInitListener {

    private final static String TAG = "MyApplication";
    private Context context;
    private TextToSpeech tts;
    private HashMap<String, String> params = new HashMap<String, String>();
    private SpeechActivityDetected speechActivityDetected;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        try {
            tts = new TextToSpeech(this, this);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "inglesparaviagem");
            speechActivityDetected = new SpeechActivityDetected();
        } catch (Exception e) {

        }
    }

    @Override
    public void onInit(int status) {
        try {
            Log.i(TAG, "onInit()");
            if (status == TextToSpeech.SUCCESS) {
                if (isEnglishLanguageAvaialbe(tts.setLanguage(new Locale("pt", "BR")))) {
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

                        @Override
                        public void onStart(String s) {
                            Log.i(TAG, "onStart()");
                            speechActivityDetected.doEvent("startSpeech");
                        }

                        @Override
                        public void onDone(String s) {
                            Log.i(TAG, "onDone()");
                            speechActivityDetected.doEvent("stopSpeech");
                        }

                        @Override
                        public void onError(String s) {
                        }
                    });
                } else {
                    Toast.makeText(this, "Desculpe, o text-to-speech idioma Inglês não está disponível.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Há um problema com seu mecanismo text-to-speech.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    public boolean isEnglishLanguageAvaialbe(int i) {
        if (i == TextToSpeech.LANG_MISSING_DATA || i == TextToSpeech.LANG_NOT_SUPPORTED)
            return false;
        else
            return true;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TextToSpeech getTts() {
        return tts;
    }

    public void setTts(TextToSpeech tts) {
        this.tts = tts;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public SpeechActivityDetected getSpeechActivityDetected() {
        return speechActivityDetected;
    }

    public void setSpeechActivityDetected(SpeechActivityDetected speechActivityDetected) {
        this.speechActivityDetected = speechActivityDetected;
    }
}