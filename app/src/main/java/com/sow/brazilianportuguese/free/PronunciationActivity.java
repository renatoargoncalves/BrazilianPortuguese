package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sow.brazilianportuguese.free.listeners.OnSpeechEventDetected;


public class PronunciationActivity extends AppCompatActivity {

    private EditText editText_pronunciation;
    private ImageButton button_pronunciation_speak, button_pronunciation_stop;
    private LinearLayout linearLayout_pronunciation_speak;
    private MyApplication myApplication;
    private String TAG = "PronunciationActivity";
    private TextView textView_pronunciation_speak_eng;
//    private AdView adView;
//    private AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation);

        myApplication = (MyApplication) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pronunciation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Practice your pronunciation!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        editText_pronunciation = (EditText) findViewById(R.id.editText_pronunciation);
        button_pronunciation_speak = (ImageButton) findViewById(R.id.button_pronunciation_speak);
        button_pronunciation_stop = (ImageButton) findViewById(R.id.button_pronunciation_stop);
        linearLayout_pronunciation_speak = (LinearLayout) findViewById(R.id.linearLayout_pronunciation_speak);
        textView_pronunciation_speak_eng = (TextView) findViewById(R.id.textView_pronunciation_speak_eng);

        button_pronunciation_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_pronunciation.getWindowToken(), 0);
                textView_pronunciation_speak_eng.setText(editText_pronunciation.getText().toString());
                linearLayout_pronunciation_speak.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myApplication.getTts().speak(editText_pronunciation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "brazilianportuguese");
                } else {
                    myApplication.getTts().speak(editText_pronunciation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        myApplication.getSpeechActivityDetected().setOnEventListener(new OnSpeechEventDetected() {
            @Override
            public void onEvent(String event) {
                if (event.equals("startSpeech")) {
                    setupButtonsForSpeech();
                } else if (event.equals("stopSpeech")) {
                    setupButtonsForSilence();
                } else {

                }
            }
        });

//        try {
//            Log.i(TAG, "adView - preparing");
//
//            adView = (AdView) findViewById(R.id.adView);
//
//            adView.setVisibility(View.GONE);
//            adRequest = new AdRequest.Builder()
////                    .addTestDevice("C6E27E792E9C776653A67DDF90F3CB03")
//                    .build();
//
//            adView.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    Log.i(TAG, "AdLoaded");
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            adView.setVisibility(View.VISIBLE);
//                        }
//                    }, 1000);
//                }
//
//                public void onAdFailedToLoad(int errorcode) {
//                    Log.i(TAG, "AdFailedToLoad: "+errorcode);
//                    adView.setVisibility(View.GONE);
//                }
//
//                public void onAdOpened() {
//                    Log.i(TAG, "AdOpened");
//                }
//
//                public void onAdClosed() {
//                    Log.i(TAG, "AdClosed");
//                    adView.setVisibility(View.GONE);
//                }
//
//                public void onAdLeftApplication() {
//                    Log.i(TAG, "AdLeftApplication");
//                    adView.setVisibility(View.GONE);
//                }
//            });
//            adView.loadAd(adRequest);
//        } catch (Exception e) {
//            Log.e(TAG, "Exception - adView: " + e.getLocalizedMessage());
//        } finally {
//            adView.setVisibility(View.GONE);
//            Log.i(TAG, "adView - finished");
//        }

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void setupButtonsForSpeech() {
        Log.i(TAG, "setupButtonsForSpeech");
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
//                linearLayout_pronunciation_speak.setVisibility(View.VISIBLE);
            } });
    }

    private void setupButtonsForSilence() {
        Log.i(TAG, "setupButtonsForStop");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                                        InputMethodManager.HIDE_IMPLICIT_ONLY);

                        linearLayout_pronunciation_speak.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onPause() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_pronunciation.getWindowToken(), 0);

        super.onPause();
    }

    @Override
    public void onBackPressed() {
//        adView.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
