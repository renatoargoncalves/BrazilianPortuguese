package com.sow.brazilianportuguese.free;

import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import com.sow.brazilianportuguese.free.CategoryProvider.Category;
import com.sow.brazilianportuguese.free.listeners.OnSpeechEventDetected;

public class PhrasesActivity extends AppCompatActivity {

    private Category category;
    private int category_id = 0;
    private LinearLayoutManager layoutManager_phrases;
    private PhraseAdapter phraseAdapter;
    private Dialog dialog;
    private MyApplication myApplication;
    private String TAG = "PhrasesActivity";
    private ImageButton imageButton_play, imageButton_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        myApplication = (MyApplication) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category_phrases);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        category_id = getIntent().getIntExtra("category_id", 0);
        category = new CategoryProvider(this).getCategoryArrayList().get(category_id);

        getSupportActionBar().setTitle(category.getTitle());


        layoutManager_phrases = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView_phrases = (RecyclerView) findViewById(R.id.recyclerView_category_phrases);
        recyclerView_phrases.setLayoutManager(layoutManager_phrases);
        phraseAdapter = new PhraseAdapter(this, category.getIdentifier(), false);
        recyclerView_phrases.setAdapter(phraseAdapter);
        phraseAdapter.setOnItemClickListener(onItemClickListener);

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
    }

    private void setupButtonsForSpeech() {
        Log.i(TAG, "setupButtonsForSpeech");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageButton_play.setVisibility(View.GONE);
                imageButton_stop.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupButtonsForSilence() {
        Log.i(TAG, "setupButtonsForStop");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageButton_play.setVisibility(View.VISIBLE);
                imageButton_stop.setVisibility(View.GONE);
            }
        });
    }


    PhraseAdapter.OnItemClickListener onItemClickListener = new PhraseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            showSpeakDialog(phraseAdapter.getPhrasesArrayList().get(position).getEng(), phraseAdapter.getPhrasesArrayList().get(position).getPort());
        }
    };

    private void showSpeakDialog(String eng, String port) {

        dialog = new Dialog(PhrasesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_speak);

        TextView textView_dialog_eng = (TextView) dialog.findViewById(R.id.textView_dialog_eng);
        textView_dialog_eng.setText("(" + eng + ")");

        final TextView textView_dialog_port = (TextView) dialog.findViewById(R.id.textView_dialog_port);
        textView_dialog_port.setText(port);

        speak(textView_dialog_port.getText().toString());

        imageButton_play = (ImageButton) dialog.findViewById(R.id.imageButton_play);
        imageButton_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak(textView_dialog_port.getText());
            }
        });

        imageButton_stop = (ImageButton) dialog.findViewById(R.id.imageButton_stop);
        imageButton_stop.setVisibility(View.GONE);
        imageButton_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myApplication.getTts().isSpeaking())
                    myApplication.getTts().stop();
            }
        });

        dialog.show();
    }

    private void speak(final CharSequence text) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myApplication.getTts().speak(text, TextToSpeech.QUEUE_FLUSH, null, "inglesparaviagem");
                } else {
                    myApplication.getTts().speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }, 500);
    }

}
