package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class PhraseProvider {

    private final static String TAG = "PhraseProvider";
    private Context context;
    private ArrayList<Phrase> phraseArrayList = new ArrayList<>();

    public PhraseProvider(Context context, String identifier, boolean search) {
        Log.i(TAG, "PhraseProvider(): "+identifier);
        Log.i(TAG, "Search: "+search);
        this.context = context;
        String[] arrayPhrases = context.getResources().getStringArray(R.array.phrases);

        if(search)
            createQueryArray(arrayPhrases, identifier);
        else
            createPhrasesArray(arrayPhrases, identifier);
    }

    private void createPhrasesArray(String[] phrases, String identifier) {
        Log.i(TAG, "createPhrasesArray: "+identifier);
        phraseArrayList.clear();
        for (int i = 0; i < phrases.length; i++) {
            if (phrases[i].split("#")[0].equals(identifier)) {
                Phrase phrase = new Phrase();
                phrase.setPort(String.valueOf(phrases[i].split("#")[2]));
                phrase.setEng(String.valueOf(phrases[i].split("#")[1]));
                phraseArrayList.add(phrase);
                Log.i(TAG, "phraseArrayList.added: "+phrase.getPort());
            }
        }
    }

    private void createQueryArray(String[] phrases, String identifier) {
        Log.i(TAG, "createQueryArray: "+identifier);
        phraseArrayList.clear();
        for (int i = 0; i < phrases.length; i++) {
            if (phrases[i].toLowerCase().contains(identifier.toLowerCase())) {
                Phrase phrase = new Phrase();
                phrase.setPort(String.valueOf(phrases[i].split("#")[2]));
                phrase.setEng(String.valueOf(phrases[i].split("#")[1]));
                phraseArrayList.add(phrase);
                Log.i(TAG, "phraseArrayList.added: "+phrase.getPort());
            }
        }
        if(phraseArrayList.size() == 0) {
            Phrase phrase = new Phrase();
            phrase.setPort("Nada encontrado...");
            phrase.setEng("Por favor, tente o dicionário online.");
            phraseArrayList.add(phrase);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Phrase> getPhraseArrayList() {
        return phraseArrayList;
    }

    public void setPhraseArrayList(ArrayList<Phrase> phraseArrayList) {
        this.phraseArrayList = phraseArrayList;
    }

    public class Phrase {

        String port;
        String eng;

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getEng() {
            return eng;
        }

        public void setEng(String eng) {
            this.eng = eng;
        }
    }

}
