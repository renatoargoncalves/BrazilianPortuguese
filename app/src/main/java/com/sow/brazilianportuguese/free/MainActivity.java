package com.sow.brazilianportuguese.free;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.plus.PlusOneButton;
import com.sow.brazilianportuguese.free.listeners.OnSpeechEventDetected;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sow.brazilianportuguese.free.R.id.imageButton_play;
import static com.sow.brazilianportuguese.free.R.id.imageButton_stop;
import static com.sow.brazilianportuguese.free.R.id.linearLayout_main_adView;
import static com.sow.brazilianportuguese.free.R.id.recyclerView_categories_list;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SearchView searchView;
    private LinearLayoutManager layoutManager_search;
    private RecyclerView recyclerView_search;
    private LinearLayout linearLayout_search;
    private Dialog dialog;
    private MyApplication myApplication;
    private String TAG = "MainActivity";
    private ImageButton imageButton_play, imageButton_stop;
    private RecyclerView recyclerView_categories;
    private AdView adView;
    private AdRequest adRequest;
    private LinearLayout linearLayout_main_adView;
    private PlusOneButton mPlusOneButton;
    private SharedPreferences sharedPref;
    private int PLUS_ONE_REQUEST_CODE = 1515;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication) getApplicationContext();

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.brazilian_portuguese);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView_categories = (RecyclerView) findViewById(recyclerView_categories_list);
        recyclerView_categories.setLayoutManager(layoutManager);
        CategoryAdapter mAdapter = new CategoryAdapter(this);
        recyclerView_categories.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);

        layoutManager_search = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_search = (RecyclerView) findViewById(R.id.recyclerView_search);
        recyclerView_search.setLayoutManager(layoutManager_search);

        linearLayout_search = (LinearLayout) findViewById(R.id.linearLayout_search);
        linearLayout_search.setVisibility(GONE);

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

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        boolean liked = sharedPref.getBoolean("liked", false);
        if(liked) {
            mPlusOneButton.setVisibility(GONE);
        } else {
            mPlusOneButton.setVisibility(VISIBLE);
        }


        linearLayout_main_adView = (LinearLayout) findViewById(R.id.linearLayout_main_adView);

        try {
            Log.i(TAG, "adView - preparing");

            adView = (AdView) findViewById(R.id.adView);
            adView.setVisibility(View.GONE);
            adRequest = new AdRequest.Builder()
                    .addTestDevice("C6E27E792E9C776653A67DDF90F3CB03")
                    .build();

            adView.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    Log.i(TAG, "AdLoaded");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adView.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                }

                public void onAdFailedToLoad(int errorcode) {
                    Log.i(TAG, "AdFailedToLoad: "+errorcode);
                    adView.setVisibility(View.GONE);
                }

                public void onAdOpened() {
                    Log.i(TAG, "AdOpened");
                }

                public void onAdClosed() {
                    Log.i(TAG, "AdClosed");
                    adView.setVisibility(View.GONE);
                }

                public void onAdLeftApplication() {
                    Log.i(TAG, "AdLeftApplication");
                    adView.setVisibility(View.GONE);
                }
            });
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.e(TAG, "Exception - adView: " + e.getLocalizedMessage());
        } finally {
            adView.setVisibility(View.GONE);
            Log.i(TAG, "adView - finished");
        }

    }

    private void setupButtonsForSpeech() {
        Log.i(TAG, "setupButtonsForSpeech");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    imageButton_play.setVisibility(View.GONE);
                    imageButton_stop.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }
        });
    }

    private void setupButtonsForSilence() {
        Log.i(TAG, "setupButtonsForStop");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    imageButton_play.setVisibility(View.VISIBLE);
                    imageButton_stop.setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }
        });
    }

    CategoryAdapter.OnItemClickListener onItemClickListener = new CategoryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent transitionIntent = new Intent(MainActivity.this, PhrasesActivity.class);
            transitionIntent.putExtra("category_id", position);
            startActivity(transitionIntent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    final PhraseAdapter phraseAdapter = new PhraseAdapter(MainActivity.this, newText.toLowerCase(), true);
                    recyclerView_search.setAdapter(phraseAdapter);
                    phraseAdapter.setOnItemClickListener(new PhraseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                            showSpeakDialog(phraseAdapter.getPhrasesArrayList().get(position).getEng(), phraseAdapter.getPhrasesArrayList().get(position).getPort());
                        }
                    });
                    linearLayout_search.setVisibility(VISIBLE);
                    recyclerView_categories.setVisibility(GONE);
                } else {
                    if (linearLayout_search.getVisibility() == VISIBLE) {
                        linearLayout_search.setVisibility(GONE);
                        recyclerView_categories.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_online_dictionary) {
            startActivity(new Intent(this, DictionaryActivity.class));
        } else if (id == R.id.nav_pronunciation) {
            startActivity(new Intent(this, PronunciationActivity.class));
        } else if (id == R.id.nav_rate) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.sow.brazilianportuguese.free")));
            startActivity(new Intent(this, RateThisAppActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showSpeakDialog(String eng, String port) {

        dialog = new Dialog(MainActivity.this);
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
                setupButtonsForSilence();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLUS_ONE_REQUEST_CODE) {
            if (resultCode == -1) {
                Toast.makeText(this, "Obrigado!", Toast.LENGTH_SHORT).show();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("liked", true);
                        editor.commit();
                        mPlusOneButton.setVisibility(GONE);
                    }
                }, 3000);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlusOneButton.initialize("https://play.google.com/store/apps/details?id=com.sow.brazilianportuguese.free", PLUS_ONE_REQUEST_CODE);
    }

}
