package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.ViewHolder> {

    private final static String TAG = "PhraseAdapter";
    private Context context;
    private ArrayList<PhraseProvider.Phrase> phrasesArrayList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public PhraseAdapter(Context context, String identifier, boolean search) {
        Log.i(TAG, "PhraseAdapter(): "+identifier);
        Log.i(TAG, "PhraseAdapter(): "+search);
        this.context = context;
        phrasesArrayList = new PhraseProvider(context, identifier, search).getPhraseArrayList();
    }

    @Override
    public PhraseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_phrase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhraseAdapter.ViewHolder holder, final int position) {
        holder.textView_port.setText(phrasesArrayList.get(position).getPort());
        holder.textView_eng.setText(phrasesArrayList.get(position).getEng());
    }

    @Override
    public int getItemCount() {
        return phrasesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView_port, textView_eng;
        LinearLayout linearLayout_phrase;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout_phrase = (LinearLayout) itemView.findViewById(R.id.linearLayout_row_phrase_title_subtitle);
            textView_port = (TextView) itemView.findViewById(R.id.textView_phrase_port);
            textView_eng = (TextView) itemView.findViewById(R.id.textView_phrase_eng);
            linearLayout_phrase.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ArrayList<PhraseProvider.Phrase> getPhrasesArrayList() {
        return phrasesArrayList;
    }

    public void setPhrasesArrayList(ArrayList<PhraseProvider.Phrase> phrasesArrayList) {
        this.phrasesArrayList = phrasesArrayList;
    }
}
