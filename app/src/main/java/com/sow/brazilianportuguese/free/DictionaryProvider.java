package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class DictionaryProvider {

    private Context context;
    ArrayList<DictionaryWord> dictionaryWords = new ArrayList<>();
    private String TAG = "DictionaryProvider";

    public DictionaryProvider(Context context, String stringBuffer) {
        this.context = context;
        String[] brokenLines = stringBuffer.split("/");
        createResultArray(brokenLines);
    }

    private void createResultArray(String[] brokenLines) {
        Log.i(TAG, "createResultArray: " + brokenLines);

        ArrayList<Integer> dictionaryWordsPostions = new ArrayList<>();
        ArrayList<Integer> endOfWordsPostions = new ArrayList<>();
        for (int i = 1; i < brokenLines.length; i++) {
            Log.i(TAG, "posicao "+i+": "+brokenLines[i]);
            if (brokenLines[i].contains("WORD:")) {
                dictionaryWordsPostions.add(i);
                if (i > 1) {
                    endOfWordsPostions.add(i - 1);
                }
            }
        }
        endOfWordsPostions.add(brokenLines.length-1);

        for (int i = 0; i < dictionaryWordsPostions.size(); i++) {
            Log.i(TAG, "Comeca: "+dictionaryWordsPostions.get(i));
            Log.i(TAG, "Termina: "+endOfWordsPostions.get(i));
            DictionaryWord dictionaryWord = new DictionaryWord();
            dictionaryWord.setWord(brokenLines[dictionaryWordsPostions.get(i)].replace("WORD","").replace(":","").replace("TRAD",",").replace("TYPE",""));
            dictionaryWord.setType(brokenLines[dictionaryWordsPostions.get(i)+1].replace("WORD","").replace(":","").replace("TRAD",",").replace("TYPE",""));
            dictionaryWord.setIdiom(brokenLines[dictionaryWordsPostions.get(i)+2].replace("WORD","").replace(":","").replace("TRAD",",").replace("TYPE",""));
            String trads = new String();
            for (int j = dictionaryWordsPostions.get(i)+3; j < endOfWordsPostions.get(i)+1; j++) {
                trads = trads.concat(brokenLines[j]);
            }
            dictionaryWord.setTranslation(trads.replace("WORD","").replace("WORD","").replace(":","").replace("TRAD",", ").replace("TYPE","").substring(2));
            dictionaryWords.add(dictionaryWord);
        }

        for (int i = 0; i < dictionaryWords.size(); i++) {
            Log.i(TAG, dictionaryWords.get(i).getWord());
            Log.i(TAG, dictionaryWords.get(i).getType());
            Log.i(TAG, dictionaryWords.get(i).getIdiom());
            Log.i(TAG, dictionaryWords.get(i).getTranslation());
            Log.i(TAG, "=======");
        }

        if(dictionaryWords.size()==0) {
            DictionaryWord dictionaryWord = new DictionaryWord();
            dictionaryWord.setWord("Palavra não encontrada!");
            dictionaryWord.setType("Certifique-se de ter digitado os acentos corretamente!");
            dictionaryWord.setIdiom("NA");
            dictionaryWord.setTranslation("Tente o dicionário online");
            dictionaryWords.add(dictionaryWord);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<DictionaryWord> getDictionaryWords() {
        return dictionaryWords;
    }

    public void setDictionaryWords(ArrayList<DictionaryWord> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }
}
