package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.view.View.GONE;

public class DictionaryActivity extends AppCompatActivity {

    private String TAG = "DictionaryActivity";
    private EditText editText_word;
    private ImageButton imageButton_query;
    private RecyclerView recyclerView_listResults;
    private ArrayAdapter resultsArray;
    private LinearLayout linearLayout_progress_bar;
    private LinearLayout linearLayout_results;
    private DictionaryAdapter dictionaryAdapter;
//    private AdView adView;
//    private AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dictionary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Online dictionary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        editText_word = (EditText) findViewById(R.id.editText_word);
        editText_word.requestFocus();
        editText_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        imageButton_query = (ImageButton) findViewById(R.id.imageButton_query);
        imageButton_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
            }
        });

        linearLayout_progress_bar = (LinearLayout) findViewById(R.id.linearLayout_progress_bar);
        linearLayout_progress_bar.setVisibility(GONE);
        linearLayout_results = (LinearLayout) findViewById(R.id.linearLayout_results);
        linearLayout_results.setVisibility(GONE);

        RecyclerView.LayoutManager layoutManager_phrases = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_listResults = (RecyclerView) findViewById(R.id.recyclerView_results);
        recyclerView_listResults.setLayoutManager(layoutManager_phrases);

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

    }

    private void doSearch() {
        linearLayout_progress_bar.setVisibility(View.VISIBLE);
        AsyncTask_queryDic asyncTask_queryDic = new AsyncTask_queryDic(editText_word.getText().toString());
        asyncTask_queryDic.execute();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_word.getWindowToken(), 0);

    }


    private class AsyncTask_queryDic extends AsyncTask {

        private String word;
        private ArrayList<String> resultsSplited;
        private StringBuffer stringBuffer;

        public AsyncTask_queryDic(String word) {
            this.word = word;
            Log.i(TAG, "AsyncTask_queryDic: " + editText_word.getText().toString());
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(TAG, "doInBackground.");
            HttpURLConnection connection;
            try {
                URL url = new URL("http://www.realtest.com.br/ingles-para-viagem/query_dic.php?word=" + word + "");

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();

                String line = new String();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                inputStream.close();
                connection.disconnect();

                Log.i(TAG, "Response: " + stringBuffer.toString());

//                resultsSplited = new ArrayList<>();
//                String[] results = stringBuffer.toString().split(",");
//                for (int i = 0; i < results.length; i++) {
//                    resultsSplited.add(results[i].replace("] ","]\n"));
//                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("DETAILS", "MalformedURLException : " + e.getMessage());
            } catch (IOException e) {
                Log.e("DETAILS", "IOException : " + e.getMessage());
            } catch (Exception e) {
                Log.e("DETAILS", "Exception AsyncTask_queryDic:doInBackground " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                super.onPostExecute(o);
                resultsArray = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, resultsSplited);
                dictionaryAdapter = new DictionaryAdapter(DictionaryActivity.this, stringBuffer.toString());
                recyclerView_listResults.setAdapter(dictionaryAdapter);
                linearLayout_progress_bar.setVisibility(View.GONE);
                linearLayout_results.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Log.e(TAG, "Exception: onPostExecute" + e.getMessage());
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(linearLayout_results.getVisibility() == View.VISIBLE)
            linearLayout_results.setVisibility(GONE);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_word.getWindowToken(), 0);
//        adView.setVisibility(View.GONE);
        super.onPause();
    }
}
